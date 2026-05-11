package com.jpalomino502.vivebien.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomTabRow(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color(0xFFEEF2F5),
        contentColor = Color(0xFF4CAF50),
        modifier = modifier.clip(RoundedCornerShape(8.dp)),
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                height = 0.dp
            )
        },
        divider = { Divider(thickness = 0.dp) }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (selectedIndex == index) Color.White else Color.Transparent
                    ),
                text = {
                    Text(
                        text = title,
                        fontWeight = if (selectedIndex == index) FontWeight.Medium else FontWeight.Normal,
                        color = if (selectedIndex == index) Color(0xFF4CAF50) else Color.Gray
                    )
                }
            )
        }
    }
}
