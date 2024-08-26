package com.example.pomodoro

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Screen(screenViewModel: ScreenViewModel = viewModel(), notificationService: NotificationService) {
    val screenUiState by screenViewModel.uiState.collectAsState()
    
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        TextButton(onClick = {
            if (screenUiState.isRunning) screenViewModel.stop()
            else screenViewModel.start()
        }) {
            Text(text = "${screenUiState.minutes} : ${screenUiState.seconds}", fontSize = 64.sp, fontWeight = FontWeight.Thin)
        }
        Spacer(modifier = Modifier.width(64.dp))
        Row {
            IconButton(onClick = { screenViewModel.reset() }) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Reset Button")
            }
            FilledIconToggleButton(checked = false, onCheckedChange = { screenViewModel.longTimer() }) {
                if (!screenUiState.isLongTimer) {
                    Text(text = "50")
                } else Text(text = "25")
            }
        }
    }

    screenViewModel.run()

    if (screenUiState.isOver) {
        if (!screenUiState.isBreak) {
            FinalDialog({ screenViewModel.breakTime() }, {screenViewModel.reset() })
            notificationService.showNotification("Session Completed")
        } else {
            screenViewModel.reset()
            notificationService.showNotification("Break Over")
        }

    }
}

@Composable
fun FinalDialog(breakTime: () -> Unit, reset: () -> Unit) {
    AlertDialog(onDismissRequest = { /*TODO*/ }, title = {Text(text = "Session Completed")},
        confirmButton = {
        TextButton(onClick = reset ) {
            Text(text = "Skip Break")
        }
    }, dismissButton = {
        TextButton(onClick = breakTime) {
            Text(text = "Start Break")
        }
        })
}
