package com.superpowered.hoerklavierschule.sql;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

public class SQLTest {
    private AppDatabase db;

    public SQLTest(Context context) {
        db = Room.databaseBuilder(context,
                AppDatabase.class, "database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    public void addPiecesForTest() {
        db.pieceDao().insert(new Piece("Für Elise", "Beethoven", 1));
        db.pieceDao().insert(new Piece("Requiem", "Mozart", 1));
        db.pieceDao().insert(new Piece("Die Kekse sind alle", "Krümelmonster", 2));

        int size = db.pieceDao().getAll().size();
    }
}
