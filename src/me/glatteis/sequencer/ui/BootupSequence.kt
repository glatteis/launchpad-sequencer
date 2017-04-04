package me.glatteis.sequencer.ui

import me.glatteis.sequencer.Color
import me.glatteis.sequencer.Launchpad
import me.glatteis.sequencer.Sequencer
import kotlin.concurrent.timer

/**
 * Created by Linus on 02.04.2017!
 */
object BootupSequence {

    fun bootupSequence(launchpad: Launchpad, sequencer: Sequencer) {

        /*
        var frame = 0
        timer(period = 100) {
            if (frame == 8) {
                cancel()

                return@timer
            }
            for (y in 0..7) {
                if (frame != 0) {
                    launchpad.setGridLedOff(frame - 1, y)
                    launchpad.setGridLedOff(7 - frame + 1, y)
                }
                launchpad.setGridLedOn(frame, y, Color(3, 3))
                launchpad.setGridLedOn(7 - frame, y, Color(3, 3))
            }
            frame++
        }
        */
    }

}