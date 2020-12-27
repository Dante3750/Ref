package com.globals.netconnect.netconnect.model

class ReferralData {
    var date: String? = null
    var refereeStatus: String? = null
    var role: String? = null
    var name: String? = null
    var emailId: String? = null
    var textcolor: String? = null

    constructor() {}

    constructor(date: String, refereeStatus: String, role: String, name: String, emailId: String, textcolor: String) {
        this.date = date
        this.refereeStatus = refereeStatus
        this.role = role
        this.name = name
        this.emailId = emailId
        this.textcolor = textcolor

    }
}