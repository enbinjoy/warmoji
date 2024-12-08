package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import dev.enbinjoy.warmoji.engine.AttackRangeComponent
import dev.enbinjoy.warmoji.engine.EnemyComponent
import dev.enbinjoy.warmoji.engine.Mappers
import dev.enbinjoy.warmoji.engine.MathUtils
import dev.enbinjoy.warmoji.engine.PlayerComponent
import dev.enbinjoy.warmoji.engine.PositionComponent
import dev.enbinjoy.warmoji.engine.WarSystem
import dev.enbinjoy.warmoji.engine.component
import dev.enbinjoy.warmoji.engine.newBullet
import dev.enbinjoy.warmoji.engine.require
import kotlin.math.sqrt

class BulletSpawnSystem : WarSystem() {
    private lateinit var player: Entity
    private lateinit var enemyArray: ImmutableArray<Entity>

    override fun addedToEngine() {
        super.addedToEngine()
        player = warEngine.getEntitiesFor(Family.all(PlayerComponent::class.java).get()).single()
        enemyArray = warEngine.getEntitiesFor(Family.all(EnemyComponent::class.java).get())
    }

    private var time: Float = 0f
    private var lastSpawnTime: Float = 0f

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        time += deltaTime
        val playerAttackSpeed = Mappers.attackSpeed.require(player)
        if (time - lastSpawnTime < playerAttackSpeed.value) return
        val playerPosition = Mappers.position.require(player)
        val playerAttackRange = Mappers.attackRange.require(player)
        val nearestEnemy = nearestEnemy(playerPosition, playerAttackRange) ?: return
        val nearestEnemyPosition = Mappers.position.require(nearestEnemy)
        val bulletDirection = MathUtils.direction(
            positionFrom = playerPosition,
            positionTo = nearestEnemyPosition,
            direction = warEngine.component(),
        )
        warEngine.newBullet(
            positionX = playerPosition.x,
            positionY = playerPosition.y,
            directionX = bulletDirection.x,
            directionY = bulletDirection.y,
        )
        lastSpawnTime = time
    }

    private fun nearestEnemy(position: PositionComponent, attackRange: AttackRangeComponent): Entity? {
        var nearestDistance = Float.MAX_VALUE
        var nearestEnemy: Entity? = null
        enemyArray.forEach { enemy ->
            val enemyPosition = Mappers.position.require(enemy)
            val distanceX = enemyPosition.x - position.x
            val distanceY = enemyPosition.y - position.y
            val distance = sqrt(distanceX * distanceX + distanceY * distanceY)
            if (distance > attackRange.value) return@forEach
            if (distance < nearestDistance) {
                nearestDistance = distance
                nearestEnemy = enemy
            }
        }
        return nearestEnemy
    }
}
