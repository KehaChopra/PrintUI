package com.yourbusiness.printui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock
import compose.icons.feathericons.MapPin
import compose.icons.feathericons.Printer

/* ---------- COLORS ---------- */
private val ScreenTop = Color(0xFFF8FAFF)
private val ScreenBottom = Color(0xFFF1F5FB)

private val CardBg = Color.White
private val BlueBtn = Color(0xFF60A5FA)
private val DisabledGray = Color(0xFFF1F5F9)

private val Green = Color(0xFF22C55E)
private val GreenBg = Color(0xFFE9F8EF)

private val Red = Color(0xFFEF4444)
private val RedBg = Color(0xFFFDECEC)

private val YellowClock = Color(0xFFFACC15)
private val MapBg = Color(0xFFEFF6FF)
private val DistanceDark = Color(0xFF1E40AF)

/* ---------- SCREEN ---------- */
@Composable
fun SelectShopScreen(onShopSelected: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient( // smooth blending of color
                    colors = listOf(ScreenTop, ScreenBottom)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Quick Print",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Select your nearest shop",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4B5563)
            )

            Spacer(Modifier.height(20.dp))

            /* ---------- MAP (ONLY ONCE) ---------- */
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MapBg),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = FeatherIcons.MapPin,
                        contentDescription = null,
                        tint = BlueBtn,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "View shops on map",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            ShopCard(
                shopName = "GBlock Stationary Shop",
                distance = "1.2 km",
                isOpen = true,
                waitTime = "5–10 mins",
                bwAvailable = true,
                colorAvailable = true,
                onShopSelected = onShopSelected
            )

            Spacer(Modifier.height(20.dp))

            ShopCard(
                shopName = "COS Stationary Shop",
                distance = "2.8 km",
                isOpen = false,
                waitTime = "5–10 mins",
                bwAvailable = true,
                colorAvailable = false,
                onShopSelected = onShopSelected
            )

            Spacer(Modifier.height(30.dp))
        }
    }
}

/* ---------- SHOP CARD ---------- */
@Composable
fun ShopCard(
    shopName: String,
    distance: String,
    isOpen: Boolean,
    waitTime: String,
    bwAvailable: Boolean,
    colorAvailable: Boolean,
    onShopSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            /* ---------- HEADER ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = shopName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = distance,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DistanceDark
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = if (isOpen) Green else Red,
                            shape = CircleShape
                        )
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    text = if (isOpen) "Open" else "Closed",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isOpen) Green else Red
                )
            }

            // Only show services section if shop is open
            if (isOpen) {
                Spacer(Modifier.height(18.dp))

                /* ---------- SERVICES ---------- */
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    ServiceChip("B&W Printing", bwAvailable)
                    ServiceChip("Color Printing", colorAvailable)
                }

                Spacer(Modifier.height(18.dp))

                Divider(color = Color(0xFFE5E7EB))

                Spacer(Modifier.height(14.dp))
            } else {
                Spacer(Modifier.height(18.dp))
            }

            /* ---------- FOOTER ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (isOpen) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = FeatherIcons.Clock,
                            contentDescription = null,
                            tint = YellowClock,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Wait: $waitTime",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { onShopSelected(shopName) },
                    enabled = isOpen,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BlueBtn,
                        disabledContainerColor = DisabledGray,
                        disabledContentColor = Color.Gray
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 24.dp,
                        vertical = 12.dp
                    )
                ) {
                    Text(
                        text = "Select Shop",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

/* ---------- SERVICE CHIP ---------- */
@Composable
fun ServiceChip(
    title: String,
    available: Boolean
) {
    Row(
        modifier = Modifier
            .background(
                color = if (available) GreenBg else RedBg,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = FeatherIcons.Printer,
            contentDescription = null,
            tint = if (available) Green else Red,
            modifier = Modifier.size(20.dp)
        )

        Spacer(Modifier.width(10.dp))

        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = if (available) "Available" else "Not Available",
                fontSize = 13.sp,
                color = if (available) Green else Red
            )
        }
    }
}