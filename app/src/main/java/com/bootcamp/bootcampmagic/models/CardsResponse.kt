package com.bootcamp.bootcampmagic.models

import java.net.HttpURLConnection

data class CardsResponse(
    var cards: List<Card>,
    var errorCode: Int = HttpURLConnection.HTTP_OK
)