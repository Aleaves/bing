package com.app.smartcode.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.smartcode.R
import com.app.smartcode.exp.ApiException
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

class RxTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_test)

        var ob1 = Observable.intervalRange(1, 4, 0, 1, TimeUnit.SECONDS)
        var ob2 = Observable.intervalRange(5, 4, 0, 1, TimeUnit.SECONDS)
        var ob3 = Observable.intervalRange(10, 4, 0, 1, TimeUnit.SECONDS)

        Observable.create<Int> {
            it.onNext(1)
            it.onNext(2)
            it.onError(ApiException(100,"222"))
            it.onNext(3)
            it.onComplete()
        }
        .subscribe(object :Observer<Int>{
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable?) {

            }

            override fun onNext(t: Int?) {

            }

            override fun onError(e: Throwable?) {

            }
        })

        Observable.fromIterable(mutableListOf(1,2,3))
        .subscribe {

        }

    }

}