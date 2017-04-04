package me.glatteis.sequencer.ui

import me.glatteis.sequencer.Color
import me.glatteis.sequencer.Launchpad
import me.glatteis.sequencer.SequenceHandler

/**
 * Created by Linus on 02.04.2017!
 */
class SequenceOverview(val launchpad: Launchpad, val sequenceHandler: SequenceHandler) : Screen {
    override fun inputBar(x: Int) {
    }

    var scrollVertical = 0
    var scrollHorizontal = 0

    override fun drawFull() {
        launchpad.clearScreen()
        for (i in scrollVertical..scrollVertical + 8) {
            if (sequenceHandler.sequences.size <= i) continue
            val sequence = sequenceHandler.sequences[i]
            for (j in scrollHorizontal..scrollHorizontal + 8) {
                if (sequence.notes.size <= j) continue
                val step = sequence.currentStep - scrollHorizontal
                if (step in 0..7 && sequence.getStepValue() == 0.toByte()) {
                    launchpad.setGridLedOn(i - scrollVertical, step, Color(1, 1))
                }
                if (sequence.notes[j] > 0) {
                    launchpad.setGridLedOn(i - scrollVertical, step, Color(3, 3))
                }
            }
        }
    }

    override fun drawUpdate() {
        for (i in scrollVertical..scrollVertical + 8) {
            if (sequenceHandler.sequences.size <= i) continue
            val sequence = sequenceHandler.sequences[i]
            for (j in scrollHorizontal..scrollHorizontal + 8) {
                if (sequence.notes.size <= j) continue
                val step = sequence.currentStep - scrollHorizontal
                val previousStep = Math.floorMod(step - 1, sequence.notes.size)
                if (previousStep in 0..7 && sequence.notes[previousStep] == 0.toByte()) {
                    launchpad.setGridLedOff(j, previousStep)
                }
                if (step in 0..7 && sequence.getStepValue() == 0.toByte()) {
                    launchpad.setGridLedOn(i - scrollVertical, step, Color(1, 1))
                }
            }
        }
    }

    override fun inputOff(x: Int, y: Int) {
    }

    override fun inputOn(x: Int, y: Int) {
        if (x in 0..7 && y in 0..7) {
            val sequenceNum = y - scrollVertical
            val step = x - scrollHorizontal
            if (sequenceHandler.sequences.size <= sequenceNum) return
            val sequence = sequenceHandler.sequences[sequenceNum]
            if (sequence.notes.size <= sequenceNum) return
            if (sequence.notes[step] > 0) {
                sequence.notes[step] = 0
                if (sequence.currentStep == step) {
                    launchpad.setGridLedOn(sequenceNum, step, Color(1, 1))
                } else {
                    launchpad.setGridLedOff(sequenceNum, step)
                }
            } else {
                sequence.notes[step] = 127
                launchpad.setGridLedOn(sequenceNum, step, Color(3, 3))
            }

        }

    }


}