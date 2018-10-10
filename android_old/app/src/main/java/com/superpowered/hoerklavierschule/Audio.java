package com.superpowered.hoerklavierschule;

import java.io.File;
import java.util.ArrayList;

public class Audio implements Comparable<Audio>{


    private Integer level;
    private File left; //audio file for left hand
    private File right;
    private String pdfLink;
    private ArrayList<Marker> markers; // list of all existing markers

    //setters and getters
    public int getLevel(){
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
    public File getLeft() {
        return left;
    }

    public void setLeft(File left) {
        this.left = left;
    }

    public File getRight() {
        return right;
    }

    public void setRight(File right) {
        this.right = right;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public ArrayList<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(ArrayList<Marker> markers) {
        this.markers = markers;
    }



    //make it sortable by levels
    public int compareTo(Audio other){
        return this.level.compareTo(other.getLevel());
    }
}
