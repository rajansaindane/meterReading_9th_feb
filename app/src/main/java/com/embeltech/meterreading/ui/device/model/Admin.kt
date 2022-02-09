package com.embeltech.meterreading.ui.device.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Admin : Serializable{
    @SerializedName("fkAdminId")
    val fkAdminId : Long = 0

    @SerializedName("firstname")
    val firstname : String = ""

    @SerializedName("lastname")
    val lastname : String = ""
}