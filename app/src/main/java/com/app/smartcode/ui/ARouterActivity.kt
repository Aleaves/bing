package com.app.smartcode.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.app.smartcode.R
import kotlinx.android.synthetic.main.activity_arouter.*

class ARouterActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arouter)
        bt1.setOnClickListener {
            ARouter.getInstance().build("/module1/one").navigation()
        }
    }
}