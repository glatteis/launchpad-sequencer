package me.glatteis.sequencer.sequencing

/**
 * Created by Linus on 29.04.2017!
 */
interface Sequence {
    val notes: ArrayList<Byte>
    var start: Int
    var end: Int
    var currentStep: Int
    fun getStepValue(): Byte
    fun expandTo(length: Int)
}