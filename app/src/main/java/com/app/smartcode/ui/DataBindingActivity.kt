package com.app.smartcode.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.app.smartcode.R
import com.app.smartcode.databinding.ActivityDataBindingBinding
import com.app.smartcode.entity.UserBindData

class DataBindingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityDataBindingBinding>(
            this,
            R.layout.activity_data_binding
        )
        var user = UserBindData(ObservableField("jack"), ObservableField(19))
        binding.user = user
        user.name.set("kangakng")
    }
}