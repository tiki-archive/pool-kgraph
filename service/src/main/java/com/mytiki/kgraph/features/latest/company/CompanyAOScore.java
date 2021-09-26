package com.mytiki.kgraph.features.latest.company;

import java.math.BigDecimal;

public class CompanyAOScore {
    private BigDecimal sensitivityScore;
    private BigDecimal breachScore;
    private BigDecimal securityScore;

    public BigDecimal getSensitivityScore() {
        return sensitivityScore;
    }

    public void setSensitivityScore(BigDecimal sensitivityScore) {
        this.sensitivityScore = sensitivityScore;
    }

    public BigDecimal getBreachScore() {
        return breachScore;
    }

    public void setBreachScore(BigDecimal breachScore) {
        this.breachScore = breachScore;
    }

    public BigDecimal getSecurityScore() {
        return securityScore;
    }

    public void setSecurityScore(BigDecimal securityScore) {
        this.securityScore = securityScore;
    }
}
