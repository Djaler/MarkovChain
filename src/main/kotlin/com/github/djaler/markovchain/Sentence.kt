package com.github.djaler.markovchain

/**
 * @author Kirill Romanov
 */
fun textToSentences(text: String): List<Sentence> {
    return text.split(Regex("""[.?!]"""))
        .asSequence()
        .filterNot { it.isEmpty() }
        .map { it.toSentence() }
        .filterNot { it.isEmpty() }
        .toList()
}

private fun String.toSentence(): Sentence {
    return this.trim()
        .split(Regex("\\b(?=,)|\\b(?=-)|\\s"))
//        .split(Regex("""[,\-"\s]"""))
        .filterNot { it.isEmpty() }
        .map { it.toLowerCase() }
        .let { Sentence(it.toTokens().withPrependedAndAppended(StartToken, EndToken)) }
}

private fun <T> List<T>.withPrependedAndAppended(prepended: T, appended: T): List<T> {
    return listOf(prepended) + this + listOf(appended)
}

private fun List<String>.toTokens(): List<Token> {
    return this.map { StringToken(it) }
}

inline class Sentence(val tokens: List<Token>) {
    fun isEmpty(): Boolean {
        return tokens.isEmpty()
    }
}

sealed class Token

object StartToken : Token()
data class StringToken(val string: String) : Token()
object EndToken : Token()
