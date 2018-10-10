package com.superpowered.hoerklavierschule.sql;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.File;
import java.util.ArrayList;

/**
 * audios for prepared files, singleFile if external file
 */
@Entity
public class Piece {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "type")
    private int type = 1; //* 1=left right; 2= external file
    @ColumnInfo(name = "name")
    private String name = "";
    @ColumnInfo(name = "author")
    private String author = "";
    @ColumnInfo(name = "shoplink")
    private String shoplink = "";

    @Ignore
    private File singleFile;
    @Ignore
    private ArrayList<Audio> audios;

    enum PieceType {
        leftRight,
        external
    }

    public Piece(String name, String author, int type) {
        this.name = name;
        this.author = author;
        this.type = type;
    }

    /////////////////////////////////////////////////////
    // add

    public void addShoplink(String shoplink) {
        this.shoplink = shoplink;
    }

    public void addSingleFile(File file) {
        this.singleFile = file;
    }

    public void addAudio(Audio audio) {
        this.audios.add(audio);
    }

    /////////////////////////////////////////////////////
    // get

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getShoplink() {
        return shoplink;
    }

    /////////////////////////////////////////////////////
    // set

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setShoplink(String shoplink) {
        this.shoplink = shoplink;
    }
}
