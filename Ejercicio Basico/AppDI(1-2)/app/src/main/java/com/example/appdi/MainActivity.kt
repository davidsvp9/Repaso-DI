package com.example.appdi

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.LiveFolders
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appdi.ui.theme.AppDITheme
import com.example.appdi.ui.theme.paBackground
import com.example.appdi.ui.theme.paBox
import com.example.appdi.ui.theme.paText
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import com.example.appdi.ui.theme.background
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            viewContainer()


        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun viewContainer(){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope= rememberCoroutineScope()
    val historial= remember{ mutableStateListOf<String>()}
    AppDITheme(){
        Scaffold(
            topBar = {toolBar()},
            content = {Content(historial=historial,
                onRegister={mensaje->
                    historial.add(mensaje)
            })},
            floatingActionButton = {FAB(snackbarHostState=snackbarHostState,
                scope=scope,
                onRegister={mensaje->
                    historial.add(mensaje)
                })
                                   },
            floatingActionButtonPosition = FabPosition.End)

    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun toolBar(){
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.myName),
                color = paText,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        colors= topAppBarColors(
            containerColor = background
        )
    )
}


@Composable
fun FAB(snackbarHostState: SnackbarHostState,
        scope: CoroutineScope,
        onRegister:(String)->Unit){
    val context= LocalContext.current
    FloatingActionButton(
        onClick = {
            showSnackbar(snackbarHostState,scope)
            onRegister("Pulsacion desde FAB: ${System.currentTimeMillis()}")
        },
        containerColor = background,
        contentColor = paText,
        modifier=Modifier.size(80.dp)
    ) {
        Icon(
            painter=painterResource(id = R.drawable.buscar),
            contentDescription = "See more",
            tint = paText
        )
    }
}


fun showSnackbar(snackbarHostState: SnackbarHostState,scope: CoroutineScope){
    scope.launch{
        snackbarHostState.showSnackbar(
            message = "Follow me to know a little more about me",
            duration = SnackbarDuration.Short
        )
    }
}


