package com.github.djaler.markovchain

import java.io.File

/**
 * @author Kirill Romanov
 */
fun main(args: Array<String>) {
    val markov = Markov()
    markov.fillFromText(fileToSentences(File("/home/djaler/Downloads/test.txt")))

    println(markov.generateSentence())
}
