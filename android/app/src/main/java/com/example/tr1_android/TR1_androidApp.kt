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
import com.example.tr1_android.ui.StoreViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tr1_android.ui.OrderScreen
import com.example.tr1_android.ui.PaymentScreen
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
    viewModel: StoreViewModel = viewModel(),
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
                        .padding(innerPadding),
                    comprarItem = {
                        viewModel.addItemToTrolley(it)
                        navController.navigate(StoreScreen.Payment.name)
                    },
                    viewModel = viewModel
                )
            }
            composable(route = StoreScreen.Payment.name) {
                PaymentScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    viewModel = viewModel,
                    onPaymentSuccess = {
                        navController.navigate(StoreScreen.Order.name)
                    }
                )
            }
            composable(route = StoreScreen.Order.name) {
                OrderScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    viewModel = viewModel,
                )
            }
        }
    }
}

