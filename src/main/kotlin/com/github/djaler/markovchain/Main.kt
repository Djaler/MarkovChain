package com.github.djaler.markovchain

import java.io.File

/**
 * @author Kirill Romanov
 */
fun main(args: Array<String>) {
    val text = File("/home/djaler/Downloads/test.txt").readText()

    val sentences = textToSentences(text)

    val markov = Markov()
    markov.fillFromText(sentences)

    println(markov.generateSentence())
}
