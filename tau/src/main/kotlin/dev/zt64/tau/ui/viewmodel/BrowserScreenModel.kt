package dev.zt64.tau.ui.viewmodel

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.model.ScreenModel
import java.nio.file.Path
import kotlin.io.path.Path

class BrowserScreenModel : ScreenModel {
    var currentLocation by mutableStateOf(Path("/"), referentialEqualityPolicy())
        private set

    val selected = mutableStateListOf<Path>()
    private var forwardStack = mutableStateListOf<Path>()
    private var backwardStack = mutableStateListOf<Path>()

    fun scan() {
    }
}