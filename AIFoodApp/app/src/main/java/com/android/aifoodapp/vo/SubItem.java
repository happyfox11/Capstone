package com.android.aifoodapp.vo;

public class SubItem {
    private String subItemTitle;
    private String subItemImg;

    public SubItem(String subItemTitle, String subItemImg) {
        this.subItemTitle = subItemTitle;
        this.subItemImg=subItemImg;
    }

    public String getSubItemTitle() {
        return subItemTitle;
    }
    public void setSubItemTitle(String subItemTitle) {
        this.subItemTitle = subItemTitle;
    }

    public String getSubItemImg(){
        return subItemImg;
    }
    public void setSubItemImg(String subItemImg){
        this.subItemImg=subItemImg;
    }
}