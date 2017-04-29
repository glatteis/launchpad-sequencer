package me.glatteis.sequencer

import me.glatteis.sequencer.sequencing.DrumSequenceHandler
import me.glatteis.sequencer.sequencing.InstrumentSequenceHandler
import me.glatteis.sequencer.sequencing.SequenceHandler
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
    var launchpad: Launchpad? = null

    val tracks = ArrayList<SequenceHandler>()

    val sequenceOverview: SequenceOverview

    var currentScreen: Screen

    val clock: Clock

    init {
        setupLaunchpad()

        tracks.add(InstrumentSequenceHandler())
        tracks.add(DrumSequenceHandler())

        sequenceOverview = SequenceOverview(this, tracks[0])
        currentScreen = sequenceOverview

        clock = Clock(this)
    }

    fun setupLaunchpad() {
        //Find all devices that consist of two devices which have the same name, like the Launchpad
        val infos = MidiSystem.getMidiDeviceInfo()
        val devices = HashMap<String, MidiDevice.Info>()
        val possibleDevices = ArrayList<Pair<MidiDevice.Info, MidiDevice.Info>>()
        for (i in infos) {
            println(i.name)
            println(i.vendor)

            if (devices.containsKey(i.name)) {
                possibleDevices.add(Pair(devices[i.name]!!, i))
                devices.remove(i.name)
            }
            devices.put(i.name, i)
        }

        if (possibleDevices.isEmpty()) {
            println("")
            return
        }

        //For all devices: Add a listener for a key stroke
        val openDevices = ArrayList<MidiDevice>()

        for ((i, o) in possibleDevices) {
            val deviceIn: MidiDevice
            val deviceOut: MidiDevice

            try {
                deviceIn = MidiSystem.getMidiDevice(i)
                deviceOut = MidiSystem.getMidiDevice(o)
                deviceIn.open()
                deviceOut.open()

                //Draw three nice dots on the Launchpad
                val temporaryLaunchpad = Launchpad(deviceOut)
                temporaryLaunchpad.clearScreen()

                for (y1 in 0..2) {
                    for (y2 in 0..1) {
                        for (x in 3..4) {
                            temporaryLaunchpad.setGridLedOn(x, y1 * 3 + y2, Color(y1 + 1, y1 + 1))
                        }
                    }
                }
            } catch (e: Exception) {
                println("Device ${i.name} could not be opened.")
                continue
            }

            openDevices.add(deviceIn)
            openDevices.add(deviceOut)

            deviceIn.transmitter.receiver = object : Receiver {
                override fun send(message: MidiMessage?, timeStamp: Long) {
                    message ?: return
                    //If someone presses what is presumably the Launchpad, make it the Launchpad
                    if (message is ShortMessage && message.command == 144) {

                        openDevices
                                .filter { it != deviceIn && it != deviceOut }
                                .forEach { it.close() }

                        deviceIn.close()
                        deviceIn.open()

                        launchpad = Launchpad(deviceOut)
                        launchpad!!.clearScreen()

                        deviceIn.transmitter.receiver = listenerObject

                        currentScreen.drawFull()
                    }
                }

                override fun close() {
                }
            }

        }


    }

    fun step() {
        for (sequenceHandler in tracks) {
            sequenceHandler.step()
        }
        currentScreen.drawUpdate()
    }

    val listenerObject = object : Receiver {
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
}

