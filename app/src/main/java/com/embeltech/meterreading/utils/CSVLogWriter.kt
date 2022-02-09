
package com.embeltech.meterreading.utils

import com.embeltech.meterreading.config.Constants.CsvWriter.COMMA_SEPARATOR
import com.opencsv.CSVWriter
import timber.log.Timber
import java.io.BufferedWriter
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset

/**
 * Class is used to write logs in CSV format.
 *
 */
class CSVLogWriter @Throws(FileNotFoundException::class)
private constructor(filePath: String) : CSVWriter(
    BufferedWriter(
        OutputStreamWriter(
            FileOutputStream(filePath, true),
//            Charset.forName("ISO-8859-1").newEncoder()
            Charset.forName("utf-8").newEncoder()
        )
    ), COMMA_SEPARATOR
) {

    // input data list
    private var headerList: MutableList<Array<String>> = ArrayList()

    companion object {
        private var mInstance: CSVLogWriter? = null

        // return instance of writer
        fun openWriter(filePath: String): CSVLogWriter {
            mInstance = CSVLogWriter(filePath)
            return mInstance!!
        }
    }

    /**
     * Write Headers in csv file
     */
    fun writeHeaders(headerList: Array<String>) {
        this.headerList.clear()
        this.headerList.add(headerList)
        super.writeAll(this.headerList)
    }

    /**
     * Write logs in csv file
     */
    fun writeLog(logs: List<Array<String>>) {
        Timber.d("Writing logs...")
        headerList.clear()
        logs.forEach {
            headerList.add(it)
        }
        super.writeAll(headerList)
    }

    /**
     * close writer
     */
    fun closeWriter() = super.close()
}