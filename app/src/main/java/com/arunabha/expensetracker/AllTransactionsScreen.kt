package com.arunabha.expensetracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.arunabha.expensetracker.ui.theme.Zinc
import com.arunabha.expensetracker.viewmodel.HomeViewModel
import com.arunabha.expensetracker.viewmodel.HomeViewModelFactory

@Composable
fun AllTransactionsScreen(navController: NavController) {

    val viewModel: HomeViewModel =
        HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)
    val state = viewModel.transactions.collectAsState(initial = emptyList())
    val bgColor = if (isSystemInDarkTheme()) Color.Black else Color.White // Hard-coded

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxWidth().background(bgColor)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Zinc)
                    .padding(horizontal = 5.dp, vertical = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = Color.White,
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(end = 7.dp, start = 5.dp)
                        .width(30.dp)
                        .height(30.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )

                Text(
                    text = "All transactions",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            LazyColumn(modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp)) {
                // Transactions list
                items(state.value) {
                    TransactionItem(
                        title = it.title,
                        amount = if (it.type == "Income") "+ $${it.amount}" else "- $${it.amount}",
                        icon = if (it.type == "Income") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        date = it.date.toString(),
                        color = if (it.type == "Income") Color.Green else Color.Red
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AllTransactionsScreenPreview() {
    AllTransactionsScreen(rememberNavController())
}