package com.kwasow.musekit.ui.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kwasow.musekit.data.ReviewRequestResult

// ====== Public composables
@Composable
fun ReviewCard(
    visible: Boolean,
    onResult: (ReviewRequestResult) -> Unit,
) {
    AnimatedVisibility(visible = visible) {
        Card(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Some text")

                Row {
                    TextButton(onClick = { onResult(ReviewRequestResult.LATER) }) {
                        Text("Later")
                    }

                    Button(onClick = { onResult(ReviewRequestResult.YES) }) {
                        Text("Review")
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
