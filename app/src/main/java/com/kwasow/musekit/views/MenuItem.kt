package com.kwasow.musekit.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.ViewMenuItemBinding
import java.lang.RuntimeException

class MenuItem(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    // ====== Fields
    private val itemTitle: String
    private val itemSubtitle: String?
    private val leadingIcon: Int
    private val leadingIconContentDescription: String
    private val useTint: Boolean

    private val binding: ViewMenuItemBinding =
        ViewMenuItemBinding.inflate(LayoutInflater.from(context), this)

    // ====== Constructors
    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.MenuItem, 0, 0)
            .apply {
                itemTitle = getString(R.styleable.MenuItem_itemTitle)
                    ?: throw RuntimeException("The 'itemTitle' attribute on MenuItem is required")
                itemSubtitle = getString(R.styleable.MenuItem_itemSubtitle)
                leadingIcon = getResourceId(R.styleable.MenuItem_leadingIcon, -1)
                leadingIconContentDescription =
                    getString(R.styleable.MenuItem_leadingIconContentDescription) ?: ""
                useTint = getBoolean(R.styleable.MenuItem_useTint, true)

                if (leadingIcon == -1) {
                    throw RuntimeException("The 'leadingIcon' attribute on MenuItem is required")
                }

                recycle()
            }

        setRootProps()
        setChildrenProps()
    }

    // ====== Public methods
    fun getTrailingImageView(): ImageView {
        return binding.menuItemTrailingIcon
    }

    fun getLeadingImageView(): ImageView {
        return binding.menuItemLeadingIcon
    }

    // ====== Private methods
    private fun setRootProps() {
        if (isClickable) {
            val value = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, value, true)
            background = AppCompatResources.getDrawable(context, value.resourceId)
        }
    }

    private fun setChildrenProps() {
        binding.menuItemTitle.text = itemTitle
        binding.menuItemSubtitle.text = itemSubtitle ?: ""
        binding.menuItemLeadingIcon.apply {
            setImageResource(leadingIcon)
            contentDescription = leadingIconContentDescription
        }

        if (useTint) {
            val value = TypedValue()
            context.theme.resolveAttribute(android.R.attr.textColorTertiary, value, true)

            binding.menuItemLeadingIcon.setColorFilter(
                ContextCompat.getColor(context, value.resourceId),
            )
        }
    }
}
