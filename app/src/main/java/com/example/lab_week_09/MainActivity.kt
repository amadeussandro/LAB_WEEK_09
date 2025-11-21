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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField

// UI
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType

// IMPORT ELEMENTS (Part 3)
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton
import com.example.lab_week_09.ui.theme.OnBackgroundItemText

// NAVIGATION
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType

// THEME
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LAB_WEEK_09Theme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ROOT → App()
                    val navController = rememberNavController()
                    App(navController = navController)
                }
            }
        }
    }
}

// -------------------------------
// DATA MODEL
// -------------------------------
data class Student(
    var name: String
)

// -------------------------------
// ROOT APP WITH NAVIGATION
// -------------------------------
@Composable
fun App(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // HOME SCREEN
        composable("home") {
            Home { listString ->
                navController.navigate("resultContent/?listData=$listString")
            }
        }

        // RESULT SCREEN
        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType
            })
        ) {
            ResultContent(
                it.arguments?.getString("listData").orEmpty()
            )
        }
    }
}

// -------------------------------
// PARENT COMPOSABLE — Home()
// -------------------------------
@Composable
fun Home(
    navigateFromHomeToResult: (String) -> Unit
) {

    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }

    var inputField by remember {
        mutableStateOf(Student(""))
    }

    HomeContent(
        listData = listData,
        inputField = inputField,
        onInputValueChange = { input ->
            inputField = inputField.copy(name = input)
        },
        onButtonClick = {
            if (inputField.name.isNotBlank()) {
                listData.add(inputField)
                inputField = Student("")
            }
        },
        navigateFromHomeToResult = {
            navigateFromHomeToResult(listData.toList().toString())
        }
    )
}

// -------------------------------
// CHILD COMPOSABLE — HomeContent()
// -------------------------------
@Composable
fun HomeContent(
    listData: List<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit
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

                OnBackgroundTitleText(
                    text = stringResource(id = R.string.enter_item)
                )

                TextField(
                    value = inputField.name,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = { onInputValueChange(it) }
                )

                Row {
                    PrimaryTextButton(
                        text = stringResource(id = R.string.button_click)
                    ) {
                        onButtonClick()
                    }

                    PrimaryTextButton(
                        text = stringResource(id = R.string.button_navigate)
                    ) {
                        navigateFromHomeToResult()
                    }
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
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}

// -------------------------------
// RESULT SCREEN
// -------------------------------
@Composable
fun ResultContent(listData: String) {
    Column(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnBackgroundItemText(text = listData)
    }
}


// PREVIEW
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home {}
}
