package com.kwasow.musekit.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.ViewMenuSectionBinding
import java.lang.Exception
import java.lang.RuntimeException

class MenuSection(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    // ====== Fields
    private var sectionTitle: String

    private val binding: ViewMenuSectionBinding =
        ViewMenuSectionBinding.inflate(LayoutInflater.from(context), this)

    // ====== Constructors
    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.MenuSection, 0, 0)
            .apply {
                sectionTitle = getString(R.styleable.MenuSection_sectionTitle)
                    ?: throw RuntimeException(
                        "The 'sectionTitle' attribute on MenuSection is required",
                    )

                recycle()
            }

        setRootProps()
        setChildrenProps()
    }

    // ====== Interface methods
    override fun addView(
        child: View?,
        index: Int,
        params: ViewGroup.LayoutParams?,
    ) {
        try {
            binding.menuSectionItemList.addView(child, index, params)
        } catch (e: Exception) {
            super.addView(child, index, params)
        }
    }

    // ======= Private methods
    private fun setRootProps() {
        orientation = VERTICAL
    }

    private fun setChildrenProps() {
        binding.menuSectionTitle.text = sectionTitle
    }
}
