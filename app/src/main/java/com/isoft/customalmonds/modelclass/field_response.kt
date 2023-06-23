package com.isoft.customalmonds.modelclass

import com.google.gson.annotations.SerializedName


data class field_response(
    @SerializedName("status")
    var status: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("field_ticket")
    var field_ticket: String,
    @SerializedName("type")
    var type: String,
    @SerializedName("grower")
    var grower: String,
    @SerializedName("ranch_name")
    var ranch_name: String,
    @SerializedName("variety")
    var variety: String,
    @SerializedName("gross_wt")
    var gross_wt: String,
    @SerializedName("net_wt")
    var net_wt: String,
    @SerializedName("created_date")
    var created_date: String,
    @SerializedName("handler")
    var handler: String,
    @SerializedName("truck_no")
    var truck_no: String,
    @SerializedName("truck_lice")
    var truck_lice: String,
    @SerializedName("semi_lic")
    var semi_lic: String,

    @SerializedName("trailer_no")
    var trailer_no: String,
    @SerializedName("trailer_lic")
    var trailer_lic: String,

    @SerializedName("driver")
    var driver: String,
    @SerializedName("order_type")
    var order_type: String,
    @SerializedName("tare_wt")
    var tare_wt: String,
    @SerializedName("semi_no")
    var semi_no: String,
    @SerializedName("rep_name")
    var rep_name: String






)