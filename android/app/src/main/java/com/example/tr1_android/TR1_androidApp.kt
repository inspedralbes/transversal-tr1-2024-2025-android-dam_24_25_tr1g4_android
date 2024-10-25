package com.example.tr1_android

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tr1_android.ui.ShopScreen
import com.example.tr1_android.ui.StoreViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreAppBar(
    currentScreen: StoreScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    onTolleyClick: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(currentScreen.name) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = { // Add actions section
            IconButton(onClick = { onTolleyClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_shopping_cart_24), // Replace with your trolley icon
                    contentDescription = "Trolley"
                )
            }
        }
    )
}

@Composable
fun TR1_androidApp(
    viewModel: StoreViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = StoreScreen.valueOf(
        backStackEntry?.destination?.route ?: StoreScreen.Login.name
    )

    Scaffold(
        modifier = Modifier,
        topBar ={
            StoreAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                onTolleyClick = {
                    navController.navigate(StoreScreen.Payment.name)
                }
                )
        }
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
                    afegirACarro = {
                        viewModel.addItemToTrolley(it)
                    },
                    storeViewModel = viewModel
                )
            }
            composable(route = StoreScreen.Payment.name) {
                PaymentScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    viewModel = viewModel,
                    onPaymentSuccess = {
                        viewModel.postCompra()
                        viewModel.clearTrolley()
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

