package com.github.djaler.markovchain

import kotlin.random.Random

/**
 * @author Kirill Romanov
 */
class Markov {
    private val probabilityMatrix = ProbabilityMatrix()
    private val prefixMaxSize = 2

    fun fillFromText(text: List<Sentence>) {
        for (sentence in text) {
            val tokens = sentence.tokens.withPrependedAndAppended(StartToken, EndToken)

            for (suffixIndex in 1 until tokens.size) {
                for (prefixSize in 1..prefixMaxSize) {
                    val prefixStartIndex = suffixIndex - prefixSize
                    if (prefixStartIndex >= 0) {
                        probabilityMatrix.add(
                            Prefix(tokens.subList(prefixStartIndex, suffixIndex)),
                            Suffix(tokens[suffixIndex])
                        )
                    }
                }
            }
        }
    }

    fun generateSentence(): String {
        val stringBuilder = StringBuilder()

        val previousTokens = DisplacingList<Token>(prefixMaxSize).apply {
            add(StartToken)
        }

        while (true) {
            when (val currentToken = getNextTokenFor(previousTokens)) {
                is StringToken -> {
                    val lastToken = previousTokens.last()
                    if (lastToken != StartToken && currentToken.string != ",") {
                        stringBuilder.append(' ')
                    }
                    stringBuilder.append(currentToken.string)
                    previousTokens.add(currentToken)
                }
                is EndToken -> {
                    stringBuilder.append('.')
                    return stringBuilder.toString().capitalize()
                }
            }
        }
    }

    private fun getNextTokenFor(previousTokens: List<Token>): Token {
        for (prefixSize in prefixMaxSize downTo 1) {
            val prefix = Prefix(previousTokens.takeLast(prefixSize))
            return getNextSuffixFor(prefix)?.token ?: continue
        }

        return EndToken
    }

    private fun getNextSuffixFor(prefix: Prefix): Suffix? {
        val probabilities = probabilityMatrix.getProbabilitiesFor(prefix)

        val randomValue = Random.nextFloat()

        var probabilityAccumulation = 0f

        for ((suffix, probability) in probabilities.toList().shuffled()) {
            probabilityAccumulation += probability.value

            if (randomValue <= probabilityAccumulation) {
                return suffix
            }
        }

        return null
    }
}

fun <T> List<T>.withPrependedAndAppended(prepended: T, appended: T): List<T> {
    return listOf(prepended) + this + listOf(appended)
}

sealed class Token

object StartToken : Token()
data class StringToken(val string: String) : Token()
object EndToken : Token()
