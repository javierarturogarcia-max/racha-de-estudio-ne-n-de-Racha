package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.RachaScreen
import com.example.ui.RachaViewModel
import com.example.ui.theme.NeonDarkBg
import com.example.ui.theme.RachaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RachaTheme {
                val viewModel: RachaViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = NeonDarkBg
                ) { _ ->
                    RachaScreen(
                        uiState = uiState,
                        onStudyTodayClicked = viewModel::onStudyTodayClicked,
                        onUndoTodayClicked = viewModel::onUndoTodayClicked,
                        onNextQuoteClicked = viewModel::nextQuote,
                        onToggleDay = viewModel::toggleDay
                    )
                }
            }
        }
    }
}
