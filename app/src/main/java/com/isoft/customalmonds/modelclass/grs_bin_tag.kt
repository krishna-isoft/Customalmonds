package com.isoft.customalmonds.modelclass

import com.google.gson.annotations.SerializedName


data class grs_bin_tag(


    @SerializedName("tagno")
    var tagno: String,
    @SerializedName("handler")
    var handler: String,
    @SerializedName("gross_wt")
    var gross_wt: String,
    @SerializedName("tare")
    var tare: String,
    @SerializedName("net_wt")
    var net_wt: String,
    @SerializedName("slid")
    var slid: String,
    @SerializedName("created_date")
    var created_date: String,
    @SerializedName("rep_name")
    var rep_name: String,
    @SerializedName("type")
    var type: String
)