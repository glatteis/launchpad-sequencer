package me.glatteis.sequencer.sequencing

/**
 * Created by Linus on 29.04.2017!
 */
class InstrumentSequence(val sequenceHandler: InstrumentSequenceHandler) : Sequence {

    override val notes = ArrayList<Byte>()

    override var start: Int
        get() = sequenceHandler.start
        set(value) {
            sequenceHandler.start = value
        }

    override var end: Int
        get() = sequenceHandler.end
        set(value) {
            sequenceHandler.end = value
        }

    override var currentStep: Int
        get() = sequenceHandler.currentStep
        set(value) {
            sequenceHandler.currentStep = value
        }

    init {
        for (i in 0..end - 1) {
            notes.add(0)
        }
    }

    override fun getStepValue(): Byte {
        if (currentStep >= end) return 0
        return notes[currentStep]
    }

    override fun expandTo(length: Int) {
        sequenceHandler.expandTo(length)
    }

}