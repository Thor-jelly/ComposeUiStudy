package com.ddongwu.composeuistudy.ui.customer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2023/7/20 15:12 <br/>
 */

@Preview
@Composable
fun DTab() {
    val list = listOf("啊啊啊", "哦哦哦")

    var tabIndex by remember {
        mutableStateOf(0)
    }

    //可以移动的tab
    //ScrollableTabRow(selectedTabIndex = selectedIndex) {}
    TabRow(
        selectedTabIndex = tabIndex,
        containerColor = Color.Red,
        modifier = Modifier.clip(RoundedCornerShape(50)),
        indicator = {
//            TabRowDefaults.Indicator(
//                modifier = Modifier.tabIndicatorOffset(it[tabIndex]),
//                color = Color.White
//            )
            Box(
                Modifier
                    .tabIndicatorOffset(it[tabIndex])
                    .fillMaxSize()
                    .padding(1.dp)
                    .background(
                        color = Color.Green,
                        if (tabIndex == 0) RoundedCornerShape(50.dp, 0.dp, 0.dp, 50.dp) else RoundedCornerShape(0.dp, 50.dp, 50.dp, 0.dp)
                    )
            )
        },
        divider = {}
    ) {
        list.forEachIndexed { index, str ->
            val selected = tabIndex == index
            Tab(selected = selected,
                modifier = Modifier.zIndex(2f),
                onClick = {
                    tabIndex = index
                }) {

                Text(
                    text = str,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp)
//                        .background(
//                            if (selected) Color.Yellow else Color.Cyan,
//                            if (index == 0) RoundedCornerShape(50.dp, 0.dp, 0.dp, 50.dp) else RoundedCornerShape(0.dp, 50.dp, 50.dp, 0.dp)
//                        )
                        .padding(10.dp, 15.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}