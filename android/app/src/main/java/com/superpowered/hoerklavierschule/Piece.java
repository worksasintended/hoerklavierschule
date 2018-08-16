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
    private File singleFile;
    private int type; // 1=left right; 2= external file

    public Piece(String name, String author, int type){
        this.name=name;
        this.author=author;
        this.type=type;
    }

    public void addShoplink(String shoplink){
        this.shoplink=shoplink;
    }

    public void addSingleFile(File file){
        this.singleFile=file;
    }

    public void addAudio(Audio audio){
        this.audios.add(audio);
    }

    public void sortLevels(){
        Collections.sort(audios);
    }
}
