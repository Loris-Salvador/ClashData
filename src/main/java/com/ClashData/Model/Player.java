package com.ClashData.Model;

public class Player {
    private int ELO;
    private String tag;

    public Player(int ELO, String tag) {
        this.ELO = ELO;
        this.tag = tag;
    }

    public Player() {
        ELO = 0;
        tag = "";
    }


    public int getELO() {
        return ELO;
    }

    public void setELO(int ELO) {
        this.ELO = ELO;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public String getTagForCRAPI()
    {
        char firstChar = tag.charAt(0);
        String restOfString = tag.substring(1);
        return "%23" + restOfString;
    }




}
