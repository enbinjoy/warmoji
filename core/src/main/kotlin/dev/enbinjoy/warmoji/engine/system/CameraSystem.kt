package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.math.Vector3
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

    private var centerX: Float = 0f
    private var centerY: Float = 0f

    private var minX: Float = 0f
    private var maxX: Float = 0f
    private var minY: Float = 0f
    private var maxY: Float = 0f

    override fun start() {
        super.start()
        centerX = warEngine.columns / 2f
        centerY = (warEngine.rows - MARGIN_BOTTOM + MARGIN_TOP) / 2f

        minX = centerX
        maxX = centerX
        minY = centerY
        maxY = centerY

        warEngine.viewport.camera.position.x = centerX
        warEngine.viewport.camera.position.y = centerY
        warEngine.viewport.camera.update()
    }

    override fun resize(width: Float, height: Float) {
        super.resize(width, height)
        minX = width / 2f - MARGIN_HORIZONTAL
        maxX = warEngine.columns - width / 2f + MARGIN_HORIZONTAL
        minY = height / 2f - MARGIN_BOTTOM
        maxY = warEngine.rows - height / 2f + MARGIN_TOP
    }

    override fun update(isTick: Boolean, deltaTime: Float) {
        super.update(isTick, deltaTime)
        val player = playerArray.single()
        val playerPosition = Mappers.position.require(player)
        val targetPositionX = if (minX > maxX) {
            centerX
        } else {
            playerPosition.renderX.coerceIn(minX, maxX)
        }
        val targetPositionY = if (minY > maxY) {
            centerY
        } else {
            playerPosition.renderY.coerceIn(minY, maxY)
        }
        val target = Vector3(targetPositionX, targetPositionY, 0f)
        warEngine.viewport.camera.position.lerp(target, 0.05f)
        warEngine.viewport.camera.update()
    }

    companion object {
        private const val MARGIN_HORIZONTAL = 1.5f
        private const val MARGIN_BOTTOM = 1f
        private const val MARGIN_TOP = 2f
    }
}
