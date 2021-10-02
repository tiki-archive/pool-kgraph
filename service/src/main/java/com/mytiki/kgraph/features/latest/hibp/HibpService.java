package com.mytiki.kgraph.features.latest.hibp;

import com.mytiki.kgraph.features.latest.graph.*;
import com.mytiki.kgraph.features.latest.sync.SyncDO;
import com.mytiki.kgraph.features.latest.sync.SyncEnum;
import com.mytiki.kgraph.features.latest.sync.SyncService;
import io.sentry.Sentry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class HibpService {
    private static final String IS_FABRICATED = "isFabricated";
    private static final String IS_RETIRED = "isRetired";
    private static final String IS_SPAM_LIST = "isSpamList";
    private static final String IS_VERIFIED = "isVerified";
    private static final String IS_SENSITIVE = "isSensitive";

    private final RestTemplate restTemplate;
    private final GraphService graphService;
    private final SyncService syncService;

    public HibpService(RestTemplate restTemplate, GraphService graphService, SyncService syncService) {
        this.restTemplate = restTemplate;
        this.graphService = graphService;
        this.syncService = syncService;
    }

    @Scheduled(fixedDelay = 1000*60*60) //1 hr
    public void index(){
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        Optional<SyncDO<Long>> hibpCached = syncService.get(SyncEnum.HIBP_CACHED);
        ZonedDateTime lastCached = hibpCached
                .map(cached -> ZonedDateTime.ofInstant(Instant.ofEpochSecond(cached.getValue()), ZoneOffset.UTC))
                .orElseGet(() -> ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
        if(lastCached.isBefore(now.minusDays(1))){
            ResponseEntity<List<HibpAO>> rsp = restTemplate.exchange(
                    "/breaches",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<HibpAO>>() {});
            if (rsp.getStatusCode().is2xxSuccessful() && rsp.getBody() != null) {
                flagEmptyClasses(rsp.getBody());
                flagUnknownClass(rsp.getBody());
                List<GraphVertexDataBreachDO> doList = rsp.getBody().stream()
                        .filter(hibpAO -> (hibpAO.getName() != null && hibpAO.getDomain() != null))
                        .filter(hibpAO -> hibpAO.getModifiedDate().isAfter(lastCached.toLocalDate()))
                        .map(this::map)
                        .collect(Collectors.toList());
                doList.forEach(dataBreachDO -> {
                    graphService.upsertVertex(dataBreachDO);
                    try {
                        graphService.upsertEdge(
                                dataBreachDO.getCollection(),
                                dataBreachDO.getValue(),
                                GraphVertexCompanyDO.COLLECTION,
                                dataBreachDO.getDomain(),
                                "TIKI");
                    }catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                        Sentry.captureException(e);
                    }
                });
                syncService.upsert(SyncEnum.HIBP_CACHED, now.toEpochSecond());
            }
        }
    }

    public List<GraphVertexDataBreachDO> findByDomain(String domain){
        List<GraphEdgeDO<? extends GraphVertexDO, ? extends GraphVertexDO>> edges =
                graphService.traverse(GraphVertexCompanyDO.COLLECTION, domain, 1);
        Set<GraphVertexDataBreachDO> breachSet = new HashSet<>();
        edges.forEach(edge -> {
            if(edge.getTo().getCollection().equals(GraphVertexDataBreachDO.COLLECTION))
                breachSet.add((GraphVertexDataBreachDO) edge.getTo());
            if(edge.getFrom().getCollection().equals(GraphVertexDataBreachDO.COLLECTION))
                breachSet.add((GraphVertexDataBreachDO) edge.getFrom());
        });
        return new ArrayList<>(breachSet);
    }

    private void flagUnknownClass(List<HibpAO> list){
        List<HibpAO> unknownClasses = list.stream()
                .filter(hibpAO -> {
                    for(String dataClass : hibpAO.getDataClasses()){
                        if(HibpWeight.forLookup(dataClass) == null)
                            return true;
                    }
                    return false;
                })
                .collect(Collectors.toList());
        unknownClasses.forEach(hibpAO -> Sentry.captureMessage("HIBP unknown class: " + hibpAO.getName()));
    }

    private void flagEmptyClasses(List<HibpAO> list){
        List<HibpAO> emptyClasses = list.stream()
                .filter(hibpAO -> hibpAO.getDataClasses().size() == 0)
                .collect(Collectors.toList());
        emptyClasses.forEach(hibpAO -> Sentry.captureMessage("HIBP no classes: " + hibpAO.getName()));
    }

    private List<String> createTypeList(HibpAO hibpAO){
        List<String> typeList = new ArrayList<>();
        if(hibpAO.getIsFabricated()) typeList.add(IS_FABRICATED);
        if(hibpAO.getIsRetired()) typeList.add(IS_RETIRED);
        if(hibpAO.getIsSpamList()) typeList.add(IS_SPAM_LIST);
        if(hibpAO.getIsVerified()) typeList.add(IS_VERIFIED);
        if(hibpAO.getIsSensitive()) typeList.add(IS_SENSITIVE);
        return typeList;
    }

    private GraphVertexDataBreachDO map(HibpAO hibpAO){
        GraphVertexDataBreachDO dataBreachDO = new GraphVertexDataBreachDO();
        dataBreachDO.setValue(hibpAO.getName());
        dataBreachDO.setDomain(hibpAO.getDomain());
        dataBreachDO.setBreached(ZonedDateTime.of(hibpAO.getBreachDate(),
                LocalTime.MIDNIGHT, ZoneOffset.UTC));
        dataBreachDO.setModified(ZonedDateTime.of(hibpAO.getModifiedDate(),
                LocalTime.MIDNIGHT, ZoneOffset.UTC));

        dataBreachDO.setPwnCount(hibpAO.getPwnCount());
        dataBreachDO.setPwnScore(calcScoreFromCount(dataBreachDO.getPwnCount()));

        dataBreachDO.setClasses(hibpAO.getDataClasses());
        dataBreachDO.setClassScore(calcScoreFromList(dataBreachDO.getClasses()));

        dataBreachDO.setTypes(createTypeList(hibpAO));
        dataBreachDO.setTypeScore(calcScoreFromList(dataBreachDO.getTypes()));

        dataBreachDO.setComboScore(calcCombo(
                dataBreachDO.getClassScore(),
                dataBreachDO.getPwnScore(),
                dataBreachDO.getTypeScore()));
        return dataBreachDO;
    }

    private BigDecimal calcScoreFromCount(int count){
        BigDecimal score = BigDecimal.valueOf(1);

        if(count > (int) HibpWeight.COUNT_100M.getLookup())
            score = score.add(BigDecimal.valueOf(HibpWeight.COUNT_100M.getWeight()));

        else if (count > (int) HibpWeight.COUNT_25M.getLookup())
            score = score.add(BigDecimal.valueOf(HibpWeight.COUNT_25M.getWeight()));

        else if (count > (int) HibpWeight.COUNT_10M.getLookup())
            score = score.add(BigDecimal.valueOf(HibpWeight.COUNT_10M.getWeight()));

        else if (count > (int) HibpWeight.COUNT_1M.getLookup())
            score = score.add(BigDecimal.valueOf(HibpWeight.COUNT_1M.getWeight()));

        return score.setScale(5, RoundingMode.HALF_UP);
    }

    private BigDecimal calcScoreFromList(List<String> list){
        BigDecimal score = BigDecimal.valueOf(1);
        for(String s : list){
            HibpWeight weight = HibpWeight.forLookup(s);
            if(weight != null) score = score.add(BigDecimal.valueOf(weight.getWeight()));
        }
        return score.setScale(5, RoundingMode.HALF_UP);
    }

    private BigDecimal calcCombo(BigDecimal classScore, BigDecimal pwnScore, BigDecimal typeScore){
        BigDecimal cap = BigDecimal.valueOf(150);
        BigDecimal score = classScore.multiply(pwnScore).multiply(typeScore);
        if(score.compareTo(cap) > 0 ) score = cap;
        return score.divide(cap, 5, RoundingMode.HALF_UP);
    }

    //TODO add a time factor to the score.
}
