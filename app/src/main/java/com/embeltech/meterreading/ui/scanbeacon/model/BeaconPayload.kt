package com.embeltech.meterreading.ui.scanbeacon.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.io.Serializable

class BeaconPayload : Serializable {

    @ColumnInfo(name = "beaconName")
    var beaconNname: String = ""

    @ColumnInfo(name = "macAddress")
    var macAddress: String = ""

    @ColumnInfo(name = "serialNumber")
    var serialNumber: String = ""

    @ColumnInfo(name = "meterReading")
    var meterReading: String = ""

    @ColumnInfo(name = "rssi")
    var rssi: String = ""

    @ColumnInfo(name = "pluseCount")
    var pluseCount: String = ""

    @ColumnInfo(name = "batteryPercentage")
    var batteryPercentage: String = ""

    @ColumnInfo(name = "date")
    var date: String = ""

    @ColumnInfo(name = "time")
    var time: String = ""


    override fun toString(): String {
        return super.toString()
    }
}