package com.isoft.customalmonds.modelclass

import com.google.gson.annotations.SerializedName


data class bittagsave_response(
    @SerializedName("status")
    var status: String,
    @SerializedName("slid")
    var slid: String,
    @SerializedName("runno")
    var runno: String,
    @SerializedName("tagno")
    var tagno: String,
    @SerializedName("create_date")
    var create_date: String,
    @SerializedName("message")
    var message: String



)