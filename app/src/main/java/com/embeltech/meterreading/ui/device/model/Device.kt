package com.embeltech.meterreading.ui.device.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.embeltech.meterreading.data.Config
import java.io.Serializable

@Entity(tableName = Config.DEVICE_TABLE_NAME, indices = [Index(value = ["id"], unique = true)])
class Device : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = "deviceImei")
    var deviceImei: String = "N/A"

    @ColumnInfo(name = "deviceAmrId")
    var deviceAmrId: String = "N/A"

    @ColumnInfo(name = "deviceSim")
    var deviceSim: String = "N/A"

    @ColumnInfo(name = "deviceImsi")
    var deviceImsi: String = "N/A"

    @ColumnInfo(name = "deviceAmrEnable")
    var deviceAmrEnable: Long = 0

    @ColumnInfo(name = "deviceWakeupTime")
    var deviceWakeupTime: String = "N/A"

    @ColumnInfo(name = "deviceTimeZone")
    var deviceTimeZone: String = ""

    @ColumnInfo(name = "deviceDataSampleCount")
    var deviceDataSampleCount: Long = 0

    @ColumnInfo(name = "deviceLiterPerPulse")
    var deviceLiterPerPulse: Double = 0.0

    @ColumnInfo(name = "deviceDateTime")
    var deviceDateTime: String = "N/A"

    @ColumnInfo(name = "deviceApplicationOfAmr")
    var deviceApplicationOfAmr: String = "N/A"

    @ColumnInfo(name = "deviceType")
    var deviceType: String = "N/A"

    @ColumnInfo(name = "deviceDiameterSize")
    var deviceDiameterSize: Long = 0

    @ColumnInfo(name = "deviceState")
    var deviceState: String = "N/A"

    @ColumnInfo(name = "deviceCity")
    var deviceCity: String = "N/A"

    @ColumnInfo(name = "deviceMACId")
    var deviceMACId: String = "N/A"

    @ColumnInfo(name = "deviceSerialNumber")
    var deviceSerialNumber: String = "N/A"

    @ColumnInfo(name = "deviceName")
    var deviceName: String = "N/A"

    @ColumnInfo(name = "deviceArea")
    var deviceArea: String = "N/A"

    @ColumnInfo(name = "deviceCustomerName")
    var deviceCustomerName: String = "N/A"

    @ColumnInfo(name = "deviceCustomerAddress")
    var deviceCustomerAddress: String = "N/A"

    @ColumnInfo(name = "deviceMeterLocation")
    var deviceMeterLocation: String = "N/A"

    @ColumnInfo(name = "deviceLitPerPrice")
    var deviceLitPerPrice: Double = 0.0

    @ColumnInfo(name = "deviceZone")
    var deviceZone: String = "N/A"

    @ColumnInfo(name = "deviceBuildingNameOrWingName")
    var deviceBuildingNameOrWingName: String = "N/A"

    @ColumnInfo(name = "deviceUser")
    var deviceUser: String = "N/A"

    @ColumnInfo(name = "deviceAxisEnable")
    var deviceAxisEnable: Long = 0

    @ColumnInfo(name = "deviceFkUserId")
    var deviceFkUserId: Long = 0

    @ColumnInfo(name = "deviceAdmin")
    var deviceAdmin: String = "N/A"

    @ColumnInfo(name = "deviceFkAdminId")
    var deviceFkAdminId: Long = 0

    @ColumnInfo(name = "deviceTypeAmrOrBle")
    var deviceTypeAmrOrBle: String = "N/A"

    @ColumnInfo(name = "deviceMeterStartReading")
    var deviceMeterStartReading: Double = 0.0

    @ColumnInfo(name = "pkDeviceDetails")
    var pkDeviceDetails : Long = 0

}