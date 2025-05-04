package com.rafi607062330092.assesment2.screen

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun displaySnackbar(
    message: String,
    action: String,
    onAction: () -> Unit,
    onDismiss: () -> Unit,
    hostState: SnackbarHostState,
    scope: CoroutineScope
) {
    scope.launch {
        val result = hostState.showSnackbar(
            message = message,
            actionLabel = action,
            duration = SnackbarDuration.Short
        )
        when (result) {
            SnackbarResult.ActionPerformed -> {
                onAction()
            }
            SnackbarResult.Dismissed -> {
                onDismiss()
            }
        }
    }
}
