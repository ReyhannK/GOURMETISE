package com.example.gourmetisemobile.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gourmetisemobile.R

@Composable
fun IconWithText(text: String, contentDescription: String, iconName: String, value: String) {
    val iconResId = when (iconName) {
        "house" -> R.drawable.house
        "croissant" -> R.drawable.croissant
        "address" -> R.drawable.map
        "phone" -> R.drawable.phone
        else -> android.R.drawable.ic_menu_close_clear_cancel
    }
    Card(
        modifier = Modifier
            .padding(start = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = contentDescription.ifEmpty { stringResource(R.string.no_content) },
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(35.dp).padding(end = 10.dp),
            )
            Row(modifier = Modifier.weight(1f)) {
                Text(
                    text = text.ifEmpty { stringResource(R.string.no_content) },
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Medium,
                    style = TextStyle(fontSize = 16.sp),
                )
                Text(
                    text = value,
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Normal,
                    style = TextStyle(fontSize = 16.sp),
                )
            }
        }
    }
}