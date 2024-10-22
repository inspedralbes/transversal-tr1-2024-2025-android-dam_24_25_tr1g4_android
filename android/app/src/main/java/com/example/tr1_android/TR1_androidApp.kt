package com.example.tr1_android

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tr1_android.ui.ShopScreen
import com.example.tr1_android.ui.theme.TR1_androidTheme

enum class StoreScreen {
    Login,
    Shop,
    Payment,
    Profile,
    Order
}

@Composable
fun TR1_androidApp(
//    viewModel: GameViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        modifier = Modifier
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = StoreScreen.Shop.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = StoreScreen.Shop.name) {
                ShopScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                )
            }
        }
    }
}

