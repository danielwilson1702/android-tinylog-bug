package com.sp.myapplication

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.poynt.os.model.Intents
import org.tinylog.configuration.Configuration
import org.tinylog.kotlin.Logger

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Configuration.set("writer.file", filesDir.path + "/logs/tiny_log_{count}.txt")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = MainFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
    }
}
