package com.hj.store

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hj.store.adapter.OnStoreClickListener
import com.hj.store.adapter.SearchResultAdapter
import com.hj.store.data.StoreListWithLogin
import com.hj.store.viewmodel.SearchViewModel

class StoreSearchResultFragment(private val stores: List<StoreListWithLogin>) : Fragment() {
    private lateinit var rootView: View
    private lateinit var searchResultList: RecyclerView
    private lateinit var searchResultAdapter: SearchResultAdapter
    private lateinit var closeButton: TextView

    private lateinit var searchViewModel: SearchViewModel

    companion object {
        fun newInstance(stores: List<StoreListWithLogin>) = StoreSearchResultFragment(stores)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.store_search_result_fragment, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        searchResultList = rootView.findViewById(R.id.search_result_list)
        searchResultAdapter = SearchResultAdapter(OnStoreClickListener { store, _ ->
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()

            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.container, StoreDetailFragment.newInstance(store))
                ?.addToBackStack(null)
                ?.commit()

            hideKeyboard()
        })

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        searchResultList.apply {
            layoutManager = gridLayoutManager
            adapter = searchResultAdapter
            hasFixedSize()
        }

        searchResultAdapter.submitList(stores)

        closeButton = rootView.findViewById(R.id.search_result_close_button)
        closeButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    private fun hideKeyboard() {
        val keyboard = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val container = rootView.findViewById<ConstraintLayout>(R.id.search_result_container)
        keyboard.hideSoftInputFromWindow(container.windowToken, 0)
    }
}