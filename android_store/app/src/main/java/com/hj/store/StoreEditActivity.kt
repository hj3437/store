package com.hj.store

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.hj.store.MainActivity.Companion.CLICK_MENU_EDIT
import com.hj.store.MainActivity.Companion.CLICK_MENU_NEW
import com.hj.store.MainActivity.Companion.INTENT_STORE_ADDRESS
import com.hj.store.MainActivity.Companion.INTENT_STORE_ID
import com.hj.store.MainActivity.Companion.INTENT_STORE_IMAGEURL
import com.hj.store.MainActivity.Companion.INTENT_STORE_MODE
import com.hj.store.MainActivity.Companion.INTENT_STORE_NAME
import com.hj.store.data.Store
import com.hj.store.viewmodel.StoreEditViewModel

class StoreEditActivity : AppCompatActivity() {
    private lateinit var storeEditViewModel: StoreEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_edit)

        storeEditViewModel = ViewModelProvider(this)[StoreEditViewModel::class.java]

        val titleEditText = findViewById<EditText>(R.id.store_edit_title_editTextText)
        val addressEditText = findViewById<EditText>(R.id.store_edit_address_editTextText)
        val imageUrlEditText = findViewById<EditText>(R.id.store_edit_imageUrl_editTextText)

        val saveButton = findViewById<Button>(R.id.store_edit_save_button)

        val storeId = intent.getIntExtra(INTENT_STORE_ID, -1)
        val storeName = intent.getStringExtra(INTENT_STORE_NAME)
        val storeAddress = intent.getStringExtra(INTENT_STORE_ADDRESS)
        val storeImageUrl = intent.getStringExtra(INTENT_STORE_IMAGEURL)

        when (intent.getStringExtra(INTENT_STORE_MODE)) {
            CLICK_MENU_NEW -> {
                titleEditText.setText("")
                addressEditText.setText("")
                imageUrlEditText.setText("")
            }
            CLICK_MENU_EDIT -> {
                titleEditText.setText(storeName)
                addressEditText.setText(storeAddress)
                imageUrlEditText.setText(storeImageUrl)
            }
        }

        saveButton.setOnClickListener {
            val titleStr = titleEditText.text.toString()
            val addressStr = addressEditText.text.toString()
            val imageUrlStr = imageUrlEditText.text.toString()

            if (titleStr.isNotEmpty() && addressStr.isNotEmpty() && imageUrlStr.isNotEmpty()) {
                val newStore = Store(
                    id = storeId,
                    name = titleStr,
                    address = addressStr,
                    imageUrl = imageUrlStr
                )

                when (intent.getStringExtra(INTENT_STORE_MODE)) {
                    CLICK_MENU_NEW -> {
                        storeEditViewModel.addStore(newStore)
                    }
                    CLICK_MENU_EDIT -> {
                        storeEditViewModel.editStore(storeId, newStore)
                    }
                }
            }
        }

        findViewById<Button>(R.id.store_edit_cancel_button).setOnClickListener {
            finish()
        }

        storeEditViewModel.storeAdd.observe(this) { isAddFinish ->
            if (isAddFinish == true) {
                storeEditViewModel.resetStoreAddState()
                finish()
            }
        }

        storeEditViewModel.storeEdit.observe(this) { isEditFinish ->
            if (isEditFinish == true) {
                storeEditViewModel.resetStoreEditState()
                finish()
            }
        }
    }
}