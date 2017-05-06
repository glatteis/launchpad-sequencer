package me.glatteis.sequencer.sequencing

/**
 * Created by Linus on 29.04.2017!
 */
class DrumSequenceHandler : SequenceHandler() {

    override val sequences = ArrayList<Sequence>()

    init {
        for (i in 0..15) {
            sequences.add(DrumSequence())
        }
    }

    override fun step() {
        if (paused) return
        for (s in sequences) {
            if (!(s as DrumSequence).paused) {
                s.step()
            }
        }
    }

}