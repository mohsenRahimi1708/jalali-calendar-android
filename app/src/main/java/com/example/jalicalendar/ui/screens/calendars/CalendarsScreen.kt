package com.example.jalicalendar.ui.screens.calendars

import android.app.PendingIntent
import androidx.activity.result.IntentSenderRequest
import com.example.jalicalendar.auth.AuthManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.jalicalendar.domain.model.Account
import com.example.jalicalendar.domain.model.Calendar
import com.example.jalicalendar.domain.repo.AccountRepository
import com.example.jalicalendar.domain.repo.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarsViewModel @Inject constructor(
    private val calendarRepo: CalendarRepository,
    accountRepo: AccountRepository,
    private val authManager: AuthManager,
    private val statusStore: com.example.jalicalendar.sync.SyncStatusStore,
    @ApplicationContext private val context: android.content.Context,
) : ViewModel() {
    val calendars = calendarRepo.observeCalendars()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val accounts = accountRepo.observeAccounts()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val signedIn: StateFlow<Boolean> = authManager.signedInFlow()
        .stateIn(viewModelScope, SharingStarted.Eagerly, authManager.signedIn)

    /** Exposes the pending auth PendingIntent to the screen's launcher. */
    private val _authIntent = MutableStateFlow<PendingIntent?>(null)
    val authIntent: StateFlow<PendingIntent?> = _authIntent

    fun toggle(calendar: Calendar) {
        viewModelScope.launch { calendarRepo.setSelected(calendar.id, !calendar.selected) }
    }

    fun signIn(launch: (IntentSenderRequest) -> Unit) {
        viewModelScope.launch {
            if (!authManager.isConfigured()) return@launch
            val pi = authManager.beginAuthorization()
            _authIntent.value = pi
            launch(IntentSenderRequest.Builder(pi).build())
        }
    }

    fun onAuthResult(data: android.content.Intent?) {
        viewModelScope.launch {
            authManager.handleAuthResponse(data)
            if (authManager.signedIn) {
                com.example.jalicalendar.sync.SyncWorker.requestOneShot(context)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authManager.signOut()
        }
    }

    fun syncNow() {
        com.example.jalicalendar.sync.SyncWorker.requestOneShot(context, expedited = true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarsScreen(navController: NavHostController) {
    val viewModel: CalendarsViewModel = hiltViewModel()
    val calendars by viewModel.calendars.collectAsStateWithLifecycle()
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val strings = com.example.jalicalendar.ui.LocalStrings.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.calendars) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.cancel)
                    }
                },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        ) {
            // Account status + auth actions
            val signedIn by viewModel.signedIn.collectAsStateWithLifecycle()
            val authLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.StartIntentSenderForResult(),
            ) { result -> viewModel.onAuthResult(result.data) }

            Text(
                text = accounts.firstOrNull()?.let { it.displayName ?: it.email }
                    ?: strings.noAccountYet,
                style = MaterialTheme.typography.titleMedium,
            )
            if (!signedIn) {
                Text(
                    text = strings.signInToSync,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                Button(
                    onClick = { viewModel.signIn { authLauncher.launch(it) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                ) { Text(strings.signIn) }
            } else {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(onClick = { viewModel.syncNow() }) { Text(strings.syncNow) }
                    OutlinedButton(onClick = { viewModel.signOut() }) { Text(strings.signOut) }
                }
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Text(
                text = strings.calendarsSubtitle,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
            )

            calendars.forEach { calendar ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .semantics {
                            contentDescription = "${strings.showCalendar}: ${calendar.title}"
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    androidx.compose.foundation.layout.Box(
                        Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Color(calendar.colorArgb)),
                    )
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                    ) {
                        Text(calendar.title, style = MaterialTheme.typography.bodyLarge)
                        if (calendar.id.startsWith(Calendar.LOCAL_ID_PREFIX)) {
                            Text(
                                text = strings.localCalendar,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    Switch(
                        checked = calendar.selected,
                        onCheckedChange = { viewModel.toggle(calendar) },
                    )
                }
            }
        }
    }
}
