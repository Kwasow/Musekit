package com.kwasow.musekit.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kwasow.musekit.BuildConfig
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.FragmentSettingsBinding
import com.kwasow.musekit.dialogs.LicensesDialogFragment
import com.kwasow.musekit.dialogs.ThemeSettingsDialogFragment
import com.kwasow.musekit.views.MenuItem

class SettingsFragment : Fragment() {

    // ====== Fields
    private lateinit var appVersion: TextView
    private lateinit var itemThemeSettings: MenuItem
    private lateinit var itemSourceCode: MenuItem
    private lateinit var itemMastodon: MenuItem
    private lateinit var itemWebsite: MenuItem
    private lateinit var itemLicenses: MenuItem

    // ====== Interface methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSettingsBinding.inflate(inflater)

        appVersion = binding.appVersion
        itemThemeSettings = binding.itemThemeSettings
        itemSourceCode = binding.itemSourceCode
        itemMastodon = binding.itemMastodon
        itemWebsite = binding.itemWebsite
        itemLicenses = binding.itemLicenses

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        appVersion.text = getString(R.string.version, BuildConfig.VERSION_NAME)
        setupAboutSection()
        setupSettingsSection()
    }

    // ====== Private methods
    private fun setupSettingsSection() {
        itemThemeSettings.setOnClickListener {
            showThemeSettingsDialog()
        }
    }

    private fun setupAboutSection() {
        itemSourceCode.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/Kwasow/Musekit")
            )
            startActivity(browserIntent)
        }

        itemMastodon.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://mstdn.social/@kwasow")
            )
            startActivity(browserIntent)
        }

        itemWebsite.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://kwasow.pl")
            )
            startActivity(browserIntent)
        }

        itemLicenses.setOnClickListener {
            showLicensesDialog()
        }
    }

    private fun showLicensesDialog() =
        LicensesDialogFragment().show(
            childFragmentManager,
            LicensesDialogFragment.TAG
        )

    private fun showThemeSettingsDialog() =
        ThemeSettingsDialogFragment().show(
            childFragmentManager,
            ThemeSettingsDialogFragment.TAG
        )
}
