package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import dev.enbinjoy.warmoji.engine.Mappers
import dev.enbinjoy.warmoji.engine.PlayerComponent
import dev.enbinjoy.warmoji.engine.require
import dev.enbinjoy.warmoji.engine.warEngine

class CameraSystem : EntitySystem() {
    private lateinit var player: Entity

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        player = engine.getEntitiesFor(Family.all(PlayerComponent::class.java).get()).single()

        warEngine.viewport.camera.position.x = warEngine.columns / 2f
        warEngine.viewport.camera.position.y = warEngine.rows / 2f
        warEngine.viewport.camera.update()
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        val playerPosition = Mappers.position.require(player)
        warEngine.viewport.camera.position.x = playerPosition.x
        warEngine.viewport.camera.position.y = playerPosition.y
        warEngine.viewport.camera.update()
    }
}
