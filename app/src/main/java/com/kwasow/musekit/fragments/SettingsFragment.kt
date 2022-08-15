package com.kwasow.musekit.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kwasow.musekit.BuildConfig
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.DialogLicensesBinding
import com.kwasow.musekit.databinding.DialogNightModeBinding
import com.kwasow.musekit.databinding.FragmentSettingsBinding

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
    binding.itemNightMode.getTrailingImageView().apply {
      when (getCurrentNightMode()) {
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> setImageResource(R.drawable.ic_theme_auto)
        AppCompatDelegate.MODE_NIGHT_YES -> setImageResource(R.drawable.ic_theme_dark)
        else -> setImageResource(R.drawable.ic_theme_light)
      }

      contentDescription = getString(R.string.contentDescription_current_theme_symbol)
    }

    binding.itemNightMode.setOnClickListener {
      showNightModeSelectionDialog()
    }
  }

  private fun getCurrentNightMode(): Int {
    val sharedPreferences = requireContext().getSharedPreferences(
      getString(R.string.preferences_file_key),
      Context.MODE_PRIVATE
    )
    return sharedPreferences.getInt(getString(R.string.preferences_night_mode), 1)
  }

  private fun showNightModeSelectionDialog() {
    val dialogBinding = DialogNightModeBinding.inflate(layoutInflater)

    val dialog = MaterialAlertDialogBuilder(requireContext())
      .setTitle(R.string.select_theme)
      .setIcon(R.drawable.ic_moon)
      .setNeutralButton(R.string.close) { _, _ -> }
      .setView(dialogBinding.root)
      .create()

    if (Build.VERSION.SDK_INT < 29) {
      dialogBinding.itemThemeFollowSystem.visibility = View.GONE
    } else {
      dialogBinding.itemThemeFollowSystem.setOnClickListener {
        setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, dialog)
      }
    }

    dialogBinding.itemThemeLight.setOnClickListener {
      setNightMode(AppCompatDelegate.MODE_NIGHT_NO, dialog)
    }

    dialogBinding.itemThemeDark.setOnClickListener {
      setNightMode(AppCompatDelegate.MODE_NIGHT_YES, dialog)
    }

    val currentThemeSelectionItem = when(getCurrentNightMode()) {
      AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> dialogBinding.itemThemeFollowSystem
      AppCompatDelegate.MODE_NIGHT_YES -> dialogBinding.itemThemeDark
      else -> dialogBinding.itemThemeLight
    }

    currentThemeSelectionItem.getTrailingImageView().apply {
      setImageResource(R.drawable.ic_check)
      contentDescription = getString(R.string.contentDescription_check_icon)
    }

    dialog.show()
  }

  private fun setNightMode(mode: Int, dialog: AlertDialog) {
    AppCompatDelegate.setDefaultNightMode(mode)
    val sharedPreferences = requireContext().getSharedPreferences(
      getString(R.string.preferences_file_key),
      Context.MODE_PRIVATE
    )
    sharedPreferences.edit {
      putInt(
        getString(R.string.preferences_night_mode),
        mode
      )
      apply()
    }
    dialog.dismiss()
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
      val inputStream = resources.openRawResource(R.raw.gpl3)
      val b = ByteArray(inputStream.available())
      inputStream.read(b)
      val license = String(b)

      dialog.dismiss()
      licenceDialogBuilder.setTitle(dialogBinding.textThisApp.text)
      licenceDialogBuilder.setMessage(license)
      licenceDialogBuilder.show()
    }

    dialogBinding.buttonLicenseIcons.setOnClickListener {
      val inputStream = resources.openRawResource(R.raw.mit)
      val b = ByteArray(inputStream.available())
      inputStream.read(b)
      val license = String(b)

      dialog.dismiss()
      licenceDialogBuilder.setTitle(dialogBinding.textIcons.text)
      licenceDialogBuilder.setMessage(license)
      licenceDialogBuilder.show()
    }

    dialog.show()
  }
}