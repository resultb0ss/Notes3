package com.example.notes3

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class AddNoteActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(topBar = {
                GetTopBar(context = LocalContext.current)
            }, content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    AddNoteFields()
                }
            })

        }
    }
}


@Composable
fun AddNoteFields(context: Context = LocalContext.current) {

    val noteTitle = remember { mutableStateOf("") }
    val noteText = remember { mutableStateOf("") }
    val titleValue = remember { mutableStateOf("") }
    val noteTextValue = remember { mutableStateOf("") }

    Column {

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(text = "Напишите что-нибудь: ", fontSize = 22.sp)
        }

        Spacer(modifier = Modifier.padding(10.dp))

        OutlinedTextField(
            value = titleValue.value,
            onValueChange = { newValue -> {
                noteTitle.value = newValue
                Log.d("@@@", "newTitle = $newValue")
            }
                            },
            label = { Text("Заголовок") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(10.dp))

        OutlinedTextField(
            value = noteTextValue.value, onValueChange = { newValue -> {
                noteText.value = newValue
                Log.d("@@@", "newTextValue = $newValue")
            }},
            label = { Text("Основное содержимое") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                if ((noteTitle.value.isEmpty()) || (noteText.value.isEmpty())){
                    Toast.makeText(context, "Заполните необходимые поля", Toast.LENGTH_LONG).show()
                } else {
                    val intent = Intent(context as? Activity, MainActivity::class.java)
                    intent.putExtra("noteTitle", noteTitle.value)
                    intent.putExtra("noteText", noteText.value)
                    context.startActivity(intent)
                }
            }) { Text(text = "Сохранить") }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetTopBar(context: Context) {
    TopAppBar(title = { Text(text = "Заметки", fontSize = 22.sp) },
        actions = {
            IconButton(onClick = {

                val intent = Intent(context as? Activity, MainActivity::class.java)
                context.startActivity(intent)

            }) { Icon(Icons.Filled.ArrowBack, contentDescription = "Вернуться") }
        })
}

fun saveNote() {

}

