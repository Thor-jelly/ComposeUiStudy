package com.ddongwu.composeuistudy.ui.customer

import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2023/7/20 15:12 <br/>
 */

@Preview
@Composable
fun DTab(
) {
    val list = listOf("啊啊啊", "哦哦哦")

    var selectedIndex by remember {
        mutableStateOf(0)
    }

    ScrollableTabRow(selectedTabIndex = selectedIndex) {

    }
    TabRow(selectedTabIndex = selectedIndex) {

    }


}