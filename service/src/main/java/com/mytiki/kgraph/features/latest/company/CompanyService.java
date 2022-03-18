package com.mytiki.kgraph.features.latest.company;

import com.mytiki.common.exception.ApiExceptionBuilder;
import com.mytiki.common.reply.ApiReplyAOMessageBuilder;
import com.mytiki.kgraph.features.latest.big_picture.BigPictureAO;
import com.mytiki.kgraph.features.latest.big_picture.BigPictureException;
import com.mytiki.kgraph.features.latest.big_picture.BigPictureService;
import com.mytiki.kgraph.features.latest.hibp.HibpService;
import com.mytiki.kgraph.features.latest.vertex.VertexCompanyDO;
import com.mytiki.kgraph.features.latest.vertex.VertexDataBreachDO;
import com.mytiki.kgraph.features.latest.vertex.VertexService;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public class CompanyService {
    private final BigPictureService bigPictureService;
    private final HibpService hibpService;
    private final VertexService vertexService;

    public CompanyService(
            BigPictureService bigPictureService,
            HibpService hibpService,
            VertexService vertexService) {
        this.bigPictureService = bigPictureService;
        this.hibpService = hibpService;
        this.vertexService = vertexService;
    }

    public CompanyAO findByDomain(String domain) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        Optional<VertexCompanyDO> company = vertexService.getVertex(VertexCompanyDO.COLLECTION, domain);
        if (
                company.isEmpty() ||
                company.get().getCached() == null ||
                company.get().getCached().isBefore(now.minusDays(30))) {
            try {
                BigPictureAO bigPictureAO = bigPictureService.find(domain);
                VertexCompanyDO cacheDO = mergeBigPicture(
                        company.isEmpty() ? new VertexCompanyDO() : company.get(),
                        bigPictureAO);
                cacheDO.setCached(now);
                cacheDO.setId(domain);
                cacheDO = vertexService.upsertVertex(cacheDO);
                return toAO(cacheDO, hibpService.findByDomain(domain));
            }catch (BigPictureException ex){
                throw new ApiExceptionBuilder()
                        .httpStatus(ex.getStatus())
                        .messages(new ApiReplyAOMessageBuilder()
                                .message(ex.getStatus().equals(HttpStatus.ACCEPTED) ?
                                        "Indexing. Try again later (30m?)" :
                                        ex.getStatus().getReasonPhrase())
                                .properties("domain", domain)
                                .build())
                        .build();
            }
        }else return toAO(company.get(), hibpService.findByDomain(domain));
    }

    public void update(BigPictureAO bigPictureAO) {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        Optional<VertexCompanyDO> company =
                vertexService.getVertex(VertexCompanyDO.COLLECTION, bigPictureAO.getDomain());
        if (company.isEmpty() || company.get().getCached().isBefore(now.minusDays(30))) {
            VertexCompanyDO cacheDO = mergeBigPicture(
                    company.isEmpty() ? new VertexCompanyDO() : company.get(),
                    bigPictureAO);
            cacheDO.setId(bigPictureAO.getDomain());
            cacheDO.setCached(now);
            vertexService.upsertVertex(cacheDO);
        }
    }

    private VertexCompanyDO mergeBigPicture(VertexCompanyDO company, BigPictureAO bigPictureAO){
        company.setAddress(bigPictureAO.getLocation());
        company.setDescription(bigPictureAO.getDescription());
        company.setName(bigPictureAO.getName());
        company.setLogo(bigPictureAO.getLogo());
        company.setPhone(bigPictureAO.getPhone());
        company.setUrl(bigPictureAO.getUrl());

        company.setFacebook(bigPictureAO.getFacebook().getHandle());
        company.setLinkedin(bigPictureAO.getLinkedin().getHandle());
        company.setTwitter(bigPictureAO.getTwitter().getHandle());

        company.setIndustry(bigPictureAO.getCategory().getIndustry());
        company.setSector(bigPictureAO.getCategory().getSector());
        company.setSubIndustry(bigPictureAO.getCategory().getSubIndustry());
        company.setTags(bigPictureAO.getTags());

        company.setSensitivityScore(bigPictureService.calcScore(
                company.getSector(), company.getIndustry(), company.getSubIndustry(), company.getTags()));
        return company;
    }

    private CompanyAO toAO(VertexCompanyDO companyDO, List<VertexDataBreachDO> breaches) {
        CompanyAOAbout about = new CompanyAOAbout();
        about.setAddress(companyDO.getAddress());
        about.setDomain(companyDO.getId());
        about.setName(companyDO.getName());
        about.setDescription(companyDO.getDescription());
        about.setLogo(companyDO.getLogo());
        about.setPhone(companyDO.getPhone());
        about.setUrl(companyDO.getUrl());

        CompanyAOType type = new CompanyAOType();
        type.setIndustry(companyDO.getIndustry());
        type.setSector(companyDO.getSector());
        type.setSubIndustry(companyDO.getSubIndustry());
        type.setTags(companyDO.getTags());

        CompanyAOSocial social = new CompanyAOSocial();
        social.setFacebook(companyDO.getFacebook());
        social.setLinkedin(companyDO.getLinkedin());
        social.setTwitter(companyDO.getTwitter());

        CompanyAOScore score = new CompanyAOScore();
        score.setSensitivityScore(companyDO.getSensitivityScore());
        score.setBreachScore(consolidateBreachScore(breaches));

        if(score.getBreachScore() != null && score.getSensitivityScore() != null)
            score.setSecurityScore(calcSecurityScore(score.getBreachScore(), score.getSensitivityScore()));

        CompanyAO companyAO = new CompanyAO();
        companyAO.setAbout(about);
        companyAO.setType(type);
        companyAO.setSocial(social);
        companyAO.setScore(score);
        return companyAO;
    }

    private BigDecimal consolidateBreachScore(List<VertexDataBreachDO> breaches){
        BigDecimal score = BigDecimal.valueOf(0);
        for(VertexDataBreachDO hibpDO : breaches){
            score = score.add(hibpDO.getComboScore());
        }
        if(score.compareTo(BigDecimal.valueOf(1)) > 0 ) score = BigDecimal.valueOf(1);
        return score.setScale(5, RoundingMode.HALF_UP);
    }

    private BigDecimal calcSecurityScore(BigDecimal breachScore, BigDecimal sensitivityScore){
        BigDecimal breachWeight = BigDecimal.valueOf(0.65);
        BigDecimal sensitivityWeight = BigDecimal.valueOf(0.35);
        return breachScore
                .multiply(breachWeight)
                .add(sensitivityScore.multiply(sensitivityWeight))
                .setScale(5, RoundingMode.HALF_UP);
    }
}
