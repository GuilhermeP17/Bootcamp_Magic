package com.bootcamp.bootcampmagic.repositories

import com.bootcamp.bootcampmagic.utils.FileUtils
import com.bootcamp.bootcampmagic.utils.RetrofitInitializer
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class CardsDataSourceTest {

    private var mockedServer = MockWebServer()
    private lateinit var dataSource: CardsDataSource


    @Before
    fun setup(){
        mockedServer.start()
        dataSource = RetrofitInitializer.getCardsDataSource(mockedServer.url("/").toString())
    }

    @After
    fun teardown() {
        mockedServer.shutdown()
    }


    @Test
    fun givenPage1_whenGetCardsRequested_shouldReturnCards(){

        val mockedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(FileUtils.readResourceFile("getCards.json"))
        mockedServer.enqueue(mockedResponse)

        val response = dataSource.getCards(1).execute()
        assertTrue(response.body() != null)
        assertTrue(response.body()!!.cards.size == 100)

    }


    @Test
    fun givenCardName_whenSearchCardsRequested_shouldReturnCards(){

        val mockedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(FileUtils.readResourceFile("searchCards.json"))
        mockedServer.enqueue(mockedResponse)

        val response = dataSource.searchCards(1, "test").execute()
        assertTrue(response.body() != null)
        assertTrue(response.body()!!.cards.isNotEmpty())

    }

}