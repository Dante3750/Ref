package com.globals.netconnect.netconnect.helper

import com.google.gson.annotations.SerializedName

class ServerResponse {


    @SerializedName("success")
    var success: Boolean = false
        internal set
    @SerializedName("message")
    var message: String? = null
        internal set

}