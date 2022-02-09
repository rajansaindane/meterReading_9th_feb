package com.embeltech.meterreading.utils

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import com.embeltech.meterreading.BuildConfig
import timber.log.Timber
import java.math.BigInteger
import java.nio.ByteBuffer
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Utility class to perform some utility operations.
 *
 */

object Utility {
    // CHecks if device has Bluetooth Support
    val isBluetoothSupported: Boolean
        get() {
            val adapter = BluetoothAdapter.getDefaultAdapter()
            return adapter != null
        }

    // Check and returns Bluetooth enable flag
    val isBluetoothEnabled: Boolean
        get() {
            val adapter = BluetoothAdapter.getDefaultAdapter()
            return adapter != null && adapter.isEnabled
        }

    // Checks if device SDK is marshmallow or above to request runtime permissions
    val isMarshMallow: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    // Returns device screen height
    fun getScreenHeight(c: Context): Int {
        val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.y
    }

    // Returns device screens width
    fun getScreenWidth(c: Context): Int {
        val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    // Converts dp to pixel and returns
    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    // Enables Device Bluetooth
    fun enableBluetooth() {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        adapter.enable()
    }

    // Checks if location service enabled on not and returns Boolean value
    fun isLocationServiceEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        ))
    }

    // Gets systems current date and time and returns in required format
    fun getCurrentDateAndTime(): String {
        val format = "yyyy-MM-dd"
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    }

    // Gets system current date and returns in milliseconds
    fun getCurrentDateAndTimeInMilliSeconds(): String {
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis.toString()
    }

    // Converts and returns given date in required display format
    fun getDisplayDateAndTime(longDate: String): String {
        val format = "dd/MM/yyyy HH:mm:ss"
        val calendar = Date(longDate.toLong())
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(calendar.time)
    }

    /**
     * Return the unique device id
     */
    fun getDeviceID(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun getParseDateHistory(date: String?): String {
        var converted = ""
        try {
            var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val newDate = simpleDateFormat.parse(date)
            simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            converted = simpleDateFormat.format(newDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return converted
    }

    fun logToFile(content: String) {
        if (!BuildConfig.DEBUG) return
        Timber.d("File Logging")
        /*writeContent(content)
                .subscribeOn(Schedulers.io())
                .subscribe()*/
    }

    /**
     * Uncomment this if writing api logs in file is a requirement
     */

    /*private fun writeContent(content: String): Completable {
        return Completable.fromAction {
            try {
                */
    /**
     * Create a new log directory if not exist
     *//*
                val dir = File(LOG_WRITER_DIR)
                if (!dir.exists()) dir.mkdirs()
                */
    /**
     * Create a new log file if not exist or return the old file
     *//*
                val logFile = File(LOG_WRITER_DIR, "${getCurrentDateForLogFile()}.txt")
                if (!logFile.exists())
                    logFile.createNewFile()

                logFile.appendText("${getCurrentDateAndTime()} $content \n")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }*/

    private val hexArray =
        charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    /**
     * Convert byte[] to string
     *
     * @param bytes array of bytes
     * @return string
     */
    fun byteArrayToHexString(bytes: ByteArray?): String {
        var hexChars = ""

        bytes?.let {
            for (b in it) {
                hexChars += String.format("%02X", b)
            }
        }

        return hexChars
    }

    /**
     * Convert hex to ascii
     *
     * @param hex string in hex format
     * @return string
     */
    fun hexToAscii(hex: String?): String {
        val output = StringBuilder()
        hex?.let {
            var i = 0
            while (i < it.length) {
                val str = it.substring(i, i + 2)
                output.append(Integer.parseInt(str, 16).toChar())
                i += 2
            }
        }
        return output.toString()
    }

    /**
     * Method used to get beacon name from byte arrays
     */
    fun beaconName(bytes: ByteArray): String {
        var beaconName = ""
        val beaconFirstIndex: Int = bytes[0].toInt()
        val beaconSecondIndex: Int
        val beaconNameIndex: Int

        Timber.i("First index is %s", beaconFirstIndex)
        beaconSecondIndex = bytes[beaconFirstIndex + 1].toInt()
        Timber.i("Second index is %s", beaconSecondIndex)
        beaconNameIndex = bytes[beaconFirstIndex + beaconSecondIndex + 2].toInt()
        Timber.i("Beacon name index is %s", beaconNameIndex)
        val fromIndex = beaconFirstIndex + beaconSecondIndex + 4
        val toIndex = (fromIndex + beaconNameIndex) - 1
        Timber.i("From index is %s and to index is %s", fromIndex, toIndex)
        val beaconNameByteArray = bytes.copyOfRange(fromIndex, toIndex)
        Timber.i(
            "Beacon name slice array is %s",
            Arrays.deepToString(beaconNameByteArray.toTypedArray())
        )
        beaconName = hexToAscii(byteArrayToHexString(beaconNameByteArray))
        return beaconName
    }

    /**
     * Returns Int beacon id from byte arrays
     */
    fun getByteArrayToHexBeaconId(bytes: ByteArray?): String {
        var hexBeaconId = ""

        bytes?.let {
            var b = it.size - 1
            while (b >= 0) {
                hexBeaconId += String.format("%02X", it[b])
                b--
            }
        }

        return hexBeaconId
    }

    /**
     * Retrieve and returns Long value from hex string
     */
    fun getHexToDecimal(hex: String?): Int {
        var long = 0
        hex?.let {
            try {
                long = Integer.parseInt(it, 16)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
        return long
    }

    fun getHexToDecimalNew(hex: String?): BigInteger {
        hex?.let {
            try {
                return BigInteger(hex, 16)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
        return BigInteger("0", 16)
    }

    /**
     * Converts LatLongs with long decimal values to required decimal points to show on App screen
     */
    fun getLatLongsInDisplayFormat(value: Double): String {
        val decimalFormat = DecimalFormat("#.#####")
        var displaySting = decimalFormat.format(value).toString()
        if (!displaySting.contains(".")) {
            displaySting += ".0"
        }
        return displaySting
    }

    /**
     * Takes Latitude and Longitudes as parameters, calculate and returns distance in meters
     */
    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta)))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60.0 * 1.1515 / 0.62137 // divided by 0.62137 as we want distance in kilimeters
        dist *= 1000 // Multiplied by 1000 to get distance in meters
        return dist
    }

    /**
     * Convert Degree to Radian and return double value
     */
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    /**
     * Convert Radian to Degree and return double value
     */
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    /**
     * Converts ascii characters to hex string
     */
    fun asciiToHex(asciiValue: String?): String {
        val hex = StringBuffer()
        asciiValue?.let {
            val chars = it.toCharArray()
            for (i in chars.indices) {
                hex.append(Integer.toHexString(chars[i].toInt()))
            }
        }
        return hex.toString()
    }

    /**
     * Converts int value to hex string
     */
    fun intToHex(value: Int): String {
        var hexString = Integer.toString(value, 16)
        if (hexString.length % 2 != 0)
            hexString = "0$hexString"
        return hexString.toUpperCase()
    }

    /**
     * Converts hex string to Byte Array to pass to beacon
     */
    fun hexStringToByteArray(s: String?): ByteArray {
        var data = ByteArray(0)
        s?.let {
            val len = it.length
            data = ByteArray(len / 2)

            var i = 0
            while (i < len) {
                data[i / 2] =
                    ((Character.digit(it[i], 16) shl 4) + Character.digit(it[i + 1], 16)).toByte()
                i += 2
            }
        }

        return data
    }

    /**
     * Converts inputted byte array to Int value
     */
    fun byteArrayToInt(byteArray: ByteArray?): Int {
        var intData = 0
        byteArray?.let {
            if (it.isNotEmpty()) {
                val buffer = ByteBuffer.wrap(it)
                intData = buffer.int
            }
        }
        return intData
    }


    /**
     * Returns beacon name of device
     *
     * */
    fun getBeaconName(hex: String): String {
        var beaconName = ""
        beaconName = hexToAscii(hex.substring(0, 10))
        return beaconName
    }

    /**
     * Returns Meter Reading of device
     *
     */
    fun getMeterReading(hex: String): Int{
        var meterReading = 0
        val reading4 = hex.substring(12,14)
        val reading2 = hex.substring(16,18)
        val reading3 = hex.substring(36,38)
        val reading1 = hex.substring(38,40)

        val s = reading4+reading3+reading2+reading1

        meterReading = getHexToDecimal(s)
        return meterReading
    }

    /**
     * Returns Serial number of device
     *
     */
    fun getSerialNumber(hex: String): Int{
        var serialNumber = 0
        val serial = hex.substring(26,34)

        serialNumber = getHexToDecimal(serial)
        return serialNumber
    }

    /**
     * Returns battery percentage of device
     *
     */
    fun getBatteryPercentage(hex: String): Int{
        var batteryPercentage = 0
        val battery = hex.substring(20,22)

        batteryPercentage = getHexToDecimal(battery)
        return batteryPercentage
    }

    private var progressDialog: ProgressDialog? = null
    fun showLoadingDialog(
        context: Context?,
        message: String?
    ) {
        if (!(progressDialog != null && progressDialog!!.isShowing)) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage(message)
            progressDialog!!.setCancelable(true)
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.show()
        }
    }

    fun cancelLoading() {
        if (progressDialog != null && progressDialog!!.isShowing) progressDialog!!.cancel()
    }

}

