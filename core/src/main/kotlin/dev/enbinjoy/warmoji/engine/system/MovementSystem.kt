package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import dev.enbinjoy.warmoji.engine.DirectionComponent
import dev.enbinjoy.warmoji.engine.Mappers
import dev.enbinjoy.warmoji.engine.PositionComponent
import dev.enbinjoy.warmoji.engine.SpeedComponent
import dev.enbinjoy.warmoji.engine.WarSystem
import dev.enbinjoy.warmoji.engine.require

class MovementSystem : WarSystem() {
    private lateinit var entityArray: ImmutableArray<Entity>

    override fun addedToEngine() {
        super.addedToEngine()
        entityArray = warEngine.getEntitiesFor(Family.all(
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
            position.x += direction.x * speed.value * deltaTime
            position.y += direction.y * speed.value * deltaTime
            val halfWidth = size?.width?.div(2f) ?: 0f
            val halfHeight = size?.height?.div(2f) ?: 0f
            val minPositionX = halfWidth
            val maxPositionX = warEngine.columns - halfWidth
            val minPositionY = halfHeight
            val maxPositionY = warEngine.rows - halfHeight
            val isOutOfBounds = position.x < minPositionX || position.x > maxPositionX ||
                position.y < minPositionY || position.y > maxPositionY
            if (isOutOfBounds) {
                when (position.outOfBoundsBehavior) {
                    PositionComponent.OutOfBoundsBehavior.CLAMP -> {
                        position.x = position.x.coerceIn(minPositionX, maxPositionX)
                        position.y = position.y.coerceIn(minPositionY, maxPositionY)
                    }
                    PositionComponent.OutOfBoundsBehavior.REMOVE -> {
                        warEngine.postRemoveEntity(entity)
                    }
                }
            }
        }
    }
}
