package zt.tau.util

fun String.contains(strings: List<String>): Boolean {
    for (string in strings) {
        if (this.contains(string)) return true
    }
    return false
}