package com.hj.store

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hj.store.adapter.OnStoreClickListener
import com.hj.store.adapter.StoreAdapter
import com.hj.store.data.Store
import com.hj.store.data.StoreListWithLogin
import com.hj.store.viewmodel.SearchViewModel
import com.hj.store.viewmodel.StoreViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var storeList: RecyclerView
    private lateinit var storeAdapter: StoreAdapter
    private lateinit var searchView: SearchView

    private lateinit var storeViewModel: StoreViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var toolbar: Toolbar

    private var storeMenu: Menu? = null

    companion object {
        const val USER_LOGIN = 1
        const val USER_GUEST = -1
        const val CLICK_MENU_NEW = "new"
        const val CLICK_MENU_EDIT = "edit"
        const val CLICK_MENU_DELETE = "delete"
        const val CLICK_STORE = "detail"

        const val INTENT_STORE_MODE = "storeMode"
        const val INTENT_STORE_ID = "storeId"
        const val INTENT_STORE_NAME = "storeName"
        const val INTENT_STORE_ADDRESS = "storeAddress"
        const val INTENT_STORE_IMAGEURL = "storeImageUrl"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.store_toolbar)
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)

        // up 버튼
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.watermelon_24x24)

        toolbar.setNavigationOnClickListener {
            supportFragmentManager.popBackStack()
        }

        storeList = findViewById(R.id.store_list)

        storeAdapter = StoreAdapter(OnStoreClickListener { store, mode ->
            when (mode) {
                CLICK_STORE -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, StoreDetailFragment.newInstance(store))
                        .addToBackStack(null)
                        .commit()
                }
                CLICK_MENU_EDIT -> {
                    askUserToEditMode(store)
                }
                CLICK_MENU_DELETE -> {
                    AlertDialog.Builder(this)
                        .setMessage(getString(R.string.ask_user_store_delete))
                        .setPositiveButton(getString(R.string.menu_delete)) { _, _ ->
                            storeViewModel.deleteStore(store.id)
                        }
                        .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                        }.create()
                        .show()
                }
            }
        })

        val gridLayoutManager = GridLayoutManager(this, 2)
        storeList.apply {
            layoutManager = gridLayoutManager
            adapter = storeAdapter
            hasFixedSize()
        }

        storeViewModel = ViewModelProvider(this)[StoreViewModel::class.java]
        storeViewModel.store.observe(this) { stores ->
            val convertStoreWithLogin = convertStoreWithLogin(stores)
            storeAdapter.submitList(convertStoreWithLogin)
        }

        storeViewModel.storeRemove.observe(this) { isDelete ->
            if (isDelete == true) {
                storeViewModel.getStores()
                storeViewModel.resetStoreStatus()
            }
        }

        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        searchViewModel.searchStore.observe(this) { stores ->
            val convertStoreWithLogin = convertStoreWithLogin(stores)
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.container,
                    StoreSearchResultFragment.newInstance(convertStoreWithLogin)
                )
                .addToBackStack(null)
                .commit()
        }
    }

    private fun convertStoreWithLogin(stores: List<Store>): List<StoreListWithLogin> {
        var isLogin = false
        if (getLoginStatus() == 1) {
            isLogin = true
        }

        return stores.map { store ->
            StoreListWithLogin(store.id, store.name, store.address, store.imageUrl, isLogin)
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (getLoginStatus() == USER_LOGIN) {
            menu?.findItem(R.id.app_bar_login)?.isVisible = false
            menu?.findItem(R.id.app_bar_logout)?.isVisible = true
            menu?.findItem(R.id.app_bar_new_store)?.isVisible = true
        } else {
            menu?.findItem(R.id.app_bar_login)?.isVisible = true
            menu?.findItem(R.id.app_bar_logout)?.isVisible = false
            menu?.findItem(R.id.app_bar_new_store)?.isVisible = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.store_menu_item, menu)
        storeMenu = menu

        if (getLoginStatus() == USER_LOGIN) {
            menu?.findItem(R.id.app_bar_login)?.isVisible = false
            menu?.findItem(R.id.app_bar_logout)?.isVisible = true
        } else {
            menu?.findItem(R.id.app_bar_login)?.isVisible = true
            menu?.findItem(R.id.app_bar_logout)?.isVisible = false
        }

        val appBarSearch = menu?.findItem(R.id.app_bar_search)
        searchView = appBarSearch?.actionView as SearchView
        searchView.apply {
            // 서치뷰 width 설정 없을경우 '타이틀...' 보여짐
            maxWidth = Integer.MAX_VALUE
            queryHint = context.getString(R.string.store_search)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.length!! >= 1) {
                    searchStore(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length!! >= 2) {
                    searchStore(newText)
                }
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_login -> {
                val newInstance = StoreLoginFragment.newInstance(OnLoginListener { islogin ->
                    if (islogin) {
                        storeViewModel.store.value.let { stores ->
                            if (stores != null) {
                                val convertStoreWithLogin = convertStoreWithLogin(stores)
                                storeAdapter.submitList(convertStoreWithLogin)
                            }
                        }
                    }
                })
                supportFragmentManager.popBackStack()

                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, newInstance)
                    .addToBackStack(null)
                    .commit()
            }
            R.id.app_bar_logout -> {
                setLogout()
                storeViewModel.store.value.let { stores ->
                    if (stores != null) {
                        val convertStoreWithLogin = convertStoreWithLogin(stores)
                        storeAdapter.submitList(convertStoreWithLogin)
                    }
                }
                supportFragmentManager.popBackStack()
                invalidateOptionsMenu()
            }
            R.id.app_bar_refresh -> {
                refresh()
            }
            R.id.app_bar_new_store -> {
                val intent = Intent(this, StoreEditActivity::class.java)
                intent.putExtra("storeMode", CLICK_MENU_NEW)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            hideKeyboard()
        } else {
            super.onBackPressed()
        }
    }

    private fun refresh() {
        storeViewModel.getStores()
        invalidateOptionsMenu()
    }

    private fun hideKeyboard() {
        val keyboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val container = findViewById<ConstraintLayout>(R.id.container)
        keyboard.hideSoftInputFromWindow(container.windowToken, 0)
    }

    private fun searchStore(query: String?) {
        val str = query ?: ""
        if (str.isNotEmpty()) {
            searchViewModel.searchStoreItems(str)
        }
    }

    private fun getLoginStatus(): Int {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val defaultValue = resources.getInteger(R.integer.guestuser_default_key)
        return sharedPref.getInt(getString(R.string.saved_user_login_key), defaultValue)
    }

    private fun setLogout() {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt(getString(R.string.saved_user_login_key), USER_GUEST)
            apply()
        }
    }

    private fun askUserToEditMode(store: StoreListWithLogin) {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.ask_user_store_edit))
            .setPositiveButton(getString(R.string.menu_edit)) { _, _ ->
                val intent = Intent(this, StoreEditActivity::class.java)
                intent.putExtra(INTENT_STORE_MODE, CLICK_MENU_EDIT)
                intent.putExtra(INTENT_STORE_ID, store.id)
                intent.putExtra(INTENT_STORE_NAME, store.name)
                intent.putExtra(INTENT_STORE_ADDRESS, store.address)
                intent.putExtra(INTENT_STORE_IMAGEURL, store.imageUrl)
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }.create()
            .show()
    }
}