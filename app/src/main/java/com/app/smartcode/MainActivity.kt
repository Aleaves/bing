package com.app.smartcode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.app.smartcode.adapter.MainAdapter
import com.app.smartcode.entity.MainItemBean
import com.app.smartcode.entity.Person
import com.app.smartcode.ui.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mAdapter = MainAdapter(getDataList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.apply {
            val gridLayoutManager = GridLayoutManager(this@MainActivity,3)

            layoutManager = gridLayoutManager
            adapter = mAdapter
        }

        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.tv_item_name ->{
                    val info = mAdapter.getItem(position)
                    val intent = Intent(this@MainActivity, Class.forName(info.className))
                    startActivity(intent)
                }
            }

        }
        val p = Person()
        p.clone()

    }

    private fun getDataList() = mutableListOf<MainItemBean>(
        MainItemBean("自定义View", "${CanvasActivity::class.qualifiedName}"),
        MainItemBean("测试不同的UI", "${TestSampleActivity::class.qualifiedName}"),
        MainItemBean("ARouter路由", "${ARouterActivity::class.qualifiedName}"),
        MainItemBean("DataBinding", "${DataBindingActivity::class.qualifiedName}"),
        MainItemBean("RxJava", "${RxTestActivity::class.qualifiedName}"),
        MainItemBean("弹幕", "${DanmakuActivity::class.qualifiedName}")
    )

}
