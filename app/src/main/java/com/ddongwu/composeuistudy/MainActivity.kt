package com.ddongwu.composeuistudy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ddongwu.composeuistudy.ui.customer.TestVCodeUi
import com.ddongwu.composeuistudy.ui.theme.ComposeUiStudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeUiStudyTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Log.d("123===", "1========${Thread.currentThread()}")
                    TestVCodeUi()
                    Log.d("123===", "2========${Thread.currentThread()}")
                }
            }
        }
    }
}