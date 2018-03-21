package com.github.on7labs.model;

/**
 * Created by androidlover5842 on 21.3.2018.
 */

public class ListBuildModel  {
    private String name;
    private String date;
    private String developerName;
    private String developerEmail;
    private String bannerUrl;
    private String description;
    private String romUrl;

    public ListBuildModel(){}

    public ListBuildModel(String name,String date,String developerName,String developerEmail,String description,String bannerUrl,String romUrl){
        this.bannerUrl=bannerUrl;
        this.name=name;
        this.developerEmail=developerEmail;
        this.developerName=developerName;
        this.date=date;
        this.description=description;
        this.romUrl=romUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDeveloperEmail(String developerEmail) {
        this.developerEmail = developerEmail;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }

    public void setRomUrl(String romUrl) {
        this.romUrl = romUrl;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDeveloperEmail() {
        return developerEmail;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public String getRomUrl() {
        return romUrl;
    }
}
