package com.yourbusiness.printui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Printer

private val BlueBtn = Color(0xFF60A5FA)

data class OrderHistoryEntry(
    val shopName: String,
    val fileName: String,
    val pages: Int,
    val amount: Int,
    val time: String
)

object OrderHistoryStore {
    val orders = mutableListOf<OrderHistoryEntry>()
}

@Composable
fun OrderHistoryScreen(
    onBackPressed: () -> Unit,
    onReprint: (OrderHistoryEntry) -> Unit
) {

    val orders = OrderHistoryStore.orders

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFF))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }

                Text(
                    text = "Order History",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(10.dp))

            if (orders.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No orders yet.",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(orders) { order ->
                        OrderHistoryCard(order = order, onReprint = onReprint)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderHistoryCard(
    order: OrderHistoryEntry,
    onReprint: (OrderHistoryEntry) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Text(order.shopName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(order.fileName, fontSize = 14.sp, color = Color.DarkGray)
            Text("Pages: ${order.pages}", fontSize = 14.sp)
            Text(order.time, fontSize = 13.sp, color = Color.Gray)

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    "₹${order.amount}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF10B981)
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { onReprint(order) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueBtn)
                ) {
                    Icon(FeatherIcons.Printer, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text("Re-print", color = Color.White)
                }
            }
        }
    }
}
