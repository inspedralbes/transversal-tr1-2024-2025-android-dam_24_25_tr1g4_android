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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tr1_android.ui.ShopScreen
import com.example.tr1_android.ui.StoreViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tr1_android.ui.LoginScreen
import com.example.tr1_android.ui.OrderScreen
import com.example.tr1_android.ui.PaymentScreen
import com.example.tr1_android.ui.theme.TR1_androidTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tr1_android.data.UserUiState
import com.example.tr1_android.ui.ProfileScreen
import com.example.tr1_android.ui.RegisterScreen

enum class StoreScreen {
    Login,
    Register,
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
    onTolleyClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
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
            if (currentScreen.name != StoreScreen.Login.name && currentScreen.name != StoreScreen.Register.name) {
                IconButton(onClick = { onProfileClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_person_24), // Replace with your profile icon
                        contentDescription = "Profile"
                    )
                }
                IconButton(onClick = { onTolleyClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_shopping_cart_24), // Replace with your trolley icon
                        contentDescription = "Trolley"
                    )
                }
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
    // Get the current UI state
    val uiState by viewModel.userUiState.collectAsState()

    Scaffold(
        modifier = Modifier,
        topBar ={
            StoreAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                onTolleyClick = {
                    navController.navigate(StoreScreen.Payment.name)
                },
                onProfileClick = {
                    navController.navigate(StoreScreen.Profile.name)
                    viewModel.getComandes()
                }
                )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = StoreScreen.Login.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = StoreScreen.Login.name) {
                LoginScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    onSendLogin = {
                        viewModel.login(it, navController)
                    },
                    onGoToRegister = {
                        navController.navigate(StoreScreen.Register.name)
                    },
                    storeViewModel = viewModel
                )
            }
            composable(route = StoreScreen.Register.name) {
                RegisterScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    onCreateAccount = {
                        viewModel.register(it, navController)
                    },
                    onGoToLogin = {
                        navController.navigate(StoreScreen.Login.name)
                    },
                    storeViewModel = viewModel
                )
            }

            composable(route = StoreScreen.Shop.name) {
                ShopScreen(
                    modifier = Modifier,
                    afegirACarro = {
                        viewModel.addItemToTrolley(it)
                    },
                    treureDelCarro = {
                        viewModel.removeItemFromTrolley(it)
                    },
                    storeViewModel = viewModel
                )
            }
            composable(route = StoreScreen.Profile.name) {
                ProfileScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    viewModel = viewModel
                )
            }
            composable(route = StoreScreen.Payment.name) {
                PaymentScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    viewModel = viewModel,
                    onPaymentSuccess = {
                        var anyBought = false
                        viewModel.uiState.value.trolley.forEach { trolleyItem ->
                            if (trolleyItem.quantity > 0) {
                                anyBought = true
                            }
                        }
                        if (!anyBought) {
                            navController.navigate(StoreScreen.Shop.name)
                        } else {
                            viewModel.postCompra()
                            viewModel.clearTrolley()
                            navController.navigate(StoreScreen.Order.name)
                        }

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



@Preview
@Composable
fun TR1_androidAppPreview() {
    TR1_androidTheme {
        TR1_androidApp()
    }
}