package com.wxeapapp.ui.select

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wxeapapp.R
import com.wxeapapp.api.request.LoginResponse
import java.util.*

/**
 * class description here

 * @author nickming
 * *
 * @version 1.0.0
 * *
 * @since 2017-08-28 上午12:32
 * * Copyright (c) 2017 nickming All right reserved.
 */

class SwitchAdapter(val mItems: ArrayList<LoginResponse.Item>, val selected: Int = 0) : RecyclerView.Adapter<SwitchAdapter.SelectionHolder>() {

    lateinit var callback: (view: View, pos: Int) -> Unit


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionHolder {
        return SelectionHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_selection_orgnization, parent, false))
    }

    override fun onBindViewHolder(holder: SelectionHolder, position: Int) {
        holder.mTextView.text = mItems[position].RegShortName
        holder.mTextView.setTextColor(Color.parseColor("#000000"))
//        if (selected == position) {
//            holder.mTextView.setTextColor(Color.parseColor("#7ff000"))
//        } else {
//            holder.mTextView.setTextColor(Color.parseColor("#000000"))
//        }
        holder.itemView.setOnClickListener {
            view ->
            callback.invoke(view, position)
        }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class SelectionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mTextView: TextView = itemView.findViewById(R.id.orgTv) as TextView

    }
}
