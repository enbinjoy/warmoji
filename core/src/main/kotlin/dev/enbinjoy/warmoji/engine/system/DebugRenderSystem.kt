package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.Color
import dev.enbinjoy.warmoji.engine.Mappers
import dev.enbinjoy.warmoji.engine.PositionComponent
import dev.enbinjoy.warmoji.engine.SizeComponent
import dev.enbinjoy.warmoji.engine.WarSystem
import dev.enbinjoy.warmoji.engine.require

class DebugRenderSystem : WarSystem() {
    private lateinit var entityArray: ImmutableArray<Entity>

    override fun init() {
        super.init()
        entityArray = warEngine.getEntitiesFor(Family.all(
            PositionComponent::class.java,
            SizeComponent::class.java,
        ).get())
    }

    override fun update(isTick: Boolean, deltaTime: Float) {
        super.update(isTick, deltaTime)
        warEngine.renderer.useShape(
            color = Color.CYAN,
        ) {
            entityArray.forEach { entity ->
                val position = Mappers.position.require(entity)
                val size = Mappers.size.require(entity)
                rect(position.x - size.width / 2f, position.y - size.height / 2f, size.width, size.height)
                rect(position.renderX - size.width / 2f, position.renderY - size.height / 2f, size.width, size.height)
            }
        }
    }
}
