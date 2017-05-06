package me.glatteis.sequencer.output

/**
 * Created by Linus on 06.05.2017!
 */
interface Output {

    //Represents a note output

    fun playNote(note: Byte, velocity: Byte, channel: Int = 0)
    fun stopNote(note: Byte, velocity: Byte, channel: Int = 0)

}