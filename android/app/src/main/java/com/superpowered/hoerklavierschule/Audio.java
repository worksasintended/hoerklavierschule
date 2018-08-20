package com.superpowered.hoerklavierschule;

import java.io.File;
import java.util.ArrayList;

public class Audio implements Comparable<Audio>{
    private Integer level;
    private File left; //audio file for left hand
    private File right;
    private String pdfLink;
    private ArrayList<Marker> markers; // list of all existing markers


    public int getLevel(){
        return level;
    }

    public int compareTo(Audio other){
        return this.level.compareTo(other.getLevel());
    }
}
