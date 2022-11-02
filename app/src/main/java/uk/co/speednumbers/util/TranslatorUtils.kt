package uk.co.speednumbers.util

import com.ibm.icu.text.RuleBasedNumberFormat
import java.util.*

fun translateNum(number: Long, language: Locale) =
    RuleBasedNumberFormat(language, RuleBasedNumberFormat.SPELLOUT).format(number)