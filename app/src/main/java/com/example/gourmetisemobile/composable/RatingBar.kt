package com.example.gourmetisemobile.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RatingBar(
    maxStars: Int = 5,
    currentRating: Int,
    title: String = "",
    onRatingChanged: (Int) -> Unit
) {
    Row {
        if (title.isNotEmpty()){
            Text(modifier = Modifier.weight(0.3f),
                text = title)
        }
        Row(modifier = Modifier.weight(0.7f)){
            for (i in 1..maxStars) {
                Icon(
                    imageVector = Icons.Default.Star ,
                    contentDescription = "Note $i",
                    modifier = Modifier
                        .clickable { onRatingChanged(i) },
                    tint = if (i <= currentRating) Color(0xFFFFD700) else Color.Gray
                )
            }
        }
    }
}