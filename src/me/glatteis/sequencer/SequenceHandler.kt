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
        sequences[0].length = 20
        sequences[1].length = 5
        sequences[5].length = 7
    }

    fun expand(length: Int) {
        for (i in sequences.size..length) {
            sequences.add(Sequence())
        }
    }

}