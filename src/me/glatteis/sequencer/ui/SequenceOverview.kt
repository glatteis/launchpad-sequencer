package me.glatteis.sequencer.ui

import me.glatteis.sequencer.Color
import me.glatteis.sequencer.Launchpad
import me.glatteis.sequencer.SequenceHandler

/**
 * Created by Linus on 02.04.2017!
 */
class SequenceOverview(val launchpad: Launchpad, val sequenceHandler: SequenceHandler) : Screen {
    var scrollVertical = 0
    var scrollHorizontal = 0

    override fun drawFull() {
        val scrollVertical = this.scrollVertical
        val scrollHorizontal = this.scrollHorizontal
        for (sequenceNum in scrollVertical..scrollVertical + 7) {
            for (step in scrollHorizontal..scrollHorizontal + 7) {
                val displayStep = step - scrollHorizontal
                if (sequenceHandler.sequences.size <= sequenceNum ||
                        sequenceHandler.sequences[sequenceNum].notes.size <= step) {
                    launchpad.bufferGridLedOff(sequenceNum - scrollVertical, displayStep)
                    continue
                }
                val sequence = sequenceHandler.sequences[sequenceNum]
                val note = sequence.notes[step]

                if (note == 0.toByte()) {
                    launchpad.bufferGridLedOff(sequenceNum - scrollVertical, displayStep)
                } else {

                    launchpad.bufferGridLedOn(sequenceNum - scrollVertical, displayStep, Color(3, 2))
                }
            }
        }
        bufferUpdate()

        launchpad.switchBuffer()
    }

    override fun drawUpdate() {
        launchpad.switchBuffer()
        bufferUpdate()
    }

    private fun bufferUpdate() {
        val scrollVertical = this.scrollVertical
        val scrollHorizontal = this.scrollHorizontal
        for (sequenceNum in scrollVertical..scrollVertical + 7) {
            if (sequenceHandler.sequences.size <= sequenceNum) continue
            val sequence = sequenceHandler.sequences[sequenceNum]
            val currentStep = sequence.currentStep

            val previousCurrentStep = Math.floorMod(currentStep - 1, sequence.length)
            if (previousCurrentStep in scrollHorizontal..scrollHorizontal + 7) {
                if (sequence.notes[previousCurrentStep] == 0.toByte()) {
                    launchpad.bufferGridLedOff(sequenceNum - scrollVertical, previousCurrentStep - scrollHorizontal)
                } else {
                    launchpad.bufferGridLedOn(sequenceNum - scrollVertical, previousCurrentStep - scrollHorizontal, Color(3, 2))
                }
            }

            if (currentStep in scrollHorizontal..scrollHorizontal + 7) {
                if (sequence.getStepValue() == 0.toByte()) {
                    launchpad.bufferGridLedOn(sequenceNum - scrollVertical, currentStep - scrollHorizontal, Color(0, 1))
                } else {
                    launchpad.bufferGridLedOn(sequenceNum - scrollVertical, currentStep - scrollHorizontal, Color(2, 3))
                }
            }
        }
    }

    override fun inputOff(x: Int, y: Int) {
    }

    override fun inputOn(x: Int, y: Int) {
        if (x in 0..7 && y in 0..7) {
            val sequenceNum = y + scrollVertical
            val step = x + scrollHorizontal
            if (sequenceHandler.sequences.size <= sequenceNum) sequenceHandler.expand(sequenceNum)
            val sequence = sequenceHandler.sequences[sequenceNum]
            if (sequence.notes.size <= step) sequence.expandTo(step)
            if (sequence.notes[step] > 0) {
                sequence.notes[step] = 0
                if (sequence.currentStep == step) {
                    launchpad.setGridLedOn(sequenceNum - scrollVertical, step - scrollHorizontal, Color(0, 1))
                } else {
                    launchpad.setGridLedOff(sequenceNum - scrollVertical, step - scrollHorizontal)
                }
            } else {
                sequence.notes[step] = 127

                launchpad.setGridLedOn(sequenceNum - scrollVertical, step - scrollHorizontal, Color(3, 2))

            }
        }
    }

    override fun inputBarOn(x: Int) {
        when (x) {
            0 -> {
                scrollVertical--
                if (scrollVertical < 0) scrollVertical = 0
                drawFull()
            }
            1 -> {
                scrollVertical++
                drawFull()
            }
            2 -> {
                scrollHorizontal--
                if (scrollHorizontal < 0) scrollHorizontal = 0
                drawFull()
            }
            3 -> {
                scrollHorizontal++
                drawFull()
            }
        }
    }

    override fun inputBarOff(x: Int) {
    }


}