package com.kwasow.musekit.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.BuildConfig
import com.kwasow.musekit.R
import com.kwasow.musekit.data.NotationStyle
import com.kwasow.musekit.models.SettingsScreenViewModel
import com.kwasow.musekit.ui.dialogs.LicenseDialog
import com.kwasow.musekit.ui.dialogs.LicensesDialog
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.ui.screens.settings.SettingsEntry
import pl.kwasow.ui.screens.settings.SettingsSection

// ====== Public composables
@Composable
fun SettingsScreen() {
    val viewModel = koinViewModel<SettingsScreenViewModel>()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
    ) {
        AppDetails()
        AppSettingsSection()
        AboutSection()
        Footer()
    }

    if (viewModel.showLicensesDialog) {
        LicensesDialog(
            onDismissRequest = { viewModel.closeDialogs() },
            openSubDialog = { name, file -> viewModel.openLicenseDialog(name, file) },
        )
    }

    val licenseName = viewModel.currentLicenseName
    val licenseText = viewModel.currentLicenseText
    if (licenseName != null && licenseText != null) {
        LicenseDialog(
            name = licenseName,
            content = licenseText,
            onDismissRequest = { viewModel.closeDialogs() },
        )
    }
}

// ====== Private composables
@Composable
private fun AppDetails() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedCard(
            modifier = Modifier.padding(24.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = stringResource(id = R.string.contentDescription_app_icon),
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.FillBounds,
            )
        }

        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = stringResource(id = R.string.version, BuildConfig.VERSION_NAME),
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
        )
    }
}

@Composable
private fun AppSettingsSection() {
    SettingsSection(title = stringResource(id = R.string.settings)) {
        SettingsEntry(
            icon = painterResource(id = R.drawable.ic_moon),
            iconDescription = stringResource(id = R.string.contentDescription_moon_icon),
            name = stringResource(id = R.string.theme),
            description = stringResource(id = R.string.theme_subtitle),
            onClick = {},
        )

        HorizontalDivider()

        SettingsEntry(
            icon = painterResource(id = R.drawable.ic_globe),
            iconDescription = stringResource(id = R.string.contentDescription_localization),
            name = stringResource(id = R.string.notation_style),
            description = stringResource(id = R.string.notation_style_subtitle),
            onClick = null,
        )

        SingleChoiceSegmentedButtonRow(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
        ) {
            NotationStyle.entries.forEachIndexed { index, style ->
                SegmentedButton(
                    shape =
                        SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = NotationStyle.entries.size,
                        ),
                    onClick = {},
                    selected = false,
                    label = { Text(stringResource(id = style.nameId)) },
                )
            }
        }
    }
}

@Composable
private fun AboutSection() {
    val viewModel = koinViewModel<SettingsScreenViewModel>()

    SettingsSection(title = stringResource(id = R.string.about)) {
        SettingsEntry(
            icon = painterResource(id = R.drawable.ic_github),
            iconDescription = stringResource(id = R.string.contentDescription_github_logo),
            name = stringResource(id = R.string.source_code),
            description = stringResource(id = R.string.source_code_subtitle),
            onClick = { viewModel.openGithub() },
        )

        HorizontalDivider()

        SettingsEntry(
            icon = painterResource(id = R.drawable.ic_mastodon),
            iconDescription = stringResource(id = R.string.contentDescription_mastodon_logo),
            name = stringResource(id = R.string.developer),
            description = stringResource(id = R.string.developer_subtitle),
            onClick = { viewModel.openMastodon() },
        )

        HorizontalDivider()

        SettingsEntry(
            icon = painterResource(id = R.drawable.ic_web),
            iconDescription = stringResource(id = R.string.contentDescription_internet_website),
            name = stringResource(id = R.string.developer_website),
            description = stringResource(id = R.string.developer_website_subtitle),
            onClick = { viewModel.openWebsite() },
        )

        HorizontalDivider()

        SettingsEntry(
            icon = painterResource(id = R.drawable.ic_file),
            iconDescription = stringResource(id = R.string.contentDescription_file_icon),
            name = stringResource(id = R.string.licenses),
            description = stringResource(id = R.string.licenses_subtitle),
            onClick = { viewModel.showLicensesDialog = true },
        )
    }
}

@Composable
private fun Footer() {
    Text(
        text = stringResource(id = R.string.special_thanks),
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center,
        fontStyle = FontStyle.Italic,
        modifier =
            Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth(),
    )
}
