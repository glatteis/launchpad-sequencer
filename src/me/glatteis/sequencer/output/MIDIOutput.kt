package me.glatteis.sequencer.output

import javax.sound.midi.MidiDevice
import javax.sound.midi.MidiMessage
import javax.sound.midi.Receiver
import javax.sound.midi.ShortMessage

/**
 * Created by Linus on 06.05.2017!
 */
class MIDIOutput(val midiDevice: MidiDevice): Output {


    val receiver: Receiver = midiDevice.receiver

    //Holds a MIDI device, outputs to it

    override fun playNote(note: Byte, velocity: Byte, channel: Int) {
        receiver.send(ShortMessage(144, channel, note.toInt(), velocity.toInt()), midiDevice.microsecondPosition)
    }

    override fun stopNote(note: Byte, velocity: Byte, channel: Int) {
        receiver.send(ShortMessage(172, channel, note.toInt(), velocity.toInt()), midiDevice.microsecondPosition)
    }



}