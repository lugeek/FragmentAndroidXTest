package com.lugeek.fragmentmaxlifecycletest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.behavior_btn).setOnClickListener {
            val intent = Intent(this@MainActivity, BehaviorActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.animation_btn).setOnClickListener {
            val intent = Intent(this@MainActivity, AnimationActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.item_shared_btn).setOnClickListener {
            val intent = Intent(this@MainActivity, ItemSharedActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.recycler_shared_btn).setOnClickListener {
            val intent = Intent(this@MainActivity, RecyclerSharedActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.nested_viewpager2).setOnClickListener {
            val intent = Intent(this@MainActivity, NestedViewPager2Activity::class.java)
            startActivity(intent)
        }

    }
}