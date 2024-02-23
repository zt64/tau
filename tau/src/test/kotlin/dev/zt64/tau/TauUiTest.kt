package dev.zt64.tau

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

class TauUiTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testUi() {
        runComposeUiTest {
            setContent {
                Tau(
                    onCloseRequest = {}
                )
            }
        }
    }
}