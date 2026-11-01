package com.example.assignment2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

/** Screen B: load an image from the internet (Glide) and pinch to scale it. */
class ImageScaleFragment : Fragment(R.layout.fragment_image_scale) {

    private var scale = 1f

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView = view.findViewById<ImageView>(R.id.imageView)

        // Load image using Glide
        Glide.with(this)
            .load("https://picsum.photos/id/237/1200/800")
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_dialog_alert)
            .into(imageView)

        val scaleDetector = ScaleGestureDetector(
            requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    scale *= detector.scaleFactor
                    scale = scale.coerceIn(0.5f, 5.0f)
                    imageView.scaleX = scale
                    imageView.scaleY = scale
                    return true
                }
            }
        )

        val gestureDetector = GestureDetector(
            requireContext(),
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    scale = 1f
                    imageView.scaleX = scale
                    imageView.scaleY = scale
                    return true
                }
            }
        )

        imageView.setOnTouchListener { _, event ->
            scaleDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)
            true
        }
    }
}
