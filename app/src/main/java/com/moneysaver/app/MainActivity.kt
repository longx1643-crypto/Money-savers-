package com.moneysaver.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Saving(val amount: Double, val note: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MoneySaverApp() }
    }
}

@Composable
fun MoneySaverApp() {
    var goal by remember { mutableStateOf(1000.0) }
    var saved by remember { mutableStateOf(0.0) }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    val history = remember { mutableStateListOf<Saving>() }
    var tab by remember { mutableStateOf(0) }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    listOf("Home", "Add", "History").forEachIndexed { i, n ->
                        NavigationBarItem(
                            selected = tab == i,
                            onClick = { tab = i },
                            icon = {},
                            label = { Text(n) }
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize()
            ) {
                Text(
                    "Money Saver",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(Modifier.height(16.dp))

                when (tab) {
                    0 -> {
                        Text("Total Saved")
                        Text(
                            "$${"%.2f".format(saved)}",
                            style = MaterialTheme.typography.displaySmall
                        )

                        Spacer(Modifier.height(20.dp))

                        Text("Goal: $${"%.2f".format(goal)}")

                        Spacer(Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = {
                                (saved / goal)
                                    .coerceIn(0.0, 1.0)
                                    .toFloat()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "${((saved / goal) * 100)
                                .coerceIn(0.0, 100.0)
                                .toInt()}% completed"
                        )
                    }

                    1 -> {
                        Text(
                            "Add Saving",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount (USD)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(8.dp))

                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            label = { Text("Note") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                amount.toDoubleOrNull()?.let { v ->
                                    if (v > 0) {
                                        saved += v
                                        history.add(Saving(v, note))
                                        amount = ""
                                        note = ""
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Save")
                        }
                    }

                    2 -> {
                        Text(
                            "History",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Spacer(Modifier.height(10.dp))

                        LazyColumn {
                            items(history.reversed()) { s ->
                                Text(
                                    "+ $${"%.2f".format(s.amount)}  ${s.note}",
                                    Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
