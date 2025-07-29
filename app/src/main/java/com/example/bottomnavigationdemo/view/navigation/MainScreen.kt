package com.example.bottomnavigationdemo.view.navigation

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bottomnavigationdemo.R
import com.example.bottomnavigationdemo.view.pages.HistoryPage
import com.example.bottomnavigationdemo.view.pages.HomePage


@Composable
fun MainScreen(mainScreenViewModel: MainScreenViewModel = viewModel()) {

    val navItemList = listOf(
        NavItem(R.drawable.home_icon),
        NavItem(R.drawable.history_icon),
    )

    /*
    var selectedIndex by remember {
        mutableIntStateOf(0)
    } */
    val selectedIndex = mainScreenViewModel.selectedIndex

    Scaffold(
        modifier = Modifier.Companion.fillMaxSize(),
        bottomBar = {
            Box(
                modifier = Modifier.Companion
                    .padding(start = 12.dp, end = 12.dp, bottom = 30.dp, top = 8.dp)
                    .wrapContentWidth(unbounded = false)
                    .consumeWindowInsets(
                        WindowInsets(
                            left = 12.dp,
                            right = 12.dp,
                            bottom = 12.dp,
                            top = 70.dp
                        )
                    )
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .height(35.dp)
                ) {
                    NavigationBar(
                        modifier = Modifier.Companion,
                        containerColor = Color(0xFF3B3B3F)
                    ) {
                        navItemList.forEachIndexed { index, navItem ->
                            val isSelected = selectedIndex == index
                            NavigationBarItem(
                                selected = selectedIndex == index,
                                onClick = {
                                    mainScreenViewModel.updateSelectedIndex(index)
                                },
                                icon = {
                                    Box(
                                        modifier = Modifier.Companion.fillMaxHeight(),
                                        contentAlignment = Alignment.Companion.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(id = navItem.icon),
                                            contentDescription = "Main",
                                            tint = if (isSelected) {
                                                Color(0xFF76C781)
                                            } else {
                                                Color.Companion.White
                                            }
                                        )
                                    }
                                },
                                alwaysShowLabel = false,
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.Companion.Transparent
                                )
                            )
                        }
                    }
                }
            }

        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.Companion
                .padding(innerPadding), selectedIndex
        )
    }
}


@Composable
fun ContentScreen(modifier: Modifier = Modifier.Companion, selectedIndex: Int) {
    when(selectedIndex) {
        0-> HomePage()
        1-> HistoryPage()
    }
}

class MainScreenViewModel() : ViewModel() {

    var selectedIndex by mutableIntStateOf(0)
        private set // Защищаем от внешнего изменения

    fun updateSelectedIndex(index: Int) {
        selectedIndex = index
    }
}