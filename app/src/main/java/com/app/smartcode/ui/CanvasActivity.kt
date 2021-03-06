package com.app.smartcode.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.app.smartcode.R
import com.app.smartcode.adapter.MainAdapter
import com.app.smartcode.entity.MainItemBean
import kotlinx.android.synthetic.main.activity_main.*

class CanvasActivity :AppCompatActivity(){

    private val mAdapter = MainAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas)
        recycler_view.apply {
            val gridLayoutManager = GridLayoutManager(this@CanvasActivity,3)

            layoutManager = gridLayoutManager
            adapter = mAdapter
        }

        mAdapter.setNewInstance(getDataList())

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.tv_item_name ->{
                    val info = mAdapter.getItem(position)
                    val intent = Intent(this@CanvasActivity, Class.forName(info.className))
                    startActivity(intent)
                }
            }

        }

    }

    private fun getDataList() = mutableListOf<MainItemBean>(
        MainItemBean(getString(R.string.brizer_page), "${BrizerActivity::class.qualifiedName}"),
        MainItemBean(getString(R.string.reader_page), "${BookPageActivity::class.qualifiedName}"),
        MainItemBean(getString(R.string.xfermode), "${XformodeActivity::class.qualifiedName}")
    )

}