package com.yourbusiness.printui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock
import compose.icons.feathericons.Upload
import compose.icons.feathericons.FileText
import compose.icons.feathericons.File
import com.yourbusiness.printui.ui.theme.*

@Composable
fun PrintOrderScreen(
    shopName: String,
    onBackPressed: () -> Unit,
    onHistoryClick: () -> Unit = {}
) {

    var bwPages by remember { mutableStateOf("") }
    var colorPages by remember { mutableStateOf("") }
    var copies by remember { mutableStateOf(1) }

    var orientation by remember { mutableStateOf("Portrait") }
    var sides by remember { mutableStateOf("One Side") }

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf("") }

    var totalAmount by remember { mutableStateOf(0) }

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedFileUri = it
            // Extract file name from URI
            selectedFileName = it.lastPathSegment?.substringAfterLast("/") ?: "Document.pdf"
        }
    }

    // Calculate total whenever inputs change
    LaunchedEffect(bwPages, colorPages, copies) {
        val bw = bwPages.toIntOrNull() ?: 0
        val color = colorPages.toIntOrNull() ?: 0
        totalAmount = (bw * 2 + color * 10) * copies
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FAFB)) // Light gray background
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 80.dp),
        ) {

            // HEADER CARD - Back button and Shop Name
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onBackPressed) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color(0xFF1F2937)
                                )
                            }

                            Spacer(Modifier.width(4.dp))

                            Text(
                                text = shopName,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827)
                            )
                        }

                        // Small History Button
                        IconButton(
                            onClick = onHistoryClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                FeatherIcons.Clock,
                                contentDescription = "History",
                                tint = Color(0xFF6B7280),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // UPLOAD BUTTON - Large
                    Button(
                        onClick = { filePickerLauncher.launch("application/pdf") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BlueBtn),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Icon(
                            FeatherIcons.Upload,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "Upload Files",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Document Preview (if uploaded)
                    if (selectedFileUri != null) {
                        Spacer(Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = BlueBtn.copy(alpha = 0.1f)
                                    ) {
                                        Icon(
                                            FeatherIcons.File,
                                            contentDescription = null,
                                            tint = BlueBtn,
                                            modifier = Modifier
                                                .size(40.dp)
                                                .padding(8.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = selectedFileName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF111827),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "Ready to print",
                                            fontSize = 12.sp,
                                            color = Color(0xFF10B981),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        selectedFileUri = null
                                        selectedFileName = ""
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = Color(0xFF6B7280),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // PRINT TYPE CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            FeatherIcons.FileText,
                            contentDescription = null,
                            tint = Color(0xFF374151),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Print Type",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF111827)
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    // BLACK & WHITE
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Black & White Pages",
                            fontSize = 15.sp,
                            color = Color(0xFF111827),
                            fontWeight = FontWeight.SemiBold
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF3F4F6)
                        ) {
                            Text(
                                "₹2/page",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF374151),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    SimpleTextField(bwPages, "e.g., 1-5, 8, 10-12") { bwPages = it }

                    Spacer(Modifier.height(20.dp))

                    // COLORED
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Colored Pages",
                            fontSize = 15.sp,
                            color = Color(0xFF111827),
                            fontWeight = FontWeight.SemiBold
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF3F4F6)
                        ) {
                            Text(
                                "₹10/page",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF374151),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    SimpleTextField(colorPages, "e.g., 1-5, 8, 10-12") { colorPages = it }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ORIENTATION CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Orientation",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF111827)
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        SelectButton(
                            text = "Portrait",
                            selected = orientation == "Portrait",
                            modifier = Modifier.weight(1f)
                        ) { orientation = "Portrait" }
                        SelectButton(
                            text = "Landscape",
                            selected = orientation == "Landscape",
                            modifier = Modifier.weight(1f)
                        ) { orientation = "Landscape" }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // PRINT SIDES CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Print Sides",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF111827)
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        SelectButton(
                            text = "One Side",
                            selected = sides == "One Side",
                            modifier = Modifier.weight(1f)
                        ) { sides = "One Side" }
                        SelectButton(
                            text = "Double Side",
                            selected = sides == "Double Side",
                            modifier = Modifier.weight(1f)
                        ) { sides = "Double Side" }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // NUMBER OF COPIES CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Number of Copies",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF111827)
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Minus Button
                        Button(
                            onClick = { if (copies > 1) copies-- },
                            modifier = Modifier.size(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BlueBtn),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("-", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        // Copies Display
                        OutlinedTextField(
                            value = copies.toString(),
                            onValueChange = {
                                val newValue = it.toIntOrNull()
                                if (newValue != null && newValue > 0) copies = newValue
                            },
                            modifier = Modifier.weight(1f),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color(0xFF111827),
                                unfocusedTextColor = Color(0xFF111827),
                                cursorColor = BlueBtn,
                                focusedBorderColor = BlueBtn,
                                unfocusedBorderColor = Color(0xFFD1D5DB),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )

                        // Plus Button
                        Button(
                            onClick = { copies++ },
                            modifier = Modifier.size(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BlueBtn),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }

        // BOTTOM PAY NOW BUTTON - Fixed at bottom with total
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xFFF9FAFB))
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueBtn),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
            ) {
                Text(
                    if (totalAmount > 0) "Pay Now - ₹$totalAmount" else "Pay Now",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }
    }
}

/* ---------- REUSABLE COMPOSABLES ---------- */

@Composable
fun SimpleTextField(value: String, placeholder: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color(0xFF9CA3AF), fontSize = 14.sp) },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color(0xFF111827),
            unfocusedTextColor = Color(0xFF111827),
            cursorColor = BlueBtn,
            focusedPlaceholderColor = Color(0xFF9CA3AF),
            unfocusedPlaceholderColor = Color(0xFF9CA3AF),
            focusedBorderColor = BlueBtn,
            unfocusedBorderColor = Color(0xFFD1D5DB),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun SelectButton(text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(12.dp),
        border = if (!selected) BorderStroke(1.dp, Color(0xFFE5E7EB)) else null,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) BlueBtn else Color.White,
            contentColor = if (selected) Color.White else Color(0xFF374151)
        ),
        elevation = if (selected) ButtonDefaults.buttonElevation(defaultElevation = 1.dp) else null
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}