package me.glatteis.sequencer

import javax.sound.midi.MidiDevice
import javax.sound.midi.ShortMessage
import javax.sound.midi.Synthesizer

/**
 * Created by Linus on 31.03.2017!
 */
class Launchpad(val device: MidiDevice) {

    fun setRowLedOn(x: Int, color: Color, offset: Int = 0) {
        if (x !in 0..7) throw RuntimeException("x has to be in range 0-7")
        val message = ShortMessage(144, 104 + x, color.asByte() + 12)
        device.receiver.send(message, device.microsecondPosition + offset)
    }

    fun setRowLedOff(x: Int, offset: Int = 0) {
        if (x !in 0..7) throw RuntimeException("x has to be in range 0-7")
        val message = ShortMessage(144, 104 + x, 12)
        device.receiver.send(message, device.microsecondPosition + offset)
    }

    fun setGridLedOn(x: Int, y: Int, color: Color, offset: Int = 0) {
        if (x !in 0..8 || y !in 0..7) throw RuntimeException("x and y have to be in range 0-8 / 0-7")
        val key = x * 16 + y
        val message = ShortMessage(144, key, color.asByte() + 12)
        device.receiver.send(message, device.microsecondPosition + offset)
    }

    fun setGridLedOn(x: Int, y: Int, color: Color, copy: Boolean, clear: Boolean, offset: Int = 0) {
        if (x !in 0..8 || y !in 0..7) throw RuntimeException("x and y have to be in range 0-8 / 0-7")
        val key = x * 16 + y
        val message = ShortMessage(144, key, color.asByte() + if (copy) 0b001 else 0 + if (clear) 0b0001 else 0)
        device.receiver.send(message, device.microsecondPosition + offset)
    }

    fun setGridLedOff(x: Int, y: Int, offset: Int = 0) {
        if (x !in 0..8 || y !in 0..7) throw RuntimeException("x and y have to be in range 0-8 / 0-7")
        val key = x * 16 + y
        device.receiver.send(ShortMessage(128, key, 12), device.microsecondPosition + offset)
    }

    fun clearScreen(offset: Int = 0) {
        device.receiver.send(ShortMessage(176, 0, 0), device.microsecondPosition + offset)
    }

}