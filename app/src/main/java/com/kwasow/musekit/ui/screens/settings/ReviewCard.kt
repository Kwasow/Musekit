package com.kwasow.musekit.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.R
import com.kwasow.musekit.data.ReviewRequestResult

// ====== Public composables
@Composable
fun ReviewCard(
    visible: Boolean,
    onResult: (ReviewRequestResult) -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
    ) {
        OutlinedCard(
            modifier = Modifier.padding(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Like the app?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription =
                            stringResource(
                                id = R.string.contentDescription_review_never,
                            ),
                        modifier = Modifier.clickable { onResult(ReviewRequestResult.NEVER) },
                    )
                }

                Text(text = "I would love to hear your opinion!")

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    TextButton(onClick = { onResult(ReviewRequestResult.LATER) }) {
                        Text(text = stringResource(id = R.string.review_later))
                    }

                    Button(onClick = { onResult(ReviewRequestResult.YES) }) {
                        Text(text = stringResource(id = R.string.review_now))
                    }
                }
            }
        }
    }
}

// ====== Previews
@Preview
@Composable
fun ReviewCardPreview() {
    ReviewCard(visible = true, onResult = {})
}
