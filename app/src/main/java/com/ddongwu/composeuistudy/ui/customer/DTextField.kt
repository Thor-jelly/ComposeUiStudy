package com.ddongwu.composeuistudy.ui.customer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * 类描述：验证码输入框 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2023/7/19 16:05 <br/>
 */

@Preview
@Composable
fun TestVCodeUi() {
    var inputTextStr by remember {
        mutableStateOf("验证码：")
    }
    Column(
        Modifier
            .background(Color.White)
            .padding(10.dp)
    ) {
        Text(text = inputTextStr)
        Spacer(modifier = Modifier.height(10.dp))
        VerificationCodeTextField(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.Yellow, shape = RoundedCornerShape(10.dp))
        ) {
            inputTextStr = "验证码：$it"
        }
    }
}

/**
 * 验证码输入框
 *
 * 思路：BasicTextField 的 decorationBox 属性
 */
@Composable
fun VerificationCodeTextField(
    modifier: Modifier = Modifier,
    codeLength: Int = 6,
    fontSize: TextUnit = 21.sp,
    onVerify: (String) -> Unit = {}
) {
    DVerificationCodeTextField(
        modifier,
        codeLength,
        onVerify = {
            //输入完成回调
            onVerify(it)
        }
    ) { _, index, code ->
        //自定义每个输入框样式
        val codeState = when {
            index < code.length -> 2 //当前位置输入框有文字
            index == code.length -> 1 //当前文字正在输入
            else -> 0//当前还未输入
        }

        val cardColor = when (codeState) {
            2 -> Color(0xFF507CF7)
            1 -> Color.White
            else -> Color(0xF5F5F5F5)
        }

        val elevation = when (codeState) {
            2 -> 2.dp
            1 -> 5.dp
            else -> 0.dp
        }

        val textColor = when (codeState) {
            2 -> Color.White
            1 -> Color.Gray
            else -> Color.Gray
        }

        /*val blinkInterval = 1000L
        var isVisible by remember { mutableStateOf(true) }
        LaunchedEffect(blinkInterval) {
            while (true) {
                isVisible = !isVisible
                delay(blinkInterval)
            }
        }*/

        key(codeState) {
            Card(
                Modifier.size(50.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = elevation)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when (codeState) {
                        2 -> {
                            //已输入
                            Text(
                                text = code[index].toString(),
                                style = TextStyle(
                                    fontSize = fontSize,
                                    color = textColor,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }

                        1 -> {
                            //正在输入
                            /*if (isVisible) {
                                Text(
                                    "_", style = TextStyle(
                                        fontSize = fontSize,
                                        color = textColor,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }*/
                        }

                        else -> {
                            //未输入
                        }
                    }
                }
            }
        }
    }
}


/**
 * 验证码输入框
 *
 * 思路：BasicTextField 的 decorationBox 属性
 *
 * @param codeLength 验证码个数
 * @param onVerify 完成输入框输入时回调
 * @param codeBox 一个 Composable 函数，用于自定义每个验证码框的样式。
 *                 codeLength：验证码个数；index：当前验证码框索引，从0开始；code：验证码
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DVerificationCodeTextField(
    modifier: Modifier = Modifier,
    codeLength: Int = 6,
    onVerify: (String) -> Unit = {},
    codeBox: @Composable RowScope.(codeLength: Int, index: Int, code: String) -> Unit
) {
    //文本输入值
    var desStr by remember { mutableStateOf("") }
    //管理当前获得焦点的文本
    val focusManager = LocalFocusManager.current
    //用于请求焦点以显示软键盘
    val focusRequester = remember { FocusRequester() }
    //控制软键盘的显示 隐藏
    val keyboardController = LocalSoftwareKeyboardController.current

    //获取焦点
    LaunchedEffect(Unit) {
        Log.d("123===", "========${Thread.currentThread()}")
        delay(50)
        focusRequester.requestFocus()
    }

    BasicTextField(
        value = desStr,
        onValueChange = { newStr ->
            //限制最大长度 并且 都是数字
            if (newStr.length <= codeLength && newStr.all { it.isDigit() }) {
                desStr = newStr

                if (newStr.length == codeLength) {
                    //输入完成后自动提交并且隐藏键盘
                    onVerify(newStr)
                    focusManager.clearFocus()
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .focusRequester(focusRequester)//监听焦点
            .onFocusChanged {
                if (it.isFocused) {
                    //获取焦点后显示键盘
                    keyboardController?.show()
                }
            },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        singleLine = true,
        decorationBox = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(horizontal = 12.dp),
                //horizontalArrangement = Arrangement.spacedBy(5.dp),//控制子组件之间的距离
                horizontalArrangement = Arrangement.SpaceAround,//控制子组件之间的距离
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until codeLength) {
                    codeBox(codeLength, i, desStr)
                }
            }
        }
    )
}