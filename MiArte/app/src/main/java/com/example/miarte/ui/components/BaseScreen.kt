package com.example.miarte.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BaseScreen(
    title: String,
    modifier: Modifier = Modifier,
    showButtonAdd: Boolean = true,
    onAddClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarApp()
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }

        if (showButtonAdd) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                FloatingActionButton(
                    onClick = { onAddClick?.invoke() },
                    containerColor = Color(0xFF666666),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
