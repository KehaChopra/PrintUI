package com.yourbusiness.printui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yourbusiness.printui.ui.theme.PrintUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrintUITheme {
                PrintAppNavigation()
            }
        }
    }
}

@Composable
fun PrintAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "select_shop"
    ) {
        composable("select_shop") {
            SelectShopScreen(
                onShopSelected = { shopName ->
                    navController.navigate("print_order/$shopName")
                }
            )
        }

        composable("print_order/{shopName}") { backStackEntry ->
            val shopName = backStackEntry.arguments?.getString("shopName") ?: ""
            PrintOrderScreen(
                shopName = shopName,
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
    }
}