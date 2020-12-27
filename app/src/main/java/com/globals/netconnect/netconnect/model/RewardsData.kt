package com.globals.netconnect.netconnect.model

/**
 * Created by Lincoln on 15/01/16.
 */
class RewardsData {
    var reward: String? = null
    var redemptionDate: String? = null
    var diffDays: Int? = null
    var textColour: String? = null
    var ProgressBarTrackColor: String? = null
    var rating: String? = null
    var ProgressBarTintColor: String? = null
    var refereeName: String? = null
    var backgroundColour: String? = null

    constructor() {}

    constructor(reward: String, redemptionDate: String, diffDays: Int,textColour:String,ProgressBarTrackColor: String, rating: String,ProgressBarTintColor: String, refereeName: String, backgroundColour: String) {
        this.reward = reward
        this.redemptionDate = redemptionDate
        this.diffDays = diffDays
        this.textColour = textColour
        this.ProgressBarTrackColor = ProgressBarTrackColor
        this.rating = rating
        this.ProgressBarTintColor = textColour
        this.refereeName = ProgressBarTrackColor
        this.backgroundColour = rating
    }
}
