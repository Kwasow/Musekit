package com.kwasow.musekit.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.ViewMenuItemBinding
import java.lang.RuntimeException

class MenuItem(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
  private val itemTitle: String
  private val leadingIcon: Int
  private val leadingIconContentDescription: String

  private val binding: ViewMenuItemBinding

  init {
    binding = ViewMenuItemBinding.inflate(LayoutInflater.from(context), this)

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

    setRootProps()
    setChildrenProps()
  }

  private fun setRootProps() {
    isClickable = true
    isFocusable = true
    val value = TypedValue()
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, value, true)
    background = AppCompatResources.getDrawable(context, value.resourceId)
    orientation = HORIZONTAL
  }

  private fun setChildrenProps() {
    binding.menuItemTitle.text = itemTitle;
    binding.menuItemLeadingIcon.apply {
      setImageResource(leadingIcon)
      contentDescription = leadingIconContentDescription
    }
  }

}