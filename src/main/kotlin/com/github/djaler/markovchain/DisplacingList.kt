package com.github.djaler.markovchain

import java.util.*

/**
 * @author Kirill Romanov
 */
class DisplacingList<T> private constructor(private val maxSize: Int, private val list: LinkedList<T>) :
    List<T> by list {
    constructor(maxSize: Int) : this(maxSize, LinkedList())

    fun add(item: T) {
        list.add(item)

        if (list.size > maxSize) {
            list.remove()
        }
    }
}
