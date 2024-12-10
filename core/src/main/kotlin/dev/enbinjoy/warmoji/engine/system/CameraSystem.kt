package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import dev.enbinjoy.warmoji.engine.Mappers
import dev.enbinjoy.warmoji.engine.PlayerComponent
import dev.enbinjoy.warmoji.engine.WarSystem
import dev.enbinjoy.warmoji.engine.require

class CameraSystem : WarSystem() {
    private lateinit var playerArray: ImmutableArray<Entity>

    override fun init() {
        super.init()
        playerArray = warEngine.getEntitiesFor(Family.all(PlayerComponent::class.java).get())
    }

    override fun start() {
        super.start()
        warEngine.viewport.camera.position.x = warEngine.columns / 2f
        warEngine.viewport.camera.position.y = warEngine.rows / 2f
        warEngine.viewport.camera.update()
    }

    override fun update(isTick: Boolean, deltaTime: Float) {
        super.update(isTick, deltaTime)
        val player = playerArray.single()
        val playerPosition = Mappers.position.require(player)
        warEngine.viewport.camera.position.x = playerPosition.renderX
        warEngine.viewport.camera.position.y = playerPosition.renderY
        warEngine.viewport.camera.update()
    }
}
