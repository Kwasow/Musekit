package com.kwasow.musekit.views

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.databinding.ViewTunerBinding
import kotlin.math.roundToInt

class TunerView : LinearLayout {

    // ====== Fields
    private val binding: ViewTunerBinding = ViewTunerBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private val checkMark: ImageView = binding.tunerCheckMark
    private val currentNote: AppCompatTextView = binding.currentNoteText
    private val currentCents: TextView = binding.currentCentsText

    private val inactiveColor = MaterialColors.getColor(
        this,
        com.google.android.material.R.attr.colorSurfaceContainerHighest
    )
    private val activeColor = MaterialColors.getColor(
        this,
        com.google.android.material.R.attr.colorPrimary
    )

    private val centBoundaries = listOf(
        -50.0, -45.0, -35.0, -25.0, -15.0, -5.0,
        5.0, 15.0, 25.0, 35.0, 45.0, 50.0
    )

    private val bars: Map<Int, MaterialCardView> = mapOf(
        -5 to binding.pitchUnder5,
        -4 to binding.pitchUnder4,
        -3 to binding.pitchUnder3,
        -2 to binding.pitchUnder2,
        -1 to binding.pitchUnder1,
        1 to binding.pitchOver1,
        2 to binding.pitchOver2,
        3 to binding.pitchOver3,
        4 to binding.pitchOver4,
        5 to binding.pitchOver5
    )

    private val timer = object : CountDownTimer(3000, 1000) {
        override fun onTick(millisUntilFinished: Long) = Unit

        override fun onFinish() {
            unmarkAll()
            setNote(null)
        }
    }

    // ====== Constructors
    constructor(context: Context) :
        super(context)

    constructor(context: Context, attrs: AttributeSet) :
        super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) :
        super(context, attrs, defStyleAttr, defStyleRes)

    // ====== Public methods
    fun updateState(note: Note?, cents: Double?) {
        timer.cancel()
        timer.start()

        unmarkAll()
        setNote(note, cents)

        if (note == null || cents == null) {
            return
        }

        if (cents < centBoundaries.first() || cents > centBoundaries.last()) {
            throw IllegalArgumentException("cents must be in range -50, 50")
        }

        for (i in 0 until (centBoundaries.size - 1)) {
            if (centBoundaries[i] <= cents && cents <= centBoundaries[i + 1]) {
                markPitchBar(i - 5)
                break
            }
        }
    }

    // ====== Private methods
    private fun setNote(note: Note?, cents: Double? = null) {
        if (note == null || cents == null) {
            currentNote.text = "—"
            currentCents.text = ""
        } else {
            val roundedCents = (cents * 100).roundToInt() / 100.0

            currentNote.text = note.getSuperscripted(context)
            currentCents.text = roundedCents.toString()
        }
    }

    private fun markPitchBar(id: Int) {
        if (id == 0) {
            checkMark.drawable.setTint(activeColor)
        } else {
            val bar = bars[id]
                ?: throw IllegalArgumentException("Bar IDs can only be in -5..5")

            bar.background.setTint(activeColor)
        }
    }

    private fun unmarkAll() {
        bars.forEach {
            it.value.background.setTint(inactiveColor)
        }

        checkMark.drawable.setTint(inactiveColor)
    }
}
