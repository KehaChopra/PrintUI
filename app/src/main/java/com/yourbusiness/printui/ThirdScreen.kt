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
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.text.style.TextAlign
import compose.icons.FeatherIcons
import compose.icons.feathericons.Clock
import compose.icons.feathericons.Upload
import compose.icons.feathericons.FileText
import compose.icons.feathericons.File
import com.yourbusiness.printui.ui.theme.*

// Data class to hold document settings
data class DocumentSettings(
    val uri: Uri,
    val fileName: String,
    var bwPages: String = "",
    var colorPages: String = "",
    var copies: Int = 1,
    var orientation: String = "Portrait",
    var sides: String = "One Side"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintOrderScreen(
    shopName: String,
    onBackPressed: () -> Unit,
    onPayNowClick: (orderId: String, eta: String, amount: String) -> Unit,
    onHistoryClick: () -> Unit = {}
) {
    var documents by remember { mutableStateOf<List<DocumentSettings>>(emptyList()) }
    val pagerState = rememberPagerState(pageCount = { documents.size })

    // File picker launcher - supports multiple files
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            val newDocs = uris.map { uri ->
                DocumentSettings(
                    uri = uri,
                    fileName = uri.lastPathSegment?.substringAfterLast("/") ?: "Document.pdf"
                )
            }
            documents = documents + newDocs
        }
    }

    // Calculate total amount across all documents
    val totalAmount = remember(documents) {
        documents.sumOf { doc ->
            val bw = doc.bwPages.toIntOrNull() ?: 0
            val color = doc.colorPages.toIntOrNull() ?: 0
            (bw * 2 + color * 10) * doc.copies
        }
    }

    // SHOW UPLOAD SCREEN if no documents
    if (documents.isEmpty()) {
        UploadScreen(
            shopName = shopName,
            onBackPressed = onBackPressed,
            onHistoryClick = onHistoryClick,
            onUploadClick = { filePickerLauncher.launch("application/pdf") }
        )
    } else {
        // SHOW DOCUMENTS WITH SETTINGS
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF9FAFB))
                    .statusBarsPadding()
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

                        // ADD MORE FILES BUTTON
                        OutlinedButton(
                            onClick = { filePickerLauncher.launch("application/pdf") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(2.dp, BlueBtn),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = BlueBtn
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = BlueBtn,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Add More Files",
                                color = BlueBtn,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))

                // Page indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "File ${pagerState.currentPage + 1} of ${documents.size}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6B7280)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Horizontal Pager for documents with scroll
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        DocumentCard(
                            document = documents[page],
                            onRemove = {
                                documents = documents.filterIndexed { index, _ -> index != page }
                            },
                            onUpdate = { updated ->
                                documents = documents.toMutableList().apply {
                                    set(page, updated)
                                }
                            }
                        )
                    }
                }
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
                    onClick = {
                        onPayNowClick("id_34598", "15 minutes", totalAmount.toString())
                    },
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
}

