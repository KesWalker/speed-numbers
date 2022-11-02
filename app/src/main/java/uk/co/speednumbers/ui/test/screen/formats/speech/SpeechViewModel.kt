package uk.co.speednumbers.ui.test.screen.formats.speech

import android.Manifest
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SpeechViewModel : ViewModel() {

    var isListening = mutableStateOf(false)
    var soundWaves = mutableStateOf(0f)
    var isError = mutableStateOf(false)
    var errorMessage = mutableStateOf("Didn't catch that, please try again.")

    fun speechError(reason: Int) {
        errorMessage.value = when(reason){
            2,4 ->  "Internet connection required."
            9 -> "Audio permission required."
            else -> "Didn't catch that, please try again."
        }
        isError.value = true
        isListening.value = false
        viewModelScope.launch {
            delay(5000)
            isError.value = false
        }
    }

}