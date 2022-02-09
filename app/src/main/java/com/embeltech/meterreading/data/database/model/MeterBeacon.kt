package com.embeltech.meterreading.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.embeltech.meterreading.data.Config.BEACON_TABLE_NAME
import java.io.Serializable

@Entity(tableName = BEACON_TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
class MeterBeacon : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "beaconName")
    var beaconName: String = ""

    @ColumnInfo(name = "beaconMacId")
    var beaconMacId: String = ""

    @ColumnInfo(name = "rssi")
    var rssi: Int = 0

    var byteArray: ByteArray? = null

    @ColumnInfo(name = "meterReading")
    var meterReading: Long = 0

    @ColumnInfo(name = "batteryPercentage")
    var batteryLevel: Int = 0

    @ColumnInfo(name = "serialNumber")
    var serialNumber: String = ""
}