package com.arunabha.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.arunabha.expensetracker.data.model.TransactionEntity
import com.arunabha.expensetracker.ui.theme.Zinc
import com.arunabha.expensetracker.viewmodel.HomeViewModel
import com.arunabha.expensetracker.viewmodel.HomeViewModelFactory

// App's Home Screen
@Composable
fun HomeScreen(navController: NavController) {
    // Initializing Viewmodel using custom viewmodel factory
    val viewModel: HomeViewModel =
        HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)

    // Starting Home Screen ...
    Surface(modifier = Modifier.fillMaxSize()) {
        val bgColor = if (isSystemInDarkTheme()) Color.Black else Color.White
        ConstraintLayout(modifier = Modifier.fillMaxSize().background(bgColor)) {
            val (nameRow, card, list, topBar, dummy, add) = createRefs()

            // Top image section which covers status bar
            Image(
                painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(topBar) {
                        // Set constraints with respect to parent
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            // Card section which will greet user, show his/her name and notification
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        // Set constraint with respect to parent
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Column {

                    // Greeting Text
                    Text(text = "Good Morning!", fontSize = 16.sp, color = Color.White)

                    // Name Text
                    Text(
                        text = "Arunabha Mandal",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Notification icon
                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            val state = viewModel.transactions.collectAsState(initial = emptyList())
            val expenses = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)

            // Card section to show balance, income and expense
            CardItem(
                modifier = Modifier
                    .constrainAs(card) {
                        top.linkTo(nameRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                balance = balance,
                income = income,
                expense = expenses
            )

            // Transaction list to show recent transactions
            TransactionList(
                modifier = Modifier.constrainAs(list) {
                    top.linkTo(card.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
                list = state.value
            )

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Zinc,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .padding(end = 10.dp, bottom = 10.dp)
                    .constrainAs(add) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .clickable {
                        navController.navigate("/add")
                    }
            )

            // Testing : Navigation bar covering this
//            Text(text = "Arunabha!!", modifier = Modifier
//                .padding(bottom =)
//                .constrainAs(dummy) {
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                    bottom.linkTo(parent.bottom)
//                }
//            )
        }
    }
}


// Card Composable to show balance, income and expense
@Composable
fun CardItem(modifier: Modifier, balance: String, income: String, expense: String) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .shadow(7.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Zinc)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Total balance
            Column {
                Text(text = "Total Balance", fontSize = 16.sp, color = Color.White)
                Text(
                    text = balance,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Dots Menu
            Image(
                painter = painterResource(id = R.drawable.dots_menu),
                contentDescription = null
            )
        }

        // Row to show Income and Expense
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Income CardRowItem
            CardRowItem(
                modifier = Modifier,
                icon = R.drawable.ic_income,
                title = "Income",
                amount = income
            )

            // Expense CardRowItem
            CardRowItem(
                modifier = Modifier,
                icon = R.drawable.ic_expense,
                title = "Expense",
                amount = expense
            )
        }


    }
}

// CardRowItem Composable for income and expense
@Composable
fun CardRowItem(
    modifier: Modifier,
    icon: Int,
    title: String,
    amount: String
) {
    Column {
        Row {
            Image(painter = painterResource(icon), contentDescription = null)
            Spacer(modifier = Modifier.size(10.dp))
            Text(text = title, fontSize = 16.sp, color = Color.White)
        }
        Text(text = amount, fontSize = 20.sp, color = Color.White)
    }
}

// TransactionList Composable to show recent transactions
@Composable
fun TransactionList(modifier: Modifier, list: List<TransactionEntity>) {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {

        // For heading of recent transactions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Recent Transactions", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "See all", fontSize = 16.sp)
            }
        }

        // Transactions list
        items(list) {
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


// TransactionItem Composable to show each transaction
@Composable
fun TransactionItem(title: String, amount: String, icon: ImageVector, date: String, color: Color) {
    // Here I did not use Row! Instead I've used Box with proper alignment and padding
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row {

            // Icon for particular transaction
//            Image(
//                painter = painterResource(icon),
//                contentDescription = null,
//                modifier = Modifier.size(40.dp)
//            )

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(43.dp),
                tint = color
            )

            Spacer(modifier = Modifier.size(8.dp))

            // Title and date of transaction
            Column {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(text = date, fontSize = 12.sp)
            }
        }

        // Amount of transaction
        Text(
            text = amount,
            fontSize = 20.sp,
            color = color,
            modifier = Modifier.align(Alignment.CenterEnd),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewHomeScreen() {
    HomeScreen(rememberNavController())
}