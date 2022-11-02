package uk.co.speednumbers.ui.test

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibm.icu.text.RuleBasedNumberFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.co.speednumbers.ui.test.AnswerState.*
import uk.co.speednumbers.ui.test.screen.TestScreens
import uk.co.speednumbers.util.AnswerMatcher
import uk.co.speednumbers.util.translateNum
import java.util.*
import javax.inject.Inject

@ExperimentalComposeUiApi
@HiltViewModel
class TestViewModel @Inject constructor(
    val prefs: SharedPreferences,
    val soundPool: SoundPool,
    val soundId: Int
) : ViewModel() {

    var currentFormat = mutableStateOf(TestScreens.WriteScreen.route)
    var minimum = mutableStateOf(prefs.getString(MINIMUM_KEY, "0")!!)
    var maximum = mutableStateOf(prefs.getString(MAXIMUM_KEY, "10")!!)
    var numberToTranslate = mutableStateOf(0L)
    var usersInput = mutableStateOf("")
    var answer = mutableStateOf("")
    var answerState = mutableStateOf(ANSWERING)
    var languageSelected = mutableStateOf(Locale(prefs.getString(LANGUAGE_KEY, "en")!!))
    var streak = mutableStateOf(0)
    var shuffle = mutableStateOf(true)
    var randomSequence = mutableStateOf(listOf<Long>())
    var options = mutableStateOf(listOf<String>())
    var showSpeaker = mutableStateOf(false)
    var soundEffects = mutableStateOf(prefs.getBoolean(SOUND_EFFECTS_KEY, true))

    private var numberSeenAlready = mutableListOf<Long>()
    var speaker: TextToSpeech? = null

    init {
        if (maximum.value.toLong() < minimum.value.toLong()) {
            setMaximum("10")
            setMinimum("0")
        }
        nextNumber(null)
    }

    fun inValidMaxAndMin() = (minimum.value.isEmpty() || maximum.value.isEmpty()) ||
            (minimum.value.toLong() > maximum.value.toLong())

    fun invalidMin() = (minimum.value.isEmpty() || minimum.value.toLong() > maximum.value.toLongOrNull() ?: 0)
    fun invalidMax() = (maximum.value.isEmpty() || maximum.value.toLong() < minimum.value.toLongOrNull() ?: 0)

    fun setNewLanguage(language: Locale) {
        languageSelected.value = language
        speaker?.language = language
        prefs.edit().putString(LANGUAGE_KEY, language.language).apply()
        showSpeaker.value =
            speaker?.availableLanguages?.find { it.language.contains(languageSelected.value.language) } != null
        if (!inValidMaxAndMin()) {
            nextNumber(null)
        }
    }

    fun setMinimum(value: String) {
        minimum.value = value.filter { it.isDigit() }
        if (minimum.value.isNotEmpty() && maximum.value.isNotEmpty() && minimum.value.toLong() > maximum.value.toLong()) {
            maximum.value = minimum.value
        }
        if (value.isNotEmpty()) {
            prefs.edit().putString(MINIMUM_KEY, value).apply()
            checkIfMatchIsInErrorState()
        }
    }

    private fun checkIfMatchIsInErrorState() {
        if (options.value.size < 4 && getRange() > 4 && currentFormat.value == TestScreens.MatchScreen.route) {
            shuffleNextNumber()
        }
    }

    fun setMaximum(value: String) {
        maximum.value = value.filter { it.isDigit() }
        if (value.isNotEmpty()) {
            checkIfMatchIsInErrorState()
            prefs.edit().putString(MAXIMUM_KEY, value).apply()
        }
    }

    fun answerButtonClick(keyboard: SoftwareKeyboardController?) {
        when (answerState.value) {
            ANSWERING -> markAnswer(keyboard)
            INCORRECT, CLOSE_ANSWER -> nextNumber(keyboard)
        }
    }

    fun nextNumber(keyboard: SoftwareKeyboardController?) {
        usersInput.value = ""
        answerState.value = ANSWERING
        keyboard?.show()

        if (shuffle.value) {
            shuffleNextNumber()
        } else {
            incrementNextNumber()
        }
    }

    private fun incrementNextNumber() {
        generateRandomSequence()
        if (numberToTranslate.value == maximum.value.toLong()) {
            numberToTranslate.value = minimum.value.toLong()
        } else {
            numberToTranslate.value++
        }

        if (!shuffle.value) {
            val toAdd = translateNum(numberToTranslate.value, languageSelected.value)

            val optionsCopy = options.value.toMutableList()
            optionsCopy.remove(toAdd)

            val newOptions = mutableListOf(toAdd)
            newOptions.addAll(optionsCopy.take(3))

            options.value = newOptions.shuffled()
        }
    }

    private fun shuffleNextNumber() {
        animateNumber(generateRandomSequence(), randomSequence.value)
    }

    private fun generateRandomSequence(): Int {
        val total = (maximum.value.toLong() - minimum.value.toLong()) - numberSeenAlready.size
        if (total <= 2L) {
            numberSeenAlready = mutableListOf()
        }
        Log.d("kesD", "generateRandomSequence: total: $total")
        val sequenceSize = (total).coerceAtLeast(1).coerceAtMost(5).toInt()
        randomSequence.value =
            generateSequence { (minimum.value.toLong()..maximum.value.toLong()).random() }
                .distinct()
                .filter { !numberSeenAlready.contains(it) }
                .take(sequenceSize)
                .toList()

        options.value = generateOptions( randomSequence.value[0])
            .map { translateNum(it, languageSelected.value) }
            .shuffled()

        return sequenceSize
    }

    private fun generateOptions(numberAnswer: Long): List<Long> {
        val optionsList = mutableListOf(numberAnswer)
        optionsList.add((numberAnswer + (2..5).random()).coerceAtMost(Long.MAX_VALUE))
        if(numberAnswer == 0L){
            optionsList.add((numberAnswer + (6..10).random()).coerceAtLeast(0))
        }else{
            optionsList.add((numberAnswer - (1..5).random()).coerceAtLeast(0))
        }
        optionsList.add((numberAnswer + 1).coerceAtMost(Long.MAX_VALUE))
        return optionsList
    }

    private fun animateNumber(count: Int, sequence: List<Long>) {
        if (count > 0) {
            numberToTranslate.value = sequence.elementAt(count - 1)
            viewModelScope.launch {
                delay(50)
                animateNumber(count - 1, sequence)
            }
        } else {
            numberSeenAlready.add(numberToTranslate.value)
        }
    }

    fun markAnswer(keyboard: SoftwareKeyboardController?) {
        answer.value = getAnswer().lowercase()
        when (AnswerMatcher.match(answer.value.lowercase(), usersInput.value.lowercase())) {
            100 -> correctAnswer(keyboard)
            in 75..100 -> closeAnswer(keyboard)
            else -> wrongAnswer(keyboard)
        }
    }

    fun markMatchAnswer(){
        answer.value = getAnswer().lowercase()
        when (AnswerMatcher.match(answer.value.lowercase(), usersInput.value.lowercase())) {
            100 -> correctAnswer(null)
            else -> wrongAnswer(null,moveOnAutomatically = true)
        }
    }

    fun markSpokenAnswer() {
        answer.value = getAnswer().lowercase()
        when (AnswerMatcher.match(answer.value.lowercase(), usersInput.value.lowercase())) {
            in 75..100 -> correctAnswer(null)
            else -> wrongAnswer(null)
        }
    }

    private fun wrongAnswer(
        keyboard: SoftwareKeyboardController?,
        moveOnAutomatically: Boolean = false
    ) {
        keyboard?.hide()
        answerState.value = INCORRECT
        streak.value = 0
        if (moveOnAutomatically) {
            nextNumberAfter()
        }
    }

    private fun closeAnswer(keyboard: SoftwareKeyboardController?) {
        answerState.value = CLOSE_ANSWER
        keyboard?.hide()
    }

    private fun correctAnswer(keyboard: SoftwareKeyboardController?) {
        streak.value++
        if (soundEffects.value) {
            soundPool.play(soundId, 0.2f, 0.2f, 1, 0, 1f)
        }
        answerState.value = CORRECT
        viewModelScope.launch {
            delay(250)
            nextNumber(keyboard)
        }
    }

    private fun nextNumberAfter() {
        viewModelScope.launch {
            delay(2000)
            nextNumber(null)
        }
    }

    private fun getAnswer() = translateNum(numberToTranslate.value, languageSelected.value)

    fun setTts(tts: TextToSpeech?) {
        speaker = tts
        speaker?.setSpeechRate(0.8f)
        speaker?.language = languageSelected.value
        showSpeaker.value =
            speaker?.availableLanguages?.find { it.language.contains(languageSelected.value.language) } != null
    }

    fun sayWords(words: String) = speaker?.speak(words, TextToSpeech.QUEUE_FLUSH, null, words)

    fun setSoundEffects(enabled: Boolean = !soundEffects.value) {
        soundEffects.value = enabled
        prefs.edit().putBoolean(SOUND_EFFECTS_KEY, enabled).apply()
    }

    private fun getRange(): Long =
        ((maximum.value.toLongOrNull() ?: 0) - (minimum.value.toLongOrNull() ?: 0))

    companion object {
        const val LANGUAGE_KEY = "languageKey"
        const val MINIMUM_KEY = "Minimum"
        const val MAXIMUM_KEY = "Maximum"
        const val SOUND_EFFECTS_KEY = "SoundEffects"
        const val SELECTED_LANGUAGE_YET_KEY = "SelectedLangYet"
    }

}