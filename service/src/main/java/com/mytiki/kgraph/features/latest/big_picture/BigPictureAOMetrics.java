package com.mytiki.kgraph.features.latest.big_picture;

public class BigPictureAOMetrics {
    private Integer alexaUsRank;
    private Integer alexaGlobalRank;
    private Integer employees;
    private String employeesRange;
    private Long marketCap;
    private Long annualRevenue;
    private String estimatedAnnualRevenue;
    private Long raised;

    public Integer getAlexaUsRank() {
        return alexaUsRank;
    }

    public void setAlexaUsRank(Integer alexaUsRank) {
        this.alexaUsRank = alexaUsRank;
    }

    public Integer getAlexaGlobalRank() {
        return alexaGlobalRank;
    }

    public void setAlexaGlobalRank(Integer alexaGlobalRank) {
        this.alexaGlobalRank = alexaGlobalRank;
    }

    public Integer getEmployees() {
        return employees;
    }

    public void setEmployees(Integer employees) {
        this.employees = employees;
    }

    public String getEmployeesRange() {
        return employeesRange;
    }

    public void setEmployeesRange(String employeesRange) {
        this.employeesRange = employeesRange;
    }

    public String getEstimatedAnnualRevenue() {
        return estimatedAnnualRevenue;
    }

    public void setEstimatedAnnualRevenue(String estimatedAnnualRevenue) {
        this.estimatedAnnualRevenue = estimatedAnnualRevenue;
    }

    public Long getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Long marketCap) {
        this.marketCap = marketCap;
    }

    public Long getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(Long annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public Long getRaised() {
        return raised;
    }

    public void setRaised(Long raised) {
        this.raised = raised;
    }
}
