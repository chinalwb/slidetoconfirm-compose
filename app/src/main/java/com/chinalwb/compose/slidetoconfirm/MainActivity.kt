package com.chinalwb.compose.slidetoconfirm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chinalwb.compose.slidetoconfirm.demo.SlideToConfirmDemo
import com.chinalwb.compose.slidetoconfirm.ui.theme.SlideToConfirmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SlideToConfirmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SlideToConfirmDemo(modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SlideToConfirmTheme {
        Greeting("Android")
    }
}