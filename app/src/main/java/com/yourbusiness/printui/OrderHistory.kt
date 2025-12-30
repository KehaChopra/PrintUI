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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourbusiness.printui.ui.theme.BlueBtn

data class OrderHistoryItem(
    val fileName: String,
    val shopName: String,
    val date: String,
    val amount: String
)

@Composable
fun OrderHistoryScreen(
    onBack: () -> Unit = {},
    onReprint: (OrderHistoryItem) -> Unit = {}
) {

    val history = remember {
        listOf(
            OrderHistoryItem("Assignment1.pdf", "G Block Stationary", "30 Dec 2025", "₹40"),
            OrderHistoryItem("Notes_unit2.pdf", "COS", "29 Dec 2025", "₹65"),
            OrderHistoryItem("Project_Report.pdf", "G Block Stationary", "28 Dec 2025", "₹120"),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF111827))
            }

            Text(
                "Order History",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            items(history) { item ->

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(item.fileName, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                        Spacer(Modifier.height(6.dp))

                        Text("Shop: ${item.shopName}", fontSize = 13.sp, color = Color(0xFF6B7280))
                        Text("Date: ${item.date}", fontSize = 13.sp, color = Color(0xFF6B7280))

                        Spacer(Modifier.height(10.dp))

                        Text(
                            "Amount Paid: ${item.amount}",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = Color(0xFF111827)
                        )

                        Spacer(Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { onReprint(item) },
                                colors = ButtonDefaults.buttonColors(containerColor = BlueBtn),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Re-Print", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}


