package uk.co.speednumbers.ui.languages

import android.content.SharedPreferences
import android.util.Log
import android.view.SoundEffectConstants
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibm.icu.text.RuleBasedNumberFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uk.co.speednumbers.util.translateNum
import java.util.*
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor() : ViewModel() {

    val defaultLocale = Locale.getDefault()
    val popularLangs =
        listOf("en", "es", "fr", "de", "zh", "ar", "hi").filter { defaultLocale.language != it }
    val languages = mutableStateOf(listOf<Locale>())

    init {
        getLanguages()
    }

    fun getLanguages() {
        if (languages.value.isEmpty()) {
            viewModelScope.launch {
                val threadPool = Executors.newCachedThreadPool().asCoroutineDispatcher()
                withContext(threadPool){
                    val defaultTranslation = translateNum(293, defaultLocale)
                    languages.value = RuleBasedNumberFormat.getAvailableLocales()
                        .distinctBy { it.language }
                        .filter { translateNum(293, it) != defaultTranslation }
                        .sortedBy { it.displayLanguage }
                        .sortedByDescending { popularLangs.contains(it.language) }
                }
            }
        }
    }

}