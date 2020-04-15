package com.bootcamp.bootcampmagic.ui.sets

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.bootcamp.bootcampmagic.R
import com.bootcamp.bootcampmagic.adapter.EndlessScrollListener
import com.bootcamp.bootcampmagic.adapter.GridSpacingItemDecoration
import com.bootcamp.bootcampmagic.adapter.SetsAdapter
import com.bootcamp.bootcampmagic.models.Card
import com.bootcamp.bootcampmagic.models.ListType
import com.bootcamp.bootcampmagic.models.LoadingType
import com.bootcamp.bootcampmagic.repositories.MtgRepository
import com.bootcamp.bootcampmagic.ui.main.ImmersiveActivity
import com.bootcamp.bootcampmagic.ui.tabs.TabbedFragmentDirections
import com.bootcamp.bootcampmagic.utils.App
import com.bootcamp.bootcampmagic.utils.SearchListener
import com.bootcamp.bootcampmagic.viewmodels.sets.SetsViewModel
import com.bootcamp.bootcampmagic.viewmodels.sets.SetsViewModelFactory
import com.bootcamp.bootcampmagic.viewmodels.sets.SetsViewModelState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.collapsing_toolbar.*
import kotlinx.android.synthetic.main.fragment_set.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SetsFragment() : Fragment() {


    private lateinit var adapter: SetsAdapter
    private lateinit var scrollListener: EndlessScrollListener
    private val viewModel: SetsViewModel by viewModels {
        App().let {
            SetsViewModelFactory(
                MtgRepository(it.getCardsDao(), it.getMtgDataSource())
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupObservables()
        setupRecyclerView()
        setupSearch()
    }


    private fun setupObservables() {
        viewModel.getSetsViewModelState().observe(viewLifecycleOwner, Observer { state ->
            if (state == null) return@Observer
            when (state) {
                is SetsViewModelState.Error ->
                    when (state.message) {
                        R.string.generic_network_error -> showNetworkError(state.message)
                    }

                is SetsViewModelState.LoadingState ->
                    when(state.type){
                        LoadingType.LOADING ->{
                            recycler_cards.visibility = View.GONE
                            loadingContent.visibility = View.VISIBLE
                            noContent.visibility = View.GONE
                        }

                        LoadingType.LOADED ->{
                            recycler_cards.visibility = View.VISIBLE
                            loadingContent.visibility = View.GONE
                            noContent.visibility = View.GONE
                        }

                        LoadingType.NO_CONTENT ->{
                            recycler_cards.visibility = View.GONE
                            loadingContent.visibility = View.GONE
                            noContent.visibility = View.VISIBLE
                        }
                    }

                is SetsViewModelState.BackgroundImage ->
                    (activity as ImmersiveActivity).setBackgroundImage(state.url)

                is SetsViewModelState.AddData -> adapter.addItems(state.items)
            }
            viewModel.clearViewState()
        })


        viewModel.getData().observe(viewLifecycleOwner, Observer { items ->
            if (items.isNotEmpty()) {
                adapter.setItems(items)
                viewModel.getSelectedItem().let { selectedItem ->
                    if (viewModel.getSelectedItem() >= 0) {
                        recycler_cards.scrollToPosition(selectedItem)
                        viewModel.setSelectedItem(-1)
                    }
                }

            }
        })
    }


    private fun setupRecyclerView() {
        val listColumns = 3
        val layoutManager = GridLayoutManager(context, listColumns)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.isHeader(position)) layoutManager.spanCount else 1
            }
        }
        recycler_cards.layoutManager = layoutManager
        recycler_cards.addItemDecoration(GridSpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.grid_item_margin)))
        adapter = SetsAdapter(clickListener)
        recycler_cards.adapter = adapter

        scrollListener = object : EndlessScrollListener(recycler_cards) {
            override fun onFirstItem() {
            }
            override fun onScroll() {
            }
            override fun onLoadMore() {
                viewModel.loadMore()
            }
        }

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
            viewModel.refreshData()
        }
    }


    private val clickListener = object : SetsAdapter.OnItemClickListener {
        override fun onItemClicked(card: Card, position: Int) {
            view?.let {

                viewModel.setSelectedItem(position)
                val action = TabbedFragmentDirections
                    .actionTabbedFragmentToOverviewFragment(ListType.SETS.value)
                Navigation.findNavController(it).navigate(action)

            }
        }
    }


    private fun setupSearch(){
        search_cards.addTextChangedListener(object : SearchListener() {
            override fun onSearchChanged(filter: String) {
                CoroutineScope(Dispatchers.Main).launch{
                    viewModel.search(filter)
                }
            }
        })

        btn_cancelar.setOnClickListener {
            search_cards.clearFocus()
            search_cards.setText("")
        }
    }


    private fun showNetworkError(errorMessage: Int) {
        view?.let {
            Snackbar.make(it, errorMessage, Snackbar.LENGTH_LONG)
                .setAction(R.string.try_again) {
                    viewModel.loadMore()
                }
                .show()
        }
    }
}