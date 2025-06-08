package com.example.bottomnavigationdemo.pages

import android.R
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bottomnavigationdemo.mybd.MyApplication
import com.example.bottomnavigationdemo.mybd.ViewModelFactory
import com.example.bottomnavigationdemo.mybd.HistoryItem
import com.example.bottomnavigationdemo.mybd.HistoryViewModel
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HistoryPage() {
    val context = LocalContext.current
    val application = context.applicationContext as MyApplication
    val viewModel: HistoryViewModel = viewModel(
        factory = ViewModelFactory(application.historyRepository)
    )
    val history by viewModel.historyItems.collectAsState()

    Scaffold(
        modifier = Modifier
            .background(Color(0xFF16161E)),
        topBar = {
            TopAppBar(
                modifier = Modifier.background(Color(0xFF3B3B3D)),
                colors = TopAppBarColors(
                    titleContentColor = Color(0xFF3B3B3D),
                    containerColor = Color(0xFF3B3B3D),
                    actionIconContentColor = Color(0xFF3B3B3D),
                    scrolledContainerColor = Color(0xFF3B3B3D),
                    navigationIconContentColor = Color(0xFF3B3B3D)),
                title = { Text("История проверок",
                    color = Color(0xFFF2F2F2))},
                actions = {
                    IconButton(onClick = { viewModel.clearHistory() }) {
                        Icon(Icons.Default.Delete, "Очистить историю",
                            tint = Color(0xFFF2F2F2))
                    }
                }
            )
        }
    ) { padding ->
        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("История пуста", fontSize = 18.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .background(Color(0xFF16161E))
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(history) { item ->
                    HistoryItemCard(item = item)
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(item: HistoryItem) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .background(Color(0xFF3B3B3D))
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFF16161E))
                .padding(16.dp)
        ) {
            // Текст (первые 100 символов)
            Text(
                text = item.text.take(100) + if (item.text.length > 100) "..." else "",
                fontSize = 16.sp,
                color = Color(0xFFF2F2F2),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Процент уникальности
            Text(
                text = "Уникальность: ${item.percent}%",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFFF2F2F2),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Совпадения
            Text("Совпадения:", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))

            if (item.getMatches().isEmpty()) {
                Text(
                    text = "Совпадений не найдено",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFF2F2F2))
            } else {
                item.getMatches().forEachIndexed { index, match ->
                    Text(
                        text = "${index + 1}. ${match.url}",
                        color = Color(0xFF64B5F6),
                        modifier = Modifier
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, match.url.toUri())
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Ошибка обработается в showToast
                                }
                            }
                            .padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}