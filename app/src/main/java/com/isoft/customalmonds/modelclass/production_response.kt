package com.isoft.customalmonds.modelclass

import com.google.gson.annotations.SerializedName


data class production_response(
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
    @SerializedName("created_date")
    var created_date: String,
    @SerializedName("viewstatus")
    var viewstatus: String,
    @SerializedName("grs_bin_tag")
    val grs_bin_tag: List<grs_bin_tag>,
    @SerializedName("sub_product")
    val sub_product: List<sub_production_response>,
    @SerializedName("rep_name")
    var rep_name: String
)