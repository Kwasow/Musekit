package com.kwasow.musekit.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kwasow.musekit.BuildConfig
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.DialogLicensesBinding
import com.kwasow.musekit.databinding.DialogThemeSettingsBinding
import com.kwasow.musekit.databinding.FragmentSettingsBinding
import com.kwasow.musekit.utils.ThemeUtils

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.appVersion.text = getString(R.string.version, BuildConfig.VERSION_NAME)
        setupAboutSection()
        setupSettingsSection()
    }

    private fun setupSettingsSection() {
        binding.itemThemeSettings.setOnClickListener {
            showThemeSettingsDialog()
        }
    }

    private fun setupAboutSection() {
        binding.itemSourceCode.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/Kwasow/Musekit")
            )
            startActivity(browserIntent)
        }
        binding.itemTwitter.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/KarolWasowski")
            )
            startActivity(browserIntent)
        }
        binding.itemWebsite.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://kwasow.github.io/#/")
            )
            startActivity(browserIntent)
        }
        binding.itemLicenses.setOnClickListener {
            showLicensesDialog()
        }
    }

    private fun showLicensesDialog() {
        val dialogBinding = DialogLicensesBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.licenses)
            .setIcon(R.drawable.ic_file)
            .setNeutralButton(R.string.close) { _, _ -> }
            .setView(dialogBinding.root)
            .create()

        val licenceDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setNeutralButton(R.string.close) { _, _ -> }

        dialogBinding.buttonLicenseThisApp.setOnClickListener {
            val license = readRawFileAsString(R.raw.gpl3)

            dialog.dismiss()
            licenceDialogBuilder.setTitle(dialogBinding.textThisApp.text)
            licenceDialogBuilder.setMessage(license)
            licenceDialogBuilder.show()
        }

        dialogBinding.buttonLicenseIcons.setOnClickListener {
            val license = readRawFileAsString(R.raw.mit)

            dialog.dismiss()
            licenceDialogBuilder.setTitle(dialogBinding.textIcons.text)
            licenceDialogBuilder.setMessage(license)
            licenceDialogBuilder.show()
        }

        dialog.show()
    }

    private fun readRawFileAsString(@RawRes id: Int): String {
        val inputStream = resources.openRawResource(id)
        val byteArray = ByteArray(inputStream.available())
        inputStream.read(byteArray)

        return String(byteArray)
    }

    private fun showThemeSettingsDialog() {
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

        // Accent colours
        dialogBinding.itemAccentGreen.getLeadingImageView().setColorFilter(Color.GREEN)
        dialogBinding.itemAccentGreen.setOnClickListener {
        }

        dialogBinding.itemAccentRed.getLeadingImageView().setColorFilter(Color.RED)
        dialogBinding.itemAccentRed.setOnClickListener {
        }

        dialogBinding.itemAccentBlue.getLeadingImageView().setColorFilter(Color.BLUE)
        dialogBinding.itemAccentBlue.setOnClickListener {
        }

        dialogBinding.itemAccentYellow.getLeadingImageView().setColorFilter(Color.YELLOW)
        dialogBinding.itemAccentYellow.setOnClickListener {
        }

        dialog.show()
    }

    private fun setNightMode(dialog: Dialog, nightMode: Int) {
        ThemeUtils.setNightMode(nightMode)
        dialog.dismiss()
    }
}
