package com.mytiki.kgraph.features.latest.hibp;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.LocalDate;
import java.util.List;

public class HibpAO {
    @JsonAlias("Name")
    private String name;
    @JsonAlias("Title")
    private String title;
    @JsonAlias("Domain")
    private String domain;
    @JsonAlias("BreachDate")
    private LocalDate breachDate;
    @JsonAlias("AddedDate")
    private LocalDate addedDate;
    @JsonAlias("ModifiedDate")
    private LocalDate modifiedDate;
    @JsonAlias("PwnCount")
    private int pwnCount;
    @JsonAlias("Description")
    private String description;
    @JsonAlias("DataClasses")
    private List<String> dataClasses;
    @JsonAlias("IsVerified")
    private boolean isVerified;
    @JsonAlias("IsFabricated")
    private boolean isFabricated;
    @JsonAlias("IsSensitive")
    private boolean isSensitive;
    @JsonAlias("IsRetired")
    private boolean isRetired;
    @JsonAlias("IsSpamList")
    private boolean isSpamList;
    @JsonAlias("LogoPath")
    private String logoPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public LocalDate getBreachDate() {
        return breachDate;
    }

    public void setBreachDate(LocalDate breachDate) {
        this.breachDate = breachDate;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getPwnCount() {
        return pwnCount;
    }

    public void setPwnCount(int pwnCount) {
        this.pwnCount = pwnCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDataClasses() {
        return dataClasses;
    }

    public void setDataClasses(List<String> dataClasses) {
        this.dataClasses = dataClasses;
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean getIsFabricated() {
        return isFabricated;
    }

    public void setIsFabricated(boolean fabricated) {
        isFabricated = fabricated;
    }

    public boolean getIsSensitive() {
        return isSensitive;
    }

    public void setIsSensitive(boolean sensitive) {
        isSensitive = sensitive;
    }

    public boolean getIsRetired() {
        return isRetired;
    }

    public void setIsRetired(boolean retired) {
        isRetired = retired;
    }

    public boolean getIsSpamList() {
        return isSpamList;
    }

    public void setIsSpamList(boolean spamList) {
        isSpamList = spamList;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
}
