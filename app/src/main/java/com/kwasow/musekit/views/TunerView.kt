package com.kwasow.musekit.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors
import com.kwasow.musekit.data.Note
import com.kwasow.musekit.databinding.ViewTunerBinding

class TunerView : LinearLayout {
    // ====== Fields
    private val binding: ViewTunerBinding = ViewTunerBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private val inactiveColor = MaterialColors.getColor(
        this,
        com.google.android.material.R.attr.colorSurfaceContainerHighest
    )
    private val activeColor = MaterialColors.getColor(
        this,
        com.google.android.material.R.attr.colorPrimary
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
    fun updateState(note: Note?, completeness: Double?) {
        unmarkAll()
        setNote(note)

        if (completeness == null) {
            return
        }

        when (completeness) {
            in -2.0..-0.81 -> markPitchBar(-5)
            in -0.81..0.62 -> markPitchBar(-4)
            in -0.62..0.43 -> markPitchBar(-3)
            in -0.43..0.24 -> markPitchBar(-2)
            in -0.24..0.05 -> markPitchBar(-1)
            in -0.05..0.05 -> markPitchBar(0)
            in 0.05..0.24 -> markPitchBar(1)
            in 0.24..0.43 -> markPitchBar(2)
            in 0.43..0.62 -> markPitchBar(3)
            in 0.62..0.81 -> markPitchBar(4)
            in 0.81..2.0 -> markPitchBar(5)
            else -> throw IllegalArgumentException("completeness has to be in -1f..1f")
        }
    }

    // ====== Private methods
    private fun setNote(note: Note?) {
        if (note == null) {
            binding.currentNoteText.text = "—"
        } else {
            binding.currentNoteText.text = note.getSuperscripted(context)
        }
    }

    private fun markPitchBar(id: Int) {
        if (id == 0) {
            binding.tunerCheckMark.drawable.setTint(activeColor)
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

        binding.tunerCheckMark.drawable.setTint(inactiveColor)
    }
}
