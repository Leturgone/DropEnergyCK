package com.example.dropenergy.presentation.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ProgressCategory(
    val categoryName: String,
    val icon: ImageVector,
    val backgroundOfIcon: Color,
    val categoryValue: Int,
    val screenFunName: String
)
