package com.embeltech.meterreading.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.embeltech.meterreading.data.Config.BEACON_TABLE_NAME
import com.polidea.rxandroidble2.scan.ScanResult
import java.io.Serializable

@Entity(tableName = BEACON_TABLE_NAME, indices = [Index(value = ["beaconId"], unique = true)])
class Beacon : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id : Long = 0

    var userId : Long = 0

    @ColumnInfo(name = "beaconId")
    var beaconId : Int = 0

    lateinit var beaconName  :String

    @ColumnInfo(name = "beaconMacId")
    lateinit var beaconMacId : String

    var beaconType : Int = 0

    var logStatus : Boolean = false

    var auditStatus : Boolean = false

    lateinit var direction : String

    lateinit var latitude  :String

    lateinit var longitude : String

    lateinit var geoFence : String

    lateinit var firmware : String

    lateinit var hardware : String

    var battery : Int = 0

//    lateinit var scanResult : ScanResult

    // Required to show only one beacon at a time to delete
    var isOpened : Boolean = false

}