package me.glatteis.sequencer.sequencing

/**
 * Created by Linus on 02.04.2017!
 */
class Sequence {
    val notes = ArrayList<Byte>()
    var end = 8
    set(value) {
        if (notes.size < value) {
            expandTo(value - 1)
        }
        field = value
    }
    var start = 0
    var currentStep = 0

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

    fun getStepValue(): Byte {
        if (currentStep >= end) return 0
        return notes[currentStep]
    }

    fun expandTo(length: Int) {
        for (i in notes.size..length) {
            notes.add(0)
        }
    }


}