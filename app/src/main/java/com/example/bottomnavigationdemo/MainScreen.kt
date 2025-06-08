package com.example.bottomnavigationdemo

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bottomnavigationdemo.pages.AccountPage
import com.example.bottomnavigationdemo.pages.HomePage
import com.example.bottomnavigationdemo.pages.HistoryPage
import com.example.bottomnavigationdemo.pages.SettingsPage

@Preview(showBackground = true)
@Composable
fun MainScreen() {

    val navItemList = listOf(
        NavItem(R.drawable.home_icon),
        NavItem(R.drawable.history_icon),
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, bottom = 30.dp, top = 8.dp)
                    .wrapContentWidth(unbounded = false)
                    .consumeWindowInsets(WindowInsets(
                        left = 12.dp,
                        right = 12.dp,
                        bottom = 12.dp,
                        top = 100.dp ))) {
                Surface (shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                ){
                    NavigationBar (modifier = Modifier,
                        containerColor = Color(0xFF3B3B3F)
                    ){
                        navItemList.forEachIndexed { index, navItem ->
                            val isSelected = selectedIndex == index
                            NavigationBarItem(
                                selected = selectedIndex == index,
                                onClick = {
                                    selectedIndex = index
                                },
                                icon = {
                                    Box(modifier = Modifier.fillMaxHeight(),
                                        contentAlignment = Alignment.BottomCenter){
                                    Icon(painter = painterResource(id = navItem.icon),
                                        contentDescription = "Main",
                                        tint = if (isSelected) {
                                            Color(0xFF76C781)
                                        } else {
                                            Color.White
                                        })
                                    }
                                },
                                alwaysShowLabel = false,
                                interactionSource = remember {
                                    MutableInteractionSource() },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.Transparent)
                            )
                        }
                    }
                }
            }

        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier
            .padding(innerPadding), selectedIndex)
    }
}


@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
    when(selectedIndex) {
        0-> HomePage()
        1-> HistoryPage()
    }
}