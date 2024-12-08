package dev.enbinjoy.warmoji

import dev.enbinjoy.kgdx.Game
import dev.enbinjoy.kgdx.game
import dev.enbinjoy.warmoji.openmoji.OpenMojiScene

val warMoji: WarMoji
    get() = game as WarMoji

class WarMoji : Game() {
    override fun create() {
        super.create()
        sceneList = listOf(OpenMojiScene())
    }
}
