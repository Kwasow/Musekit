package com.kwasow.musekit.dialogs

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.DialogThemeSettingsBinding
import com.kwasow.musekit.utils.ThemeUtils

class ThemeSettingsDialogFragment : DialogFragment() {

    // ====== Fields
    companion object {
        const val TAG = "ThemeSettingsDialogFragment"
    }

    // ====== Interface methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = DialogThemeSettingsBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setNeutralButton(R.string.close) { _, _ -> }
            .setView(dialogBinding.root)
            .create()

        // Theme settings
        when (ThemeUtils.getNightMode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                dialogBinding.itemThemeFollowSystem.getTrailingImageView()
                    .setImageResource(R.drawable.ic_check)
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                dialogBinding.itemThemeLight.getTrailingImageView()
                    .setImageResource(R.drawable.ic_check)
            }

            AppCompatDelegate.MODE_NIGHT_YES -> {
                dialogBinding.itemThemeDark.getTrailingImageView()
                    .setImageResource(R.drawable.ic_check)
            }
        }

        if (Build.VERSION.SDK_INT >= 29) {
            dialogBinding.itemThemeFollowSystem.setOnClickListener {
                setNightMode(dialog, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        } else {
            dialogBinding.itemThemeFollowSystem.visibility = View.GONE
        }

        dialogBinding.itemThemeLight.getLeadingImageView().setColorFilter(Color.WHITE)
        dialogBinding.itemThemeLight.setOnClickListener {
            setNightMode(dialog, AppCompatDelegate.MODE_NIGHT_NO)
        }

        dialogBinding.itemThemeDark.getLeadingImageView().setColorFilter(Color.BLACK)
        dialogBinding.itemThemeDark.setOnClickListener {
            setNightMode(dialog, AppCompatDelegate.MODE_NIGHT_YES)
        }

        return dialog
    }

    // ====== Private methods
    private fun setNightMode(dialog: Dialog, nightMode: Int) {
        ThemeUtils.setNightMode(nightMode)
        dialog.dismiss()
    }
}
