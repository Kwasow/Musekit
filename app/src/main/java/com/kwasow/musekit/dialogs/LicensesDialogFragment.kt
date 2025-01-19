package com.kwasow.musekit.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.RawRes
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.DialogLicensesBinding

class LicensesDialogFragment: DialogFragment() {

    // ====== Fields
    companion object {
        const val TAG = "LicensesDialogFragment"
    }

    // ====== Interface methods
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogLicensesBinding.inflate(layoutInflater)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.licenses)
            .setIcon(R.drawable.ic_file)
            .setNeutralButton(R.string.close) { _, _ -> }
            .setView(binding.root)
            .create()

        val licenseDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setNeutralButton(R.string.close) { _, _ -> }

        binding.buttonLicenseThisApp.setOnClickListener {
            val license = readRawFileAsString(R.raw.gpl3)

            dialog.dismiss()
            licenseDialogBuilder.setTitle(binding.textThisApp.text)
            licenseDialogBuilder.setMessage(license)
            licenseDialogBuilder.show()
        }

        binding.buttonLicenseIcons.setOnClickListener {
            val license = readRawFileAsString(R.raw.mit)

            dialog.dismiss()
            licenseDialogBuilder.setTitle(binding.textIcons.text)
            licenseDialogBuilder.setMessage(license)
            licenseDialogBuilder.show()
        }

        binding.buttonLicenseTarsos.setOnClickListener {
            val license = readRawFileAsString(R.raw.gpl3)

            dialog.dismiss()
            licenseDialogBuilder.setTitle(binding.textTarsos.text)
            licenseDialogBuilder.setMessage(license)
            licenseDialogBuilder.show()
        }

        return dialog
    }

    // ====== Private methods
    private fun readRawFileAsString(@RawRes id: Int): String {
        val inputStream = resources.openRawResource(id)
        val byteArray = ByteArray(inputStream.available())
        inputStream.read(byteArray)

        return String(byteArray)
    }
}