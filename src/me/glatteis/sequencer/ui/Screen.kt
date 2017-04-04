package me.glatteis.sequencer.ui

/**
 * Created by Linus on 02.04.2017!
 */
interface Screen {

    fun drawFull()
    fun drawUpdate()
    fun inputOn(x: Int, y: Int)
    fun inputOff(x: Int, y: Int)
    fun inputBar(x: Int)
}