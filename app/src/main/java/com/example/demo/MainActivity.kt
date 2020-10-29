package com.example.demo

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.animation.Animation
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.demo.animator.ValueAnimatorDelegate
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileWriter

class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBtnCreate.setOnClickListener {
            write11()
            //jump()
            /*if(checkPermission()) {
                write()
            }*/
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            write()
        }
    }

    private fun checkPermission() : Boolean{
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            return false
        }
        return true
    }

    private fun write() {
        val filePath = Environment.getExternalStoragePublicDirectory("").toString() + "/test.txt"
        val fw = FileWriter(filePath)
        fw.write("阿彪好帅123")
        fw.close()
        Toast.makeText(this,"写入成功",Toast.LENGTH_SHORT).show()
    }

    private fun write11() {
        val file = File(filesDir,"a.text")
        val file1 = File(getExternalFilesDir(""),"b1.txt")
        Log.d("ABiao","path: "+file1.absolutePath)
        val fw = FileWriter(file1.absoluteFile)
        fw.write("123")
        fw.close()
        Toast.makeText(this,"写入成功",Toast.LENGTH_SHORT).show()
    }

    private fun jump() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        startActivityForResult(intent, 1)
    }

    private fun getImage() {
        //val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != 1 || data == null || resultCode != RESULT_OK) {
            return
        }
        val uri = data.data
        Log.d("ABiao","uri: $uri")
    }
}