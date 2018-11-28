package com.github.djaler.markovchain

/**
 * @author Kirill Romanov
 */
class ProbabilityMatrix {
    private val matrix: MutableMap<Prefix, ProbabilityVector> = mutableMapOf()

    fun add(prefix: Prefix, suffix: Suffix) {
        matrix.getOrPut(prefix) { ProbabilityVector() }.add(suffix)
    }

    fun getProbabilitiesFor(prefix: Prefix): Map<Suffix, Probability> {
        return matrix[prefix]?.calculateProbabilities() ?: emptyMap()
    }
}

class ProbabilityVector {
    private val counts: MutableMap<Suffix, Int> = mutableMapOf()

    fun add(suffix: Suffix) {
        counts.merge(suffix, 1, Int::plus)
    }

    fun calculateProbabilities(): Map<Suffix, Probability> {
        val sum = counts.map { it.value }.sum()

        return counts.mapValues { Probability(it.value.toFloat() / sum) }
    }
}

inline class Probability(val value: Float)

sealed class Prefix
data class StringPrefix(val strings: List<String?>) : Prefix() {
    constructor(string: String) : this(listOf(string))
}

object StartPrefix : Prefix()

sealed class Suffix
data class StringSuffix(val value: String) : Suffix()

object EndSuffix : Suffix()
