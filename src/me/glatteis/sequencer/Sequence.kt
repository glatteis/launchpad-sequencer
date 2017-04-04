package me.glatteis.sequencer

/**
 * Created by Linus on 02.04.2017!
 */
class Sequence {

    val notes = ArrayList<Byte>()
    var length = 8
    var currentStep = 0

    init {
        for (i in 0..length - 1) {
            notes.add(0)
        }
    }

    fun step() {
        currentStep++
        currentStep %= length
    }

    fun getStepValue(): Byte {
        return notes[currentStep]
    }

}