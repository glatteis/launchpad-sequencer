package me.glatteis.sequencer

import kotlin.concurrent.timer

/**
 * Created by Linus on 02.04.2017!
 */
class Clock(sequencer: Sequencer) {

    private var halfStep = false

    val clockTimer = timer(period = 60000 / 400) {
        if (halfStep) {
            sequencer.launchpad?.setRowLedOn(4, Color(3, 0), 100)
        } else {
            sequencer.launchpad?.setRowLedOff(4)
        }
        halfStep = !halfStep
        sequencer.step()
    }

   }