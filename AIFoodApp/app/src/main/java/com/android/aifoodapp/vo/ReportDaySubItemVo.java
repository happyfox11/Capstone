package com.android.aifoodapp.vo;

// 자식 아이템
public class ReportDaySubItemVo {
    private String subItemImage;
    private String subItemTitle;
    private String subItemDesc;
    private Double subItemIntake;

    public ReportDaySubItemVo(String subItemTitle, String subItemDesc, String subItemImage, Double subItemIntake) {
        this.subItemTitle = subItemTitle;
        this.subItemDesc = subItemDesc;
        this.subItemImage = subItemImage;
        this.subItemIntake = subItemIntake;
    }

    public String getSubItemImage() {
        return subItemImage;
    }

    public void setSubItemImage(String subItemImage) {
        this.subItemImage = subItemImage;
    }

    public String getSubItemTitle() {
        return subItemTitle;
    }

    public void setSubItemTitle(String subItemTitle) {
        this.subItemTitle = subItemTitle;
    }

    public String getSubItemDesc() {
        return subItemDesc;
    }

    public void setSubItemDesc(String subItemDesc) {
        this.subItemDesc = subItemDesc;
    }

    public Double getSubItemIntake(){
        return subItemIntake;
    }

    public void setSubItemIntake(Double subItemIntake){
        this.subItemIntake=subItemIntake;
    }
}