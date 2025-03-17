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
import com.kwasow.musekit.managers.ThemeManager

class ThemeSettingsDialogFragment : DialogFragment() {
    // ====== Fields
    companion object {
        const val TAG = "ThemeSettingsDialogFragment"
    }

    // ====== Interface methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogThemeSettingsBinding.inflate(layoutInflater)

        val dialog =
            MaterialAlertDialogBuilder(requireContext())
                .setNeutralButton(R.string.close) { _, _ -> }
                .setView(binding.root)
                .create()

        // Theme settings
        when (ThemeManager.getNightMode()) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                binding.itemThemeFollowSystem.getTrailingImageView()
                    .setImageResource(R.drawable.ic_check)
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                binding.itemThemeLight.getTrailingImageView()
                    .setImageResource(R.drawable.ic_check)
            }

            AppCompatDelegate.MODE_NIGHT_YES -> {
                binding.itemThemeDark.getTrailingImageView()
                    .setImageResource(R.drawable.ic_check)
            }
        }

        if (Build.VERSION.SDK_INT >= 29) {
            binding.itemThemeFollowSystem.setOnClickListener {
                setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        } else {
            binding.itemThemeFollowSystem.visibility = View.GONE
        }

        binding.itemThemeLight.getLeadingImageView().setColorFilter(Color.WHITE)
        binding.itemThemeLight.setOnClickListener {
            setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.itemThemeDark.getLeadingImageView().setColorFilter(Color.BLACK)
        binding.itemThemeDark.setOnClickListener {
            setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        return dialog
    }

    // ====== Private methods
    private fun setNightMode(nightMode: Int) {
        ThemeManager.setNightMode(nightMode)
        dismiss()
    }
}
