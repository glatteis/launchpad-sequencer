package me.glatteis.sequencer

import me.glatteis.sequencer.ui.Screen
import me.glatteis.sequencer.ui.SequenceOverview
import javax.sound.midi.*


/**
 * Created by Linus on 31.03.2017!
 */

fun main(args: Array<String>) {
    Sequencer()
}

class Sequencer {
    var launchpad: Launchpad
    val sequenceHandler: SequenceHandler

    val sequenceOverview: SequenceOverview

    var currentScreen: Screen

    val clock: Clock

    init {
        var deviceOut: MidiDevice? = null
        var deviceIn: MidiDevice? = null
        val infos = MidiSystem.getMidiDeviceInfo()
        for (i in infos) {
            if (i.name.contains("Launchpad") && i.description == "External MIDI Port") {
                deviceOut = MidiSystem.getMidiDevice(i)
            } else if (i.name.contains("Launchpad") && i.description == "No details available") {
                deviceIn = MidiSystem.getMidiDevice(i)
            }
        }
        deviceOut ?: throw RuntimeException("No Launchpad OUT found")
        deviceIn ?: throw RuntimeException("No Launchpad IN found")
        deviceIn.open()
        deviceOut.open()



        launchpad = Launchpad(deviceOut)
        launchpad.clearScreen()

        sequenceHandler = SequenceHandler()

        sequenceOverview = SequenceOverview(launchpad, sequenceHandler)
        currentScreen = sequenceOverview

        clock = Clock(launchpad, this)

        deviceIn.transmitter.receiver = object : Receiver {
            override fun send(message: MidiMessage?, timeStamp: Long) {
                message ?: return
                if (message is ShortMessage) {
                    val key = message.data1
                    val velocity = message.data2
                    if (message.command == 144) {
                        val x = key % 16
                        val y = key / 16
                        if (x < 9 && y < 8) {
                            if (velocity == 0) {
                                currentScreen.inputOff(x, y)
                            } else {
                                currentScreen.inputOn(x, y)
                            }
                        }
                    } else if (message.command == 176) {
                        if (key in 104..111) {
                            if (velocity == 0) {
                                currentScreen.inputBarOff(key - 104)
                            } else {
                                currentScreen.inputBarOn(key - 104)
                            }
                        }
                    }

                }
            }

            override fun close() {
            }
        }
        currentScreen.drawFull()
    }

    fun step() {
        for (sequence in sequenceHandler.sequences) {
            sequence.step()
        }
        currentScreen.drawUpdate()
    }
}

