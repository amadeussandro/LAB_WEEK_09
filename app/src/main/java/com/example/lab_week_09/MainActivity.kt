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

// RUNTIME
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType

// ELEMENTS (Part 3)
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton
import com.example.lab_week_09.ui.theme.OnBackgroundItemText

// NAVIGATION
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType

// MOSHI
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

// THEME
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme


// --------------------------------------------------
// MAIN ACTIVITY
// --------------------------------------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LAB_WEEK_09Theme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    App(navController)
                }
            }
        }
    }
}


// --------------------------------------------------
// DATA MODEL
// --------------------------------------------------
data class Student(
    var name: String
)


// --------------------------------------------------
// ROOT: App Navigation
// --------------------------------------------------
@Composable
fun App(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // HOME PAGE
        composable("home") {
            Home { json ->
                navController.navigate("resultContent/?listData=$json")
            }
        }

        // RESULT PAGE
        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") { type = NavType.StringType })
        ) { backStack ->

            ResultContent(
                listData = backStack.arguments?.getString("listData").orEmpty()
            )
        }
    }
}


// --------------------------------------------------
// HOME (Parent)
// --------------------------------------------------
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

    var inputField by remember { mutableStateOf(Student("")) }

    HomeContent(
        listData = listData,
        inputField = inputField,
        onInputValueChange = { input ->
            inputField = inputField.copy(name = input)
        },
        onButtonClick = {
            if (inputField.name.isNotBlank()) {      // FIX: prevent empty submit
                listData.add(inputField)
                inputField = Student("")
            }
        },
        navigateFromHomeToResult = {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val type = Types.newParameterizedType(List::class.java, Student::class.java)
            val adapter = moshi.adapter<List<Student>>(type)
            val json = adapter.toJson(listData.toList())

            navigateFromHomeToResult(json)
        }
    )
}


// --------------------------------------------------
// HOME CONTENT (Child)
// --------------------------------------------------
@Composable
fun HomeContent(
    listData: List<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit
) {

    LazyColumn {

        item {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OnBackgroundTitleText(text = stringResource(id = R.string.enter_item))

                TextField(
                    value = inputField.name,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    onValueChange = onInputValueChange
                )

                Row {
                    PrimaryTextButton(
                        text = stringResource(id = R.string.button_click)
                    ) { onButtonClick() }

                    PrimaryTextButton(
                        text = stringResource(id = R.string.button_navigate)
                    ) { navigateFromHomeToResult() }
                }
            }
        }

        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}


// --------------------------------------------------
// RESULT CONTENT (JSON → LIST)
// --------------------------------------------------
@Composable
fun ResultContent(listData: String) {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val type = Types.newParameterizedType(List::class.java, Student::class.java)
    val adapter = moshi.adapter<List<Student>>(type)

    val dataList = adapter.fromJson(listData) ?: emptyList()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(dataList) { item ->
            OnBackgroundItemText(text = item.name)
        }
    }
}


// --------------------------------------------------
// PREVIEW
// --------------------------------------------------
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home {}
}
