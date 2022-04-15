package com.hj.store

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hj.store.MainActivity.Companion.CLICK_MENU_EDIT
import com.hj.store.MainActivity.Companion.CLICK_MENU_NEW
import com.hj.store.MainActivity.Companion.INTENT_STORE_MODE
import com.hj.store.data.StoreDetailEditItem
import com.hj.store.viewmodel.StoreDetailViewModel

class StoreDetailEditActivity : AppCompatActivity() {
    private lateinit var storeDetailViewModel: StoreDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_detail_edit)

        storeDetailViewModel = ViewModelProvider(this)[StoreDetailViewModel::class.java]

        val mode = intent.getStringExtra(INTENT_STORE_MODE)

        val titleEditText = findViewById<EditText>(R.id.store_item_title_editTextText)
        val priceEditText = findViewById<EditText>(R.id.store_item_price_editTextText)
        val imageUrlEditText = findViewById<EditText>(R.id.store_item_imageUrl_editTextText)

        val storeId = intent.getIntExtra(MainActivity.INTENT_STORE_ID, -1)
        val itemId = intent.getIntExtra(StoreDetailFragment.INTENT_STORE_ITEM_ID, -1)
        val itemName = intent.getStringExtra(StoreDetailFragment.INTENT_STORE_ITEM_NAME)
        val itemPrice = intent.getIntExtra(StoreDetailFragment.INTENT_STORE_ITEM_PRICE, 0)
        val itemImageUrl = intent.getStringExtra(StoreDetailFragment.INTENT_STORE_ITEM_IMAGEURL)

        when (mode) {
            CLICK_MENU_EDIT -> {
                titleEditText.setText(itemName)
                priceEditText.setText("$itemPrice")
                imageUrlEditText.setText(itemImageUrl)
            }
            CLICK_MENU_NEW -> {
                titleEditText.setText("")
                priceEditText.setText("")
                imageUrlEditText.setText("")
            }
        }

        val saveButton = findViewById<Button>(R.id.store_item_save_button)
        saveButton.setOnClickListener {
            val titleStr = titleEditText.text.toString()
            val priceStr = priceEditText.text.toString()
            val imageUrlStr = imageUrlEditText.text.toString()

            Log.d("아이템", "편집중... $titleStr, $priceStr, $imageUrlStr")
            if (titleStr.isNotEmpty() && priceStr.isNotEmpty() && imageUrlStr.isNotEmpty()) {
                val storeItem = StoreDetailEditItem(titleStr, priceStr, imageUrlStr)
                when (mode) {
                    CLICK_MENU_EDIT -> {
                        storeDetailViewModel.editItem(storeId, itemId, storeItem)
                    }
                    CLICK_MENU_NEW -> {
                        storeDetailViewModel.addItem(storeId, storeItem)
                    }
                }
            }
        }

        val cancelButton = findViewById<Button>(R.id.store_item_cancel_button)
        cancelButton.setOnClickListener {
            finish()
        }

        storeDetailViewModel.storeItemEdit.observe(this) { isEditFinish ->
            if (isEditFinish == true) {
                storeDetailViewModel.resetStoreItemEditState()
                finish()
            }
        }
        storeDetailViewModel.storeItemAdd.observe(this) { isAddFinish ->
            if (isAddFinish == true) {
                storeDetailViewModel.resetStoreItemAddState()
                finish()
            }
        }
    }
}