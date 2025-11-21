package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

// FOUNDATION
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

// MATERIAL 3
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

// TEXT INPUT (Foundation, bukan UI)
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

// UI CORE
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// THEME
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LAB_WEEK_09Theme {

                // Step 13 — Surface & Home(list)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val list = listOf("Tanu", "Tina", "Tono")
                    Home(items = list)
                }
            }
        }
    }
}

//
// STEP 10 → Home() versi awal (tanpa parameter)
//
@Preview(showBackground = true)
@Composable
fun Home() {
    Column {
        Text(
            text = stringResource(id = R.string.list_title)
        )
    }
}

//
// STEP 12 → Home(items) dengan LazyColumn
//
@Composable
fun Home(
    items: List<String>
) {
    LazyColumn {

        // INPUT SECTION
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = stringResource(id = R.string.enter_item))

                TextField(
                    value = "",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = { }
                )

                Button(onClick = { }) {
                    Text(text = stringResource(id = R.string.button_click))
                }
            }
        }

        // LIST SECTION
        items(items) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = item)
            }
        }
    }
}

//
// PREVIEW sUNTUK HOME(items)
//
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home(listOf("Tanu", "Tina", "Tono"))
}
