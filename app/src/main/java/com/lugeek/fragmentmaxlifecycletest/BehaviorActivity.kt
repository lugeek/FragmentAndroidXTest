package com.lugeek.fragmentmaxlifecycletest

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

class BehaviorActivity : AppCompatActivity() {

    companion object {
        val TAG = "BehaviorActivity"
    }

    lateinit var viewPager : ViewPager
    lateinit var pagerAdapter : FragmentPagerAdapter
    private val list: List<String>  = listOf("title1", "title2", "title3", "title4", "title5")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_behavior)
        viewPager = findViewById(R.id.view_pager)
        pagerAdapter = object : FragmentPagerAdapter(supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            // 只有当前Fragment才是onResume, 相邻的是onStart
            override fun getCount(): Int {
                return list.count()
            }

            override fun getItem(position: Int): Fragment {
                return MyFragment(list.elementAtOrElse(position) { "default" })
            }
        }
        viewPager.adapter = pagerAdapter
    }

    class MyFragment : Fragment(R.layout.fragment_layout) {
        companion object {
            operator fun invoke(title: String) : Fragment {
                val fragment = MyFragment()
                fragment.arguments = bundleOf("title" to title)
                return fragment
            }
        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            val title = requireArguments().getString("title") ?: "default"
            Log.i(TAG, "$title onAttach")
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val title = requireArguments().getString("title") ?: "default"
            Log.i(TAG, "$title onCreate")
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val title = requireArguments().getString("title") ?: "default"
            Log.i(TAG, "$title onCreateView")
            return super.onCreateView(inflater, container, savedInstanceState)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            val title = requireArguments().getString("title") ?: "default"
            val titleView = view.findViewById<TextView>(R.id.title_view)
            titleView.text = title

            Log.i(TAG, "$title onViewCreated")
        }

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            val title = requireArguments().getString("title") ?: "default"
            Log.i(TAG, "$title onActivityCreated")
        }

        override fun onStart() {
            super.onStart()
            val title = requireArguments().getString("title") ?: "default"
            Log.i(TAG, "$title onStart")
        }

        override fun onResume() {
            super.onResume()
            val title = requireArguments().getString("title") ?: "default"
            Log.i(TAG, "$title onResume")
            // do lazy load here
        }

        override fun onPause() {
            super.onPause()
            val title = requireArguments().getString("title") ?: "default"
            Log.i(TAG, "$title onPause")
        }

        override fun onStop() {
            super.onStop()
            val title = requireArguments().getString("title") ?: "default"
            Log.i(TAG, "$title onStop")
        }

        override fun onDestroyView() {
            super.onDestroyView()
            val title = requireArguments().getString("title") ?: "default"
            Log.i(TAG, "$title onDestroyView")
        }

        override fun onDestroy() {
            super.onDestroy()
            val title = requireArguments().getString("title") ?: "default"
            Log.i(TAG, "$title onDestroy")
        }

        override fun setUserVisibleHint(isVisibleToUser: Boolean) {
            super.setUserVisibleHint(isVisibleToUser)
            val title = requireArguments().getString("title") ?: "default"
            Log.i(TAG, "$title setUserVisibleHint:$isVisibleToUser")
        }

        override fun onHiddenChanged(hidden: Boolean) {
            super.onHiddenChanged(hidden)
            val title = requireArguments().getString("title") ?: "default"
            Log.i(TAG, "$title onHiddenChanged:$hidden")
        }

    }
}