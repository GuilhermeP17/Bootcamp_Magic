package com.bootcamp.bootcampmagic.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bootcamp.bootcampmagic.utils.FileUtils
import com.bootcamp.bootcampmagic.utils.RetrofitInitializer
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection

class CardsRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private var mockedServer = MockWebServer()
    private lateinit var dataSource: CardsDataSource
    private val databaseDao = mockk<CardsDao>()
    private lateinit var repository: CardsRepository


    @Before
    fun setup(){
        mockedServer.start()
        dataSource = RetrofitInitializer.getCardsDataSource(mockedServer.url("/").toString())
        every {
            databaseDao.updateData(any())
        } just Runs


        repository = CardsRepository(dataSource, databaseDao)
    }

    @After
    fun teardown() {
        mockedServer.shutdown()
    }


    @Test
    fun givenPageNumber_whenGetCardsRequestedOffline_shouldReturnCachedCardsList(){

        //First access, should write to cache
        var mockedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(FileUtils.readResourceFile("getCards.json"))
        mockedServer.enqueue(mockedResponse)

        val onlineItems = repository.getCards(1).cards
        every {
            databaseDao.getAll()
        } returns onlineItems


        //Second access, should be offline, and access cached items
        mockedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_BAD_GATEWAY)
        mockedServer.enqueue(mockedResponse)


        val cachedItems = repository.getCards(1).cards
        assertEquals(cachedItems.size, onlineItems.size)
        assertEquals(cachedItems[2].name , onlineItems[2].name)
    }


    @Test
    fun givenPageNumber_whenGetCardsRequested_shouldReturnCardsList(){
        val mockedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(FileUtils.readResourceFile("getCards.json"))
        mockedServer.enqueue(mockedResponse)

        val response = repository.getCards(10)
        assertTrue(response.errorCode == HttpURLConnection.HTTP_OK)
        assertTrue(response.cards.isNotEmpty())
    }


    @Test
    fun givenPageNumberAndFilter_whenSearchCardsRequested_shouldReturnCardsList(){
        val mockedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(FileUtils.readResourceFile("searchCards.json"))
        mockedServer.enqueue(mockedResponse)

        val response = repository.searchCards(10, "Test")
        assertTrue(response.errorCode == HttpURLConnection.HTTP_OK)
        assertTrue(response.cards.isNotEmpty())
    }

}