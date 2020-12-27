package com.globals.netconnect.netconnect.model

/**
 * Created by Lincoln on 15/01/16.
 */
class Job {
    var rewardsAmount: String? = null
    var jobId: String? = null
    var role: String? = null
    var requisition: String? = null
    var location: String? = null

    constructor() {}

    constructor(rewardsAmount: String, jobId: String, role: String,requisition: String, location: String) {
        this.rewardsAmount = rewardsAmount
        this.jobId = jobId
        this.role = role
        this.requisition = requisition
        this.location = location
    }
}
