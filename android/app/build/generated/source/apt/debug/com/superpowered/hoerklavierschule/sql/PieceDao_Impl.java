package com.superpowered.hoerklavierschule.sql;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.util.StringUtil;
import android.database.Cursor;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class PieceDao_Impl implements PieceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfPiece;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfPiece;

  public PieceDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPiece = new EntityInsertionAdapter<Piece>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Piece`(`id`,`type`,`name`,`author`,`shoplink`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Piece value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getType());
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        if (value.getAuthor() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getAuthor());
        }
        if (value.getShoplink() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getShoplink());
        }
      }
    };
    this.__deletionAdapterOfPiece = new EntityDeletionOrUpdateAdapter<Piece>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Piece` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Piece value) {
        stmt.bindLong(1, value.getId());
      }
    };
  }

  @Override
  public long insert(Piece piece) {
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfPiece.insertAndReturnId(piece);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public long[] insert(Piece... pieces) {
    __db.beginTransaction();
    try {
      long[] _result = __insertionAdapterOfPiece.insertAndReturnIdsArray(pieces);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Piece piece) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfPiece.handle(piece);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Piece> getAll() {
    final String _sql = "SELECT * FROM piece";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfType = _cursor.getColumnIndexOrThrow("type");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfAuthor = _cursor.getColumnIndexOrThrow("author");
      final int _cursorIndexOfShoplink = _cursor.getColumnIndexOrThrow("shoplink");
      final List<Piece> _result = new ArrayList<Piece>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Piece _item;
        final int _tmpType;
        _tmpType = _cursor.getInt(_cursorIndexOfType);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpAuthor;
        _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
        _item = new Piece(_tmpName,_tmpAuthor,_tmpType);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpShoplink;
        _tmpShoplink = _cursor.getString(_cursorIndexOfShoplink);
        _item.setShoplink(_tmpShoplink);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Piece> loadAllByIds(int[] pieceIds) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM piece WHERE id IN (");
    final int _inputSize = pieceIds.length;
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int _item : pieceIds) {
      _statement.bindLong(_argIndex, _item);
      _argIndex ++;
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfType = _cursor.getColumnIndexOrThrow("type");
      final int _cursorIndexOfName = _cursor.getColumnIndexOrThrow("name");
      final int _cursorIndexOfAuthor = _cursor.getColumnIndexOrThrow("author");
      final int _cursorIndexOfShoplink = _cursor.getColumnIndexOrThrow("shoplink");
      final List<Piece> _result = new ArrayList<Piece>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Piece _item_1;
        final int _tmpType;
        _tmpType = _cursor.getInt(_cursorIndexOfType);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpAuthor;
        _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
        _item_1 = new Piece(_tmpName,_tmpAuthor,_tmpType);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item_1.setId(_tmpId);
        final String _tmpShoplink;
        _tmpShoplink = _cursor.getString(_cursorIndexOfShoplink);
        _item_1.setShoplink(_tmpShoplink);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
