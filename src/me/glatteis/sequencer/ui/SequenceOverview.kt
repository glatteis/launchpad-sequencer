package me.glatteis.sequencer.ui

import me.glatteis.sequencer.Color
import me.glatteis.sequencer.Launchpad
import me.glatteis.sequencer.Sequencer
import me.glatteis.sequencer.sequencing.SequenceHandler

/**
 * Created by Linus on 02.04.2017!
 */
class SequenceOverview(val sequencer: Sequencer, val sequenceHandler: SequenceHandler) : Screen {
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
                    sequencer.launchpad?.bufferGridLedOff(sequenceNum - scrollVertical, displayStep)
                    continue
                }
                val sequence = sequenceHandler.sequences[sequenceNum]
                val note = sequence.notes[step]

                if (note == 0.toByte()) {
                    sequencer.launchpad?.bufferGridLedOff(sequenceNum - scrollVertical, displayStep)
                } else {

                    sequencer.launchpad?.bufferGridLedOn(sequenceNum - scrollVertical, displayStep, Color(3, 2))
                }
            }
        }
        bufferUpdate()

        sequencer.launchpad?.switchBuffer()
    }

    override fun drawUpdate() {
        sequencer.launchpad?.switchBuffer()
        bufferUpdate()
    }

    private fun bufferUpdate() {
        val scrollVertical = this.scrollVertical
        val scrollHorizontal = this.scrollHorizontal
        for (sequenceNum in scrollVertical..scrollVertical + 7) {
            if (sequenceHandler.sequences.size <= sequenceNum) continue
            val sequence = sequenceHandler.sequences[sequenceNum]
            val currentStep = sequence.currentStep

            if (sequence.end - sequence.start == 0) continue

            val previousCurrentStep = sequence.start + Math.floorMod(currentStep - sequence.start - 1, sequence.end - sequence.start)
            if (previousCurrentStep in scrollHorizontal..scrollHorizontal + 7) {
                if (sequence.notes[previousCurrentStep] == 0.toByte()) {
                    sequencer.launchpad?.bufferGridLedOff(sequenceNum - scrollVertical, previousCurrentStep - scrollHorizontal)
                } else {
                    sequencer.launchpad?.bufferGridLedOn(sequenceNum - scrollVertical, previousCurrentStep - scrollHorizontal, Color(3, 2))
                }
            }

            if (currentStep in scrollHorizontal..scrollHorizontal + 7) {
                if (sequence.getStepValue() == 0.toByte()) {
                    sequencer.launchpad?.bufferGridLedOn(sequenceNum - scrollVertical, currentStep - scrollHorizontal, Color(0, 1))
                } else {
                    sequencer.launchpad?.bufferGridLedOn(sequenceNum - scrollVertical, currentStep - scrollHorizontal, Color(2, 3))
                }
            }
        }
    }

    fun bufferFullSequence(sequenceNum: Int) {
        val sequence = sequenceHandler.sequences[sequenceNum]
        for (step in scrollHorizontal..scrollHorizontal + 7) {
            val displayStep = step - scrollHorizontal
            val note = sequence.notes[step]
            if (note == 0.toByte()) {
                sequencer.launchpad?.bufferGridLedOff(sequenceNum - scrollVertical, displayStep)
            } else {
                sequencer.launchpad?.bufferGridLedOn(sequenceNum - scrollVertical, displayStep, Color(3, 2))
            }
        }
        if (sequence.currentStep in scrollHorizontal..scrollHorizontal + 7) {
            if (sequence.getStepValue() == 0.toByte()) {
                sequencer.launchpad?.bufferGridLedOn(sequenceNum - scrollVertical, sequence.currentStep - scrollHorizontal, Color(0, 1))
            } else {
                sequencer.launchpad?.bufferGridLedOn(sequenceNum - scrollVertical, sequence.currentStep - scrollHorizontal, Color(2, 3))
            }
        }

    }

    override fun inputOn(x: Int, y: Int) {
        if (x in 0..7 && y in 0..7) {
            val sequenceNum = y + scrollVertical
            val step = x + scrollHorizontal
            if (sequenceHandler.sequences.size <= sequenceNum) return
            val sequence = sequenceHandler.sequences[sequenceNum]
            if (sequence.notes.size <= step) sequence.expandTo(step)
            if (shiftPressed) {
                if (shift1Pressed && !shift2Pressed) {
                    val start = x + scrollHorizontal
                    if (sequence.end < start) {
                        sequence.start = sequence.end - 1
                        sequence.end = start + 1
                    } else {
                        sequence.start = start
                    }
                    bufferFullSequence(sequenceNum)
                } else if (!shift1Pressed && shift2Pressed) {
                    val end = x + scrollHorizontal
                    if (sequence.start > end) {
                        sequence.end = sequence.start + 1
                        sequence.start = end - 1
                    } else {
                        sequence.end = end + 1
                    }
                    bufferFullSequence(sequenceNum)
                } else {
                    if (sequence.end > step) {
                        sequence.currentStep = sequence.start + ((step - 1 - sequence.start) % sequence.end - sequence.start)
                    }
                    bufferFullSequence(sequenceNum)
                }
            } else {
                if (sequence.notes[step] > 0) {
                    sequence.notes[step] = 0
                    if (sequence.currentStep == step) {
                        sequencer.launchpad?.setGridLedOn(sequenceNum - scrollVertical, step - scrollHorizontal, Color(0, 1))
                    } else {
                        sequencer.launchpad?.setGridLedOff(sequenceNum - scrollVertical, step - scrollHorizontal)
                    }
                } else {
                    sequence.notes[step] = 127

                    sequencer.launchpad?.setGridLedOn(sequenceNum - scrollVertical, step - scrollHorizontal, Color(3, 2))

                }
            }
        }
    }

    override fun inputOff(x: Int, y: Int) {
    }

    var shiftPressed = false
    var shift1Pressed = false
    var shift2Pressed = false

    override fun inputBarOn(x: Int) {
        when (x) {
            0 -> {
                scrollVertical -= if (shiftPressed) 8 else 1
                if (scrollVertical < 0) scrollVertical = 0
                drawFull()
            }
            1 -> {
                scrollVertical += if (shiftPressed) 8 else 1
                drawFull()
            }
            2 -> {
                scrollHorizontal -= if (shiftPressed) 8 else 1
                if (scrollHorizontal < 0) scrollHorizontal = 0
                drawFull()
            }
            3 -> {
                scrollHorizontal += if (shiftPressed) 8 else 1
                drawFull()
            }
            4 -> {
                if (shiftPressed) {
                    for (s in sequenceHandler.sequences) {
                        s.currentStep = s.end
                    }
                }
                drawFull()
            }
            5 -> {
                sequencer.launchpad?.setRowLedOn(5, Color(0, 3))
                shift1Pressed = true
            }
            6 -> {
                sequencer.launchpad?.setRowLedOn(6, Color(3, 0))
                shift2Pressed = true
            }
            7 -> {
                sequencer.launchpad?.setRowLedOn(7, Color(3, 3))
                shiftPressed = true
            }
        }
    }

    override fun inputBarOff(x: Int) {
        when (x) {
            5 -> {
                sequencer.launchpad?.setRowLedOff(5)
                shift1Pressed = false
            }
            6 -> {
                sequencer.launchpad?.setRowLedOff(6)
                shift2Pressed = false
            }
            7 -> {
                sequencer.launchpad?.setRowLedOff(7)
                shiftPressed = false
            }
        }
    }


}