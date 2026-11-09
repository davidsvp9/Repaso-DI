package com.example.appdavid3_4

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appdavid3_4.R
import com.example.appdavid3_4.bottomItem.BottomItem
import com.example.appdavid3_4.ui.theme.AppDavid34Theme
import com.example.appdavid3_4.ui.theme.background
import com.example.appdavid3_4.ui.theme.paBackground
import com.example.appdavid3_4.ui.theme.paBox
import com.example.appdavid3_4.ui.theme.paSelected
import com.example.appdavid3_4.ui.theme.paText
import com.example.appdavid3_4.viewModel.HistoricalViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.jvm.java

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            ViewContainer(navController = navController)
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ViewContainer(navController: NavHostController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val viewModel : HistoricalViewModel=viewModel()
    val bottomItems = listOf(
        BottomItem(route = "Home", icon = R.drawable.home, label = "Home"),
        BottomItem(route = "AboutMe", icon = R.drawable.user, label = "AboutMe"),
        BottomItem(route = "Gallery", icon = R.drawable.gallery, label = "Gallery"),
        BottomItem(route = "Settings", icon = R.drawable.settings, label = "Settings"),
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var topBarTitle by remember { mutableStateOf("Home") }
    LaunchedEffect(currentRoute) {
        bottomItems.find { it.route == currentRoute }?.let {
            topBarTitle = it.label
        }
    }
    AppDavid34Theme() {
        Scaffold(
            topBar = { topBar(topBarTitle) },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                bottomBar(
                    navController = navController,
                    buttomItems = bottomItems
                )
            },
            content = { padding ->
                NavHost(
                    navController = navController,
                    startDestination = "Home",
                    modifier = Modifier.padding(padding)
                ) {
                    composable("Home") {
                        ContentHome()}
                    composable("AboutMe") {
                        ContentAboutMe(viewModel=viewModel,snackbarHostState = snackbarHostState)
                    }
                    composable("Gallery") {
                        ContentGallery()
                    }
                    composable("Settings") {
                        ContentSettings()
                    }
                }
            },
            floatingActionButton = {
                FAB(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    scope = scope,
                    viewModel = viewModel()
                    )
            },
            floatingActionButtonPosition = FabPosition.End
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun topBar(topBarTitle: String) {
    CenterAlignedTopAppBar(
        title = { Text(text = topBarTitle, color = paText) },
        colors = topAppBarColors(
            containerColor = paBox,
            titleContentColor = paText
        )
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun bottomBar(
    navController: NavController, buttomItems: List<BottomItem>
) {
    NavigationBar(
        containerColor = background, contentColor = paText
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Log.d("ROUTE", "Current route: $currentRoute")
        buttomItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected, onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }, icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.size(if (selected) 32.dp else 24.dp)
                    )
                }, colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = paText,
                    unselectedIconColor = paSelected,
                    selectedTextColor = paText,
                    unselectedTextColor = paSelected,
                    indicatorColor = paBox
                )

            )
        }
    }
}

@Composable
fun FAB(
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    navController: NavController,
    viewModel: HistoricalViewModel = viewModel(),
) {
    val context = LocalContext.current
    FloatingActionButton(
        onClick = {
            showSnackBar(snackbarHostState, scope, "Follow me to know sa little more about me!")
            viewModel.registrar("Pulsado desde FAB: ${System.currentTimeMillis()}")
            navController.navigate("AboutMe") {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        modifier = Modifier.size(60.dp),
        contentColor = paText,
        containerColor = background
    ) {
        Icon(
            painter = painterResource(id = R.drawable.buscar),
            contentDescription = "See more",
            tint = paText,
        )
    }
}

fun showSnackBar(snackbarHostState: SnackbarHostState, scope: CoroutineScope, message: String) {
    scope.launch {
        snackbarHostState.showSnackbar(

            message = message,
            duration = SnackbarDuration.Short
        )
    }
}

@Composable
fun ContentHome() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = paBackground)
            .padding(top = 25.dp)
    ) { }
}

