package com.bootcamp.bootcampmagic.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.models.CardsResponse
import com.bootcamp.bootcampmagic.repositories.CardsRepository
import com.bootcamp.bootcampmagic.utils.CoroutinesTestRule
import com.bootcamp.bootcampmagic.utils.ListUtils
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.HttpURLConnection

class SetsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    private val mockedRepository = mockk<CardsRepository>()
    private lateinit var setsViewModel: SetsViewModel


    /*@Before
    fun setup(){
        every {
            mockedRepository.getCards(1)
        } returns CardsResponse(ListUtils.createCardsList(0), HttpURLConnection.HTTP_OK)

        setsViewModel = SetsViewModel(mockedRepository)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun whenPage1Requested_shouldReturnCardsList(){
        runBlockingTest {

            val mockedItems = 13
            every {
                mockedRepository.getCards(any())
            } returns CardsResponse(ListUtils.createCardsList(mockedItems), HttpURLConnection.HTTP_OK)

            setsViewModel.loadCards()

            assertEquals(
                setsViewModel.getData().value?.size,
                mockedItems)
        }
    }

    @Test
    fun whenRefreshRequested_shouldReturnCardsList(){
        var currentPage = 1
        val mockedItems = 11
        every {
            mockedRepository.getCards(any())
        } returns CardsResponse(ListUtils.createCardsList(mockedItems), HttpURLConnection.HTTP_OK)


        //Page 1
        setsViewModel.refresh()
        assertEquals(
            setsViewModel.getData().value?.size,
            (mockedItems * currentPage))
        assertEquals(
            setsViewModel.getPage(),
            currentPage)


        //Page 2
        currentPage = 2
        setsViewModel.loadCards()
        assertEquals(
            setsViewModel.getData().value?.size,
            (mockedItems * currentPage))
        assertEquals(
            setsViewModel.getPage(),
            currentPage)


        //Page 1 refreshed
        currentPage = 1
        setsViewModel.refresh()
        assertEquals(
            setsViewModel.getData().value?.size,
            (mockedItems * currentPage))
        assertEquals(
            setsViewModel.getPage(),
            currentPage)


    }

    @Test
    fun whenConnectionError_shouldDisplayError(){
        every {
            mockedRepository.getCards(any())
        } returns CardsResponse(ListUtils.createCardsList(0), HttpURLConnection.HTTP_BAD_REQUEST)

        setsViewModel.loadCards()

        assertEquals(
            setsViewModel.getViewState().value,
            SetsViewModelState.Error(R.string.generic_network_error)
        )
    }*/

}