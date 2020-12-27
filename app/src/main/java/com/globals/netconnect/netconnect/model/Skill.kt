package com.globals.netconnect.netconnect.model

/**
 * Created by Lincoln on 15/01/16.
 */
class Skill {
    var backgroundColor: String? = null
    var textColour: String? = null
    var skill: String? = null

    constructor() {}

    constructor(backgroundColor: String, textColour: String, skill: String) {
        this.backgroundColor = backgroundColor
        this.textColour = textColour
        this.skill = skill
    }
}
