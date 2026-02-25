package com.example.spaceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel
import coil.compose.AsyncImage
import com.example.spaceapp.model.SpaceResult
import com.example.spaceapp.ui.theme.SpaceAppTheme
import com.example.spaceapp.viewmodel.SpaceState
import com.example.spaceapp.viewmodel.SpaceViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpaceAppTheme {
                // 1. Create the NavController here at the top level
                val navController = rememberNavController()
                val spaceViewModel: SpaceViewModel = koinViewModel()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // 2. Set up the NavHost with your two routes
                    NavHost(
                        navController = navController,
                        startDestination = "space_list",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("space_list") {
                            SpaceScreen(
                                viewModel = spaceViewModel,
                                navController = navController
                            )
                        }
                        // 3. Detail route accepts an `id` argument in the path
                        composable("space_detail/{spaceId}") { backStackEntry ->
                            val spaceId = backStackEntry.arguments?.getString("spaceId")
                            SpaceDetailScreen(
                                spaceId = spaceId,
                                viewModel = spaceViewModel,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun SpaceScreen(viewModel: SpaceViewModel, navController: NavController, modifier: Modifier = Modifier) {
    val state by viewModel.spaceState.observeAsState(SpaceState.Loading)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            is SpaceState.Error -> {}
            SpaceState.Loading -> CircularProgressIndicator()
            is SpaceState.Success -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items((state as SpaceState.Success).spaces) { space ->
                        // 4. Pass navController into each item
                        SpaceItems(space = space, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun SpaceItems(space: SpaceResult,navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable{
                println(space.id)
                // 5. Navigate to the detail screen, passing the id
                navController.navigate("space_detail/${space.id}")
            },
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(end = 10.dp)) {
                AsyncImage(
                    model = space.image_url,
                    contentDescription = space.summary,
                    modifier = Modifier.size(100.dp)
                )
            }
            Column() {
                Row() {
                    Text(
                        text = "Article ${space.id}",
                        fontWeight = FontWeight(800)
                    )
                }
                Row() {
                    Text(
                        text = space.title
                    )
                }

            }
        }
    }
}

@Composable
fun SpaceDetailScreen(
    spaceId: String?,
    viewModel: SpaceViewModel,
    navController: NavController
) {
    val state by viewModel.spaceState.observeAsState(SpaceState.Loading)

    // Find the matching item from the already-loaded list
    val space = (state as? SpaceState.Success)?.spaces?.find { it.id.toString() == spaceId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = { navController.popBackStack() }) {
            Text("Back")
        }

        if (space != null) {
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = space.image_url,
                contentDescription = space.summary,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = space.title, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = space.summary ?: "No summary available.")
        } else {
            Text("Loading detail...")
        }
    }
}