package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import dev.enbinjoy.warmoji.engine.DirectionComponent
import dev.enbinjoy.warmoji.engine.EnemyComponent
import dev.enbinjoy.warmoji.engine.Mappers
import dev.enbinjoy.warmoji.engine.PlayerComponent
import dev.enbinjoy.warmoji.engine.PositionComponent
import dev.enbinjoy.warmoji.engine.WarSystem
import dev.enbinjoy.warmoji.engine.require
import kotlin.math.sqrt

class EnemyAISystem : WarSystem() {
    private lateinit var player: Entity
    private lateinit var enemyArray: ImmutableArray<Entity>

    override fun addedToEngine() {
        super.addedToEngine()
        player = warEngine.getEntitiesFor(Family.all(PlayerComponent::class.java).get()).single()
        enemyArray = warEngine.getEntitiesFor(Family.all(EnemyComponent::class.java).get())
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        val playerPosition = Mappers.position.require(player)
        enemyArray.forEach { enemy ->
            val enemyPosition = Mappers.position.require(enemy)
            val enemyDirection = Mappers.direction.require(enemy)
            targetDirection(
                positionFrom = enemyPosition,
                positionTo = playerPosition,
                direction = enemyDirection,
            )
        }
    }

    companion object {
        private fun targetDirection(
            positionFrom: PositionComponent,
            positionTo: PositionComponent,
            direction: DirectionComponent,
        ) {
            val distanceX = positionTo.x - positionFrom.x
            val distanceY = positionTo.y - positionFrom.y
            val distance = sqrt(distanceX * distanceX + distanceY * distanceY)
            direction.x = if (distance == 0f) 0f else distanceX / distance
            direction.y = if (distance == 0f) 0f else distanceY / distance
        }
    }
}
