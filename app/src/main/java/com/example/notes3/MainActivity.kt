package com.example.notes3

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var notesList = remember {
                mutableStateListOf<Note>(
                    Note(
                        "Добрый день",
                        "Оставьте свою первую заметку"
                    )
                )
            }

            if (intent.extras != null) {
                val noteTitle = intent.getStringExtra("noteTitle").toString()
                val noteText = intent.getStringExtra("noteText").toString()
                val note = Note(noteTitle, noteText)
                notesList.add(note)
            }

            MyDrawer(notesList)


        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyDrawer(notes: MutableList<Note>, context: Context = LocalContext.current) {
    val selectedItem = remember { mutableStateOf(notes[0]) }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet(
                drawerContainerColor = Color.DarkGray,
                drawerContentColor = Color.LightGray
            ) {

                notes.forEach { item ->
                    NavigationDrawerItem(
                        label = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.name, fontSize = 22.sp)
                                IconButton(onClick = {
                                    if (notes.size != 1) {
                                        val index = search(notes, item)
                                        Log.d("@@@", "index = $index")
                                        notes.removeAt(index)
                                    } else {
                                        scope.launch{drawerState.close()}
                                        scope.launch{snackbarHostState.showSnackbar(
                                            "Должна быть минимум одна заметка",
                                            actionLabel = "Ок",
                                            duration = SnackbarDuration.Short)}
                                    }
                                }) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "Удалить"
                                    )
                                }
                            }
                        },
                        selected = selectedItem.value == item,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            selectedItem.value = item
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color(0xff534582),
                            unselectedContainerColor = Color.DarkGray,
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.White
                        )
                    )
                }
            }
        },

        content = {
            Scaffold (
                floatingActionButton = {CreateFAB(context = LocalContext.current)},
                topBar = { GetTopBarMain(context,scope,drawerState)},
                snackbarHost = {SnackbarHost(snackbarHostState)}

            ) { innerPadding -> {

            }
                Column (modifier = Modifier.padding(innerPadding)) {

                    MyContent(selectedItem)


                }



            }
        }
    )
}


@Composable
fun CreateFAB(context: Context) {

    FloatingActionButton(onClick = {
        val activity = context as? Activity
        val intent = Intent(activity, AddNoteActivity::class.java)
        context.startActivity(intent)
    }) {
        Icon(Icons.Filled.Add, contentDescription = "AddNote")
    }
}

private fun search(notes: MutableList<Note>, note: Note): Int {
    var result = -1
    for (i in notes.indices) {
        if ((note.name == notes[i].name) || (note.noteText == notes[i].noteText)) result = i
    }
    return result
}

@Composable
fun MyContent(selectedItem: MutableState<Note>){

    Column(modifier = Modifier.padding(top = 10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = selectedItem.value.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .background(color = Color.LightGray, shape = RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(20.dp))
        ) {
            Text(
                text = selectedItem.value.noteText,
                fontSize = 24.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetTopBarMain(context: Context, scope: CoroutineScope, drawerState: DrawerState) {
    TopAppBar(title = { Text(text = "Заметки", fontSize = 22.sp) },
        actions = {
            IconButton(onClick = {
                scope.launch { drawerState.open() }
            },
                content = {
                    Icon(Icons.Filled.Menu, contentDescription = "Меню")
                })
        })
}