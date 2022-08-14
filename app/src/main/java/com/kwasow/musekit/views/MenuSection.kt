package com.kwasow.musekit.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.kwasow.musekit.R
import java.lang.RuntimeException

class MenuSection(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
  private val sectionTitle: String

  init {
    inflate(context, R.layout.view_menu_section, this)

    orientation = VERTICAL

    context.theme.obtainStyledAttributes(attrs, R.styleable.MenuSection, 0, 0)
      .apply {
        sectionTitle = getString(R.styleable.MenuSection_sectionTitle)
          ?: throw RuntimeException("The 'sectionTitle' attribute on MenuSection is required")

        recycle()
      }

    findViewById<TextView>(R.id.menuSectionTitle).text = sectionTitle
  }

  override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
    val view = findViewById<LinearLayout>(R.id.menuSectionItemList)
    if (view == null) {
      super.addView(child, index, params)
    } else {
      view.addView(child, index, params)
    }
  }
}