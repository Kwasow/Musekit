package com.kwasow.musekit.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.kwasow.musekit.databinding.ViewTunerBinding

class TunerView : LinearLayout {
    // ====== Fields
    private val binding: ViewTunerBinding = ViewTunerBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
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
}
