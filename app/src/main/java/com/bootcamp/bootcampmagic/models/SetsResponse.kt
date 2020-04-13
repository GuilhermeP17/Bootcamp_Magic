package com.bootcamp.bootcampmagic.models

import java.net.HttpURLConnection

data class SetsResponse(
    var sets: List<CardSet>,
    var code: Int = HttpURLConnection.HTTP_UNAVAILABLE
)