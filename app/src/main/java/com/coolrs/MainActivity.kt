package com.coolrs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.coolrs.lib.Effect
import com.coolrs.lib.color.BlackAndWhiteColorEffect
import com.coolrs.lib.color.BrightnessColorEffect
import com.coolrs.lib.color.ColorGrayscaleEffect
import com.coolrs.lib.color.HistoEqColorEffect
import com.coolrs.lib.color.InvertColorEffect
import com.coolrs.lib.color.OffsetColorEffect
import com.coolrs.lib.color.PolaroidColorEffect
import com.coolrs.lib.color.SaturateColorEffect
import com.coolrs.lib.color.SepiaColorEffect
import com.coolrs.lib.conway.ConwayEffect
import com.coolrs.lib.filter.BloomFilterEffect
import com.coolrs.lib.filter.BlurFilterEffect
import com.coolrs.lib.filter.BoxBlurFilterEffect
import com.coolrs.lib.filter.EdgeFilterEffect
import com.coolrs.lib.filter.EmbossFilterEffect
import com.coolrs.lib.filter.SharpenFilterEffect
import com.coolrs.lib.fire.CoolFireEffect
import com.coolrs.lib.fire.FireEffect
import com.coolrs.lib.fire.FirePalette
import com.coolrs.lib.fire.IceFireEffect
import com.coolrs.lib.fire.PinkFireEffect
import com.coolrs.lib.fire.PoisonFireEffect
import com.coolrs.lib.fire.PurpleFireEffect
import com.coolrs.lib.fire.SmokeFireEffect
import com.coolrs.lib.fire.TextFireEffect
import com.coolrs.lib.transition.BlendTransitionEffect
import com.coolrs.lib.transition.HorizontalTransitionEffect
import com.coolrs.lib.transition.VerticalTransitionEffect
import com.coolrs.lib.turmite.TurmiteEffect
import kotlinx.android.synthetic.main.activity_main.fab
import kotlinx.android.synthetic.main.activity_main.name
import kotlinx.android.synthetic.main.activity_main.rs_image

class MainActivity : AppCompatActivity() {

    companion object { private const val CURRENT_KEY = "current" }

    private val effects: MutableList<Effect> = mutableListOf()
	private var current: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Try to retrieve state
        savedInstanceState?.let { current = it.getInt(CURRENT_KEY) }

		// Fire effects
        effects.add(FireEffect(rs_image, 128, 128, 4.05f))
        effects.add(CoolFireEffect(rs_image, 128, 128, 4.015f))
        effects.add(SmokeFireEffect(rs_image, 128, 128, 4.025f))
        effects.add(PoisonFireEffect(rs_image, 128, 128, 4.035f))
		effects.add(IceFireEffect(rs_image, 128, 128, 4.045f))
        effects.add(PinkFireEffect(rs_image, 128, 128, 4.055f))
        effects.add(PurpleFireEffect(rs_image, 128, 128, 4.0f))

        // TextFire
        effects.add(TextFireEffect(rs_image, 128, 128, "Fire", FirePalette.Fire, 6f))
        effects.add(TextFireEffect(rs_image, 128, 128, "Cool", FirePalette.Cool, 5.5f))
        effects.add(TextFireEffect(rs_image, 128, 128, "Smoke", FirePalette.Smoke, 6.5f))
        effects.add(TextFireEffect(rs_image, 128, 128, "Poison", FirePalette.Poison, 5f))
        effects.add(TextFireEffect(rs_image, 128, 128, "Ice", FirePalette.Ice, 4.5f))
        effects.add(TextFireEffect(rs_image, 128, 128, "Pink", FirePalette.Pink, 7f))
        effects.add(TextFireEffect(rs_image, 128, 128, "Purple", FirePalette.Purple, 4.25f))

        // Color effects
        effects.add(ColorGrayscaleEffect(rs_image, resources.openRawResource(R.raw.test3)))
        effects.add(PolaroidColorEffect(rs_image, resources.openRawResource(R.raw.test3)))
        effects.add(InvertColorEffect(rs_image, resources.openRawResource(R.raw.test3)))
        effects.add(BlackAndWhiteColorEffect(rs_image, resources.openRawResource(R.raw.test3)))
        effects.add(SepiaColorEffect(rs_image, resources.openRawResource(R.raw.test3)))
        effects.add(BrightnessColorEffect(rs_image, resources.openRawResource(R.raw.test3), 0.5f))
        effects.add(SaturateColorEffect(rs_image, resources.openRawResource(R.raw.test3), 1.5f))
        effects.add(OffsetColorEffect(rs_image, resources.openRawResource(R.raw.test3), 0.3f, 0f, -0.2f))
        effects.add(HistoEqColorEffect(rs_image, resources.openRawResource(R.raw.test3)))

        // Filter effects
        effects.add(BlurFilterEffect(rs_image, resources.openRawResource(R.raw.test3)))
        effects.add(SharpenFilterEffect(rs_image, resources.openRawResource(R.raw.test3)))
        effects.add(EdgeFilterEffect(rs_image, resources.openRawResource(R.raw.test3)))
        effects.add(EmbossFilterEffect(rs_image, resources.openRawResource(R.raw.test3)))
        effects.add(BoxBlurFilterEffect(rs_image, resources.openRawResource(R.raw.test3)))
        effects.add(BloomFilterEffect(rs_image, resources.openRawResource(R.raw.test3)))

		// Conway's Game of Life
		effects.add(ConwayEffect(rs_image, 90, 160))

        // Turmites
        effects.add(TurmiteEffect(rs_image, 90, 160, intArrayOf(44, 79, 0, 'N'.toInt()), "140210320040"))
        effects.add(TurmiteEffect(rs_image, 90, 160, intArrayOf(44, 79, 0, 'N'.toInt()), "140220380080"))
        effects.add(TurmiteEffect(rs_image, 90, 160, intArrayOf(44, 79, 0, 'N'.toInt()), "182020180020080081"))
        effects.add(TurmiteEffect(rs_image, 90, 160, intArrayOf(44, 79, 0, 'N'.toInt()), "121020010121"))
        effects.add(TurmiteEffect(rs_image, 90, 160, intArrayOf(44, 79, 0, 'N'.toInt()), "111021180120"))
        effects.add(TurmiteEffect(rs_image, 90, 160, intArrayOf(44, 79, 0, 'N'.toInt()), "121021110111"))
        effects.add(TurmiteEffect(rs_image, 90, 160, intArrayOf(44, 79, 0, 'N'.toInt()), "120210040"))
        effects.add(TurmiteEffect(rs_image, 90, 160, intArrayOf(44, 79, 0, 'N'.toInt()), "120220340440410"))

        // Transitions
        effects.add(HorizontalTransitionEffect(rs_image, resources.openRawResource(R.raw.test), resources.openRawResource(R.raw.test2)))
        effects.add(VerticalTransitionEffect(rs_image, resources.openRawResource(R.raw.test), resources.openRawResource(R.raw.test2)))
        effects.add(BlendTransitionEffect(rs_image, resources.openRawResource(R.raw.test), resources.openRawResource(R.raw.test2)))

        // Fab
        fab.setOnClickListener {
            effects[current].stop()
            current = (current + 1) % effects.size
            effects[current].start()
            name.text = effects[current].name()
        }
    }

    override fun onResume() {
        super.onResume()
        effects[current].start()
        name.text = effects[current].name()
    }

    override fun onPause() {
		effects[current].stop()
        name.text = ""
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CURRENT_KEY, current)
    }

}
