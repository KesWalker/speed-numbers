package uk.co.speednumbers.util

import android.util.Log
import kotlin.math.roundToInt

class AnswerMatcher {

    companion object {

        fun match(string1: String, string2: String) = if (string1 != string2) {
            calcMatch(string1, string2).roundToInt()
        } else {
            100
        }

        private fun calcMatch(string1: String, string2: String): Double {
            var string1 = string1
            var string2 = string2
            var maxLength = Math.max(string1.length, string2.length).toDouble()
            var res = 0.0
            val dirMatchPoints = 100.0 / maxLength
            val containsPoints = dirMatchPoints * 0.25
            val wrongPositionPoints = dirMatchPoints * 0.5
            res -= dirMatchPoints * 0.5 * Math.abs(string1.length - string2.length).toDouble()
            if (string1.length > string2.length) {
                string2 = padString(string2, maxLength)
            } else if (string1.length < string2.length) {
                string1 = padString(string1, maxLength)
            }
            val charArray1 = string1.toCharArray()
            val charArray2 = string2.toCharArray()
            maxLength = charArray1.size.toDouble()
            string1 = String(charArray1)
            string2 = String(charArray2)
            var x = 0
            while (x < maxLength) {
                if (charArray1[x] == charArray2[x]) {
                    res += dirMatchPoints
                } else {
                    var indexOfChar = if (x.toDouble() == maxLength - 1.0) x - 1 else x
                    if (string1.contains(string2[x].toString())) {
                        indexOfChar = if (indexOfChar == 0) 1 else indexOfChar
                        for (y in indexOfChar - 1..indexOfChar + 1) {
                            if (string1[y] == string2[x]) {
                                res += wrongPositionPoints
                                break
                            }
                        }
                        res += containsPoints
                    }
                }
                ++x
            }
            res = if (res < 0.0) 0.0 else res
            return res
        }

        private fun padString(word: String, maxLength: Double): String {
            val sb = StringBuilder(word)
            while (sb.length < maxLength) {
                sb.append("#")
            }
            return sb.toString()
        }
    }
}