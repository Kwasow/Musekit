package com.kwasow.musekit.extensions

fun <T> Collection<T>.mostCommon(): T =
    this
        .groupingBy { it }
        .eachCount()
        .maxBy { it.value }
        .key
