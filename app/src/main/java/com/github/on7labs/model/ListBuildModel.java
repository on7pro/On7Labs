package com.github.on7labs.model;

/**
 * Created by androidlover5842 on 21.3.2018.
 */

public class ListBuildModel {
    private String name;
    private String date;
    private String developerName;
    private String developerEmail;
    private String bannerUrl;
    private String description;
    private String romUrl;
    private String credits;
    private Integer version;
    private String status;
    private String source;
    private ScreenShots screenShots;

    public ListBuildModel() {
    }

    public ListBuildModel(String name, String date, String developerName, String developerEmail, String description, String bannerUrl, Integer version, String status, String romUrl,ScreenShots screenShots) {
        this.bannerUrl = bannerUrl;
        this.name = name;
        this.developerEmail = developerEmail;
        this.developerName = developerName;
        this.date = date;
        this.description = description;
        this.romUrl = romUrl;
        this.version = version;
        this.status = status;
        this.screenShots=screenShots;
    }

    public ListBuildModel(String name, String date, String developerName, String developerEmail, String description, String bannerUrl, Integer version, String status, String romUrl, String credits, String source,ScreenShots screenShots) {
        this.bannerUrl = bannerUrl;
        this.name = name;
        this.developerEmail = developerEmail;
        this.developerName = developerName;
        this.date = date;
        this.description = description;
        this.romUrl = romUrl;
        this.version = version;
        this.status = status;
        this.credits = credits;
        this.source = source;
        this.screenShots=screenShots;
    }

    public ListBuildModel(String name, String date, String developerName, String developerEmail, String description, String bannerUrl, Integer version, String status, String romUrl, String credits,ScreenShots screenShots) {
        this.bannerUrl = bannerUrl;
        this.name = name;
        this.developerEmail = developerEmail;
        this.developerName = developerName;
        this.date = date;
        this.description = description;
        this.romUrl = romUrl;
        this.version = version;
        this.status = status;
        this.credits = credits;
        this.screenShots=screenShots;
    }

    public ListBuildModel(String name, String date, String developerName, String developerEmail, String description, String bannerUrl, Integer version, String status, String romUrl, String source, boolean test,ScreenShots screenShots) {
        this.bannerUrl = bannerUrl;
        this.name = name;
        this.developerEmail = developerEmail;
        this.developerName = developerName;
        this.date = date;
        this.description = description;
        this.romUrl = romUrl;
        this.version = version;
        this.status = status;
        this.source = source;
        this.screenShots=screenShots;
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

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setScreenShots(ScreenShots screenShots) {
        this.screenShots = screenShots;
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

    public String getCredits() {
        return credits;
    }

    public String getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }

    public Integer getVersion() {
        return version;
    }

    public ScreenShots getScreenShots() {
        return screenShots;
    }
}
