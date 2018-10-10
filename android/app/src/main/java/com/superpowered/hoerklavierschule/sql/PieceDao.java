package com.superpowered.hoerklavierschule.sql;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PieceDao {
    @Query("SELECT * FROM piece")
    List<Piece> getAll();

    @Query("SELECT * FROM piece WHERE id IN (:pieceIds)")
    List<Piece> loadAllByIds(int[] pieceIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Piece piece);

    // Insert multiple items
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insert(Piece ... pieces);

    @Delete
    void delete(Piece piece);
}
