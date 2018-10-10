package com.superpowered.hoerklavierschule.sql;

import com.superpowered.hoerklavierschule.Marker;

import java.io.File;
import java.util.ArrayList;

public class Audio implements Comparable<Audio> {
    private Integer level;
    private String pdfLink;

    private File left; //audio file for left hand
    private File right;

    private ArrayList<Marker> markers; // list of all existing markers


    public int getLevel() {
        return level;
    }

    public int compareTo(Audio other) {
        return this.level.compareTo(other.getLevel());
    }
}
