package com.github.djaler.markovchain

import kotlin.random.Random

/**
 * @author Kirill Romanov
 */
class Markov {
    private val probabilityMatrix = ProbabilityMatrix()

    fun fillFromText(text: List<Sentence>) {
        for (sentence in text) {
            val tokens = sentence.tokens

            probabilityMatrix.add(StartPrefix, StringSuffix(tokens.first()))

            for (i in 0 until tokens.size - 1) {
                probabilityMatrix.add(StringPrefix(tokens[i]), StringSuffix(tokens[i + 1]))
            }

            probabilityMatrix.add(StringPrefix(tokens.last()), EndSuffix)
        }
    }

    fun generateSentence(): String {
        val stringBuilder = StringBuilder()

        var prefix: Prefix = StartPrefix

        while (true) {
            when (val suffix = getNextFor(prefix)) {
                is EndSuffix -> {
                    stringBuilder.deleteCharAt(stringBuilder.length - 1)
                    stringBuilder.append('.')
                    return stringBuilder.toString().capitalize()
                }
                is StringSuffix -> {
                    stringBuilder.append(suffix.value + ' ')
                    prefix = StringPrefix(suffix.value)
                }
            }
        }
    }

    private fun getNextFor(prefix: Prefix): Suffix {
        val probabilities = probabilityMatrix.getProbabilitiesFor(prefix).toList().shuffled()

        val randomValue = Random.nextFloat()

        var probabilityAccumulation = 0f

        for ((suffix, probability) in probabilities) {
            probabilityAccumulation += probability.value

            if (randomValue <= probabilityAccumulation) {
                return suffix
            }
        }

        throw Error("Matrix is empty")
    }
}

