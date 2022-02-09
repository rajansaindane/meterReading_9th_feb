package com.embeltech.meterreading.utils

import com.embeltech.meterreading.data.database.model.MeterBeacon
import com.google.gson.Gson
import timber.log.Timber
import java.util.*

/**
 * Class is used for parsing input data and return list of array of string.
 *
 */
class LogDataParser {

    companion object {

        // Parse Diagnostic Logs data

        fun parseDiagnosticData(logs: List<MeterBeacon>?): List<Array<String>> {

            val parseLogList: ArrayList<Array<String>> = ArrayList()

            logs?.forEachIndexed { _, it ->
                val parsedData: Array<String> = Array(7) { "" }
                parsedData[0] = it.id.toString()
                parsedData[1] = it.beaconName
                parsedData[2] = it.beaconMacId
                parsedData[3] = it.meterReading.toString()
                parsedData[4] = it.serialNumber
                parsedData[5] = it.rssi.toString()
                parsedData[6] = it.batteryLevel.toString()

                parseLogList.add(parsedData)
            }
            Timber.d("Diagnostic Log Parse list data ${Gson().toJson(parseLogList)}")
            return parseLogList
        }
    }
}