package com.mytiki.kgraph.features.latest.company;

public class CompanyAO {
    private CompanyAOAbout about;
    private CompanyAOType type;
    private CompanyAOSocial social;
    private CompanyAOScore score;

    public CompanyAOAbout getAbout() {
        return about;
    }

    public void setAbout(CompanyAOAbout about) {
        this.about = about;
    }

    public CompanyAOType getType() {
        return type;
    }

    public void setType(CompanyAOType type) {
        this.type = type;
    }

    public CompanyAOSocial getSocial() {
        return social;
    }

    public void setSocial(CompanyAOSocial social) {
        this.social = social;
    }

    public CompanyAOScore getScore() {
        return score;
    }

    public void setScore(CompanyAOScore score) {
        this.score = score;
    }
}
