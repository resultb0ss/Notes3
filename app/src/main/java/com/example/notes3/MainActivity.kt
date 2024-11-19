package com.example.notes3

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {



            val context = LocalContext.current
            val notes = mutableListOf<Note>(Note("Добрый день","Оставьте свою первую заметку"))
            val noteTitle = intent.getStringExtra("noteTitle").toString()
            val noteText = intent.getStringExtra("noteText").toString()
            val note = Note(noteTitle, noteText)
            notes.add(note)

            MyDrawer(notes)


        }
    }
}



@Composable
fun MyDrawer(notes: MutableList<Note>) {
    val selectedItem = remember { mutableStateOf(notes[0]) }
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerContent = {
            Column {
                notes.forEach { note ->
                    TextButton(
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                            selectedItem.value = note
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.LightGray,
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(text = note.name, fontSize = 22.sp)
                    }
                }
            }
        },
        scrimColor = Color.DarkGray,
        content = {
            Column (modifier = Modifier.fillMaxSize().padding(top = 70.dp)) {
                IconButton(onClick = {
                    scope.launch { drawerState.open() }
                },
                    content = {
                        Icon(Icons.Filled.Menu, contentDescription = "Меню")
                    })
                Column (modifier = Modifier.fillMaxHeight(0.9f)){
                    Text(text = selectedItem.value.name, fontSize = 24.sp)
                    Text(text = selectedItem.value.noteText, fontSize = 24.sp)
                }
                Row (modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End,) {

                    CreateFAB(context = LocalContext.current)
                }
            }
        }
    )
}

@Composable
fun CreateFAB(context: Context){

    FloatingActionButton( onClick = {
        val activity = context as? Activity
        val intent = Intent(activity, AddNoteActivity::class.java)
        context.startActivity(intent)
    }) {
        Icon(Icons.Filled.Add, contentDescription = "AddNote")
    }
}
