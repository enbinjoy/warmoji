package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import dev.enbinjoy.warmoji.engine.EnemyComponent
import dev.enbinjoy.warmoji.engine.Mappers
import dev.enbinjoy.warmoji.engine.MathUtils
import dev.enbinjoy.warmoji.engine.PlayerComponent
import dev.enbinjoy.warmoji.engine.WarSystem
import dev.enbinjoy.warmoji.engine.require

class EnemyAISystem : WarSystem() {
    private lateinit var playerArray: ImmutableArray<Entity>
    private lateinit var enemyArray: ImmutableArray<Entity>

    override fun init() {
        super.init()
        playerArray = warEngine.getEntitiesFor(Family.all(PlayerComponent::class.java).get())
        enemyArray = warEngine.getEntitiesFor(Family.all(EnemyComponent::class.java).get())
    }

    override fun tick(tickDeltaTime: Float) {
        super.tick(tickDeltaTime)
        val player = playerArray.single()
        val playerPosition = Mappers.position.require(player)
        enemyArray.forEach { enemy ->
            val enemyPosition = Mappers.position.require(enemy)
            val enemyDirection = Mappers.direction.require(enemy)
            MathUtils.direction(
                positionFrom = enemyPosition,
                positionTo = playerPosition,
                direction = enemyDirection,
            )
        }
    }
}
