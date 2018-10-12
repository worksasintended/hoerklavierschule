package com.superpowered.hoerklavierschule.sql;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public class AppDatabase_Impl extends AppDatabase {
  private volatile PieceDao _pieceDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Piece` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` INTEGER NOT NULL, `name` TEXT, `author` TEXT, `shoplink` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"6c0c6cd6aa915e13788f3b4372597ae6\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Piece`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsPiece = new HashMap<String, TableInfo.Column>(5);
        _columnsPiece.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsPiece.put("type", new TableInfo.Column("type", "INTEGER", true, 0));
        _columnsPiece.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsPiece.put("author", new TableInfo.Column("author", "TEXT", false, 0));
        _columnsPiece.put("shoplink", new TableInfo.Column("shoplink", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPiece = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPiece = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPiece = new TableInfo("Piece", _columnsPiece, _foreignKeysPiece, _indicesPiece);
        final TableInfo _existingPiece = TableInfo.read(_db, "Piece");
        if (! _infoPiece.equals(_existingPiece)) {
          throw new IllegalStateException("Migration didn't properly handle Piece(com.superpowered.hoerklavierschule.sql.Piece).\n"
                  + " Expected:\n" + _infoPiece + "\n"
                  + " Found:\n" + _existingPiece);
        }
      }
    }, "6c0c6cd6aa915e13788f3b4372597ae6", "814d79d99bbb372f079e5aee88c7f765");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Piece");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Piece`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public PieceDao pieceDao() {
    if (_pieceDao != null) {
      return _pieceDao;
    } else {
      synchronized(this) {
        if(_pieceDao == null) {
          _pieceDao = new PieceDao_Impl(this);
        }
        return _pieceDao;
      }
    }
  }
}
