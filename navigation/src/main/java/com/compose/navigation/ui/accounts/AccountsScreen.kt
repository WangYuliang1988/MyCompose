package com.compose.navigation.ui.accounts

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.compose.navigation.R
import com.compose.navigation.data.Account
import com.compose.navigation.ui.components.AccountRow
import com.compose.navigation.ui.components.StatementBody

/**
 * The Accounts screen.
 */
@Composable
fun AccountsBody(
    accounts: List<Account>,
    onAccountClick: (String) -> Unit = {}
) {
    StatementBody(
        modifier = Modifier.semantics { contentDescription = "Accounts Screen" },
        items = accounts,
        colors = { account -> account.color },
        amounts = { account -> account.balance },
        amountsTotal = accounts.map { account -> account.balance }.sum(),
        circleLabel = stringResource(id = R.string.total),
        rows = { account ->
            AccountRow(
                modifier = Modifier.clickable { onAccountClick(account.name) },
                name = account.name,
                number = account.number,
                amount = account.balance,
                color = account.color,
                showArrow = true
            )
        }
    )
}

@Composable
fun SingleAccountBody(account: Account) {
    StatementBody(
        items = listOf(account),
        colors = { account.color },
        amounts = { account.balance },
        amountsTotal = account.balance,
        circleLabel = account.name,
        rows = {
            AccountRow(
                name = it.name,
                number = it.number,
                amount = it.balance,
                color = it.color,
                showArrow = false
            )
        }
    )
}