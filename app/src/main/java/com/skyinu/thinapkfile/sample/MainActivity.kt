package com.skyinu.thinapkfile.sample

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Observable.just("test2")
            .subscribe({
                hello.text = it
            }, {
                print(it.message)
            })
    }
}
