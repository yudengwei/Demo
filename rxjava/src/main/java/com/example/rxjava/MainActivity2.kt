package com.example.rxjava

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.properties.Delegates

class MainActivity2 : AppCompatActivity() {

    private var disposable : Disposable by Delegates.notNull()

    companion object{
        const val TAG = "MainActivity2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        test()
    }

    fun test(){
        disposable = Observable.create<Int> {
            it.onNext(1)
            while (true){
                Log.d(TAG, "test: ")
            }
            Log.d(TAG, "end")
        }.subscribeOn(Schedulers.io())
            .subscribe {

            }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}