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

    override fun addedToEngine() {
        super.addedToEngine()
        entityArray = warEngine.getEntitiesFor(Family.all(
            PositionComponent::class.java,
            SizeComponent::class.java,
        ).get())
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
