package com.ddongwu.composeuistudy.ui.test

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

/**
 * 类描述：学习 练习 composeUi  <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2023/7/17 16:55 <br/>
 */

@Preview
@Composable
fun CounterComponent() {
    Log.d("123===", "scope-1 运行")
    var counter by remember { mutableStateOf(0) }

    Column {
        Log.d("123===", "scope-2 运行")
        Button(onClick = {
            Log.d("123===", "button 点击")
            counter++
        }) {
            Log.d("123===", "scope-3 运行")
            Text("+")
        }
        Text("$counter")
    }
}

/**
 * 返回键处理
 */
@Composable
fun backPressHandler(enable: Boolean = true, onBackPressed: () -> Unit) {
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current){
        "没有通过 LocalOnBackPressedDispatcherOwner 提供 OnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val backCallback = remember {
        object :OnBackPressedCallback(enable){
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
    }

    DisposableEffect(backDispatcher){
        backDispatcher.addCallback(backCallback)
        onDispose {
            //当Compose 进入 onDispose 时
            //移除 backCallback 避免泄漏
            backCallback.remove()
        }
    }
}
