package com.github.on7labs.model;

/**
 * Created by androidlover5842 on 23.3.2018.
 */

public class ScreenShots {
    private String screenShot1;
    private String screenShot2;
    private String screenShot3;
    private String screenShot4;
    private String screenShot5;
    public ScreenShots(){

    }

    public ScreenShots(String screenShot1){
        this.screenShot1=screenShot1;
    }
    public ScreenShots(String screenShot1,String screenShot2){
        this.screenShot1=screenShot1;
        this.screenShot2=screenShot2;
    }
    public ScreenShots(String screenShot1,String screenShot2,String screenShot3){
        this.screenShot1=screenShot1;
        this.screenShot2=screenShot2;
        this.screenShot3=screenShot3;
    }

    public ScreenShots(String screenShot1,String screenShot2,String screenShot3,String screenShot4){
        this.screenShot1=screenShot1;
        this.screenShot2=screenShot2;
        this.screenShot3=screenShot3;
        this.screenShot4=screenShot4;
    }

    public ScreenShots(String screenShot1,String screenShot2,String screenShot3,String screenShot4,String screenShot5){
        this.screenShot1=screenShot1;
        this.screenShot2=screenShot2;
        this.screenShot3=screenShot3;
        this.screenShot4=screenShot4;
        this.screenShot5=screenShot5;
    }

    public String getScreenShot1() {
        return screenShot1;
    }

    public String getScreenShot2() {
        return screenShot2;
    }

    public String getScreenShot3() {
        return screenShot3;
    }

    public String getScreenShot4() {
        return screenShot4;
    }

    public String getScreenShot5() {
        return screenShot5;
    }

    public void setScreenShot1(String screenShot1) {
        this.screenShot1 = screenShot1;
    }

    public void setScreenShot2(String screenShot2) {
        this.screenShot2 = screenShot2;
    }

    public void setScreenShot3(String screenShot3) {
        this.screenShot3 = screenShot3;
    }

    public void setScreenShot4(String screenShot4) {
        this.screenShot4 = screenShot4;
    }

    public void setScreenShot5(String screenShot5) {
        this.screenShot5 = screenShot5;
    }
}
