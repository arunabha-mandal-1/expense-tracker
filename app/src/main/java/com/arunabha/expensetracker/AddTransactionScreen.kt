package com.arunabha.expensetracker

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.arunabha.expensetracker.data.model.TransactionEntity
import com.arunabha.expensetracker.ui.theme.Zinc
import com.arunabha.expensetracker.viewmodel.AddTransactionViewModel
import com.arunabha.expensetracker.viewmodel.AddTransactionViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun AddTransaction(navController: NavController, transactionEntity: TransactionEntity?) {

    val viewModel = AddTransactionViewModelFactory(LocalContext.current).create(
        AddTransactionViewModel::class.java
    )
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current


    Surface(modifier = Modifier.fillMaxSize()) {
        val bgColor = if (isSystemInDarkTheme()) Color.Black else Color.White
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor)
        ) {
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
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        navController.popBackStack() // Back to main screen
                    }
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
                transactionEntity,
                onAddTransactionClick = {
                    Log.d("err2", it.title + it.amount + it.date + it.category + it.type)

                    val updateFlag = (transactionEntity != null)
                    val nullOrEmptyFlag =
                        it.title.isNullOrEmpty() || it.amount.toString()
                            .isNullOrEmpty() || it.date.isNullOrEmpty()
                                || it.category.isNullOrEmpty() || it.type.isNullOrEmpty()

                    if (updateFlag) {
                        if (!nullOrEmptyFlag) {

                            // update
                            coroutineScope.launch {
                                if (viewModel.updateTransaction(it)) {
                                    navController.popBackStack() // Back to previous screen
                                } else {

                                    // database error
                                    Toast.makeText(
                                        context,
                                        "Failed to update transaction!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            // fill the fields
                            Toast.makeText(context, "Fill up all the fields!", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("err", it.title + it.amount + it.date + it.category + it.type)
                        }
                    } else {
                        if (!nullOrEmptyFlag) {

                            // add
                            coroutineScope.launch {
                                if (viewModel.addTransaction(it)) {
                                    navController.popBackStack() // Back to previous screen
                                } else {

                                    // database error
                                    Toast.makeText(
                                        context,
                                        "Failed to add transaction!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {

                            // fill the fields
                            Toast.makeText(context, "Fill up all the fields!", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("err", it.title + it.amount + it.date + it.category + it.type)
                        }
                    }
                }
            )
        }
    }
}

// DataForm composable: Form to take input of transaction details
@Composable
fun DataForm(
    modifier: Modifier,
    transactionEntity: TransactionEntity?,
    onAddTransactionClick: (model: TransactionEntity) -> Unit
) {


    var name by remember { mutableStateOf(transactionEntity?.title ?: "") }
//    var amount by remember { mutableStateOf(transactionEntity?.amount.toString() ?: "") }
    var amount by remember { mutableStateOf(if (transactionEntity?.amount == null) "" else transactionEntity.amount.toString()) }
    var date by remember { mutableStateOf(Utils.convertDateStringToMillis(transactionEntity?.date)) }
    var category by remember { mutableStateOf(transactionEntity?.category ?: "") }
    var type by remember { mutableStateOf(transactionEntity?.type ?: "") }

    var dateDialogVisibility by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
            .shadow(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // Name
//        Text(text = "Name", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(3.dp))
        OutlinedTextField(
            label = { Text("Transaction Name", fontSize = 14.sp, color = Color.Gray) },
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
                focusedBorderColor = Color.Black,
                focusedTextColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.size(5.dp))

        // Amount
//        Text(text = "Amount", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(3.dp))
        OutlinedTextField(
            label = { Text("Amount", fontSize = 14.sp, color = Color.Gray) },
            value = amount,
            onValueChange = { amount = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(5.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
                focusedBorderColor = Color.Black,
                focusedTextColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.size(5.dp))

        // Date
//        Text(text = "Date", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(3.dp))
        OutlinedTextField(
            label = { Text("Select Date", fontSize = 14.sp, color = Color.Gray) },
            value = if (date == 0L) "" else Utils.formatDateToHumanReadableForm(date),
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
//                .clickable {
//                    dateDialogVisibility.value = true
//                }
            ,
            shape = RoundedCornerShape(5.dp),
            enabled = false,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.clickable {
                        dateDialogVisibility = true
                    }
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
                focusedBorderColor = Color.Black,
                focusedTextColor = Color.Black,
                disabledBorderColor = Color.Black,
                disabledTextColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.size(5.dp))


        // Category
        // Need dropdown
//        Text(text = "Category", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(3.dp))
        DropdownSelector(
            list = listOf(
                "Food",
                "Shopping",
                "Entertainment",
                "Transportation",
                "Education",
                "Other"
            ),
            onItemSelected = { category = it },
            item = category,
            label = "Select Category"
        )
        Spacer(modifier = Modifier.size(5.dp))

        // Type
        // Button or dropdown
//        Text(text = "Type", fontSize = 14.sp)
        Spacer(modifier = Modifier.size(3.dp))
        DropdownSelector(
            list = listOf(
                "Income",
                "Expense"
            ),
            onItemSelected = { type = it },
            item = type,
            label = "Select Type"
        )
        Spacer(modifier = Modifier.size(8.dp))

//        val errFlag = name.value.isEmpty() || amount.value.isEmpty()
//                || date.value.toString().isEmpty() || category.value.isEmpty()
//                || type.value.isEmpty()
        // Add Button
        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Zinc),
//            enabled = !errFlag,
            onClick = {
                val transaction = TransactionEntity(
                    id = transactionEntity?.id,
                    title = name,
                    amount = amount.toDoubleOrNull() ?: 0.00,
                    date = Utils.formatDateToHumanReadableForm(date),
                    category = category,
                    type = type
                )
                onAddTransactionClick(transaction)
            }
        ) {
            Text(
                text = "Add Transaction",
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 3.dp)
            )
        }

        // Need date picker dialog
        if (dateDialogVisibility) {
            TransactionDatePickerDialog(
                onDateSelected = {
                    date = it
                    dateDialogVisibility = false
                },
                onDismiss = {
                    dateDialogVisibility = false
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

// This function is redundant now...
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDropdown(list: List<String>, onItemSelected: (item: String) -> Unit) {
    val expanded = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value },
//        modifier = Modifier.border()
    ) {
        OutlinedTextField(
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
            placeholder = { Text(text = "Select Category", style = TextStyle.Default) }
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
fun DropdownSelector(
    list: List<String>,
    label: String,
    item: String,
    onItemSelected: (item: String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(item) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = { selectedItem = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text(text = label, color = Color.Gray, fontSize = 14.sp) },
            shape = RoundedCornerShape(5.dp),
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    modifier = Modifier.clickable { expanded = !expanded },
                    tint = Color.Black
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Black,
                focusedBorderColor = Color.Black,
                focusedTextColor = Color.Black
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            list.forEach { label ->
                DropdownMenuItem(
                    text = { Text(text = label) },
                    onClick = {
                        selectedItem = label
                        expanded = false
                        onItemSelected(selectedItem)
                    }
                )
            }
        }
    }
}

//@Preview(
//    showBackground = true,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    name = "AddTransactionPreviewDark"
//)
@Composable
@Preview(showBackground = true)
fun AddTransactionPreview() {
    AddTransaction(rememberNavController(), null)
}