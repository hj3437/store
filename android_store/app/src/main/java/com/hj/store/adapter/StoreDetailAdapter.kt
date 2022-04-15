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
import com.hj.store.MainActivity.Companion.CLICK_MENU_DELETE
import com.hj.store.MainActivity.Companion.CLICK_MENU_EDIT
import com.hj.store.R
import com.hj.store.data.StoreDetailItem
import java.text.DecimalFormat


class StoreDetailAdapter(private val clickListener: OnStoreDetailClickListener) :
    ListAdapter<StoreDetailItem, StoreDetailAdapter.StoreDetailItemViewHolder>(
        StoreDetailItemDiffUtil
    ) {

    class StoreDetailItemViewHolder(private val rootView: View) :
        RecyclerView.ViewHolder(rootView) {
        fun bind(item: StoreDetailItem, clickListener: OnStoreDetailClickListener) {
            val itemImageView = rootView.findViewById<ImageView>(R.id.store_detail_item_imageView)

            Glide.with(rootView.context)
                .load(item.imageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .fallback(R.drawable.ic_placeholder)
                .into(itemImageView)

            val itemTitleTextView =
                rootView.findViewById<TextView>(R.id.store_detail_item_title_textView)
            itemTitleTextView.text = item.name

            val itemPriceTextView =
                rootView.findViewById<TextView>(R.id.store_detail_item_price_textView)

            val formatter = DecimalFormat("#,###")
            val price = formatter.format(item.price)
            itemPriceTextView.text = "${price}원"

            val editTextView = rootView.findViewById<TextView>(R.id.store_detail_item_edit_textView)


            val deleteTextView =
                rootView.findViewById<TextView>(R.id.store_detail_item_delete_textView)


            if (item.isLogin) {
                editTextView.visibility = View.VISIBLE
                editTextView.setOnClickListener {
                    clickListener.clickListener(item, CLICK_MENU_EDIT)
                }

                deleteTextView.visibility = View.VISIBLE
                deleteTextView.setOnClickListener {
                    clickListener.clickListener(item, CLICK_MENU_DELETE)
                }
            } else {
                editTextView.visibility = View.INVISIBLE
                deleteTextView.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreDetailItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rootView = inflater.inflate(R.layout.store_detail_list_item, parent, false)
        return StoreDetailItemViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: StoreDetailItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}

object StoreDetailItemDiffUtil : DiffUtil.ItemCallback<StoreDetailItem>() {
    override fun areItemsTheSame(oldItem: StoreDetailItem, newItem: StoreDetailItem) =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: StoreDetailItem, newItem: StoreDetailItem) =
        oldItem == newItem
}

class OnStoreDetailClickListener(val clickListener: (item: StoreDetailItem, mode: String) -> Unit) {
    fun onClick(item: StoreDetailItem, mode: String) = clickListener(item, mode)
}