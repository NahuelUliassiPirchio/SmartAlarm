package com.example.smartalarm;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlarmDao {
    @Insert
    void insert(Alarm alarm);

    @Query("SELECT * FROM alarm_table")
    List<Alarm> getAll();

    @Update
    void update(Alarm alarm);

    @Delete
    void delete(Alarm alarm);

    @Delete
    void deleteAllOfThese(List<Alarm> alarmsToDelete);
}
