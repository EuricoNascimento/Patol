package com.eurico.patol.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.eurico.patol.R
import com.eurico.patol.ui.screen.LoadingScreen
import com.eurico.patol.ui.screen.MaterialListScreen
import com.eurico.patol.ui.screen.MaterialScreen

enum class RouterSet{
    LOADING_SCREEN,
    LIST_SCREEN,
    MATERIAL_SCREEN
}

@Composable
fun Router() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.tertiary),
        topBar = {
            var currentRoute = navBackStackEntry?.destination?.route ?: ""
            if (currentRoute == RouterSet.MATERIAL_SCREEN.name) {
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
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = stringResource(R.string.back),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
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
        }
    )
}