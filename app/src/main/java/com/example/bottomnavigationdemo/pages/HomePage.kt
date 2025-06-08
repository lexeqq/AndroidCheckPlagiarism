package com.example.bottomnavigationdemo.pages

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import com.example.bottomnavigationdemo.R
import com.example.bottomnavigationdemo.apifiles.PlagiarismResponse
import com.example.bottomnavigationdemo.apifiles.RetrofitClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.bottomnavigationdemo.Constants
import com.example.bottomnavigationdemo.mybd.*

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomePage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val application = context.applicationContext as MyApplication // Объявлено первым
    val viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(
            (context.applicationContext as MyApplication).historyRepository
        )
    )

    // Текст из поля ввода
    var textInput by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val job = remember { Job() }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val result by viewModel.result.collectAsState()

    LaunchedEffect(error) {
        error?.let {
            showToast(context, "Ошибка: $it", Toast.LENGTH_LONG)
            viewModel.clearError()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            job.cancel()
        }
    }

    Scaffold(modifier = Modifier) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF16161E))
                .padding(
                    top = 20.dp,
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 140.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Проверка на плагиат",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )

            OutlinedTextField(
                value = textInput,
                onValueChange = { newText -> textInput = newText },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        Color(0xFF3B3B3D),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        1.dp,
                        Color.Gray,
                        shape = RoundedCornerShape(20.dp)
                    ),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                placeholder = {
                    if (textInput.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Вставьте текст или введите вручную",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    Text(
                        text = "Уникальность: ${result?.percent?.toString() ?: ""}%",
                        color = Color(0xFFF2F2F2)
                    )

                    // Измененный блок для отображения совпадений
                    if (result != null) {
                        if (result!!.matches.isNotEmpty()) {
                            Text(
                                text = "Совпадения:",
                                color = Color(0xFFF2F2F2)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 150.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                result!!.matches.forEachIndexed { index, match ->
                                    Text(
                                        text = "${index + 1}. ${match.url}",
                                        color = Color(0xFF64B5F6),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 8.dp, top = 4.dp)
                                            .clickable {
                                                try {
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(match.url))
                                                    context.startActivity(intent)
                                                } catch (e: Exception) {
                                                    showToast(context, "Не удалось открыть ссылку", Toast.LENGTH_SHORT)
                                                }
                                            }
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = "Совпадения: нет",
                                color = Color(0xFFF2F2F2)
                            )
                        }
                    } else {
                        Text(
                            text = "Совпадения: ",
                            color = Color(0xFFF2F2F2)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(45.dp))

            Button(
                onClick = {
                    if (textInput.isBlank()) {
                        showToast(context, "Введите текст для проверки", Toast.LENGTH_SHORT)
                        return@Button
                    }
                    viewModel.checkPlagiarism(textInput)
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF76C781)),
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Проверить",
                        color = Color(0xFFF2F2F2),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

class HomeViewModel (private val historyRepository: HistoryRepository) : ViewModel() {
    // Состояния UI:
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _result = MutableStateFlow<PlagiarismResponse?>(null)
    val result: StateFlow<PlagiarismResponse?> = _result

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    fun checkPlagiarism(text: String) {
        viewModelScope.launch { // Асинхронная операция
            _isLoading.value = true
            _result.value = null // Сбрасываем предыдущий результат
            _error.value = null
            try {
                val response = RetrofitClient.instance.checkPlagiarism(
                    apiKey = Constants.API_KEY,
                    text = text
                )
                _result.value = response

                // Сохранение в БД
                historyRepository.insert(HistoryItem.fromResponse(text, response))
            } catch (e: Exception) {
                _error.value = e.message ?: "Неизвестная ошибка"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() { _error.value = null }
    fun clearResult() { _result.value = null }
}

fun isContextValid(context: Context): Boolean {
    return (context as? Activity)?.isDestroyed == false
}

fun showToast(context: Context, message: String, duration: Int) {
    if (isContextValid(context)) {
        Toast.makeText(context, message, duration).show()
    }
}