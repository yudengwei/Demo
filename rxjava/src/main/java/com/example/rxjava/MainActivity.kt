package com.example.rxjava

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.CompletableSource
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.SingleSource
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.BiConsumer
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    /**
     * subscribeOn : 生产时线程 , 被观察者线程
     * observeOn : 接收时线程，观察者线程，可多次调用
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
        //window()
        //concat()
        //concatArray()
        //merge()
        //mergeAndConcat()
        //concatDelayError()
        //zip()
        //combineLatest()
        //reduce()
        //startWith()
        //count()
        //observableOn()
        //filter()
        //delay()
        //ofType()
        //skip()
        //distinct()
        //take()
        //debounce()
        //all()
        //takeWhile()
        //sequenceEqual()
        test1()

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

    fun concat() {
        //将多个observable组合在一起成一个observable，顺序发送，最多4个
        Observable.concat(Observable.just(1,2),Observable.just(3,4)
        ,Observable.just(5,6))
            .subscribe {
                Log.d(TAG,"it: $it")
            }
    }

    fun concatArray() {
        //与concat一样，没有最大个数的限制
        Observable.concatArray(Observable.just(1,2),Observable.just(3,4)
            ,Observable.just(5,6),Observable.just(7,8),Observable.just(9,10))
            .subscribe {
                Log.d(TAG,"it: $it")
            }
    }

    fun merge() {
        Observable.merge(Observable.just(1,2),Observable.just(3,4)
            ,Observable.just(5,6))
            .subscribe {
                Log.d(TAG,"it: $it")
            }
    }

    fun mergeAndConcat() {

        /**
         * merge: 所有observable异步执行，没有先后顺序
         * concat: 所有observable串行执行，只有前面的执行完才能执行下一个，假如前面的没执行完那么下一个永远都不会执行
         */

        /*Observable.merge(Observable.interval(1,TimeUnit.SECONDS)
            .map {
                "A$it"
            },Observable.interval(1,TimeUnit.SECONDS)
            .map {
                "b$it"
            })
            .subscribe {
                Log.d(TAG,"it: $it")
            }*/
        Log.d(TAG,"-----")
        Observable.concat(Observable.interval(1,TimeUnit.SECONDS)
            .map {
                "A$it"
            },Observable.interval(1,TimeUnit.SECONDS)
            .map {
                "b$it"
            })
            .subscribe {
                Log.d(TAG,"it: $it")
            }
    }

    fun concatDelayError() {
        /**
         * 1 2 1 2
         * 接收到onError还是会继续发射，无法接收报错信息
         * concatArrayDelayError 是一样的
         * concat 接收到onError事件会停止
         */
        Observable.concatDelayError(arrayListOf(Observable.create<Int> {
            it.onNext(1)
            it.onNext(2)
            it.onError(Throwable("阿彪你好帅"))
        },Observable.create<Int> {
            it.onNext(1)
            it.onNext(2)
        }))
            .subscribe({
                Log.d(TAG, "it: $it")
            },{
                Log.d(TAG, "throwe: ${it.message}")
            })

    }

    fun zip() {
        /**
         * 2020-10-30 09:52:30.172 14300-14300/com.example.rxjava D/ABiao: A发射的事件------0
        2020-10-30 09:52:30.172 14300-14300/com.example.rxjava D/ABiao: A发射的事件------1
        2020-10-30 09:52:30.172 14300-14300/com.example.rxjava D/ABiao: A发射的事件------2
        2020-10-30 09:52:30.172 14300-14300/com.example.rxjava D/ABiao: A发射的事件------3
        2020-10-30 09:52:30.172 14300-14300/com.example.rxjava D/ABiao: A发射的事件------4
        2020-10-30 09:52:30.172 14300-14300/com.example.rxjava D/ABiao: b发射的事件-----0
        2020-10-30 09:52:30.172 14300-14300/com.example.rxjava D/ABiao: A0B0
        2020-10-30 09:52:30.172 14300-14300/com.example.rxjava D/ABiao: b发射的事件-----1
        2020-10-30 09:52:30.172 14300-14300/com.example.rxjava D/ABiao: A1B1
        2020-10-30 09:52:30.173 14300-14300/com.example.rxjava D/ABiao: b发射的事件-----2
        2020-10-30 09:52:30.173 14300-14300/com.example.rxjava D/ABiao: A2B2
         最终发射的size与发射数量最小的Observable的size一样
         */
        Observable.zip(Observable.range(0, 5)
            .map {
                Log.d(TAG, "A发射的事件------$it")
                "A$it"
            }, Observable.range(0, 3)
            .map {
                Log.d(TAG, "b发射的事件-----$it")
                "B$it"
            }, BiFunction<String,String,String> { t1, t2 ->
                "$t1$t2"
            })
            .subscribe {
                Log.d(TAG, it)
            }
    }

    fun combineLatest() {
        /**
         * 2020-10-30 09:56:21.562 14511-14511/com.example.rxjava D/ABiao: A发射的事件------0
        2020-10-30 09:56:21.563 14511-14511/com.example.rxjava D/ABiao: A发射的事件------1
        2020-10-30 09:56:21.563 14511-14511/com.example.rxjava D/ABiao: A发射的事件------2
        2020-10-30 09:56:21.563 14511-14511/com.example.rxjava D/ABiao: A发射的事件------3
        2020-10-30 09:56:21.563 14511-14511/com.example.rxjava D/ABiao: A发射的事件------4
        2020-10-30 09:56:21.563 14511-14511/com.example.rxjava D/ABiao: b发射的事件-----0
        2020-10-30 09:56:21.563 14511-14511/com.example.rxjava D/ABiao: A4B0
        2020-10-30 09:56:21.563 14511-14511/com.example.rxjava D/ABiao: b发射的事件-----1
        2020-10-30 09:56:21.563 14511-14511/com.example.rxjava D/ABiao: A4B1
        2020-10-30 09:56:21.563 14511-14511/com.example.rxjava D/ABiao: b发射的事件-----2
        2020-10-30 09:56:21.563 14511-14511/com.example.rxjava D/ABiao: A4B2
         当b发射时，只会与A最新的发射合并发射，导致A0A1A2A3没有发射
         */
        Observable.combineLatest(Observable.range(0, 5)
            .map {
                Log.d(TAG, "A发射的事件------$it")
                "A$it"
            }, Observable.range(0, 3)
            .map {
                Log.d(TAG, "b发射的事件-----$it")
                "B$it"
            }, BiFunction<String,String,String> { t1, t2 ->
            "$t1$t2"
        })
            .subscribe {
                Log.d(TAG, it)
            }
    }

    fun reduce() {
        /**
         * 2020-10-30 10:06:06.564 14850-14850/com.example.rxjava D/ABiao: ----start----
        2020-10-30 10:06:06.564 14850-14850/com.example.rxjava D/ABiao: t1: 0, t2: 1
        2020-10-30 10:06:06.564 14850-14850/com.example.rxjava D/ABiao: ----start----
        2020-10-30 10:06:06.564 14850-14850/com.example.rxjava D/ABiao: t1: 1, t2: 2
        2020-10-30 10:06:06.564 14850-14850/com.example.rxjava D/ABiao: ----start----
        2020-10-30 10:06:06.564 14850-14850/com.example.rxjava D/ABiao: t1: 3, t2: 3
        2020-10-30 10:06:06.564 14850-14850/com.example.rxjava D/ABiao: ----start----
        2020-10-30 10:06:06.565 14850-14850/com.example.rxjava D/ABiao: t1: 6, t2: 4
        2020-10-30 10:06:06.565 14850-14850/com.example.rxjava D/ABiao: ---end----
        2020-10-30 10:06:06.565 14850-14850/com.example.rxjava D/ABiao: it: 10
         用法与scan类似，区别就是scan是执行多次subscribe，而reduce是只执行一次subscribe
         */
        Observable.range(0,5)
            .reduce { t1,t2->
                Log.d(TAG,"----start----")
                Log.d(TAG,"t1: $t1, t2: $t2")
                t1 + t2
            }
            .subscribe {
                Log.d(TAG,"---end----")
                Log.d(TAG, "it: $it")
            }
    }

    fun startWith() {
        /**
         * 2020-10-30 10:30:48.443 15304-15304/com.example.rxjava D/ABiao: it: 1
        2020-10-30 10:30:48.444 15304-15304/com.example.rxjava D/ABiao: it: 3
        2020-10-30 10:30:48.444 15304-15304/com.example.rxjava D/ABiao: it: 4
        2020-10-30 10:30:48.444 15304-15304/com.example.rxjava D/ABiao: it: 5
        2020-10-30 10:30:48.444 15304-15304/com.example.rxjava D/ABiao: it: 6
         */
        Observable.just(5,6)
            .startWithArray(3,4)
            .startWith(SingleSource {
                it.onSuccess(1)
            })
            .subscribe {
                Log.d(TAG, "it: $it")
            }
    }

    fun count() {
        /**
         * count 返回发射的个数
         */
        Observable.just(0,5,7)
            .count()
            .subscribe { it ->
                Log.d(TAG, "it: $it")
            }
    }

    fun observableOn() {
        /**
         * 2020-10-30 13:45:55.346 16600-16659/com.example.rxjava D/ABiao: currentThread: RxCachedThreadScheduler-1
        2020-10-30 13:45:55.347 16600-16659/com.example.rxjava D/ABiao: currentThread: RxCachedThreadScheduler-1
        2020-10-30 13:45:55.348 16600-16660/com.example.rxjava D/ABiao: it: 1, currentThread: RxNewThreadScheduler-1
        2020-10-30 13:45:55.348 16600-16660/com.example.rxjava D/ABiao: it: 2, currentThread: RxNewThreadScheduler-1
        2020-10-30 13:45:55.349 16600-16660/com.example.rxjava D/ABiao: it: 3, currentThread: RxNewThreadScheduler-1
         */
        Observable.just(1,2,3)
            .observeOn(Schedulers.io())
            .flatMap {
                Log.d(TAG,"currentThread: ${Thread.currentThread().name}")
                Observable.just(it)
            }
            .observeOn(Schedulers.newThread())
            .subscribe {
                Log.d(TAG, "it: $it, currentThread: ${Thread.currentThread().name}")
            }
    }

    fun filter() {
        // 过滤
        Observable.range(1,5)
            .filter {
                it > 3
            }
            .subscribe {
                Log.d(TAG, "it: $it")
            }
    }

    fun delay() {
        //延迟 delat 秒后就开始后执行
        Observable.just(1,2)
            .delay(5,TimeUnit.SECONDS)
            .subscribe {
                Log.d(TAG, "it: $it")
            }
    }

    fun ofType() {
        //过滤类型
        Observable.just(User("1",1),"3","4")
            .ofType(User::class.java)
            .subscribe {
                Log.d(TAG,it.toString())
            }
    }

    fun skip() {
        /**
         * count : 小于0报异常，等于0不起作用，大于发射的数量会全部舍弃不发射
         */
        Observable.just(1,2,3,5)
            .skip(0)
            .subscribe {
                Log.d(TAG, "it: $it")
            }
    }

    fun distinct() {
        /**
         * 没有重写equals,返回两个。只重写了equals，返回两个。只重写hashCode，返回两个。重写equals和hashCode，返回一个。
         * 所以去重是根据equals和hashCode来判断的

         * distinct(): 去掉全部的重复事件
        2020-10-30 14:18:14.516 19313-19313/com.example.rxjava D/ABiao: com.example.rxjava.People@1
        2020-10-30 14:18:14.516 19313-19313/com.example.rxjava D/ABiao: com.example.rxjava.People@3

         * distinctUntilChanged(): 去掉连续的重复事件，当中间有不一样时再重新发射
        2020-10-30 14:18:14.516 19313-19313/com.example.rxjava D/ABiao: com.example.rxjava.People@1
        2020-10-30 14:18:14.516 19313-19313/com.example.rxjava D/ABiao: com.example.rxjava.People@3
        2020-10-30 14:18:14.516 19313-19313/com.example.rxjava D/ABiao: com.example.rxjava.People@1
         */
        Observable.just(People("1",1),People("1",1),People("2",3),People("1",1))
            .distinctUntilChanged()
            .subscribe {
                Log.d(TAG, it.toString())
            }
    }

    fun take() {
        //控制最大的发射数量
        /**
        2020-10-30 14:22:48.976 19404-19404/com.example.rxjava D/ABiao: it: 0
        2020-10-30 14:22:48.976 19404-19404/com.example.rxjava D/ABiao: it: 1
         */
        Observable.range(0,5)
            .take(2)
            .subscribe {
                Log.d(TAG, "it: $it")
            }
    }

    fun debounce() {
        /**
         * debounce：
         * 如果两次发射的时间间隔小于设置的时间间隔，则不会发射第一次的事件,假如说只有一次就直接发射,假如说有多次，且每次间隔都小于设置的时间间隔，则只会发射最后一次事件
         *throttleWithTimeout效果与debounce一样
         */
        Observable.create<Int> {
            it.onNext(1)
            Thread.sleep(300)
            it.onNext(2)
            it.onNext(3)
        }
            .throttleWithTimeout(1,TimeUnit.SECONDS)
            .subscribe {
                Log.d(TAG,"it: $it")
            }
    }

    /**
     * firstElement，listElement, elementAt(index : Int) & elementAtOrError(index : Int)
     * 获取某个下标的事件，假如超出事件的最大数量，第一个没有任何事件，第二个会报错
     */


    fun all() {

        //判断全部事件是否满足某个条件，全部满足返回true，否则返回false
        Observable.range(0,5)
            .all {
                it > 1
            }
            .subscribe { it ->
                Log.d(TAG,"it : $it")
            }
    }

    fun takeWhile() {
        /**
         * 当某个事件就不满足条件，则这个事件后面的事件都不发射
         * skipWhile则正好相反，满足条件不发射
         */
        Observable.just(5,1,2)
            .takeWhile {
                it > 1
            }
            .subscribe {
                Log.d(TAG,"it: $it")
            }
    }

    fun sequenceEqual() {
        /**
         * 判断是否相同 ，必须顺序和数量相同，否则返回false
         */
        Observable.sequenceEqual(Observable.just(1,2),
        Observable.just(2,1))
            .subscribe { it ->
                Log.d(TAG, "it: $it")
            }
    }

    fun test1() {
        // 2020-10-30 15:16:46.220 21489-21489/com.example.rxjava D/ABiao: User(name=2, age=1)
        val just = Observable.just(User("1", 1))
        just.subscribe {
            it.name = "2"
        }
        just.subscribe {
            Log.d(TAG, it.toString())
        }
    }

    fun test1() {
        // 2020-10-30 15:16:46.220 21489-21489/com.example.rxjava D/ABiao: User(name=2, age=1)
        val just = Observable.just(1)
        just.subscribe { it ->
            it++
        }
    }
}