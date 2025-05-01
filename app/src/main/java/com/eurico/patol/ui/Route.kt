package com.eurico.patol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.eurico.patol.R
import com.eurico.patol.ui.screen.ConfigurationScreen
import com.eurico.patol.ui.screen.LoadingScreen
import com.eurico.patol.ui.screen.MaterialListScreen
import com.eurico.patol.ui.screen.MaterialScreen

enum class RouterSet(val value: Int) {
    LOADING_SCREEN(0),
    LIST_SCREEN(R.string.list_screen),
    MATERIAL_SCREEN(R.string.material),
    CONFIGURATION(R.string.configuration)
}

@Composable
fun Router() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.tertiary),
        topBar = {
            if (currentRoute != RouterSet.LOADING_SCREEN.name) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(
                            RoundedCornerShape(
                                bottomEnd = 16.dp,
                                bottomStart = 16.dp
                            )
                        )
                        .background(MaterialTheme.colorScheme.background),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { navController.popBackStack() }
                        )
                        Text(
                            text = stringResource(R.string.configuration),
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(RouterSet.CONFIGURATION.name)
                                }
                        )
                    }
                }
            }
        },
        content = { padding ->
            NavHostContainer(
                navController = navController,
                padding = padding
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    )
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = RouterSet.LOADING_SCREEN.name,
        modifier = Modifier
            .padding(paddingValues = padding),
        builder = {
            composable(RouterSet.LOADING_SCREEN.name) {
                LoadingScreen(navController = navController)
            }
            composable(RouterSet.LIST_SCREEN.name) {
                MaterialListScreen(navController = navController)
            }
            composable(RouterSet.MATERIAL_SCREEN.name + "/{materialId}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("materialId")?.toLongOrNull()
                MaterialScreen(id)
            }
            composable(RouterSet.CONFIGURATION.name) {
                ConfigurationScreen()
            }
        }
    )
}