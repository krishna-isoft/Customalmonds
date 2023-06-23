package com.isoft.customalmonds.api

import com.isoft.customalmonds.modelclass.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AlmondsApi {
//    @GET("login.php?cc=ca")
//    suspend fun getQLogin() : Response<login_response>

    @GET("login.php")
    fun getQLogin(
        @Query("cc") cc: String?,
        @Query("user_id") user_id: String?,
        @Query("password") pwd: String?
    ): Call<login_response?>?

    @GET("all_production.php")
    fun getproductionvalue(
        @Query("cc") cc: String?,
        @Query("user_id") user_id: String?
    ): Call<List<production_response>?>?
    @GET("search_production.php")
    fun getproductionvaluesearch(
        @Query("cc") cc: String?,@Query("id") id: String?,
        @Query("user_id") user_id: String?
    ): Call<List<production_response>?>?

    @GET("add_bintag.php")
    fun savebintag(
        @Query("cc") cc: String?,
        @Query("runno") runno: String?,
        @Query("handler") handler: String?,
        @Query("grswt") grswt: String?,
        @Query("tare") tare: String?,
        @Query("netwet") netwet: String?,
        @Query("user_id") user_id: String?,
        @Query("type") type: String?
    ): Call<List<bittagsave_response>?>?



    @GET("handler_det.php")
    fun gethandler(
        @Query("cc") cc: String?
    ): Call<List<handler_response>?>?

    @GET("variety_det.php")
    fun getvariety(
        @Query("cc") cc: String?
    ): Call<List<handler_response>?>?

    @GET("gower_det.php")
    fun getgrower(
        @Query("cc") cc: String?
    ): Call<List<handler_response>?>?

    @GET("get_range.php")
    fun getranch(
        @Query("cc") cc: String?,
        @Query("gid") gid: String?
    ): Call<List<handler_response>?>?



    @GET("all_field_ticket.php")
    fun getfieldvalue(
        @Query("cc") cc: String?,
        @Query("user_id") user_id: String?
    ): Call<List<field_response>?>?


    @GET("search_field_ticket.php")
    fun getfieldvaluesearch(
        @Query("cc") cc: String?,@Query("id") id: String?,
        @Query("user_id") user_id: String?
    ): Call<List<field_response>?>?





    @GET("save_field_ticket.php")
    fun savefieldticket(
        @Query("cc") cc: String?,
        @Query("grower") grower: String?,
        @Query("field") field: String?,
        @Query("variety") variety: String?,
        @Query("handler") handler: String?,
        @Query("truck") truck: String?,
        @Query("trucklic") trucklic: String?,
        @Query("semi") semi: String?,
        @Query("semilic") semilic: String?,
        @Query("trail") trail: String?,
        @Query("traillic") traillic: String?,
        @Query("driver") driver: String?,
        @Query("order_type") order_type: String?,
        @Query("user_id") user_id: String?,
        @Query("gross") gross: String?,
        @Query("tare") tare: String?,
        @Query("netwt") netwt: String?,
        @Query("fticket") fticket: String?

    ): Call<fticketsave_response>?




    @GET("add_sub_bintag.php")
    fun savebintagsub(
        @Query("cc") cc: String?,
        @Query("runno") runno: String?,
        @Query("handler") handler: String?,
        @Query("grswt") grswt: String?,
        @Query("tare") tare: String?,
        @Query("netwet") netwet: String?,
        @Query("user_id") user_id: String?,
        @Query("type") type: String?
    ): Call<List<bittagsave_response>?>?




}