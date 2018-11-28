package com.github.djaler.markovchain

/**
 * @author Kirill Romanov
 */
inline class Sentence(val tokens: List<String>) {
    fun isEmpty(): Boolean {
        return tokens.isEmpty()
    }
}

private fun String.toSentence(): Sentence {
    return this.trim()
//        .split(Regex("\\b(?=,)|\\b(?=-)|\\s"))
        .split(Regex("""[,\-"\s]"""))
        .filterNot { it.isEmpty() }
        .map { it.toLowerCase() }
        .let { Sentence(it) }
}

fun textToSentences(text: String): List<Sentence> {
    return text.split(Regex("""[.?!]"""))
        .asSequence()
        .map { it.toSentence() }
        .filterNot { it.isEmpty() }
        .toList()
}
