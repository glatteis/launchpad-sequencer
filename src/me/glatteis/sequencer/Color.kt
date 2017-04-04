package me.glatteis.sequencer

/**
 * Created by Linus on 02.04.2017!
 */
data class Color(val red: Int, val green: Int) {

    init {
        if (red > 3 || green > 3) throw RuntimeException("Red and green values have to be smaller than three.")
    }

    fun asByte(): Int {
        return red + green * 16
    }

}