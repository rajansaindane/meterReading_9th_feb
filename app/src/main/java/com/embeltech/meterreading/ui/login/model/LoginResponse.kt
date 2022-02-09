package com.embeltech.meterreading.ui.login.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LoginResponse : Serializable {
    @SerializedName("token")
    val token: String = ""

    @SerializedName("perList")
    val perList: List<PerList>? = null
}

class PerList : Serializable {
    @SerializedName("fkUserID")
    val fkUserID : Long = 0

    @SerializedName("firstname")
    val firstname : String = ""

    @SerializedName("lastname")
    val lastname : String = ""

    @SerializedName("username")
    val username : String = ""

    @SerializedName("role")
    val role : String = ""
}