package com.example.tr1_android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tr1_android.R
import com.example.tr1_android.data.LoginRequest
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onSendLogin: (LoginRequest) -> Unit = {},
    storeViewModel: StoreViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val storeUiState by storeViewModel.uiState.collectAsState()

    /* TODO */ // No funciona el dialogo de error
    if (storeUiState.showDialog) {
        WrongLoginDialog(modifier, storeViewModel)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo
        Image(
            painter = painterResource(id = R.drawable.app_logo), // Replace with your logo
            contentDescription = "App Logo",
            modifier = Modifier.size(300.dp) // Adjust size as needed
        )
        Spacer(Modifier.height(32.dp))

        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(), // Hide password
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(32.dp))

        // Login Button
        Button(onClick = { onSendLogin(LoginRequest(email, password)) }) {
            Text("Login")
        }
    }

}

@Composable
private fun WrongLoginDialog(
    modifier: Modifier = Modifier,
    storeViewModel: StoreViewModel = viewModel()
) {
        AlertDialog(
            onDismissRequest = {
                storeViewModel.setShowDialog(value = false)
            },
            title = { stringResource(R.string.usuari_o_contrasenya_incorrectes) },
            modifier = modifier,
            confirmButton = {
                TextButton(onClick = { storeViewModel.setShowDialog(value = false) }) {
                    Text(text = stringResource(R.string.tancar))
                }
            }
        )

}
