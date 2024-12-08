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

class PlayerEnemyCollisionSystem : WarSystem() {
    private lateinit var player: Entity
    private lateinit var enemyArray: ImmutableArray<Entity>

    override fun addedToEngine() {
        super.addedToEngine()
        player = warEngine.getEntitiesFor(Family.all(PlayerComponent::class.java).get()).single()
        enemyArray = warEngine.getEntitiesFor(Family.all(EnemyComponent::class.java).get())
    }

    override fun tick(tickDeltaTime: Float) {
        super.tick(tickDeltaTime)
        val playerPosition = Mappers.position.require(player)
        val playerSize = Mappers.size.require(player)
        enemyArray.forEach { enemy ->
            val enemyPosition = Mappers.position.require(enemy)
            val enemySize = Mappers.size.require(enemy)
            if (MathUtils.checkCollision(playerPosition, playerSize, enemyPosition, enemySize)) {
                warEngine.newWar()
            }
        }
    }
}
