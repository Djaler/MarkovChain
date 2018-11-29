package com.github.djaler.markovchain

import java.io.File

/**
 * @author Kirill Romanov
 */
private val SENTENCES_DELIMITERS = arrayOf(".", "?", "!", "\n", "(", ")", ":", ";")
private val WORDS_DELIMITERS = arrayOf(" ", "\t", "\"")
private val MEANINGFUL_DELIMITERS = arrayOf(",", "—")
private val MEANINGFUL_DELIMITERS_SPLIT_REGEX = Regex(MEANINGFUL_DELIMITERS.joinToString("|") { "\\b(?=$it)" })

fun fileToSentences(file: File): List<Sentence> {
    return textToSentences(file.readText())
}

fun textToSentences(text: String): List<Sentence> {
    return text.split(*SENTENCES_DELIMITERS)
        .asSequence()
        .filter { it.isNotBlank() }
        .map { it.toSentence() }
        .filter { it.tokens.size > 1 }
        .toList()
}

private fun String.toSentence(): Sentence {
    return this.trim()
        .split(*WORDS_DELIMITERS)
        .flatMap { it.split(MEANINGFUL_DELIMITERS_SPLIT_REGEX) }
        .filterNot { it.isEmpty() }
        .map { it.toLowerCase() }
        .map { StringToken(it) }
        .let { Sentence(it) }
}

inline class Sentence(val tokens: List<StringToken>)
