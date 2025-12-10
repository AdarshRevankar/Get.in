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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.adrino.getin.ui.screen.ConfirmationScreen
import com.adrino.getin.ui.screen.EventDetailScreen
import com.adrino.getin.ui.screen.HomeScreen
import com.adrino.getin.ui.screen.ReviewScreen
import com.adrino.getin.ui.viewmodel.EventViewModel
import com.adrino.getin.ui.viewmodel.SlotViewModel


@Composable
fun GetInNavigation(
    navController: NavHostController
) {
    val eventViewModel: EventViewModel = hiltViewModel()
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
            val slotViewModel: SlotViewModel = hiltViewModel()
            selectedEvent?.let { event ->
                EventDetailScreen(
                    event = event,
                    viewModel = slotViewModel,
                    onBackClick = {
                        eventViewModel.clearSelectedEvent()
                        eventViewModel.clearSelectedSlot()
                        navController.popBackStack(NavRoute.Home.route, inclusive = false)
                    },
                    onSlotClick = { slot ->
                        eventViewModel.setSelectedSlot(slot)
                        val eventId = event.eventId ?: return@EventDetailScreen
                        val slotId = slot.slotId ?: return@EventDetailScreen
                        navController.navigate(
                            NavRoute.Review.createRoute(eventId, slotId)
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
            val isBooking by eventViewModel.isBooking.collectAsState()
            
            val event = selectedEvent
            val slot = selectedSlot
            
            if (event != null && slot != null) {
                val eventId = event.eventId
                val slotId = slot.slotId
                
                if (eventId != null && slotId != null) {
                    ReviewScreen(
                        event = event, slot = slot,
                        onBackClick = {
                            if (!isBooking) {
                                eventViewModel.clearSelectedSlot()
                                navController.popBackStack()
                            }
                        },
                        onBookNowClick = { customer ->
                            eventViewModel.bookSlot(
                                eventId = eventId,
                                slotId = slotId,
                                customer = customer,
                                onSuccess = {
                                    navController.navigate(NavRoute.Confirmation.route) {
                                        popUpTo(NavRoute.Review.route) { inclusive = true }
                                    }
                                },
                                onError = { errorMessage ->
                                    navController.navigate(NavRoute.Confirmation.route) {
                                        popUpTo(NavRoute.Review.route) { inclusive = true }
                                    }
                                }
                            )
                        },
                        isBooking = isBooking
                    )
                }
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