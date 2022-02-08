package com.kwasow.musekit.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kwasow.musekit.BuildConfig
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.DialogLicensesBinding
import com.kwasow.musekit.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(inflater)
        return binding.root
    }

    // TODO: Add developer website
    override fun onStart() {
        super.onStart()

        binding.appVersion.text = getString(R.string.version, BuildConfig.VERSION_NAME)
        binding.buttonSourceCode.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/Kwasow/Musekit"))
            startActivity(browserIntent)
        }
        binding.buttonTwitter.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/KarolWasowski")
            )
            startActivity(browserIntent)
        }
        binding.buttonLicenses.setOnClickListener {
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