package me.glatteis.sequencer.sequencing

/**
 * Created by Linus on 02.04.2017!
 */
class DrumSequence : Sequence {


    override val notes = ArrayList<Byte>()
    override var end = 8
    set(value) {
        if (notes.size < value) {
            expandTo(value - 1)
        }
        field = value
    }
    override var start = 0
    override var currentStep = 0

    //The notes and channels on which this drum sequence will output data
    var note: Byte = 0
    var channel = 0

    var paused = false

    init {
        for (i in 0..end - 1) {
            notes.add(0)
        }
    }

    fun step() {
        currentStep++
        if (currentStep >= end || currentStep < start) {
            currentStep = start
        }
    }

    override fun getStepValue(): Byte {
        if (currentStep >= end) return 0
        return notes[currentStep]
    }

    override fun expandTo(length: Int) {
        for (i in notes.size..length) {
            notes.add(0)
        }
    }


}