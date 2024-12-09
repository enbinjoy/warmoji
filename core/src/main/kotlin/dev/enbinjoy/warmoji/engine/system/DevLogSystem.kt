package dev.enbinjoy.warmoji.engine.system

import dev.enbinjoy.kgdx.game
import dev.enbinjoy.warmoji.engine.WarSystem

class DevLogSystem : WarSystem() {
    override fun updateWithTick() {
        super.updateWithTick()
        internalUpdate()
    }

    override fun updateWithoutTick(deltaTime: Float) {
        super.updateWithoutTick(deltaTime)
        internalUpdate()
    }

    private fun internalUpdate() {
        game.devLog("${warEngine.entities.size()}")
    }
}
