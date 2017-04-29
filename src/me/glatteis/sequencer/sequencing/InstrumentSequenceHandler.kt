package me.glatteis.sequencer.sequencing

import sun.security.util.Length

/**
 * Created by Linus on 29.04.2017!
 */
class InstrumentSequenceHandler : SequenceHandler() {

    var start = 0
    var end = 8
    var currentStep = 0

    override val sequences: Array<Sequence> = Array(128) {
        InstrumentSequence(this)
    }

    init {
        println(end)
    }

    override fun step() {
        if (paused) return
        currentStep++
        if (currentStep >= end || currentStep < start) {
            currentStep = start
        }
    }

    fun expandTo(length: Int) {
        for (s in sequences) {
            for (i in s.notes.size ..length) {
                s.notes.add(0)
            }
        }

    }

}