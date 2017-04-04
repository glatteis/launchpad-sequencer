package me.glatteis.sequencer

import kotlin.concurrent.timer

/**
 * Created by Linus on 02.04.2017!
 */
class Clock(launchpad: Launchpad, sequencer: Sequencer) {

    private var halfStep = false

    val clockTimer = timer(period = 100) {
        if (halfStep) {
            launchpad.setRowLedOn(4, Color(3, 0))
        } else {
            launchpad.setRowLedOff(4)
        }
        halfStep = !halfStep
        sequencer.step()
    }

}