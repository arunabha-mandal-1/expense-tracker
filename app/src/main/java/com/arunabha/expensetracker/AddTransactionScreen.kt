package com.arunabha.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.arunabha.expensetracker.data.model.TransactionEntity
import com.arunabha.expensetracker.ui.theme.Zinc
import com.arunabha.expensetracker.viewmodel.AddTransactionViewModel
import com.arunabha.expensetracker.viewmodel.AddTransactionViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun AddTransaction(navController: NavController) {

    val viewModel = AddTransactionViewModelFactory(LocalContext.current).create(
        AddTransactionViewModel::class.java
    )
    val coroutineScope = rememberCoroutineScope()


    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topBar, titleRow, dataForm) = createRefs()

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

            // Title, Back and Menu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .constrainAs(titleRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null
                )
                Text(
                    text = "Add Transaction",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Image(
                    painter = painterResource(id = R.drawable.dots_menu),
                    contentDescription = null
                )
            }

            // Form to take input of transaction details
            DataForm(
                modifier = Modifier
                    .padding(top = 60.dp)
                    .constrainAs(dataForm) {
                        top.linkTo(titleRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                onAddTransactionClick = {
                    coroutineScope.launch {
                        if(viewModel.addTransaction(it)){
                            navController.popBackStack() // Back to previous screen
                        }
                    }
                }
            )
        }
    }
}

// DataForm composable: Form to take input of transaction details
@Composable
fun DataForm(modifier: Modifier, onAddTransactionClick: (model: TransactionEntity) -> Unit) {


    val name = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }

    val date = remember { mutableStateOf(0L) }
    val dateDialogVisibility = remember { mutableStateOf(false) }

    val category = remember { mutableStateOf("") }

    val type = remember { mutableStateOf("") }


    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
            .shadow(5.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // Name
        Text(text = "Name", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(3.dp))
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))

        // Amount
        Text(text = "Amount", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(3.dp))
        OutlinedTextField(
            value = amount.value,
            onValueChange = { amount.value = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
        )
        Spacer(modifier = Modifier.size(8.dp))

        // Date
        Text(text = "Date", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(3.dp))
        OutlinedTextField(
            value = if (date.value == 0L) "" else Utils.formatDateToHumanReadableForm(date.value),
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    dateDialogVisibility.value = true
                },
            shape = RoundedCornerShape(10.dp),
            enabled = false,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.Black,
                disabledTextColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.size(8.dp))


        // Category
        // Need dropdown
        Text(text = "Category", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(3.dp))
        TransactionDropdown(
            list = listOf(
                "Food",
                "Shopping",
                "Entertainment",
                "Transportation",
                "Education",
                "Other"
            ), onItemSelected = { category.value = it }
        )
        Spacer(modifier = Modifier.size(8.dp))

        // Type
        // Button or dropdown
        Text(text = "Type", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(3.dp))
        TransactionDropdown(
            list = listOf(
                "Income",
                "Expense"
            ), onItemSelected = { type.value = it }
        )
        Spacer(modifier = Modifier.size(8.dp))

        // Add Button
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Zinc),
            onClick = {
                val transaction = TransactionEntity(
                    id = null,
                    title = name.value,
                    amount = amount.value.toDoubleOrNull() ?: 0.00,
                    date = Utils.formatDateToHumanReadableForm(date.value),
                    category = category.value,
                    type = type.value
                )
                onAddTransactionClick(transaction)
            }
        ) {
            Text(text = "Add Transaction", fontWeight = FontWeight.SemiBold)
        }

        // Need date picker dialog
        if (dateDialogVisibility.value) {
            TransactionDatePickerDialog(
                onDateSelected = {
                    date.value = it
                    dateDialogVisibility.value = false
                },
                onDismiss = {
                    dateDialogVisibility.value = false
                }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDatePickerDialog(
    // Two callbacks
    onDateSelected: (date: Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0L
    DatePickerDialog(
        onDismissRequest = { onDismiss() },

        // Confirm
        confirmButton = {
            TextButton(onClick = { onDateSelected(selectedDate) }) {
                Text(text = "Confirm")
            }
        },

        // Dismiss
        dismissButton = {
            TextButton(onClick = { onDateSelected(selectedDate) }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDropdown(list: List<String>, onItemSelected: (item: String) -> Unit) {
    val expanded = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        TextField(
            value = selectedItem.value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .clip(RoundedCornerShape(10.dp)),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            },
            placeholder = { Text(text = "Select") }
        )

        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            list.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        selectedItem.value = item
                        expanded.value = false
                        onItemSelected(selectedItem.value)
                    }
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun AddTransactionPreview() {
    AddTransaction(rememberNavController())
}