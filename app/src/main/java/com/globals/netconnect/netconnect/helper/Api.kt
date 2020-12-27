package com.globals.netconnect.netconnect.helper

import com.globals.netconnect.netconnect.model.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.POST
import retrofit2.http.Multipart


interface Api {
    @FormUrlEncoded
    @POST("login")
    fun userData(
        @Field("employeeId") employeeId: String,
        @Field("password") password: String,
        @Field("tokenId") tokenId: String
    ): Call<LoginResponse>


    @FormUrlEncoded
    @POST("forgotPassword")
    fun forgetPassword(
        @Field("employeeId") employeeId: String
    ): Call<LoginResponse>


    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("employeeId") employeeId: String,
        @Field("emailId") emailId: String,
        @Field("mobileNo") mobileNo: String,
        @Field("password") password: String,
        @Field("tokenId") tokenId: String
    ): Call<LoginResponse>


    @FormUrlEncoded
    @POST("updateRegisteredData")
    fun updateUser(
        @Field("employeeId") employeeId: String,
        @Field("emailId") emailId: String,
        @Field("mobileNo") mobileNo: String,
        @Field("password") password: String
    ): Call<LoginResponse>

//    @Multipart
//    @POST("referee")
//    fun refereeData(
//        @Part("employeeId") employeeId: Int,
//        @Part("firstName") firstName: String,
//        @Part("lastName") lastName: String,
//        @Part("middleName") middleName: String,
//        @Part("mobileNumber") mobileNumber: String,
//        @Part("emailId") emailId: String,
//
//        @PartMap resume: Map<String, RequestBody>,
//        @Part("jobId") jobId: Int
//
//    ): Call<ServerResponse>

//
//    @Multipart
//    @POST("referee")
//    fun uploadFile(
//
//
//
//        @Part("resume") name: HashMap<String, RequestBody>,
//        @Part("employeeId") employeeId: Int,
//        @Part("firstName") firstName: String,
//        @Part("lastName") lastName: String,
//        @Part("middleName") middleName: String,
//        @Part("mobileNumber") mobileNumber: String,
//        @Part("emailId") emailId: String,
//        @Part("jobId") jobId: Int
//        ): Call<ServerResponse>


    @Multipart
    @POST("referee")
    fun uploadFile(
        @Part resume: MultipartBody.Part,
        @Part("employeeId") employeeId: Int,
        @Part("firstName") firstName: String,
        @Part("lastName") lastName: String,
        @Part("middleName") middleName: String,
        @Part("mobileNumber") mobileNumber: String,
        @Part("emailId") emailId: String,
        @Part("jobId") jobId: Int
    ): Call<ServerResponse>


}