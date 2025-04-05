package com.example.dropenergy.ProgressScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dropenergy.R
import com.example.dropenergy.ui.components.ProgressCategory
import com.example.dropenergy.ui.theme.Green
import com.example.dropenergy.ui.theme.Yellow40


lateinit var  progressCategories: List<ProgressCategory>


@Composable
fun ProgressSection(navController : NavHostController){

    progressCategories = listOf(
        ProgressCategory(
            categoryName = stringResource(id = R.string.money),
            icon = Icons.Rounded.AttachMoney,
            backgroundOfIcon = Yellow40,
            categoryValue = 53,
            screenFunName = "moneyScreen"
        ),
        ProgressCategory(
            categoryName = stringResource(id = R.string.cans),
            icon = ImageVector.vectorResource(R.drawable.energy_drink),
            backgroundOfIcon = Green,
            categoryValue = 1,
            screenFunName = "canScreen"
        )
    )

    Column {
        Text(text = stringResource(id = R.string.progress),
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        LazyRow{
            items(progressCategories.size){
                val cat = progressCategories[it]
                var lastPaddingEnd = 0.dp
                if (it == progressCategories.size - 1){
                    lastPaddingEnd = 16.dp
                }
                Box(modifier = Modifier
                    .padding(start = 16.dp, end = lastPaddingEnd)
                ) {
                    //Для элемента
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .size(120.dp)
                            .clickable {}
                            .padding(13.dp)
                            .clickable(onClick = { navController.navigate(cat.screenFunName) }
                            ), verticalArrangement = Arrangement.SpaceBetween)
                    {
                        Row {
                            //Внутренности
                            Box(
                                Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(cat.backgroundOfIcon)
                                    .padding(6.dp)
                            )
                            {
                                Icon(
                                    imageVector = cat.icon,
                                    contentDescription = cat.screenFunName,
                                    tint = Color.White
                                )

                            }
                            Text(
                                text = cat.categoryName,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                modifier = Modifier.offset(8.dp,5.dp)
                            )
                        }

                    }
                }
            }
        }

    }

}
