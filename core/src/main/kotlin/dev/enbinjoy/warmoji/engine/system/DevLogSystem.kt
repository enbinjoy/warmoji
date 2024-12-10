package dev.enbinjoy.warmoji.engine.system

import dev.enbinjoy.kgdx.game
import dev.enbinjoy.warmoji.engine.WarSystem

class DevLogSystem : WarSystem() {
    override fun update(isTick: Boolean, deltaTime: Float) {
        super.update(isTick, deltaTime)
        game.devLog("${warEngine.entities.size()}")
    }
}
