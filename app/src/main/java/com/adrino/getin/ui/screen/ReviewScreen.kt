package com.adrino.getin.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.adrino.getin.data.model.Customer
import com.adrino.getin.data.model.Event
import com.adrino.getin.data.model.Slot
import com.adrino.getin.ui.component.EventDetailTopBar
import com.adrino.getin.util.MOBILE_NUMBER_LENGTH
import com.adrino.getin.util.formatTimeToAMPM

@Preview
@Composable
fun Item() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            color = MaterialTheme.colorScheme.onSurface,
            strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Text(
            text = "Book Now",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier,
    event: Event,
    slot: Slot,
    onBackClick: () -> Unit,
    onBookNowClick: (Customer) -> Unit,
    isBooking: Boolean = false
) {
    var userName by rememberSaveable { mutableStateOf("") }
    var userPhoneNumber by rememberSaveable { mutableStateOf("") }

    val isFormValid = userName.isNotBlank() && userPhoneNumber.isNotBlank() &&
            userPhoneNumber.isDigitsOnly() && userPhoneNumber.length == MOBILE_NUMBER_LENGTH

    BackHandler(enabled = isBooking) {
        // Disabled back press
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EventDetailTopBar(onBackClick = onBackClick)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Review the details header
            Text(
                text = "Review the details",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Event Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Event Details",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Event: ${event.eventName.orEmpty()}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Location: ${event.eventLocation.orEmpty()}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Date: ${event.eventDate.orEmpty()}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Slot Details & Timings",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Slot: ${slot.name.orEmpty()}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Start Time: ${formatTimeToAMPM(slot.startTime)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "End Time: ${formatTimeToAMPM(slot.endTime)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "General Instructions",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "• Please arrive 15 minutes before the scheduled time\n" +
                                "• Bring a valid ID for verification\n" +
                                "• Contact us if you need to reschedule or cancel\n" +
                                "• Follow all event guidelines and safety protocols",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "Your Information",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            OutlinedTextField(
                value = userPhoneNumber,
                onValueChange = {  enteredPhoneNumber ->
                    if (enteredPhoneNumber.length <= MOBILE_NUMBER_LENGTH) {
                        userPhoneNumber = enteredPhoneNumber
                    }
                },
                label = { Text("Mobile Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onBookNowClick(Customer(userName, userPhoneNumber)) },
                enabled = isFormValid && !isBooking,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isBooking) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                    Text(
                        text = "Book Now",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

