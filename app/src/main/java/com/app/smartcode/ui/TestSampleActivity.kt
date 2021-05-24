package com.app.smartcode.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.smartcode.R
import kotlinx.android.synthetic.main.activity_test_sample_1.*
import kotlinx.android.synthetic.main.activity_test_sample_2.*

class TestSampleActivity : AppCompatActivity(){
    private var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(flag) {
            setContentView(R.layout.activity_test_sample_1)
        }else{
            setContentView(R.layout.activity_test_sample_2)
        }
        findViewById<TextView>(R.id.tv_name).text = "我的世界"
    }

}