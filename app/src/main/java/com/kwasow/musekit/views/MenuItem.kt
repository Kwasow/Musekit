package com.kwasow.musekit.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.kwasow.musekit.R
import java.lang.RuntimeException

class MenuItem(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
  private val itemTitle: String
  private val leadingIcon: Int
  private val leadingIconContentDescription: String

  init {
    inflate(context, R.layout.view_menu_item, this)

    isClickable = true
    isFocusable = true
    val value = TypedValue()
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, value, true)
    background = AppCompatResources.getDrawable(context, value.resourceId)
    orientation = HORIZONTAL

    context.theme.obtainStyledAttributes(attrs, R.styleable.MenuItem, 0, 0)
      .apply {
        itemTitle = getString(R.styleable.MenuItem_itemTitle)
          ?: throw RuntimeException("The 'title' attribute on MenuItem is required")
        leadingIcon = getResourceId(R.styleable.MenuItem_leadingIcon, -1)
        leadingIconContentDescription =
          getString(R.styleable.MenuItem_leadingIconContentDescription) ?: ""

        if (leadingIcon == -1) {
          throw RuntimeException("The 'leadingIcon' attribute on MenuItem is required")
        }

        recycle()
      }

    findViewById<TextView>(R.id.menuItemTitle).text = itemTitle;
    findViewById<ImageView>(R.id.menuItemLeadingIcon).apply {
      setImageResource(leadingIcon)
      contentDescription = leadingIconContentDescription
    }
  }

}