package com.embeltech.meterreading.config

import android.os.Environment
import com.embeltech.meterreading.config.Constants.DiaLogHeaders.BATTERY
import com.embeltech.meterreading.config.Constants.DiaLogHeaders.BEACON_MAC_ID
import com.embeltech.meterreading.config.Constants.DiaLogHeaders.BEACON_NAME
import com.embeltech.meterreading.config.Constants.DiaLogHeaders.ID
import com.embeltech.meterreading.config.Constants.DiaLogHeaders.METER_READING
import com.embeltech.meterreading.config.Constants.DiaLogHeaders.RSSI
import com.embeltech.meterreading.config.Constants.DiaLogHeaders.SERIAL_NUMBER

class Constants {

    // To get runtime permissions request codes
    object Permission {
        const val LOCATION_PERMISSION_REQUEST = 100
        const val GPS_ENABLE_REQUEST = 101
        const val REQUEST_ENABLE_BT = 102
        const val STORAGE_PERMISSION_REQUEST = 103
    }

    // To get beacon service and characteristic ids before scan starts
    object Beacon {
        const val SCAN_TIMEOUT: Long = 1000
        const val CONNECT_TIMEOUT: Long = 5
        const val BEACON_SERVICE_FILTER = "0000047b-0000-1000-8000-00805f9b34fb"

        //        val BEACON_SERVICE_FILTER = "b7b42d2a-caf8-4404-9b8c-07cb8ac8047a"
        const val BEACON_TYPE_CHARACTERISTICS = "ca98e061-d015-46a7-8e5d-8150c963122b"
        const val BEACON_ID_CHARACTERISTICS = "cef4c9cf-041e-4067-a79e-00be7933a192"
        const val BEACON_GEOFENCE_CHARACTERISTICS = "dd30ec94-175f-4665-a148-933f64029d97"
        const val BEACON_FIRMWARE_CHARACTERISTICS = "00002A26-0000-1000-8000-00805f9b34fb"
        const val BEACON_HARDWARE_CHARACTERISTICS = "00002A27-0000-1000-8000-00805f9b34fb"
        const val BEACON_BATTERY_CHARACTERISTICS = "00002A19-0000-1000-8000-00805f9b34fb"
        const val BEACON_ATDL_CHARACTERISTICS = "dd30ec94-175f-4665-a148-933f64029d98"
        const val BEACON_RESET_CHARACTERISTICS = "dd30ec94-175f-4665-a148-933f64029d99"
    }

    // Flags to identify progress bar status
    object Progress {
        //For beacon scanning process
        const val PROGRESS_SCANNING_BEACON_STARTED = 0
        const val PROGRESS_SCANNED_BEACONS_FOUND = 1
        const val PROGRESS_NO_SCANNED_BEACONS_FOUND = 2

        //For beacon connection process
        const val PROGRESS_CONNECT_BEACON_STARTED = 3
        const val PROGRESS_CONNECT_BEACONS_SUCCESS = 4
        const val PROGRESS_CONNECT_BEACON_FAILED = 5
        const val PROGRESS_RESET_CONNECT_BEACON_FAILED = 8

        const val SHOW_PROGRESS = 10
        const val HIDE_PROGRESS = 11
    }

    // TO get Template name available or not flags
    object Status {
        const val TEMPLATE_NAME_AVAILABLE = 6
        const val TEMPLATE_NAME_NOT_AVAILABLE = 7
    }

    // Value to draw circle on google map
    object GeoFence {
        const val RADIUS = 250.0
    }

    // Constant required to export csv files
    object CsvWriter {
        const val COMMA_SEPARATOR = ','
        const val CSV = ".csv"
        private const val APP_NAME = "MeterReading"
        val DIA_LOG_HEADERS =
            arrayOf(
                ID,
                BEACON_NAME,
                BEACON_MAC_ID,
                METER_READING,
                SERIAL_NUMBER,
                RSSI,
                BATTERY
            )

        private val ROOT_FOLDER =
            Environment.getExternalStorageDirectory().toString() + "/" + APP_NAME
        val LOG_WRITER_DIR = "$ROOT_FOLDER/Logs/"
        const val METER_READING_LOGGING = "Meter_Reading_Logging"
    }

    // Diagnostic Log export Headers
    object DiaLogHeaders {
        const val ID = "Sr.No."
        const val BEACON_NAME = "Beacon Name"
        const val BEACON_MAC_ID = "Beacon Mac Id"
        const val BATTERY = "Battery(%)"
        const val METER_READING = "Meter Reading"
        const val SERIAL_NUMBER = "Serial Number"
        const val RSSI = "RSSI"
    }

    // CSV Export status messages
    object StatusMessages {
        const val DIA_LOG_SUCCESS = "Diagnostic Logs exported successfully"
        const val DIA_LOG_EMPTY = "No Diagnostic Logs found"
        const val AUDIT_TRAIL_SUCCESS = "Audit Trail data exported successfully"
        const val AUDIT_TRAIL_EMPTY = "No Audit Trails found"
        const val LOG_SUCCESS = "Tables exported successfully"
        const val LOG_EMPTY = "No data available to export"
    }
}