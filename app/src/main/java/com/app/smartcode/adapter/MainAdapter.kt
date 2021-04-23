package com.app.smartcode.adapter

import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.app.smartcode.R
import com.app.smartcode.entity.MainItemBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class MainAdapter(dataList:MutableList<MainItemBean>) : BaseQuickAdapter<MainItemBean,BaseViewHolder>(R.layout.item_main_btn,dataList) {

    init {
        addChildClickViewIds(R.id.tv_item_name)
    }

    override fun convert(holder: BaseViewHolder, item: MainItemBean) {
        holder.setText(R.id.tv_item_name,item.itemName)
    }

}