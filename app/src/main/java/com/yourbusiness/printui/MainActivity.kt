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

        /* ---------- SCREEN 1 ---------- */
        composable("select_shop") {
            SelectShopScreen(
                onShopSelected = { shopName ->
                    navController.navigate("print_order/$shopName")
                }
            )
        }

        /* ---------- SCREEN 2 ---------- */
        composable("print_order/{shopName}") { backStackEntry ->
            val shopName = backStackEntry.arguments?.getString("shopName") ?: ""

            PrintOrderScreen(
                shopName = shopName,
                onBackPressed = {
                    navController.popBackStack()
                },
                onPayNowClick = { orderId, eta, amount ->
                    navController.navigate("payment_success/$orderId/$eta/$amount")
                },
                onHistoryClick = {
                    navController.navigate("order_history")
                }
            )
        }

        /* ---------- SCREEN 3 ---------- */
        composable("payment_success/{orderId}/{eta}/{amount}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val eta = backStackEntry.arguments?.getString("eta") ?: ""
            val amount = backStackEntry.arguments?.getString("amount") ?: ""

            PaymentSuccessScreen(
                orderId = orderId,
                etaText = eta,
                amount = amount,
                onBackToHome = {
                    navController.navigate("select_shop") {
                        popUpTo("select_shop") { inclusive = true }
                    }
                }
            )
        }

        /* ---------- SCREEN 4 : ORDER HISTORY ---------- */
        composable("order_history") {
            OrderHistoryScreen(
                onBack = { navController.popBackStack() },
                onReprint = {
                    navController.navigate("select_shop")
                }
            )
        }
    }
}
