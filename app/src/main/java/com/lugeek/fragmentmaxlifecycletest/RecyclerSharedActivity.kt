package com.lugeek.fragmentmaxlifecycletest

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.SharedElementCallback
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

class RecyclerSharedActivity : AppCompatActivity() {

    companion object {
        val images = listOf(
            "http://e.hiphotos.baidu.com/image/pic/item/a1ec08fa513d2697e542494057fbb2fb4316d81e.jpg",
            "http://c.hiphotos.baidu.com/image/pic/item/30adcbef76094b36de8a2fe5a1cc7cd98d109d99.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/7c1ed21b0ef41bd5f2c2a9e953da81cb39db3d1d.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/55e736d12f2eb938d5277fd5d0628535e5dd6f4a.jpg",
            "http://e.hiphotos.baidu.com/image/pic/item/4e4a20a4462309f7e41f5cfe760e0cf3d6cad6ee.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/9d82d158ccbf6c81b94575cfb93eb13533fa40a2.jpg",
            "http://e.hiphotos.baidu.com/image/pic/item/4bed2e738bd4b31c1badd5a685d6277f9e2ff81e.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/0d338744ebf81a4c87a3add4d52a6059252da61e.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/f2deb48f8c5494ee5080c8142ff5e0fe99257e19.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/4034970a304e251f503521f5a586c9177e3e53f9.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/279759ee3d6d55fbb3586c0168224f4a20a4dd7e.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/e824b899a9014c087eb617650e7b02087af4f464.jpg",
            "http://c.hiphotos.baidu.com/image/pic/item/9c16fdfaaf51f3de1e296fa390eef01f3b29795a.jpg",
            "http://d.hiphotos.baidu.com/image/pic/item/b58f8c5494eef01f119945cbe2fe9925bc317d2a.jpg",
            "http://h.hiphotos.baidu.com/image/pic/item/902397dda144ad340668b847d4a20cf430ad851e.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/359b033b5bb5c9ea5c0e3c23d139b6003bf3b374.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/8d5494eef01f3a292d2472199d25bc315d607c7c.jpg",
            "http://b.hiphotos.baidu.com/image/pic/item/e824b899a9014c08878b2c4c0e7b02087af4f4a3.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/6d81800a19d8bc3e770bd00d868ba61ea9d345f2.jpg"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.sharedElementsUseOverlay = true
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_recycler_shared)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<GridFragment>(R.id.fragment_container_view)
            }
        }
    }

    open class GridFragment : Fragment(R.layout.fragment_grid) {

        companion object {
            var gridPosition = 0
        }

        lateinit var recyclerView: RecyclerView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            exitTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image_exit)
            setExitSharedElementCallback(object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: MutableList<String>?,
                    sharedElements: MutableMap<String, View>?
                ) {
                    // 返回去的时候，transitionName不用变，一来一回必须用同一个transitionName，sharedView需要随着上层的改变而改变。
                    val curViewHolder = recyclerView.findViewHolderForAdapterPosition(gridPosition)
                    val transitionName = images[gridPosition]
                    (curViewHolder as? GridAdapter.ImageViewHolder)?.imageView?.let {
                        sharedElements?.put(names?.get(0) ?: "", it)
                    }
                }
            })
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
            val adapter = GridAdapter()
            adapter.fragment = this
            recyclerView.adapter = adapter

            postponeEnterTransition()
            (view.parent as? ViewGroup)?.doOnPreDraw {
                // 确保在measure和layout之后，在即将绘制之前，开始enter动画
                startPostponedEnterTransition()
            }

            scrollToPosition()
        }

        private fun scrollToPosition() {
            recyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
                override fun onLayoutChange(
                    v: View,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                    oldLeft: Int,
                    oldTop: Int,
                    oldRight: Int,
                    oldBottom: Int
                ) {
                    recyclerView.removeOnLayoutChangeListener(this)
                    val layoutManager = recyclerView.layoutManager
                    val viewAtPosition =
                        layoutManager?.findViewByPosition(gridPosition)
                    // Scroll to position if the view for the current position is null (not currently part of
                    // layout manager children), or it's not completely visible.
                    if (viewAtPosition == null || layoutManager
                            .isViewPartiallyVisible(viewAtPosition, false, true)
                    ) {
                        recyclerView.post { layoutManager?.scrollToPosition(gridPosition) }
                    }
                }
            })
        }
    }

    class GridAdapter : RecyclerView.Adapter<GridAdapter.ImageViewHolder>() {

        var fragment: Fragment? = null

        init {

        }

        class ImageViewHolder(itemView: View, fragment: Fragment?) : RecyclerView.ViewHolder(
            itemView
        ) {
            var imageView: ImageView? = null
            var imgPosition: Int = 0

            init {
                itemView.setOnClickListener {
                    GridFragment.gridPosition = imgPosition // 位置修改
                    (fragment?.exitTransition as? TransitionSet)?.excludeTarget(itemView, true) // TransitionSet必须import AndroidX 中的
                    val pagerFragment = ImagePagerFragment(imgPosition)
                    fragment?.parentFragmentManager?.commit {
                        setReorderingAllowed(true)
                        imageView?.let {
                            addSharedElement(it, it.transitionName)
                        }
                        replace(R.id.fragment_container_view, pagerFragment)
                        addToBackStack(null)
                    }
                }
            }

            fun bind(position: Int) {
                imgPosition = position
                val url = images.elementAtOrElse(position){""}
                imageView?.let {
                    ViewCompat.setTransitionName(it, url)
                    Glide.with(it).load(url).apply(RequestOptions().dontTransform()).into(it)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            // 警告！！！
            // ImageView不能作为ViewHolder的最外层view，否则动画会存在遮挡。
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_card_item, parent, false)

            val holder = ImageViewHolder(view, fragment)
            holder.imageView = view.findViewById(R.id.card_image)
            return holder
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            holder.bind(position)
        }

        override fun getItemCount(): Int {
            return images.count();
        }
    }

    class ImagePagerFragment : Fragment(R.layout.fragment_image_pager) {

        var curPosition: Int = 0
        lateinit var viewPager: ViewPager

        companion object {
            operator fun invoke(position: Int) : Fragment {
                val fragment: ImagePagerFragment = ImagePagerFragment()
                fragment.arguments = bundleOf("position" to position)
                return fragment
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            sharedElementEnterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image)
            setEnterSharedElementCallback(object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: MutableList<String>?,
                    sharedElements: MutableMap<String, View>?
                ) {
                    // 返回去的时候，transitionName不用变，一来一回必须用同一个transitionName，sharedView需要随着ViewPager改变而改变。
                    val transitionName = images[curPosition]
                    val sharedView = (viewPager.adapter?.instantiateItem(
                        viewPager,
                        curPosition
                    ) as? Fragment)?.view?.findViewById<ImageView>(
                        R.id.image
                    )
                    sharedView?.let {
                        sharedElements?.put(names?.get(0) ?: "", it)
                    }
                }
            })
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            curPosition = requireArguments().getInt("position", 0)
            viewPager = view.findViewById<ViewPager>(R.id.view_pager)
            viewPager.adapter = object : FragmentStatePagerAdapter(
                childFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
                override fun getCount(): Int {
                    return images.count()
                }

                override fun getItem(position: Int): Fragment {
                    return ImageItemFragment(position)
                }
            }
            viewPager.currentItem = curPosition
            viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    // 修改选中的位置
                    curPosition = position
                    GridFragment.gridPosition = position
                }
            })

            if (savedInstanceState == null) {
                // 只在页面创建时触发动画
                // 延迟enter动画，因为要等child的Fragment中的图片加载出来
                postponeEnterTransition()
            }
        }
    }

    class ImageItemFragment : Fragment(R.layout.fragment_image_item) {
        companion object {
            operator fun invoke(position: Int) : Fragment {
                val fragment = ImageItemFragment()
                fragment.arguments = bundleOf("position" to position)
                return fragment
            }
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            val position = requireArguments().getInt("position", 0)
            val imageView = view.findViewById<ImageView>(R.id.image)
            val url = images[position]
            ViewCompat.setTransitionName(imageView, url)
            Glide.with(view).load(images[position]).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // 图片加载完毕，开始enter的动画
                    parentFragment?.startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // 图片加载完毕，开始enter的动画
                    parentFragment?.startPostponedEnterTransition()
                    return false
                }

            }).apply(RequestOptions().dontTransform()).into(imageView)
        }
    }
}