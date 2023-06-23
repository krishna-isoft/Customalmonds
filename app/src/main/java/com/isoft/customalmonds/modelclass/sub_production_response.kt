package com.isoft.customalmonds.modelclass

import com.google.gson.annotations.SerializedName
data class sub_production_response(
    @SerializedName("status")
    var status: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("pid")
    var pid: String,
    @SerializedName("type")
    var type: String,
    @SerializedName("grower")
    var grower: String,
    @SerializedName("ranch_name")
    var ranch_name: String,
    @SerializedName("variety")
    var variety: String,
    @SerializedName("field_ticket")
    var field_ticket: String,
    @SerializedName("total_netwt")
    var total_netwt: String,
    @SerializedName("viewstatus")
    var viewstatus: String,
    @SerializedName("created_date")
    var created_date: String,
    @SerializedName("sub_grs_bin_tag")
    val sub_grs_bin_tag: List<sub_grs_bin_tag>,
    @SerializedName("rep_name")
    var rep_name: String
)