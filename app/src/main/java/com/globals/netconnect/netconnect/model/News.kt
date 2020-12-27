package com.globals.netconnect.netconnect.model

class News {
    var news: String? = null
    var dateNews: String? = null
    var image: String? = null
    var titleNews: String? = null

    constructor() {}

    constructor(news: String, dateNews: String, image: String,titleNews: String) {
        this.news = news
        this.dateNews = dateNews
        this.image = image
        this.titleNews = titleNews
    }

}