@Composable
fun Content(historial: SnapshotStateList<String>,
            onRegister:(String)->Unit) {
    var counterLikes by rememberSaveable() { mutableStateOf(0) }
    val context= LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 25.dp)
            .background(color = paBackground)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(800.dp))
                        .border(6.dp, color = paText, RoundedCornerShape(800.dp)),
                    painter = painterResource(id = R.drawable.walter),
                    contentDescription = stringResource(id = R.string.imgText)
                )
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable{counterLikes++},
                    painter = painterResource(id = R.drawable.corazon),
                    colorFilter = ColorFilter.tint(color = paText),
                    contentDescription = stringResource(id=R.string.corazonDesc)
                )
                Text(
                    text = counterLikes.toString(),
                    color = paText,
                    modifier = Modifier.padding(start=4.dp)
                )
                Text(
                    text = stringResource(id = R.string.myName),
                    color = paText,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(id = R.string.myDescription),
                    color = paText,
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(color = paBox)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.height(60.dp),
                                painter = painterResource(id = R.drawable.study),
                                contentDescription = stringResource(id = R.string.logoStudy),
                                colorFilter = ColorFilter.tint(color = paText),
                                contentScale = ContentScale.FillHeight
                            )
                            Column(modifier = Modifier.padding(10.dp).fillMaxWidth()) {
                                Text(
                                    text = stringResource(id = R.string.studyTittle),
                                    color = paText,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(bottom = 10.dp)
                                )
                                Text(
                                    text = stringResource(id = R.string.studyText),
                                    color = paText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.wrapContentSize()
                                )

                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(color = paBox)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.height(60.dp),
                                painter = painterResource(id = R.drawable.pesa),
                                contentDescription = stringResource(id = R.string.logoGym),
                                colorFilter = ColorFilter.tint(color = paText),
                                contentScale = ContentScale.FillHeight
                            )
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = stringResource(id = R.string.gymTittle),
                                    color = paText,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(bottom = 10.dp)
                                )
                                Text(
                                    text = stringResource(id = R.string.gymText),
                                    color = paText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.wrapContentSize()
                                )

                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(color = paBox)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.height(60.dp),
                                painter = painterResource(id = R.drawable.bebida),
                                contentDescription = stringResource(id = R.string.logoFood),
                                colorFilter = ColorFilter.tint(color = paText),
                                contentScale = ContentScale.FillHeight
                            )
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = stringResource(id = R.string.foodTittle),
                                    color = paText,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(bottom = 10.dp)
                                )
                                Text(
                                    text = stringResource(id = R.string.foodText),
                                    color = paText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.wrapContentSize()
                                )

                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(color = paBox)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.height(60.dp),
                                painter = painterResource(id = R.drawable.videojuegos),
                                contentDescription = stringResource(id = R.string.logoHobbies),
                                colorFilter = ColorFilter.tint(color = paText),
                                contentScale = ContentScale.FillHeight
                            )
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = stringResource(id = R.string.hobbiesText),
                                    color = paText,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(bottom = 10.dp)
                                )
                                Text(
                                    text = stringResource(id = R.string.hobbiesText),
                                    color = paText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.wrapContentSize()
                                )

                            }
                        }

                    }

                    historial.forEach { item ->
                        Text(
                            text = item,
                            color = Color.White,
                            modifier= Modifier.fillMaxWidth()
                                .padding(8.dp)
                                .clickable{
                                    onRegister("Has pulsado el item: $item")
                                }
                        )
                    }
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply{
                                data= Uri.parse("mailto:davidsvp9@gmail.com")
                                putExtra(Intent.EXTRA_SUBJECT,"Saludos desde Compose")
                                putExtra(Intent.EXTRA_TEXT,"Hola, saludos desde mi App")
                            }
                            context.startActivity(intent)
                        },
                        colors= ButtonDefaults.buttonColors(
                            containerColor = background,
                            contentColor = paText
                        ),
                        modifier=Modifier.width(200.dp)

                    ) {
                        Text("Enviar correo")
                    }

                    Button(
                        onClick = {
                            val intent=Intent(Intent.ACTION_SEND).apply{
                                type="text/plain"
                                putExtra(Intent.EXTRA_TEXT,"Mira esta app de Compose")
                            }
                            context.startActivity(intent)
                        },
                        colors= ButtonDefaults.buttonColors(
                            containerColor = background,
                            contentColor = paText
                        ),
                        modifier=Modifier.width(200.dp),

                    ) {
                        Text("Compartir contenido")
                    }

                    Button(
                        onClick = {
                            val url= "http://www.youtube.com"
                            val intent=Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        colors= ButtonDefaults.buttonColors(
                            containerColor = background,
                            contentColor = paText
                        ),
                        modifier=Modifier.width(200.dp),
                    ) {
                        Text("Abrir youtube")
                    }

                    Button(
                        onClick = {
                            val intent=Intent(context,MainActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors= ButtonDefaults.buttonColors(
                            containerColor = background,
                            contentColor = paText
                        ),
                        modifier=Modifier.width(200.dp),

                    ) {
                        Text("Abrir otra actividad")
                    }

                    Row(
                        modifier=Modifier.fillMaxWidth().padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            modifier = Modifier.height(50.dp),
                            painter = painterResource(id = R.drawable.instagram),
                            contentDescription = stringResource(id = R.string.instagramDesc),
                            colorFilter = ColorFilter.tint(color=paText),
                            contentScale = ContentScale.FillHeight
                        )
                        Image(
                            modifier = Modifier.height(50.dp),
                            painter = painterResource(id = R.drawable.linkedin),
                            contentDescription = stringResource(id = R.string.linkedinDesc),
                            contentScale = ContentScale.FillHeight,
                            colorFilter = ColorFilter.tint(color=paText)
                        )
                        Image(
                            modifier = Modifier.height(50.dp),
                            painter = painterResource(id = R.drawable.twitter),
                            contentDescription = stringResource(id = R.string.twitterDesc),
                            contentScale = ContentScale.FillHeight,
                            colorFilter = ColorFilter.tint(color=paText)
                        )
                        Image(
                            modifier = Modifier.height(50.dp),
                            painter = painterResource(id = R.drawable.whatsapp),
                            contentDescription = stringResource(id = R.string.whatsappDesc),
                            contentScale = ContentScale.FillHeight,
                            colorFilter = ColorFilter.tint(color=paText)
                        )
                    }
                }
            }
        }
    }
}