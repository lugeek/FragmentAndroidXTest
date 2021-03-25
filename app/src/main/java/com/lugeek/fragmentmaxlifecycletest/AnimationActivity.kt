package com.lugeek.fragmentmaxlifecycletest

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.transition.TransitionInflater

class AnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<AnimationFragmentA>(R.id.fragment_container_view)
            }
        }
    }

    class AnimationFragmentA : Fragment(R.layout.fragment_animation_a) {
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            val textView = view.findViewById<TextView>(R.id.text_a)
            textView.setOnClickListener {
                val fragment = AnimationFragmentB()
                parentFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out
                    )
                    // 为FragmentTransaction设置动画
                    replace(R.id.fragment_container_view, fragment)
                    addToBackStack(null)
                }
            }
        }
    }

    class AnimationFragmentB : Fragment(R.layout.fragment_animation_b) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
//            val inflater = TransitionInflater.from(requireContext())
//            enterTransition = inflater.inflateTransition(R.transition.slide_right)
            // 单独为Fragment的enter和exit设置动画
        }
    }
}