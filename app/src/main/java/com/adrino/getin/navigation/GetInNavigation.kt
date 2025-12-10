package com.adrino.getin.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.adrino.getin.data.repository.EventRepository
import com.adrino.getin.ui.screen.ConfirmationScreen
import com.adrino.getin.ui.screen.EventDetailScreen
import com.adrino.getin.ui.screen.HomeScreen
import com.adrino.getin.ui.screen.ReviewScreen
import com.adrino.getin.ui.viewmodel.EventViewModel
import com.adrino.getin.ui.viewmodel.EventViewModelFactory
import com.adrino.getin.ui.viewmodel.SlotViewModel
import com.adrino.getin.ui.viewmodel.SlotViewModelFactory


@Composable
fun GetInNavigation(
    navController: NavHostController,
    repository: EventRepository,
    eventViewModelFactory: EventViewModelFactory
) {
    val eventViewModel = viewModel<EventViewModel>(factory = eventViewModelFactory)
    NavHost(
        navController = navController,
        startDestination = NavRoute.Home.route,
        modifier = Modifier.fillMaxSize()
    ) {
        // Home Screen
        composable(
            route = NavRoute.Home.route,
            enterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            }
        ) {
            HomeScreen(navController, eventViewModel)
        }

        // Event Detail Screen
        composable(
            route = NavRoute.EventDetail.route,
            arguments = listOf(
                navArgument("eventId") { type = NavType.StringType }
            ),
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) +
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
            }
        ) { backStackEntry ->
            val selectedEvent by eventViewModel.selectedEvent.collectAsState()
            val slotViewModel: SlotViewModel = viewModel(
                factory = SlotViewModelFactory(repository)
            )
            selectedEvent?.let {
                EventDetailScreen(
                    event = selectedEvent!!,
                    viewModel = slotViewModel,
                    onBackClick = {
                        eventViewModel.clearSelectedEvent()
                        navController.popBackStack()
                    },
                    onSlotClick = { slot ->
                        eventViewModel.setSelectedSlot(slot)
                        navController.navigate(
                            NavRoute.Review.createRoute(
                                selectedEvent?.eventId ?: "",
                                slot.slotId ?: ""
                            )
                        )
                    }
                )
            }
        }

        // Review Screen
        composable(
            route = NavRoute.Review.route,
            arguments = listOf(
                navArgument("eventId") { type = NavType.StringType },
                navArgument("slotId") { type = NavType.StringType }
            ),
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) +
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
            }
        ) { backStackEntry ->
            val selectedEvent by eventViewModel.selectedEvent.collectAsState()
            val selectedSlot by eventViewModel.selectedSlot.collectAsState()
            
            if (selectedEvent != null && selectedSlot != null) {
                ReviewScreen(
                    event = selectedEvent!!,
                    slot = selectedSlot!!,
                    onBackClick = {
                        eventViewModel.clearSelectedSlot()
                        navController.popBackStack()
                    },
                    onBookNowClick = { userName, userEmail ->
                        navController.navigate(NavRoute.Confirmation.route) {
                            popUpTo(NavRoute.Review.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        // Confirmation Screen
        composable(
            route = NavRoute.Confirmation.route,
            enterTransition = {
                fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300))
            }
        ) {
            ConfirmationScreen(
                onGoToHomeClick = {
                    eventViewModel.clearSelectedEvent()
                    eventViewModel.clearSelectedSlot()
                    navController.popBackStack(NavRoute.Home.route, inclusive = false)
                }
            )
        }
    }
}