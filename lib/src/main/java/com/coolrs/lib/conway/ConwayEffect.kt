package com.coolrs.lib.conway

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import androidx.core.graphics.ColorUtils
import com.coolrs.lib.Effect
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

open class ConwayEffect(
	private val image: ImageView,
	private val width: Int = 128,
	private val height: Int = 128
) : Effect {

	private var bitmap: Bitmap? = null
	private var job: Job? = null

	override fun start() {
		bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply { eraseColor(Color.BLACK) }

		repeat((width * height * 0.2).toInt()) {
			bitmap?.let { it.setPixel(Random.nextInt(it.width  - 1), Random.nextInt(it.height - 1), Color.RED) }
		}

		image.setImageBitmap(bitmap)
		job = GlobalScope.launch { loop() }
	}

	override fun stop() {
		runBlocking { job?.cancelAndJoin() }
		image.setImageBitmap(null)
		bitmap?.recycle()
		bitmap = null
	}

	override fun name() = "Conway's Game of Life"

	private suspend fun loop() {
		while (true) {
			bitmap.conway(image.context)
			image.postInvalidate()
			delay(50)
		}
	}

}
