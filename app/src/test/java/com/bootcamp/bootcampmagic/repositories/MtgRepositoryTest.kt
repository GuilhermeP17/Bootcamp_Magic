package com.bootcamp.bootcampmagic.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bootcamp.bootcampmagic.utils.CoroutinesTestRule
import com.bootcamp.bootcampmagic.utils.FileUtils
import com.bootcamp.bootcampmagic.utils.RetrofitInitializer
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import java.net.HttpURLConnection

class MtgRepositoryTest{

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    @ExperimentalCoroutinesApi
    var coroutinesTestRule = CoroutinesTestRule()

    private lateinit var repository: MtgRepository
    private lateinit var dataSource: MtgDataSource
    private val mockedServer = MockWebServer()
    private val database: CardsDao = mockk()


    @Before
    @ExperimentalCoroutinesApi
    fun setup(){
        mockedServer.start()
        dataSource = RetrofitInitializer.getMtgDataSource(mockedServer.url("/").toString())
        every {
            database.updateData(any())
        } just Runs
        repository = MtgRepository(database, dataSource, coroutinesTestRule.testDispatcherProvider)
    }

    @After
    fun teardown() {
        mockedServer.shutdown()
    }


    @Test
    @ExperimentalCoroutinesApi
    fun whenGetSetsRequested_shouldReturnSetsResponse(){
        coroutinesTestRule.testDispatcher.runBlockingTest{
            val mockedResponse = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(FileUtils.readResourceFile("getSets.json"))
            mockedServer.enqueue(mockedResponse)


            val response = repository.getSets()


            Assert.assertTrue(response.code == HttpURLConnection.HTTP_OK)
            Assert.assertTrue(response.sets.size == 469)

        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun whenGetCardsRequested_shouldReturnCardsResponse(){
        coroutinesTestRule.testDispatcher.runBlockingTest{
            val mockedResponse = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(FileUtils.readResourceFile("getCards.json"))
            mockedServer.enqueue(mockedResponse)


            val response = repository.getCards(1, "set1", false)


            Assert.assertTrue(response.code == HttpURLConnection.HTTP_OK)
            Assert.assertTrue(response.cards.size == 100)

        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun whenSearchCardsRequested_shouldReturnCardsResponse(){
        coroutinesTestRule.testDispatcher.runBlockingTest{
            val mockedResponse = MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(FileUtils.readResourceFile("searchCards.json"))
            mockedServer.enqueue(mockedResponse)


            val response = repository.searchCards(1, "set1")


            Assert.assertTrue(response.code == HttpURLConnection.HTTP_OK)
            Assert.assertTrue(response.cards.isNotEmpty())

        }
    }

}