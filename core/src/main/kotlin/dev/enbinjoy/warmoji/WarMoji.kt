package dev.enbinjoy.warmoji

import dev.enbinjoy.kgdx.Game
import dev.enbinjoy.kgdx.game

val warMoji: WarMoji
    get() = game as WarMoji

class WarMoji : Game() {
    override fun create() {
        super.create()
        sceneList = listOf(WarMojiScene())
    }
}
