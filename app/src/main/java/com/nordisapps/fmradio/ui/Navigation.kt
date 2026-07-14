package com.nordisapps.fmradio.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

sealed class Screen(val route: String, val label: String) {
    object Home : Screen("home", "Главная")
    object Favorites : Screen("favorites", "Избранное")
}

private val bottomNavItems = listOf(Screen.Home, Screen.Favorites)
private val tabOrder = listOf(Screen.Home.route, Screen.Favorites.route)

private fun tabIndex(route: String?): Int {
    return tabOrder.indexOf(route).let { if (it == -1) 0 else it }
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    favoriteStations: List<Double>,
    onStationSelected: (Double) -> Unit,
    onFavoriteToggle: (Double) -> Unit,
    homeContent: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { screen ->
                    val selected =
                        currentDestination?.hierarchy?.any { it.route == screen.route } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    Screen.Home -> Icons.Filled.Home
                                    Screen.Favorites -> Icons.Filled.Star
                                },
                                contentDescription = null
                            )
                        },
                        label = { Text(screen.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                val towardsRight =
                    tabIndex(targetState.destination.route) > tabIndex(initialState.destination.route)
                slideIntoContainer(
                    towards = if (towardsRight) AnimatedContentTransitionScope.SlideDirection.Left
                    else AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(250)
                )
            },
            exitTransition = {
                val towardsRight =
                    tabIndex(targetState.destination.route) > tabIndex(initialState.destination.route)
                slideOutOfContainer(
                    towards = if (towardsRight) AnimatedContentTransitionScope.SlideDirection.Left
                    else AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(250)
                )
            }
        ) {
            composable(Screen.Home.route) {
                homeContent()
            }
            composable(Screen.Favorites.route) {
                FavouritesScreen(
                    favoriteStations = favoriteStations,
                    onFavoriteToggle = onFavoriteToggle,
                    onStationSelected = onStationSelected
                )
            }
        }
    }
}

@Composable
fun FavouritesScreen(
    favoriteStations: List<Double>,
    onStationSelected: (Double) -> Unit,
    onFavoriteToggle: (Double) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "Избранные станции",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (favoriteStations.isEmpty()) {
            Text("Пока нет избранных станций. Нажмите на звёздочку рядом со станцией, чтобы добавить.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(favoriteStations) { freq ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { onStationSelected(freq) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "$freq MHz",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                        }
                        IconButton(onClick = { onFavoriteToggle(freq) }) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Убрать из избранного",
                                tint = Color(0xFFFFC107)
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}