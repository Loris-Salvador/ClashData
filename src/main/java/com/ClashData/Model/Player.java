package com.ClashData.Model;

public class Player implements Comparable<Player> {
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

    @Override
    public int compareTo(Player other) {
        return Integer.compare(this.ELO, other.ELO);
    }

}
