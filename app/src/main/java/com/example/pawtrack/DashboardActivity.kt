package com.example.pawtrack

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pawtrack.ui.theme.PawtrackTheme
import com.example.pawtrack.compose.CatInfoBox

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PawtrackTheme {
                val context = LocalContext.current

                Scaffold(
                    containerColor = Color(red = 226, green = 255, blue = 172),
                    topBar = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp, top = 15.dp, end = 12.dp)

                        ){
                            Image(
                                painter = painterResource(id = R.drawable.paww),
                                contentDescription = "logo",
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.TopStart)

                            )
                            Text(
                                text = "PawTrack",
                                color = Color(red = 122, green = 188, blue = 0),
                                fontSize = 40.sp,
                                fontFamily = FontFamily.Cursive,
                                modifier = Modifier
                                    .padding(start = 60.dp)

                            )
                            IconButton(
                                onClick = { /* Handle the menu click action here */ },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Burger Menu Icon",
                                    tint = Color(red = 122, green = 188, blue = 0),
                                    modifier = Modifier
                                        .size(80.dp)


                                )
                            }
                        }
                    },
                    bottomBar = {
                        Button(
                            onClick = {
                                val intent = Intent(context, AddCatActivity::class.java)
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(red = 122, green = 188, blue = 0),
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)


                        ) {
                            Text(text = "ADD CAT")
                        }
                    }

                ){ innerPadding ->

                    val cats = listOf(
                        Cat("Whiskers", "Active", imageRes = android.R.drawable.ic_menu_camera, 75),
                        Cat("Fluffy", "Resting", imageRes = android.R.drawable.ic_menu_gallery, 30),
                        Cat("Shadow", "Exploring", imageRes = android.R.drawable.ic_menu_compass, 60),
                    )
                    val cat_chunked = cats.chunked(2)

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(Color(red = 226, green = 255, blue = 172))
                    ) {
                        items(cat_chunked.size) { catPair ->
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                if (cat_chunked[catPair].size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))

                                    var current_cat = cat_chunked[catPair].get(0)

                                    for ( cat in cats) {
                                        if(cat.name == current_cat.name){
                                            CatInfoBox(
                                                catName = cat.name,
                                                status = cat.status,
                                                imageRes = cat.imageRes,
                                                meterValue = cat.meterValue
                                            )
                                        }
                                    }
                                }
                                else {
                                    for (cat in cat_chunked) {
                                        CatInfoBox(
                                            catName = cat[catPair].name,
                                            status = cat[catPair].status,
                                            imageRes = cat[catPair].imageRes,
                                            meterValue = cat[catPair].meterValue
                                        )
                                    }
                                }

                            }

                        }

                    }

                }
            }
        }
    }
}

data class Cat(
    val name: String,
    val status: String,
    val imageRes: Int,
    val meterValue: Int
)

