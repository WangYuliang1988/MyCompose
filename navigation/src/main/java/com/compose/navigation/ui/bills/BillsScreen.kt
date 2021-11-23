package com.compose.navigation.ui.bills

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import com.compose.navigation.R
import com.compose.navigation.data.Bill
import com.compose.navigation.ui.components.BillRow
import com.compose.navigation.ui.components.StatementBody

/**
 * The Bills screen.
 */
@Composable
fun BillsBody(
    bills: List<Bill>,
    onBillClick: (String) -> Unit
) {
    StatementBody(
        modifier = Modifier.clearAndSetSemantics { contentDescription = "Bills" },
        items = bills,
        amounts = { bill -> bill.amount },
        colors = { bill -> bill.color },
        amountsTotal = bills.map { bill -> bill.amount }.sum(),
        circleLabel = stringResource(R.string.due),
        rows = { bill ->
            BillRow(
                modifier = Modifier.clickable { onBillClick(bill.name) },
                name = bill.name,
                due = bill.due,
                amount = bill.amount,
                color = bill.color,
                showArrow = true
            )
        }
    )
}

@Composable
fun SingleBillBody(bill: Bill) {
    StatementBody(
        items = listOf(bill),
        amounts = { bill.amount },
        colors = { bill.color },
        amountsTotal = bill.amount,
        circleLabel = bill.name,
        rows = {
            BillRow(
                name = it.name,
                due = it.due,
                amount = it.amount,
                color = it.color,
                showArrow = false
            )
        }
    )
}