@Composable
fun ContentAboutMe(
    viewModel: HistoricalViewModel = viewModel(),
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = paBackground)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(800.dp))
                        .border(2.dp, color = paText, RoundedCornerShape(800.dp)),
                    painter = painterResource(id = R.drawable.walter),
                    contentDescription = stringResource(id = R.string.imgText)
                )
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            viewModel.addLikes()
                            showSnackBar(snackbarHostState, scope, "Thanks for your like!!")
                            viewModel.registrar("Pulsado desde Like: ${System.currentTimeMillis()}")
                        },
                    painter = painterResource(id = R.drawable.corazon),
                    contentDescription = stringResource(id = R.string.corazonDesc),
                    colorFilter = ColorFilter.tint(paText)
                )

                Text(
                    text = viewModel.likes.toString(),
                    color = paText
                )
                Text(
                    text = stringResource(id = R.string.myName),
                    color = paText,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = stringResource(id = R.string.myDescription),
                    color = paText,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(color = paBox)
                    )
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .height(60.dp),
                                contentScale = ContentScale.FillHeight,
                                contentDescription = stringResource(id = R.string.logoStudy),
                                painter = painterResource(id = R.drawable.study),
                                colorFilter = ColorFilter.tint(paText)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.studyTittle),
                                    color = paText,
                                )
                                Text(
                                    text = stringResource(id = R.string.studyText),
                                    color = paText,
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(color = paBox)
                    )
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .height(60.dp),
                                contentScale = ContentScale.FillHeight,
                                contentDescription = stringResource(id = R.string.logoGym),
                                painter = painterResource(id = R.drawable.pesa),
                                colorFilter = ColorFilter.tint(paText)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.gymTittle),
                                    color = paText,
                                )
                                Text(
                                    text = stringResource(id = R.string.gymText),
                                    color = paText,
                                )
                            }
                        }

                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(color = paBox)
                    )
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .height(60.dp),
                                contentScale = ContentScale.FillHeight,
                                contentDescription = stringResource(id = R.string.logoFood),
                                painter = painterResource(id = R.drawable.bebida),
                                colorFilter = ColorFilter.tint(paText)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.foodTittle),
                                    color = paText,
                                )
                                Text(
                                    text = stringResource(id = R.string.foodText),
                                    color = paText,
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(color = paBox)
                    )
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier
                                    .height(60.dp),
                                contentScale = ContentScale.FillHeight,
                                contentDescription = stringResource(id = R.string.logoHobbies),
                                painter = painterResource(id = R.drawable.videojuegos),
                                colorFilter = ColorFilter.tint(paText)
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.hobbiesTittle),
                                    color = paText,
                                )
                                Text(
                                    text = stringResource(id = R.string.hobbiesText),
                                    color = paText,
                                )
                            }
                        }
                    }
                    viewModel.historical.forEach { item ->
                        Text(
                            text = item,
                            color = paText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        )
                    }
                    Button(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(10.dp),
                        onClick = {
                            val url = "https://www.youtube.com"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = background,
                            contentColor = paText
                        )
                    ) {
                        Text("Ir a youtube")
                    }
                    Button(
                        modifier = Modifier
                            .padding(10.dp)
                            .width(200.dp),
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:example@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Saludos desde Compose")
                                putExtra(Intent.EXTRA_TEXT, "Hola, este es un mensaje de prueba")

                            }
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = background,
                            contentColor = paText
                        )
                    ) {
                        Text("Enviar correo")
                    }

                    Button(
                        modifier = Modifier
                            .padding(10.dp)
                            .width(200.dp),
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "Mira esta app")

                            }
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = background,
                            contentColor = paText
                        )
                    ) {
                        Text("Compartir contenido")
                    }
                    //Esto no entra en examen
                    Button(
                        modifier = Modifier
                            .padding(10.dp)
                            .width(200.dp),
                        onClick = {
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = background,
                            contentColor = paText
                        )
                    ) {
                        Text("Abrir otra Actividad")
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            modifier = Modifier
                                .height(60.dp)
                                .clickable {
                                    val url = "https://www.instagram.com"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                },
                            contentScale = ContentScale.FillHeight,
                            contentDescription = stringResource(id = R.string.instagramDesc),
                            painter = painterResource(id = R.drawable.instagram),
                            colorFilter = ColorFilter.tint(paText)
                        )
                        Image(
                            modifier = Modifier
                                .height(60.dp)
                                .clickable {
                                    val url = "https://www.linkedin.com"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                },
                            contentScale = ContentScale.FillHeight,
                            contentDescription = stringResource(id = R.string.linkedinDesc),
                            painter = painterResource(id = R.drawable.linkedin),
                            colorFilter = ColorFilter.tint(paText)
                        )
                        Image(
                            modifier = Modifier
                                .height(60.dp)
                                .clickable {
                                    val url = "https://www.twitter.com"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                },
                            contentScale = ContentScale.FillHeight,
                            contentDescription = stringResource(id = R.string.twitterDesc),
                            painter = painterResource(id = R.drawable.twitter),
                            colorFilter = ColorFilter.tint(paText)
                        )
                        Image(
                            modifier = Modifier
                                .height(60.dp)
                                .clickable {
                                    val url = "https://www.whatsapp.com"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                },
                            contentScale = ContentScale.FillHeight,
                            contentDescription = stringResource(id = R.string.whatsappDesc),
                            painter = painterResource(id = R.drawable.whatsapp),
                            colorFilter = ColorFilter.tint(paText)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ContentGallery() {
    val images = listOf(
        R.drawable.bd1, R.drawable.bd2, R.drawable.bd3, R.drawable.bd4,
        R.drawable.bd4, R.drawable.bd3, R.drawable.bd2, R.drawable.bd1,
        R.drawable.bd3, R.drawable.bd1, R.drawable.bd4, R.drawable.bd2
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .background(paBackground)
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(images.size) { index ->
            Image(
                painter = painterResource(id = images[index]),
                contentDescription = "Imagen $index",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(15.dp))
            )
        }
    }
}


@Composable
fun ContentSettings() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(paBackground)
            .padding(top = 25.dp)
    ) { }
}
