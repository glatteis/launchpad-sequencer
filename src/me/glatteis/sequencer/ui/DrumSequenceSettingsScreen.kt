package me.glatteis.sequencer.ui

import me.glatteis.sequencer.Color
import me.glatteis.sequencer.Sequencer
import me.glatteis.sequencer.sequencing.DrumSequence

/**
 * Created by Linus on 06.05.2017!
 */
class DrumSequenceSettingsScreen(val sequence: DrumSequence, val sequencer: Sequencer, val parent: Screen) : Screen {

    enum class SettingType {
        NOTE, CHANNEL
    }

    val settingType = SettingType.NOTE
    var scrollHorizontal = 0

    override fun drawFull() {
        for (i in 0..7) {
            if (i == scrollHorizontal / 8) {
                sequencer.launchpad?.bufferGridLedOn(8, i, Color(3, 3))
            } else {
                sequencer.launchpad?.bufferGridLedOff(8, i)
            }
        }

        if (settingType == SettingType.NOTE) {
            val note = sequence.note
            for (i in 0..63) {
                val x = i % 8
                val y = i / 8
                if (i + scrollHorizontal * 8 > 127) {
                    sequencer.launchpad?.bufferGridLedOff(x, y)
                } else if (i + scrollHorizontal * 8 == note.toInt()) {
                    sequencer.launchpad?.bufferGridLedOn(x, y, Color(3, 2))
                } else {
                    sequencer.launchpad?.bufferGridLedOn(x, y, Color(0, 2))

                }
            }
        } else if (settingType == SettingType.CHANNEL) {
            val channel = sequence.channel
            for (i in 0..63) {
                val x = i % 8
                val y = i / 8
                if (i + scrollHorizontal * 8 > 127) {
                    sequencer.launchpad?.bufferGridLedOff(x, y)
                } else if (i + scrollHorizontal * 8 == channel) {
                    sequencer.launchpad?.bufferGridLedOn(x, y, Color(3, 2))
                } else {
                    sequencer.launchpad?.bufferGridLedOn(x, y, Color(0, 2))

                }
            }
        }
        sequencer.launchpad?.switchBuffer()
    }

    override fun drawUpdate() {

    }

    override fun inputOn(x: Int, y: Int) {
        if (x == 8) {
            sequencer.currentScreen = parent
            sequencer.launchpad?.clearBuffer()
            parent.drawFull()
            return
        }
        val number = y * 8 + x + scrollHorizontal * 8
        if (settingType == SettingType.NOTE && number < 127) {
            sequence.note = number.toByte()
        }
        drawFull()
    }

    override fun inputOff(x: Int, y: Int) {
    }

    override fun inputBarOn(x: Int) {
        when (x) {
            0 -> {
                scrollHorizontal -= 8
                scrollHorizontal = Math.max(0, scrollHorizontal)
                drawFull()
            }
            1 -> {
                scrollHorizontal += 8
                scrollHorizontal = Math.max(0, scrollHorizontal)
                drawFull()
            }
        }
    }

    override fun inputBarOff(x: Int) {
    }
}