package com.pixelperfectsoft.tcg_nexus.model.classes

data class News(
    var id: Int,
    var image: String,
    var title: String,
    var link: String,
    var provider: String,
    var date: String,
)
