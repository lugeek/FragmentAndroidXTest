package com.lugeek.fragmentmaxlifecycletest

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val KEY_ITEM_POSITION = "com.lugeek.fragmentmaxlifecycletest.NestedViewPager2Activity.KEY_ITEM_POSITION"

internal val PAGE_COLORS = listOf(
    0xEEFF0000.toInt(),
    0xEE00FF00.toInt(),
    0xEE0000FF.toInt(),
    0xEE00FFFF.toInt()
)

internal val CELL_COLORS = listOf(
    0xFFEEEEEE.toInt(),
    0xFF666666.toInt()
)

class NestedViewPager2Activity : AppCompatActivity() {

    companion object {
        val titles = listOf("page 0", "page 1", "page 2", "page 3")
    }

    lateinit var viewPager: ViewPager2
    lateinit var tabLayout: TabLayout
    lateinit var stateAdapter: FragmentStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_viewpager)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)

        stateAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return titles.count()
            }

            override fun createFragment(position: Int): Fragment {
                return PageFragment.create(position)
            }

        }
        viewPager.adapter = stateAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Page ${(position)}"
        }.attach()


    }

    class PageFragment : Fragment(R.layout.fragment_page_nested_viewpager2) {

        lateinit var title: TextView
        lateinit var rv1: RecyclerView
        lateinit var rv2: RecyclerView

        companion object {
            fun create(position: Int) =
                PageFragment().apply {
                    arguments = Bundle(1).apply {
                        putInt(KEY_ITEM_POSITION, position)
                    }
                }
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val position = requireArguments().getInt(KEY_ITEM_POSITION)
            title = view.findViewById<TextView>(R.id.page_title).apply {
                text = titles[position]
            }
            rv1 = view.findViewById(R.id.first_rv)
            rv1.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rv1.adapter = RvAdapter(RecyclerView.HORIZONTAL)
            rv2 = view.findViewById(R.id.second_rv)
            rv2.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            rv2.adapter = RvAdapter(RecyclerView.VERTICAL)

            view.setBackgroundColor(PAGE_COLORS[position % PAGE_COLORS.size])
        }

    }

    class RvAdapter(private val orientation: Int) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {
        override fun getItemCount(): Int {
            return 40
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val tv = TextView(parent.context)
            tv.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                if (orientation == RecyclerView.HORIZONTAL) {
                    width = WRAP_CONTENT
                } else {
                    height = WRAP_CONTENT
                }
            }
            tv.textSize = 20f
            tv.gravity = Gravity.CENTER
            tv.setPadding(20, 55, 20, 55)
            return ViewHolder(tv)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder) {
                tv.text = "第${position}个"
                tv.setBackgroundColor(CELL_COLORS[position % CELL_COLORS.size])
            }
        }

        class ViewHolder(val tv: TextView) : RecyclerView.ViewHolder(tv)
    }


}