package me.glatteis.sequencer

import javax.sound.midi.MidiDevice
import javax.sound.midi.ShortMessage

/**
 * Created by Linus on 31.03.2017!
 */
class Launchpad(val device: MidiDevice) {

    fun setRowLedOn(x: Int, color: Color, offset: Int = 0) {
        if (x !in 0..7) throw RuntimeException("x has to be in range 0-7")
        val message = ShortMessage(176, 104 + x, color.asByte() + 12)
        device.receiver.send(message, device.microsecondPosition + offset)
    }

    fun setRowLedOff(x: Int, offset: Int = 0) {
        if (x !in 0..7) throw RuntimeException("x has to be in range 0-7")
        val message = ShortMessage(176, 104 + x, 12)
        device.receiver.send(message, device.microsecondPosition + offset)
    }

    fun setGridLedOn(x: Int, y: Int, color: Color, offset: Int = 0) {
        if (x !in 0..8 || y !in 0..7) throw RuntimeException("x / y have to be in range 0-8 / 0-7")
        val key = x * 16 + y
        val message = ShortMessage(144, key, color.asByte() + 12)
        device.receiver.send(message, device.microsecondPosition + offset)
    }

    fun bufferGridLedOn(x: Int, y: Int, color: Color, offset: Int = 0) {
        if (x !in 0..8 || y !in 0..7) throw RuntimeException("x / y have to be in range 0-8 / 0-7")
        val key = x * 16 + y
        val message = ShortMessage(144, key, color.asByte())
        device.receiver.send(message, device.microsecondPosition + offset)
    }

    fun setGridLedOff(x: Int, y: Int, offset: Int = 0) {
        if (x !in 0..8 || y !in 0..7) throw RuntimeException("x / y have to be in range 0-8 / 0-7")
        val key = x * 16 + y
        device.receiver.send(ShortMessage(128, key, 12), device.microsecondPosition + offset)
    }

    fun bufferGridLedOff(x: Int, y: Int, offset: Int = 0) {
        if (x !in 0..8 || y !in 0..7) throw RuntimeException("x / y have to be in range 0-8 / 0-7")
        val key = x * 16 + y
        device.receiver.send(ShortMessage(128, key, 0), device.microsecondPosition + offset)
    }

    fun clearScreen(offset: Int = 0) {
        device.receiver.send(ShortMessage(176, 0, 0), device.microsecondPosition + offset)
    }

    fun clearBuffer(offset: Int = 0) {
        device.receiver.send(ShortMessage(176, 0, 16), device.microsecondPosition + offset)
    }

    //Action: 0 for default, 1 for flash, 2 for copy
    fun setAsBuffer(updating: Int, displaying: Int, action: Int) {
        device.receiver.send(ShortMessage(176, 0, 4 * updating + displaying + 32 + action * 8), device.microsecondPosition)
    }

    var updatingBuffer = false
    fun switchBuffer() {
        if (updatingBuffer) {
            setAsBuffer(1, 0, 2)
        } else {
            setAsBuffer(0, 1, 2)
        }
        updatingBuffer = !updatingBuffer
    }

}