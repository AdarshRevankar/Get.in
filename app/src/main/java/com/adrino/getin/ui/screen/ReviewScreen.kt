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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.core.text.isDigitsOnly
import com.adrino.getin.R
import com.adrino.getin.data.model.Customer
import com.adrino.getin.data.model.Event
import com.adrino.getin.data.model.Slot
import com.adrino.getin.ui.component.DetailCard
import com.adrino.getin.ui.component.EventDetailTopBar
import com.adrino.getin.util.CARD_CORNER_RADIUS
import com.adrino.getin.util.DEFAULT_SPACING
import com.adrino.getin.util.MEDIUM_SPACING
import com.adrino.getin.util.MOBILE_NUMBER_LENGTH
import com.adrino.getin.util.SMALL_SPACING
import com.adrino.getin.util.TEXT_FIELD_CORNER_RADIUS
import com.adrino.getin.util.formatTimeToAMPM

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
        // Back press disabled during booking
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
                .padding(DEFAULT_SPACING.dp),
            verticalArrangement = Arrangement.spacedBy(DEFAULT_SPACING.dp)
        ) {
            // Review the details header
            Text(
                text = stringResource(R.string.review_the_details),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Event Details Card
            DetailCard(title = stringResource(R.string.event_details)) {
                Text(
                    text = stringResource(R.string.event, event.eventName.orEmpty()),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(SMALL_SPACING.dp))
                Text(
                    text = stringResource(R.string.location, event.eventLocation.orEmpty()),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(SMALL_SPACING.dp))
                Text(
                    text = stringResource(R.string.date, event.eventDate.orEmpty()),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Slot Details Card
            DetailCard(title = stringResource(R.string.slot_details_timings)) {
                Text(
                    text = stringResource(R.string.slot, slot.name.orEmpty()),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(SMALL_SPACING.dp))
                Text(
                    text = stringResource(R.string.start_time, formatTimeToAMPM(slot.startTime)),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(SMALL_SPACING.dp))
                Text(
                    text = stringResource(R.string.end_time, formatTimeToAMPM(slot.endTime)),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // General Instructions Card
            DetailCard(title = stringResource(R.string.general_instructions)) {
                Text(
                    text = stringResource(R.string.general_instructions_content),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = stringResource(R.string.your_information),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(TEXT_FIELD_CORNER_RADIUS.dp)
            )

            OutlinedTextField(
                value = userPhoneNumber,
                onValueChange = { enteredPhoneNumber ->
                    if (enteredPhoneNumber.length <= MOBILE_NUMBER_LENGTH) {
                        userPhoneNumber = enteredPhoneNumber
                    }
                },
                label = { Text(stringResource(R.string.mobile_number)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(TEXT_FIELD_CORNER_RADIUS.dp)
            )

            Spacer(modifier = Modifier.height(MEDIUM_SPACING.dp))

            Button(
                onClick = { onBookNowClick(Customer(userName, userPhoneNumber)) },
                enabled = isFormValid && !isBooking,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(CARD_CORNER_RADIUS.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isBooking) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                    Text(
                        text = stringResource(R.string.book_now),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(DEFAULT_SPACING.dp))
        }
    }
}

