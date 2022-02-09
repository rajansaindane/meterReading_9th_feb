package com.embeltech.meterreading.data.database

import androidx.room.*
import com.embeltech.meterreading.data.Config
import com.embeltech.meterreading.data.database.model.MeterBeacon
import com.embeltech.meterreading.ui.device.model.Device
import io.reactivex.Single

/**
 * Interface where all the DB related queries are declared.
 */

@Dao
interface BeaconsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBeacon(beacon: MeterBeacon): Long

    @Update
    fun updateBeacon(beacon: MeterBeacon): Int

    @Delete
    fun deleteBeacon(beacon: MeterBeacon): Int

    @Query("select id from beacon where id = :beaconId")
    fun getBeaconInsertId(beaconId: Int): Long

    @Query("select * from ${Config.BEACON_TABLE_NAME}")
    fun getAllBeaconList(): List<MeterBeacon>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveDevice(device: Device): Long

    @Query("select * from ${Config.DEVICE_TABLE_NAME}")
    fun getAllDevices(): Single<List<Device>>
}