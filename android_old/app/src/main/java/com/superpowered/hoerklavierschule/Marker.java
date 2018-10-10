package com.superpowered.hoerklavierschule;

import java.io.File;

public class Marker {
    private double position; // position in ms
    private int type; // 1=text, 2=pic
    private File markerFile; // either .txt or .jpg

    //getters and setters
    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public File getMarkerFile() {
        return markerFile;
    }

    public void setMarkerFile(File markerFile) {
        this.markerFile = markerFile;
    }

}
