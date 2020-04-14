package com.bootcamp.bootcampmagic.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bootcamp.bootcampmagic.repositories.MtgRepository
import com.bootcamp.bootcampmagic.utils.CoroutinesTestRule
import com.bootcamp.bootcampmagic.utils.ListUtils
import com.bootcamp.bootcampmagic.viewmodels.sets.SetsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SetsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    @ExperimentalCoroutinesApi
    var coroutinesTestRule = CoroutinesTestRule()


    private val repository: MtgRepository = mockk()
    private lateinit var viewModel: SetsViewModel


    @Before
    @ExperimentalCoroutinesApi
    fun setup(){
        coEvery {
            repository.getCachedCards()
        } returns ListUtils.createCardsList(10)

        coroutinesTestRule.testDispatcher.runBlockingTest{
            viewModel = SetsViewModel(
                repository,
                coroutinesTestRule.testDispatcherProvider
            )
        }
    }


    @Test
    fun whenLoadMoreRequested_shouldUpdateLiveData(){

    }












    /*

    private val mockedRepository = mockk<CardsRepository>()
    private lateinit var setsViewModel: SetsViewModel


    @ExperimentalCoroutinesApi
    @Before
    fun setup(){
        coEvery {
            mockedRepository.getCache()
        } returns ListUtils.createCardsList(13)

        coroutinesTestRule.testDispatcher.runBlockingTest{
            setsViewModel = SetsViewModel(mockedRepository, coroutinesTestRule.testDispatcherProvider)
        }
    }


    @ExperimentalCoroutinesApi
    @Test
    fun whenPageRequested_shouldReturnCardsList(){
        coroutinesTestRule.testDispatcher.runBlockingTest{

            val mockedItems = 10
            val totalItems = (setsViewModel.getData().value?.size?.plus(mockedItems))
            coEvery {
                mockedRepository.getCards(any())
            } returns CardsResponse(ListUtils.createCardsList(mockedItems), HttpURLConnection.HTTP_OK)

            setsViewModel.loadCards()

            assertEquals(
                setsViewModel.getData().value?.size,
                totalItems)

        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun whenRefreshRequested_shouldReturnCardsList(){
        coroutinesTestRule.testDispatcher.runBlockingTest{

            var currentPage = 1
            val mockedItems = 11
            coEvery {
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
    }

    @ExperimentalCoroutinesApi
    @Test
    fun whenConnectionError_shouldDisplayError(){
        coroutinesTestRule.testDispatcher.runBlockingTest{

            coEvery {
                mockedRepository.getCards(any())
            } returns CardsResponse(ListUtils.createCardsList(0), HttpURLConnection.HTTP_BAD_REQUEST)

            setsViewModel.loadCards()

            assertEquals(
                setsViewModel.getViewState().value,
                SetsViewModelState.Error(R.string.generic_network_error)
            )

        }
    }*/

}