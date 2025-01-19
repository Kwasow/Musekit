package com.kwasow.musekit.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.RawRes
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kwasow.musekit.R
import com.kwasow.musekit.databinding.DialogLicensesBinding

class LicensesDialogFragment : DialogFragment() {

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

        binding.buttonLicenseThisApp.setOnClickListener {
            showSubDialog(R.raw.gpl3, binding.textThisApp.text)
        }

        binding.buttonLicenseIcons.setOnClickListener {
            showSubDialog(R.raw.mit, binding.textIcons.text)
        }

        binding.buttonLicenseTarsos.setOnClickListener {
            showSubDialog(R.raw.gpl3, binding.textTarsos.text)
        }

        return dialog
    }

    // ====== Private methods
    private fun showSubDialog(
        @RawRes licenseId: Int,
        title: CharSequence
    ) {
        val licenseDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setNeutralButton(R.string.close) { _, _ -> }

        val license = readRawFileAsString(licenseId)

        dismiss()
        licenseDialogBuilder.setTitle(title)
        licenseDialogBuilder.setMessage(license)
        licenseDialogBuilder.show()
    }

    private fun readRawFileAsString(@RawRes id: Int): String {
        val inputStream = resources.openRawResource(id)
        val byteArray = ByteArray(inputStream.available())
        inputStream.read(byteArray)

        return String(byteArray)
    }
}