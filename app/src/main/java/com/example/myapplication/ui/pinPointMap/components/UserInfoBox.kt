package com.example.myapplication.ui.pinPointMap.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.entity.UserLocation
import com.skydoves.landscapist.glide.GlideImage


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserInfoBox(
    modifier: Modifier = Modifier,
    userLocation: UserLocation
) {
    Box(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GlideImage(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(80.dp)
                        .background(Color.Blue)
                        .padding(2.dp)
                        .clip(CircleShape),
                    imageModel = { userLocation.avatarUrl }
                )

                Column {
                    Text(
                        text = userLocation.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextWithIcon(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = userLocation.signalType,
                            icon = Icons.Outlined.LocationOn
                        )

                        TextWithIcon(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = userLocation.birthdate,
                            icon = Icons.Outlined.DateRange
                        )

                        TextWithIcon(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = userLocation.lastActive,
                            icon = Icons.Outlined.Refresh
                        )
                    }
                }
            }

            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.5f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White
                ),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = "Посмотреть историю",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun TextWithIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = icon,
            contentDescription = "user_date_icon",
            tint = Color.Blue
        )

        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        )
    }
}