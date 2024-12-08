package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import dev.enbinjoy.warmoji.engine.Mappers
import dev.enbinjoy.warmoji.engine.PlayerComponent
import dev.enbinjoy.warmoji.engine.WarSystem
import dev.enbinjoy.warmoji.engine.require

class CameraSystem : WarSystem() {
    private lateinit var player: Entity

    override fun addedToEngine() {
        super.addedToEngine()
        player = warEngine.getEntitiesFor(Family.all(PlayerComponent::class.java).get()).single()

        warEngine.viewport.camera.position.x = warEngine.columns / 2f
        warEngine.viewport.camera.position.y = warEngine.rows / 2f
        warEngine.viewport.camera.update()
    }

    override fun updateWithTick() {
        super.updateWithTick()
        internalUpdate()
    }

    override fun updateWithoutTick(deltaTime: Float) {
        super.updateWithoutTick(deltaTime)
        internalUpdate()
    }

    private fun internalUpdate() {
        val playerPosition = Mappers.position.require(player)
        warEngine.viewport.camera.position.x = playerPosition.renderX
        warEngine.viewport.camera.position.y = playerPosition.renderY
        warEngine.viewport.camera.update()
    }
}
