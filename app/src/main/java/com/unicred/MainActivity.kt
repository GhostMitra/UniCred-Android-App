package com.unicred

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.unicred.ui.navigation.UniCredNavigation
import com.unicred.ui.theme.UniCredTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniCredTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UniCredNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}