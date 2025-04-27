package com.eurico.patol.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.eurico.patol.R
import com.eurico.patol.ui.RouterSet
import com.eurico.patol.ui.screen.components.RotatingIcon
import com.eurico.patol.viewmodel.LoadingViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun LoadingScreen(
    viewModel: LoadingViewModel = koinViewModel(),
    navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val loginPhrase = viewModel.loginPhrase.collectAsState()


        when(loginPhrase.value.value) {
            0 -> RotatingIcon()
            else -> {
                Text(
                    text = stringResource(loginPhrase.value.value)
                )
                LaunchedEffect(Unit) {
                    if (loginPhrase.value.value == R.string.sucess_login) {
                        navController.navigate(RouterSet.LIST_SCREEN.name) {
                            popUpTo(RouterSet.LOADING_SCREEN.name) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }
    }
}