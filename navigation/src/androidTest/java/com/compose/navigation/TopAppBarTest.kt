package com.compose.navigation

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.compose.navigation.ui.components.RallyTopAppBar
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

    // ComposeTestRule lets you set the Compose content under test and interact with it.
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun rallyTopAppBarTest() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTopAppBar(
                allScreens = allScreens,
                onTabSelected = {},
                currentScreen = RallyScreen.Accounts
            )
        }

        // Print the semantics tree which Compose uses for test.
        composeTestRule.onRoot().printToLog("rallyTopAppBarTest")

        // The semantics trees always tries to be as compact as possible, which means the descendants
        // of one node will always be merged into it. But in tests we oftentimes need to access all
        // nodes, to query the unmerged semantics tree, we can pass 'useUnmergedTree = true' to the finder.
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("rallyTopAppBarTest_allNodes")

        // Pattern for finding UI elements, checking their properties and
        // performing actions is: composeTestRule{.finder}{.assertion}{.action}
        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertIsSelected()
    }
}