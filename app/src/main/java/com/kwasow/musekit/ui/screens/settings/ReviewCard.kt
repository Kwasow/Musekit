package com.kwasow.musekit.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    AnimatedVisibility(visible = visible) {
        Card(
            modifier = Modifier.padding(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.review_question),
                )

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { onResult(ReviewRequestResult.NEVER) }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription =
                                stringResource(
                                    id = R.string.contentDescription_review_never,
                                ),
                        )
                    }

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
