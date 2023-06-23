package com.isoft.customalmonds.modelclass

import com.google.gson.annotations.SerializedName


data class handler_response(
    @SerializedName("status")
    var status: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String
)