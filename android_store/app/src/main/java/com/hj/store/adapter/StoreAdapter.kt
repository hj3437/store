package com.hj.store.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hj.store.MainActivity
import com.hj.store.MainActivity.Companion.CLICK_STORE
import com.hj.store.R
import com.hj.store.data.StoreListWithLogin


class StoreAdapter(private val clickListener: OnStoreClickListener) :
    ListAdapter<StoreListWithLogin, StoreAdapter.StoreViewHolder>(StoreDiffUtil) {

    class StoreViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
        fun bind(store: StoreListWithLogin, clickListener: OnStoreClickListener) {
            val storeImageView = rootView.findViewById<ImageView>(R.id.store_list_imageView)

            Glide.with(rootView.context)
                .load(store.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .fallback(R.drawable.ic_placeholder)
                .into(storeImageView)

            val titleTextView = rootView.findViewById<TextView>(R.id.store_list_title_textVIew)
            titleTextView.text = store.name

            val addressTextView = rootView.findViewById<TextView>(R.id.store_list_address_textView)
            addressTextView.text = store.address

            val editTextView = rootView.findViewById<TextView>(R.id.store_list_edit_textView)
            val deleteTextView =
                rootView.findViewById<TextView>(R.id.store_list_delete_textView)

            if (store.isLogin) {
                editTextView.visibility = View.VISIBLE

                editTextView.setOnClickListener {
                    clickListener.onClick(store, MainActivity.CLICK_MENU_EDIT)
                }

                deleteTextView.visibility = View.VISIBLE

                deleteTextView.setOnClickListener {
                    clickListener.onClick(store, MainActivity.CLICK_MENU_DELETE)
                }
            } else {
                editTextView.visibility = View.INVISIBLE
                deleteTextView.visibility = View.INVISIBLE
            }

            storeImageView.setOnClickListener {
                clickListener.onClick(store, CLICK_STORE)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rootView = inflater.inflate(R.layout.store_list_item, parent, false)
        return StoreViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}

object StoreDiffUtil : DiffUtil.ItemCallback<StoreListWithLogin>() {
    override fun areItemsTheSame(oldItem: StoreListWithLogin, newItem: StoreListWithLogin) = oldItem.isLogin == newItem.isLogin

    override fun areContentsTheSame(oldItem: StoreListWithLogin, newItem: StoreListWithLogin) = oldItem == newItem
}

class OnStoreClickListener(val clickListener: (store: StoreListWithLogin, mode: String) -> Unit) {
    fun onClick(store: StoreListWithLogin, mode: String) = clickListener(store, mode)
}