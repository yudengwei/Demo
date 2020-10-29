package com.example.rxjava

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    /**
     * subscribeOn : 生产时线程
     * observeOn : 接收时线程
     */

    private var disable : Disposable by Delegates.notNull()
    
    companion object{
        const val TAG = "ABiao"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //interval()
        //range()
        //create()
        //just()
        //fromArray()
        //defer()
        //timer()
        //map()
        //flatMap()
        //buffer()
        //groupBy()
        //scan()
        window()

        mStop.setOnClickListener {
            startActivity(Intent(this,MainActivity2::class.java))
        }

    }

    //rangeLong
    fun range(){
        /**
         * return : 10, 11
         */
        disable = Observable.range(10,2)
            .observeOn(Schedulers.io())
            .subscribe {
                Log.d(TAG, "test1: $it")
            }
    }

    fun create(){
        /**
        2020-10-29 15:50:33.166 6939-6975/com.example.rxjava D/ABiao: test1: User(name=dengwei 0, age=0)
        2020-10-29 15:50:33.166 6939-6975/com.example.rxjava D/ABiao: test1: User(name=dengwei 1, age=1)
        2020-10-29 15:50:33.166 6939-6975/com.example.rxjava D/ABiao: test1: User(name=dengwei 2, age=2)
        2020-10-29 15:50:33.166 6939-6975/com.example.rxjava D/ABiao: test1: User(name=dengwei 3, age=3)
        2020-10-29 15:50:33.166 6939-6975/com.example.rxjava D/ABiao: test1: User(name=dengwei 4, age=4)
        2020-10-29 15:50:33.166 6939-6975/com.example.rxjava D/ABiao: test1: User(name=dengwei 5, age=5)
         */
        disable = Observable.create<User> {
            for(i in 0 .. 5){
                it.onNext(User("dengwei $i",i))
            }
            it.onComplete()
        }.observeOn(Schedulers.io()).subscribe {
            Log.d(TAG, "test1: $it")
        }
    }

    fun defer(){
        /**
         * 只有在调用了 subscribe 才会调用 defer 里的方法
         * 因为 defer() 只有观察者订阅的时候才会创建新的被观察者，所以每订阅一次就会打印一次，并且都是打印 a 最新的值
         */
        var a = 0
        val observable = Observable.defer{
            Observable.just(a)
        }
        a = 1
        observable.subscribe {
            Log.d(TAG, "a: $a")
        }
        a = 2
        observable.subscribe {
            Log.d(TAG, "a1: $a")
        }
    }

    fun just() {
        Observable.just(User("1",1),User("2",2))
            .observeOn(Schedulers.io())
            .subscribe {
                Log.d(TAG, "$it")
            }
    }

    /**
     * fromArray与just的区别就是just最多只能生成10个数据
     */

    fun fromArray() {
        val userArray = arrayOf(User("1",1),User("2",2))
        Observable.fromArray(userArray)
            .observeOn(Schedulers.io())
            .subscribe {
                for (user in it) {
                    Log.d(TAG, "$user")
                }
            }
    }

    fun timer() {
        Observable.timer(2, TimeUnit.SECONDS)
            .observeOn(Schedulers.io())
            .subscribe {
                Log.d(TAG, "it: $it")
            }
    }

    /**
     * timer与interval 的区别是timer只执行一次
     */

    fun interval(){
        /**
         * 延迟 period 秒后执行，每隔 period 秒执行一次，也就是说刚开始是不会执行的
         */
        /*var s = System.currentTimeMillis()
        disable = Observable.interval(5, TimeUnit.SECONDS)
            .observeOn(Schedulers.io())
            .subscribe {
                val n = System.currentTimeMillis()
                Log.d(TAG, "test1: $it,thread: ${Thread.currentThread().name}, i: ${n - s} ")
                s = n
            }*/

        /**
         * initialDelay 延迟 initialDelay 秒后执行
         */
        var s = System.currentTimeMillis()
        disable = Observable.interval(0,5,TimeUnit.SECONDS)
            .observeOn(Schedulers.io())
            .subscribe {
                val n = System.currentTimeMillis()
                Log.d(TAG, "test1: $it,thread: ${Thread.currentThread().name}, i: ${n - s} ")
                s = n
            }
    }

    fun map() {
        Observable.just(1,2,5)
            .map {
                if (it % 2 == 0) it
                else it + 1
            }
            .subscribe {
                Log.d("ABiao","it: $it")
            }

    }

    /**
     * map与flatMap的区别： map返回对象类型，具体数据，比如 int，String等
     * flatMap返回Observable
     * concatMap与flatMap用法一样，但是flatMap是无序的，而concatMap是一定会保证顺序
     */

    fun flatMap() {
        val c = arrayListOf("语文", "物理")
        val c1 = arrayListOf("数学", "生物")
        val c2 = arrayListOf("英语", "美术")
        for (i in 0 until 1000) {
            c.add("$i")
            c1.add("$i")
            c2.add("$i")
        }
        val cc = arrayListOf(Cursor(c), Cursor(c1), Cursor(c2))

        //map的实现方式，不优雅
        Observable.just(Students(cc))
            .subscribeOn(Schedulers.io())
            .map {
                it.curoses
            }
            .subscribe {
                for (cursor in it) {
                    for (cursor in cursor.cursors) {
                        Log.d(TAG, "cursor: $cursor")
                    }
                }
            }

        //flatMap的实现方式，优雅
        Observable.just(Students(cc))
            .flatMap {
                Observable.fromIterable(it.curoses)
            }
            .flatMap {
                Observable.fromIterable(it.cursors)
            }
            .subscribe {
                Log.d(TAG, "it: $it")
            }
    }

    fun buffer() {
        /**
         * skip 不能为0，代表的是指针的起始点
         * count 2 skip 1 : 1 2, 2 3, 3 4, 4 5,
         *  count 2 skip 2 : 1 2, 3 4, 5
         *  count 2 skip 3 :1 2, 4 5
         *  count 2 skip 4 : 1 2, 5
         */
        Observable.just(1,2,3,4,5)
            .buffer(2, 3)
            .subscribe { it ->
                Log.d(TAG, "size: ${it.size}")
                it.forEach{
                    Log.d(TAG, "it: $it")
                }
            }
    }

    fun groupBy() {
        /**
        比3小
        it: 1
        it: 2
        it: 3
        比3大
        it: 4
        it: 5
        it: 6
        it: 7
        it: 8
        it: 9
         */
        Observable.range(1,9)
            .groupBy {
                if (it  > 3) "比3大"
                else "比3小"
            }
            .subscribe { it ->
                Log.d(TAG, it.key)
                it.subscribe {
                    Log.d(TAG,"it: $it")
                }
            }
    }

    fun scan() {
        /**
        2020-10-29 19:20:18.386 11646-11646/com.example.rxjava D/ABiao: it: 1
        2020-10-29 19:20:18.386 11646-11646/com.example.rxjava D/ABiao: -------
        2020-10-29 19:20:18.386 11646-11646/com.example.rxjava D/ABiao: t1: 1, t2: 2
        2020-10-29 19:20:18.386 11646-11646/com.example.rxjava D/ABiao: it: 2
        2020-10-29 19:20:18.386 11646-11646/com.example.rxjava D/ABiao: -------
        2020-10-29 19:20:18.386 11646-11646/com.example.rxjava D/ABiao: t1: 2, t2: 3
        2020-10-29 19:20:18.386 11646-11646/com.example.rxjava D/ABiao: it: 6
        2020-10-29 19:20:18.386 11646-11646/com.example.rxjava D/ABiao: -------
        2020-10-29 19:20:18.386 11646-11646/com.example.rxjava D/ABiao: t1: 6, t2: 4
        2020-10-29 19:20:18.387 11646-11646/com.example.rxjava D/ABiao: it: 24
        2020-10-29 19:20:18.387 11646-11646/com.example.rxjava D/ABiao: -------
        2020-10-29 19:20:18.387 11646-11646/com.example.rxjava D/ABiao: t1: 24, t2: 5
        2020-10-29 19:20:18.387 11646-11646/com.example.rxjava D/ABiao: it: 120
         */
        Observable.range(1,5)
            .scan { t1: Int, t2: Int ->
                // 第一次会先打印第一位的值，第二次时t1为第一位，t2为第二位，第三次时t1为上一次的结果返回值，t2为第三位。。。以此类推
                Log.d(TAG,"-------")
                Log.d(TAG,"t1: $t1, t2: $t2")
                t1 * t2
            }
            .subscribe {
                //这里的it是scan里的返回值
                Log.d(TAG, "it: $it")
            }
    }

    fun window() {
        /**
         *  分组
         * 2020-10-29 19:24:59.481 11937-11937/com.example.rxjava D/ABiao: -----
        2020-10-29 19:24:59.484 11937-11937/com.example.rxjava D/ABiao: it: 0
        2020-10-29 19:24:59.484 11937-11937/com.example.rxjava D/ABiao: it: 1
        2020-10-29 19:24:59.484 11937-11937/com.example.rxjava D/ABiao: -----
        2020-10-29 19:24:59.485 11937-11937/com.example.rxjava D/ABiao: it: 2
        2020-10-29 19:24:59.485 11937-11937/com.example.rxjava D/ABiao: it: 3
        2020-10-29 19:24:59.485 11937-11937/com.example.rxjava D/ABiao: -----
        2020-10-29 19:24:59.485 11937-11937/com.example.rxjava D/ABiao: it: 4
         */
        Observable.range(0,5)
            .window(2)
            .subscribe {
                Log.d(TAG,"-----")
                it.subscribe {
                    Log.d(TAG, "it: $it")
                }
            }

    }
}