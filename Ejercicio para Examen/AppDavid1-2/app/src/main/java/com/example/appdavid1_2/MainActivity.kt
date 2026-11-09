package com.example.appdavid1_2

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appdavid1_2.ui.theme.AppDavid12Theme
import com.example.appdavid1_2.ui.theme.background
import com.example.appdavid1_2.ui.theme.paBackground
import com.example.appdavid1_2.ui.theme.paBox
import com.example.appdavid1_2.ui.theme.paText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import kotlin.jvm.java

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViewContainer()
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ViewContainer() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val historial = remember { mutableStateListOf<String>() }
    AppDavid12Theme() {
        Scaffold(
            topBar = { topBar() },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            content = {padding->
                Content(
                    modifier=Modifier.padding(padding).fillMaxSize()
                        .background(color = paBackground),
                    historial = historial,
                    snackbarHostState = snackbarHostState,
                    onRegister = { mensaje ->
                        historial.add(mensaje)
                    })
            },
            floatingActionButton = {
                FAB(
                    snackbarHostState = snackbarHostState,
                    scope = scope,
                    onRegister = { mensaje ->
                        historial.add(mensaje)
                    })
            },
            floatingActionButtonPosition = FabPosition.End
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun topBar() {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = R.string.myName), color = paText) },
        colors = topAppBarColors(
            containerColor = paBox,
            titleContentColor = paText
        )
    )
}

@Composable
fun FAB(snackbarHostState: SnackbarHostState, scope: CoroutineScope, onRegister: (String) -> Unit) {
    val context = LocalContext.current
    FloatingActionButton(
        onClick = {
            showSnackBar(snackbarHostState, scope, "Follow me to know sa little more about me!")
            onRegister("Pulsado desde FAB: ${System.currentTimeMillis()}")
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

fun showSnackBar(snackbarHostState: SnackbarHostState, scope: CoroutineScope,message:String) {
    scope.launch {
        snackbarHostState.showSnackbar(

            message = message,
            duration = SnackbarDuration.Short
        )
    }
}


@Composable
fun Content(
    modifier: Modifier,
    onRegister: (String) -> Unit,
    historial: SnapshotStateList<String>,
    snackbarHostState: SnackbarHostState
) {
    val context=LocalContext.current
    val scope= rememberCoroutineScope()
    var counter by rememberSaveable { mutableIntStateOf(0) }
    LazyColumn(
        modifier = modifier
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
                            counter++
                            showSnackBar(snackbarHostState, scope, "Thanks for your like!!")
                            onRegister("Pulsado desde Like: ${System.currentTimeMillis()}")
                        },
                    painter = painterResource(id = R.drawable.corazon),
                    contentDescription = stringResource(id = R.string.corazonDesc),
                    colorFilter = ColorFilter.tint(paText)
                )
                Text(
                    text = counter.toString(),
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
                    historial.forEach { item ->
                        Text(
                            text = item,
                            color = paText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .clickable {
                                    onRegister("Has pulsado el item: $item")
                                }
                        )
                    }
                    Button(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(10.dp),
                        onClick = {
                            val url="https://www.youtube.com"
                            val intent= Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        colors= ButtonDefaults.buttonColors(
                            containerColor = background,
                            contentColor = paText
                        )
                    ) {
                        Text("Ir a youtube")
                    }
                    Button(
                        modifier= Modifier
                            .padding(10.dp)
                            .width(200.dp),
                        onClick = {
                            val intent=Intent(Intent.ACTION_SENDTO).apply{
                                data=Uri.parse("mailto:example@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT,"Saludos desde Compose")
                                putExtra(Intent.EXTRA_TEXT,"Hola, este es un mensaje de prueba")

                            }
                            context.startActivity(intent)
                        },
                        colors= ButtonDefaults.buttonColors(
                            containerColor = background,
                            contentColor = paText
                        )
                    ) {
                        Text("Enviar correo")
                    }

                    Button(
                        modifier= Modifier
                            .padding(10.dp)
                            .width(200.dp),
                        onClick = {
                            val intent=Intent(Intent.ACTION_SEND).apply{
                                type="text/plain"
                                putExtra(Intent.EXTRA_TEXT,"Mira esta app")

                            }
                            context.startActivity(intent)
                        },
                        colors= ButtonDefaults.buttonColors(
                            containerColor = background,
                            contentColor = paText
                        )
                    ) {
                        Text("Compartir contenido")
                    }
                    //Esto no entra en examen
                    Button(
                        modifier= Modifier
                            .padding(10.dp)
                            .width(200.dp),
                        onClick = {
                            val intent=Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors= ButtonDefaults.buttonColors(
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
                                .height(60.dp).
                            clickable {
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
                                .clickable{
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
                                .clickable{
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