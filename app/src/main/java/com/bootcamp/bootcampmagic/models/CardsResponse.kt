package com.bootcamp.bootcampmagic.models

import java.net.HttpURLConnection

data class CardsResponse(
    var cards: List<Card>,
    var code: Int = HttpURLConnection.HTTP_UNAVAILABLE
)