package com.example.pawtrack

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.example.pawtrack.ui.theme.PawtrackTheme
import com.example.pawtrack.compose.CatInfoBox
import kotlinx.coroutines.launch

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PawtrackTheme {
                val context = LocalContext.current
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerMenu()
                    }
                ) {

                    Scaffold(
                        containerColor = Color(red = 226, green = 255, blue = 172),
                        topBar = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 12.dp, top = 15.dp, end = 12.dp)

                            ) {
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
                                    onClick = { scope.launch { drawerState.open() } },
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

                    ) { innerPadding ->

                        val cats = listOf(
                            Cat(
                                "Whiskers",
                                "Active",
                                imageRes = android.R.drawable.ic_menu_camera,
                                75
                            ),
                            Cat(
                                "Fluffy",
                                "Resting",
                                imageRes = android.R.drawable.ic_menu_gallery,
                                30
                            ),
                            Cat(
                                "Shadow",
                                "Exploring",
                                imageRes = android.R.drawable.ic_menu_compass,
                                60
                            ),
                        )
                        val catChunked = cats.chunked(2)

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .background(Color(red = 226, green = 255, blue = 172))
                        ) {
                            items(catChunked.size) { catPair ->
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    if (catChunked[catPair].size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))

                                        val currentCat = catChunked[catPair][0]

                                        for (cat in cats) {
                                            if (cat.name == currentCat.name) {
                                                CatInfoBox(
                                                    catName = cat.name,
                                                    status = cat.status,
                                                    imageRes = cat.imageRes,
                                                    meterValue = cat.meterValue
                                                )
                                            }
                                        }
                                    } else {
                                        for (cat in catChunked) {
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
}
@Composable
fun DrawerMenu() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(320.dp)
            .background(Color(red = 226, green = 255, blue = 172))
            .padding(16.dp)
    ) {
        Text(
            text = "Menu",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Account",
            fontSize = 20.sp,
            modifier = Modifier.clickable { /* Handle Click */ }
        )
        Spacer(modifier = Modifier.height(8.dp)) // Adds space between items
        Text(
            text = "Logout",
            fontSize = 20.sp,
            modifier = Modifier.clickable { /* Handle Click */ }
        )
        Spacer(modifier = Modifier.height(8.dp)) // Adds space between items
        Text(
            text = "History",
            fontSize = 20.sp,
            modifier = Modifier.clickable { /* Handle Click */ }
        )
        Spacer(modifier = Modifier.height(8.dp)) // Adds space between items
        Text(
            text = "Help",
            fontSize = 20.sp,
            modifier = Modifier.clickable { /* Handle Click */ }
        )
    }
}


data class Cat(
    val name: String,
    val status: String,
    val imageRes: Int,
    val meterValue: Int
)

