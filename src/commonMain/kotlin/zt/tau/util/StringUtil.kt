package zt.tau.util

fun String.contains(vararg strings: String) = strings.any { this in it }