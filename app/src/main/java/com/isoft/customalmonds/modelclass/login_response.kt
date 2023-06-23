package com.isoft.customalmonds.modelclass

import com.google.gson.annotations.SerializedName


data class login_response(
    @SerializedName("status")
    var status: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("id")
    var id: String
)