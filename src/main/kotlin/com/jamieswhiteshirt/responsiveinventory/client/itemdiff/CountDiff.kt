package com.jamieswhiteshirt.responsiveinventory.client.itemdiff

data class CountDiff(val increased: Int, val decreased: Int) {
    operator fun plus(other: CountDiff) = CountDiff(this.increased + other.increased, this.decreased + other.decreased)
}
