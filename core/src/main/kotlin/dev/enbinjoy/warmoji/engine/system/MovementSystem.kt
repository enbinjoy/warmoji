package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import dev.enbinjoy.warmoji.engine.DirectionComponent
import dev.enbinjoy.warmoji.engine.Mappers
import dev.enbinjoy.warmoji.engine.PositionComponent
import dev.enbinjoy.warmoji.engine.SpeedComponent
import dev.enbinjoy.warmoji.engine.require
import dev.enbinjoy.warmoji.engine.warEngine

class MovementSystem : EntitySystem() {
    private lateinit var entityArray: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        entityArray = engine.getEntitiesFor(Family.all(
            PositionComponent::class.java,
            SpeedComponent::class.java,
            DirectionComponent::class.java,
        ).get())
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        entityArray.forEach { entity ->
            val position = Mappers.position.require(entity)
            val speed = Mappers.speed.require(entity)
            val direction = Mappers.direction.require(entity)
            val size = Mappers.size.get(entity)
            val halfWidth = size?.width?.div(2f) ?: 0f
            val halfHeight = size?.height?.div(2f) ?: 0f
            val minX = halfWidth
            val maxX = warEngine.columns - halfWidth
            val minY = halfHeight
            val maxY = warEngine.rows - halfHeight
            position.x = (position.x + direction.x * speed.value * deltaTime).coerceIn(minX, maxX)
            position.y = (position.y + direction.y * speed.value * deltaTime).coerceIn(minY, maxY)
        }
    }
}
