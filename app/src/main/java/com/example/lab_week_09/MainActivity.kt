package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

// FOUNDATION
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions

// MATERIAL 3
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

// UI
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType

// THEME
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LAB_WEEK_09Theme {

                // STEP 6 — Surface memanggil Home()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home()   // ← tanpa parameter lagi
                }
            }
        }
    }
}

// -------------------------------
// STEP 2 — Data Model Student
// -------------------------------
data class Student(
    var name: String
)

// -------------------------------
// STEP 3 — Home() sebagai Parent
// -------------------------------
@Composable
fun Home() {

    // STATE LIST
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }

    // STATE INPUT
    var inputField = remember {
        mutableStateOf(Student(""))
    }

    // PANGGIL CHILD
    HomeContent(
        listData = listData,
        inputField = inputField.value,
        onInputValueChange = { input ->
            inputField.value = inputField.value.copy(name = input)
        },
        onButtonClick = {
            if (inputField.value.name.isNotBlank()) {
                listData.add(inputField.value)
                inputField.value = Student("")
            }
        }
    )
}

// -------------------------------
// STEP 4 — HomeContent Child
// -------------------------------
@Composable
fun HomeContent(
    listData: List<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit
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
                    value = inputField.name,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = { onInputValueChange(it) }
                )

                Button(onClick = { onButtonClick() }) {
                    Text(text = stringResource(id = R.string.button_click))
                }
            }
        }

        // LIST SECTION
        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = item.name)
            }
        }
    }
}

// PREVIEW
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home()
}
