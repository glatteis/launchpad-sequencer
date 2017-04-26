package me.glatteis.sequencer

import kotlin.concurrent.timer

/**
 * Created by Linus on 02.04.2017!
 */
class Clock(launchpad: Launchpad, sequencer: Sequencer) {

    private var halfStep = false

    val clockTimer = timer(period = 60000 / 250) {
        if (halfStep) {
            launchpad.setRowLedOn(4, Color(2, 0), 100)
        } else {
            launchpad.setRowLedOff(4)
        }
        halfStep = !halfStep
        sequencer.step()
    }

   }