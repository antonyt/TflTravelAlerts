package com.tfltravelalerts.persistence

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Database
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.support.annotation.WorkerThread
import com.tfltravelalerts.model.ConfiguredAlarm
import com.tfltravelalerts.model.Time

@Dao
interface ConfiguredAlarmDao {
    companion object {
        const val TABLE_NAME = "configured_alarms"
    }

    @WorkerThread
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAlarms(): List<ConfiguredAlarmEntity>

    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(alarm: ConfiguredAlarmEntity)

    @WorkerThread
    @Insert
    fun addAlarms(alarms: List<ConfiguredAlarmEntity>)

    @WorkerThread
    @Delete
    fun deleteAlarm(alarm: ConfiguredAlarmEntity)
}

@Database(entities = [ConfiguredAlarmEntity::class], version = 1)
@TypeConverters(DayTypeConverter::class, LineTypeConverter::class)
abstract class ConfiguredAlarmDatabase : RoomDatabase() {
    abstract fun alarmsDao(): ConfiguredAlarmDao
}

@Entity(tableName = ConfiguredAlarmDao.TABLE_NAME)
data class ConfiguredAlarmEntity(
        @field:PrimaryKey(autoGenerate = true) val id: Int,
        @Embedded val time: Time,
        val days: Int,
        val lines: Int,
        val notifyGoodService: Boolean,
        val enabled: Boolean) {
    constructor(alarm: ConfiguredAlarm) : this(
            alarm.id,
            alarm.time,
            DayTypeConverter.daysConverter(alarm.days),
            LineTypeConverter.linesConverter(alarm.lines),
            alarm.notifyGoodService,
            alarm.enabled)

    @delegate:Ignore
    val alarm by lazy {
        ConfiguredAlarm(
                id,
                time,
                DayTypeConverter.daysConverter(days),
                LineTypeConverter.linesConverter(lines),
                notifyGoodService,
                enabled)
    }
}
