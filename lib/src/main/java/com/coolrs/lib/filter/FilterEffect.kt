package com.coolrs.lib.filter

import android.widget.ImageView
import com.coolrs.lib.color.ColorGrayscaleEffect
import java.io.InputStream

class BlurFilterEffect(private val image: ImageView, src: InputStream) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Filter (Blur)"
	override fun apply() = bitmap.blur(image.context)
}

class SharpenFilterEffect(private val image: ImageView, src: InputStream) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Filter (Sharpen)"
	override fun apply() = bitmap.sharpen(image.context)
}

class EdgeFilterEffect(private val image: ImageView, src: InputStream) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Filter (Edge)"
	override fun apply() = bitmap.edge(image.context)
}

class EmbossFilterEffect(private val image: ImageView, src: InputStream) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Filter (Emboss)"
	override fun apply() = bitmap.emboss(image.context)
}

class BoxBlurFilterEffect(private val image: ImageView, src: InputStream) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Filter (Box Blur)"
	override fun apply() = bitmap.boxBlur(image.context)
}

class BloomFilterEffect(private val image: ImageView, src: InputStream) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Filter (Bloom)"
	override fun apply() = bitmap.bloom(image.context)
}
