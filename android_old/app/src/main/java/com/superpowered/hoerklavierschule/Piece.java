package com.superpowered.hoerklavierschule;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

// audios for prepared files, singleFile if external file
public class Piece {
    private String name;
    private String author;
    private String shoplink;
    private ArrayList<Audio> audios;

    public Piece(String name, String author){
        this.name=name;
        this.author=author;
    }

    public Piece(){

    }

    //sort audios by level (you want them displayed in numerical order)
    public void sortLevels(){
        Collections.sort(audios);
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShoplink() {
        return shoplink;
    }

    public void setShoplink(String shoplink) {
        this.shoplink = shoplink;
    }

    public ArrayList<Audio> getAudios() {
        return audios;
    }

    public void setAudios(ArrayList<Audio> audios) {
        this.audios = audios;
    }
}
