package me.glatteis.sequencer

/**
 * Created by Linus on 02.04.2017!
 */
class SequenceHandler {

    val sequences = ArrayList<Sequence>()

    init {
        for (i in 0..7) {
            sequences.add(Sequence())
        }
    }

}