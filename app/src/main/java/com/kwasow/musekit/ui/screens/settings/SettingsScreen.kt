package com.kwasow.musekit.ui.screens.settings

import android.os.Build
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.kwasow.musekit.data.dialogs.LicenseDialogInfo
import com.kwasow.musekit.ui.components.SettingsEntry
import com.kwasow.musekit.ui.components.SettingsSection
import com.kwasow.musekit.ui.dialogs.LicenseDialog
import org.koin.androidx.compose.koinViewModel
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM as NIGHT_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO as NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES as NIGHT_YES

// ====== Public composables
@Composable
fun SettingsScreen() {
    val viewModel = koinViewModel<SettingsScreenViewModel>()
    var licenseDialog = remember { LicenseDialogInfo() }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
    ) {
        AppDetails()
        AppSettingsSection()
        AboutSection(
            onOpenLicenseDialog = { licenseDialog.state = LicenseDialogInfo.State.DIALOG_OPEN },
        )
        Footer()
    }

    LicenseDialog(
        details = licenseDialog,
        openFile = { viewModel.openFile(it) },
    )
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
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
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
        ThemeSetting()

        HorizontalDivider()

        NotationStyleSetting()
    }
}

@Composable
private fun ThemeSetting() {
    val viewModel = koinViewModel<SettingsScreenViewModel>()
    val nightMode by viewModel.nightMode.collectAsState()

    val count = if (Build.VERSION.SDK_INT >= 29) 3 else 2

    SettingsEntry(
        icon = painterResource(id = R.drawable.ic_moon),
        iconDescription = stringResource(id = R.string.contentDescription_moon_icon),
        name = stringResource(id = R.string.theme),
        description = stringResource(id = R.string.theme_subtitle),
        onClick = null,
    )

    SingleChoiceSegmentedButtonRow(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
    ) {
        SegmentedButton(
            shape =
                SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = count,
                ),
            onClick = { viewModel.updateNightMode(NIGHT_NO) },
            selected = nightMode == NIGHT_NO,
            label = { Text(stringResource(id = R.string.theme_light)) },
        )

        if (Build.VERSION.SDK_INT >= 29) {
            SegmentedButton(
                shape =
                    SegmentedButtonDefaults.itemShape(
                        index = 1,
                        count = count,
                    ),
                onClick = { viewModel.updateNightMode(NIGHT_SYSTEM) },
                selected = nightMode == NIGHT_SYSTEM,
                label = { Text(stringResource(id = R.string.theme_auto)) },
            )
        }

        SegmentedButton(
            shape =
                SegmentedButtonDefaults.itemShape(
                    index = count - 1,
                    count = count,
                ),
            onClick = { viewModel.updateNightMode(NIGHT_YES) },
            selected = nightMode == NIGHT_YES,
            label = { Text(stringResource(id = R.string.theme_dark)) },
        )
    }
}

@Composable
private fun NotationStyleSetting() {
    val viewModel = koinViewModel<SettingsScreenViewModel>()
    val notationStyle by viewModel.notationStyle.collectAsState()

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
                onClick = { viewModel.updateNotationStyle(style) },
                selected = notationStyle?.id == style.id,
                label = { Text(stringResource(id = style.nameId)) },
            )
        }
    }
}

@Composable
private fun AboutSection(onOpenLicenseDialog: () -> Unit) {
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
            onClick = onOpenLicenseDialog,
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
