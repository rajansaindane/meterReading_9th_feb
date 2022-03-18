package com.embeltech.meterreading.ui.device.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SaveDeviceResponse : Serializable {
    @SerializedName("present")
    val present: Boolean = false


}
