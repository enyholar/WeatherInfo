package com.gideondev.weatherapp.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherElementIcon(
    iconRes: Int,
    iconTitle: Int,
    iconDescription: String,
    modifier: Modifier = Modifier,
    iconDegrees: Int = 0,
    tintColor: Color = MaterialTheme.colors.onSurface
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .background(
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconRes),
                tint = tintColor,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .rotate(iconDegrees.toFloat())
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = stringResource(id = iconTitle),
                    color = tintColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Text(
                    text = iconDescription,
                    color = tintColor,
                    fontSize = 12.sp
                )
            }
        }
    }
}
