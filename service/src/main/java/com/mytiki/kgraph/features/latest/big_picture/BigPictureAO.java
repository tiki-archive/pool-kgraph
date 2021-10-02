package com.mytiki.kgraph.features.latest.big_picture;

import java.util.List;

public class BigPictureAO {
    private String id;
    private String name;
    private String legalName;
    private String domain;
    private List<String> domainAliases;
    private String url;
    private List<String> tags;
    private BigPictureAOCategory category;
    private String description;
    private Integer foundedYear;
    private String location;
    private BigPictureAOGeo geo;
    private BigPictureAOMetrics metrics;
    private List<String> tech;
    private BigPictureAOSocial facebook;
    private BigPictureAOSocial linkedin;
    private BigPictureAOTwitter twitter;
    private BigPictureAOSocial crunchbase;
    private String logo;
    private Boolean emailProvider;
    private String type;
    private String ticker;
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<String> getDomainAliases() {
        return domainAliases;
    }

    public void setDomainAliases(List<String> domainAliases) {
        this.domainAliases = domainAliases;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public BigPictureAOCategory getCategory() {
        return category;
    }

    public void setCategory(BigPictureAOCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(Integer foundedYear) {
        this.foundedYear = foundedYear;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigPictureAOGeo getGeo() {
        return geo;
    }

    public void setGeo(BigPictureAOGeo geo) {
        this.geo = geo;
    }

    public BigPictureAOMetrics getMetrics() {
        return metrics;
    }

    public void setMetrics(BigPictureAOMetrics metrics) {
        this.metrics = metrics;
    }

    public List<String> getTech() {
        return tech;
    }

    public void setTech(List<String> tech) {
        this.tech = tech;
    }

    public BigPictureAOSocial getFacebook() {
        return facebook;
    }

    public void setFacebook(BigPictureAOSocial facebook) {
        this.facebook = facebook;
    }

    public BigPictureAOSocial getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(BigPictureAOSocial linkedin) {
        this.linkedin = linkedin;
    }

    public BigPictureAOTwitter getTwitter() {
        return twitter;
    }

    public void setTwitter(BigPictureAOTwitter twitter) {
        this.twitter = twitter;
    }

    public BigPictureAOSocial getCrunchbase() {
        return crunchbase;
    }

    public void setCrunchbase(BigPictureAOSocial crunchbase) {
        this.crunchbase = crunchbase;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Boolean getEmailProvider() {
        return emailProvider;
    }

    public void setEmailProvider(Boolean emailProvider) {
        this.emailProvider = emailProvider;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
