package me.glatteis.sequencer.sequencing

/**
 * Created by Linus on 29.04.2017!
 */
class DrumSequenceHandler : SequenceHandler() {

    override val sequences = Array<Sequence>(128) {
        DrumSequence()
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