// FULL SCREEN UPLOAD VIEW
@Composable
fun UploadScreen(
    shopName: String,
    onBackPressed: () -> Unit,
    onHistoryClick: () -> Unit,
    onUploadClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FAFB))
                .statusBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // HEADER
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
            }

            // CENTER CONTENT - Upload illustration and text
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Upload Icon - Clickable
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = BlueBtn.copy(alpha = 0.1f),
                    modifier = Modifier
                        .size(120.dp),
                    onClick = onUploadClick
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            FeatherIcons.Upload,
                            contentDescription = null,
                            tint = BlueBtn,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "Upload Your Documents",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Select one or multiple PDF files\nto start printing",
                    fontSize = 15.sp,
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Center
                )
            }

            // BOTTOM BUTTON
            Button(
                onClick = onUploadClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueBtn),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
            ) {
                Icon(
                    FeatherIcons.Upload,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "Choose Files",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DocumentCard(
    document: DocumentSettings,
    onRemove: () -> Unit,
    onUpdate: (DocumentSettings) -> Unit
) {
    Column {
        // Document Preview Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
                border = BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
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
                                    .size(36.dp)
                                    .padding(8.dp)
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = document.fileName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF111827),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Ready to print",
                                fontSize = 11.sp,
                                color = Color(0xFF10B981),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = Color(0xFF6B7280),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // PRINT TYPE CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        FeatherIcons.FileText,
                        contentDescription = null,
                        tint = Color(0xFF374151),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Print Type",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF111827)
                    )
                }

                Spacer(Modifier.height(14.dp))

                // BLACK & WHITE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Black & White Pages",
                        fontSize = 13.sp,
                        color = Color(0xFF111827),
                        fontWeight = FontWeight.SemiBold
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF3F4F6)
                    ) {
                        Text(
                            "₹2/page",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = document.bwPages,
                    onValueChange = { onUpdate(document.copy(bwPages = it)) },
                    placeholder = {
                        Text(
                            "e.g., 1-5, 8, 10-12",
                            color = Color(0xFF9CA3AF),
                            fontSize = 13.sp
                        )
                    },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
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
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                )

                Spacer(Modifier.height(14.dp))

                // COLORED
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Colored Pages",
                        fontSize = 13.sp,
                        color = Color(0xFF111827),
                        fontWeight = FontWeight.SemiBold
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF3F4F6)
                    ) {
                        Text(
                            "₹10/page",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = document.colorPages,
                    onValueChange = { onUpdate(document.copy(colorPages = it)) },
                    placeholder = {
                        Text(
                            "e.g., 1-5, 8, 10-12",
                            color = Color(0xFF9CA3AF),
                            fontSize = 13.sp
                        )
                    },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
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
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // COMBINED SETTINGS CARD (Orientation + Sides + Copies)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // ORIENTATION
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { onUpdate(document.copy(orientation = "Portrait")) },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = if (document.orientation != "Portrait") BorderStroke(1.dp, Color(0xFFE5E7EB)) else null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (document.orientation == "Portrait") BlueBtn else Color.White,
                            contentColor = if (document.orientation == "Portrait") Color.White else Color(0xFF374151)
                        ),
                        elevation = if (document.orientation == "Portrait") ButtonDefaults.buttonElevation(defaultElevation = 1.dp) else null
                    ) {
                        Text("Portrait", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                    Button(
                        onClick = { onUpdate(document.copy(orientation = "Landscape")) },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = if (document.orientation != "Landscape") BorderStroke(1.dp, Color(0xFFE5E7EB)) else null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (document.orientation == "Landscape") BlueBtn else Color.White,
                            contentColor = if (document.orientation == "Landscape") Color.White else Color(0xFF374151)
                        ),
                        elevation = if (document.orientation == "Landscape") ButtonDefaults.buttonElevation(defaultElevation = 1.dp) else null
                    ) {
                        Text("Landscape", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }

                Spacer(Modifier.height(10.dp))

                // PRINT SIDES
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { onUpdate(document.copy(sides = "One Side")) },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = if (document.sides != "One Side") BorderStroke(1.dp, Color(0xFFE5E7EB)) else null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (document.sides == "One Side") BlueBtn else Color.White,
                            contentColor = if (document.sides == "One Side") Color.White else Color(0xFF374151)
                        ),
                        elevation = if (document.sides == "One Side") ButtonDefaults.buttonElevation(defaultElevation = 1.dp) else null
                    ) {
                        Text("One Side", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                    Button(
                        onClick = { onUpdate(document.copy(sides = "Double Side")) },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = if (document.sides != "Double Side") BorderStroke(1.dp, Color(0xFFE5E7EB)) else null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (document.sides == "Double Side") BlueBtn else Color.White,
                            contentColor = if (document.sides == "Double Side") Color.White else Color(0xFF374151)
                        ),
                        elevation = if (document.sides == "Double Side") ButtonDefaults.buttonElevation(defaultElevation = 1.dp) else null
                    ) {
                        Text("Double Side", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }

                Spacer(Modifier.height(10.dp))

                // NUMBER OF COPIES
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Minus Button
                    Button(
                        onClick = {
                            if (document.copies > 1) {
                                onUpdate(document.copy(copies = document.copies - 1))
                            }
                        },
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BlueBtn),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "-",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Copies Display
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(53.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .then(
                                Modifier.padding(1.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        OutlinedTextField(
                            value = document.copies.toString(),
                            onValueChange = {
                                val newValue = it.toIntOrNull()
                                if (newValue != null && newValue > 0) {
                                    onUpdate(document.copy(copies = newValue))
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF111827)
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(8.dp),
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
                    }

                    // Plus Button
                    Button(
                        onClick = {
                            onUpdate(document.copy(copies = document.copies + 1))
                        },
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BlueBtn),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "+",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}