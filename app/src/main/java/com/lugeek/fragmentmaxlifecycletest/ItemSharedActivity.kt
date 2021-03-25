package com.lugeek.fragmentmaxlifecycletest

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.transition.TransitionInflater

class ItemSharedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_shared)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<SharedFragmentA>(R.id.fragment_container_view)
            }
        }
    }

    class SharedFragmentA : Fragment(R.layout.fragment_shared_a) {
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            val textView = view.findViewById<TextView>(R.id.text_a)
            ViewCompat.setTransitionName(textView, "shared_a")
            textView.setOnClickListener {
                val fragment = SharedFragmentB()
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    addSharedElement(textView, "shared_b")
                    replace(R.id.fragment_container_view, fragment)
                    addToBackStack(null)
                }
            }
        }
    }

    class SharedFragmentB : Fragment(R.layout.fragment_shared_b) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            sharedElementEnterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_text)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            val textView = view.findViewById<TextView>(R.id.text_b)
            ViewCompat.setTransitionName(textView, "shared_b")
        }
    }
}