package ru.skyPro.serviceBankRecommendation3.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String nameOne;
    private String descriptionOne;
    private String nameTwo;
    private String descriptionTwo;
    private String nameThree;
    private String descriptionThree;

    public String getNameOne() {
        return nameOne;
    }

    public void setNameOne(String nameOne) {
        this.nameOne = nameOne;
    }

    public String getDescriptionOne() {
        return descriptionOne;
    }

    public void setDescriptionOne(String descriptionOne) {
        this.descriptionOne = descriptionOne;
    }

    public String getNameTwo() {
        return nameTwo;
    }

    public void setNameTwo(String nameTwo) {
        this.nameTwo = nameTwo;
    }

    public String getDescriptionTwo() {
        return descriptionTwo;
    }

    public void setDescriptionTwo(String descriptionTwo) {
        this.descriptionTwo = descriptionTwo;
    }

    public String getNameThree() {
        return nameThree;
    }

    public void setNameThree(String nameThree) {
        this.nameThree = nameThree;
    }

    public String getDescriptionThree() {
        return descriptionThree;
    }

    public void setDescriptionThree(String descriptionThree) {
        this.descriptionThree = descriptionThree;
    }
}
