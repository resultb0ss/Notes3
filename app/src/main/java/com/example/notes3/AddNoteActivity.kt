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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

class AddNoteActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }

            Scaffold(topBar = {
                GetTopBar(context = LocalContext.current)
            },
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center
                ) {
                    AddNoteFields(snackbarHostState = snackbarHostState)
                }
            }

        }
    }
}


@Composable
fun AddNoteFields(context: Context = LocalContext.current, snackbarHostState: SnackbarHostState) {

    val noteTitle = remember { mutableStateOf("") }
    val noteText = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()



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
            value = noteTitle.value,

            onValueChange = { newValue -> noteTitle.value = newValue },
            label = { Text("Заголовок") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.padding(10.dp))

        OutlinedTextField(
            value = noteText.value, onValueChange = { newValue -> noteText.value = newValue },
            label = { Text("Основное содержимое") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                if ((noteTitle.value.isEmpty()) || (noteText.value.isEmpty())) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Заполните необходимые поля",
                            actionLabel = "Ok",
                            duration = SnackbarDuration.Short
                        )
                    }
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
