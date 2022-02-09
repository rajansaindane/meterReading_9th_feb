package com.embeltech.meterreading.ui.billing.model

import com.google.gson.annotations.SerializedName

class DeviceDataDetail {

    @SerializedName("billingDate")
    var billingDate: String = ""
    @SerializedName("billNo")
    var billNumber: Long = 0
    var nameAndAddress: String = ""
    @SerializedName("billAmrId")
    var amrId: String = ""
    @SerializedName("totalLitre")
    var totalConsumption: Double = 0.0
    @SerializedName("litrePerPrice")
    var pricePerLiter: Double = 0.0
    @SerializedName("startDate")
    var startDate: String = ""
    @SerializedName("endDate")
    var endDate: String = ""
    @SerializedName("totalAmount")
    var totalAmount : Double = 0.0
    @SerializedName("billUsername")
    var billUserName : String = ""
    @SerializedName("billUserId")
    var billUserId : Long = 0
    @SerializedName("billAddress")
    var billingAddress : String = ""
    @SerializedName("billCity")
    var billingCity : String = ""
    @SerializedName("billState")
    var billingState : String = ""

}