package com.arunabha.expensetracker

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.arunabha.expensetracker.data.model.TransactionEntity
import com.arunabha.expensetracker.ui.theme.Zinc
import com.arunabha.expensetracker.viewmodel.HomeViewModel
import com.arunabha.expensetracker.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun AllTransactionsScreen(navController: NavController) {

    val viewModel: HomeViewModel =
        HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)
    val state = viewModel.transactions.collectAsState(initial = emptyList())
    val bgColor = if (isSystemInDarkTheme()) Color.Black else Color.White // Hard-coded
    val coroutineScope = rememberCoroutineScope()


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor)
        ) {
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
                items(state.value) { transaction ->
//                    TransactionItem(
//                        title = it.title,
//                        amount = if (it.type == "Income") "+ $${it.amount}" else "- $${it.amount}",
//                        icon = if (it.type == "Income") Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
//                        date = it.date.toString(),
//                        color = if (it.type == "Income") Color.Green else Color.Red
//                    )
                    TransactionItemDetails(
                        transactionEntity = transaction,
                        onDeleteClicked = {
                            coroutineScope.launch {
                                val flag = viewModel.deleteTransaction(transaction)
                            }
                        },
                        onUpdateClicked = {
                            // Passing transaction entity
                            val json = Uri.encode(Json.encodeToString(transaction))
                            navController.navigate("/add/$json")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Transactions() {
    // pass TransactionItemDetails() inside lazyColumn to show all the transaction details
    // ...
}

@Composable
fun TransactionItemDetails(
    transactionEntity: TransactionEntity,
    onDeleteClicked: () -> Unit,
    onUpdateClicked: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val textColor = Color.White
    // What we have in the prev composable = title, amount, icon, date, color
    // What we have in the Transaction model = title, amount, date, category, type

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp, 3.dp)
            .shadow(3.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Zinc)
                .padding(5.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(5.dp, 5.dp)
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = transactionEntity.title,
                        color = textColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = transactionEntity.date,
                        color = textColor,
                        fontSize = 10.sp
                    )
                }

                Text(
                    text = if (transactionEntity.type == "Income") {
                        "+ ₹${transactionEntity.amount}"
                    } else {
                        "- ₹${transactionEntity.amount}"
                    },
                    color = textColor,
                    modifier = Modifier
                        .padding(top = 5.dp, end = 10.dp)
                        .weight(0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.End
                )


                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    tint = textColor,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .clickable {
                            expanded = !expanded
                        },
                    contentDescription = if (expanded) {
                        stringResource(id = R.string.show_less)
                    } else stringResource(
                        id = R.string.show_more
                    )
                )
            }

            if (expanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 4.dp, top = 1.dp, bottom = 3.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        // type and category
                        Text(
                            text = transactionEntity.type,
                            overflow = TextOverflow.Ellipsis,
                            color = textColor,
                            fontSize = 12.sp,
                            maxLines = 1,
                            softWrap = true,
                            modifier = Modifier.weight(0.4f)
                        )
                        Text(
                            text = transactionEntity.category,
                            overflow = TextOverflow.Ellipsis,
                            color = textColor,
                            fontSize = 12.sp,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(0.5f)
                    ) {
                        // edit and delete
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit transaction",
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .height(20.dp)
                                .width(20.dp)
                                .clickable {
                                    onUpdateClicked()
                                },
                            tint = textColor
                        )
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete transaction",
                            modifier = Modifier
                                .padding(start = 2.dp)
                                .height(20.dp)
                                .width(20.dp)
                                .clickable {
                                    onDeleteClicked()
                                },
                            tint = textColor
                        )
                    }
                }
            }
        }
    }
}

//@Composable
//@Preview(showBackground = true)
//fun AllTransactionsScreenPreview() {
//    AllTransactionsScreen(rememberNavController())
//}

@Composable
@Preview(showBackground = true)
fun TransactionItemPreview() {
    TransactionItem(
        title = "Book",
        amount = "$ 500",
        icon = Icons.Default.KeyboardArrowUp,
        date = "18/11/2024",
        color = Color.Green
    )
}

@Composable
@Preview(showBackground = true)
fun TransactionItemDetailsPreview() {
    TransactionItemDetails(
        TransactionEntity(
            1,
            "Math Book",
            200000000000000.33,
            "12/11/20",
            "Education",
            "Expense"
        ),
        onDeleteClicked = {},
        onUpdateClicked = {}
    )
}
