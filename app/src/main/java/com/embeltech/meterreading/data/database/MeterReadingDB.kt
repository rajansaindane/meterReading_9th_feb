package com.embeltech.meterreading.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.embeltech.meterreading.data.database.model.MeterBeacon
import com.embeltech.meterreading.ui.device.model.Device

/**
 * Class to define Meter Reading database schema
 */

@Database(entities = [MeterBeacon::class, Device::class], version = 1)
abstract class MeterReadingDB : RoomDatabase() {

    abstract fun beaconInstallerDao(): BeaconsDao

    companion object {
        @JvmField
        val MIGRATION_1_2 = Migration1To2()
        val MIGRATION_2_3 = Migration2To3()
    }
}

/**Write your DB migration code here
 * DB migration from version 1 to 2
 *
 */
class Migration1To2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //database.execSQL("CREATE TABLE IF NOT EXISTS ${Config.REMINDER_ALARM_TABLE_NAME} (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `reminderID` INTEGER, `isFired` INTEGER NOT NULL DEFAULT 0, FOREIGN KEY(`reminderID`) REFERENCES ${Config.REMINDER_TABLE_NAME} (`id`) ON UPDATE CASCADE ON DELETE CASCADE )")
    }
}

/**
 * DB migration from version 2 to 3
 */
class Migration2To3 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        /*database.execSQL("ALTER TABLE ${Config.VEHICLES_TABLE_NAME} ADD `share_status` INTEGER DEFAULT 0 NOT NULL")
        database.execSQL("CREATE TABLE IF NOT EXISTS ${Config.TYRE_DETECTION_EVENT_TABLE_NAME} (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tag_id` TEXT UNIQUE NOT NULL, `sensor_data` TEXT NOT NULL, `time_stamp` TEXT NOT NULL, `last_low_pressure_time_stamp` TEXT, `last_high_pressure_time_stamp` TEXT, `last_high_temperature_time_stamp` TEXT, FOREIGN KEY(`tag_id`) REFERENCES ${Config.TAG_CONFIGURATIONS_TABLE_NAME} (`tag_id`) ON UPDATE CASCADE ON DELETE CASCADE )")
        database.execSQL("CREATE UNIQUE INDEX index_tyre_detection_event_tag_id ON ${Config.TYRE_DETECTION_EVENT_TABLE_NAME} (`tag_id`)")
        database.execSQL("ALTER TABLE ${Config.REMINDER_TABLE_NAME} ADD `is_reminder_fired` INTEGER DEFAULT 0 NOT NULL")*/
    }
}
