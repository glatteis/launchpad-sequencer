package me.glatteis.sequencer.sequencing

import me.glatteis.sequencer.sequencing.Sequence

/**
 * Created by Linus on 02.04.2017!
 */
abstract class SequenceHandler {

    var paused = false

    abstract val sequences: Array<Sequence>

    abstract fun step()

}