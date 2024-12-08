package dev.enbinjoy.warmoji

import dev.enbinjoy.kgdx.Game
import dev.enbinjoy.kgdx.game
import dev.enbinjoy.warmoji.engine.WarEngine
import dev.enbinjoy.warmoji.openmoji.OpenMojiManager

val warMoji: WarMoji
    get() = game as WarMoji

class WarMoji : Game() {
    lateinit var openMojiManager: OpenMojiManager
        private set

    override fun create() {
        super.create()
        openMojiManager = OpenMojiManager()
        sceneList = listOf(WarEngine())
    }

    override fun dispose() {
        sceneList = null
        openMojiManager.dispose()
        super.dispose()
    }
}
