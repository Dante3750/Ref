package com.globals.netconnect.netconnect.model

class NotificationModel{

    var timeStamp: String? = null
    var titleNot: String? = null
    var body: String? = null

    constructor() {}

    constructor(timeStamp: String, titleNot: String, body: String) {
        this.timeStamp = timeStamp
        this.titleNot = titleNot
        this.body = body

    }

}