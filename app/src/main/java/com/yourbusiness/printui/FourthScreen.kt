package com.yourbusiness.printui

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourbusiness.printui.ui.theme.*

@Composable
fun PaymentSuccessScreen(
    orderId: String,
    etaText: String,
    amount: String,
    onBackToHome: () -> Unit = {}
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(ScreenTop, ScreenBottom)
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            /* ---------- Tick Icon ---------- */
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Green),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(Modifier.height(22.dp))

            /* ---------- Title ---------- */
            Text(
                text = "Payment Successful",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Your order has been placed successfully",
                fontSize = 15.sp,
                color = TextGray
            )

            Spacer(Modifier.height(22.dp))

            /* ---------- Card ---------- */
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {

                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "📦 Order ID",
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )

                    Text(
                        text = orderId,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DistanceDark
                    )

                    Spacer(Modifier.height(14.dp))

                    Divider()

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = "💰 Amount Paid",
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )

                    Text(
                        text = "₹$amount",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DistanceDark
                    )

                    Spacer(Modifier.height(14.dp))

                    Divider()

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = "⏳ Estimated Ready Time",
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )

                    Text(
                        text = etaText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Green
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = onBackToHome,
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlueBtn
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text("Back to Home", color = Color.White, fontSize = 15.sp)
            }
        }
    }
}