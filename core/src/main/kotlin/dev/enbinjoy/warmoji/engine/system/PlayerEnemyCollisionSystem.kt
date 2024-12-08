package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import dev.enbinjoy.warmoji.engine.EnemyComponent
import dev.enbinjoy.warmoji.engine.Mappers
import dev.enbinjoy.warmoji.engine.PlayerComponent
import dev.enbinjoy.warmoji.engine.PositionComponent
import dev.enbinjoy.warmoji.engine.SizeComponent
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

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        val playerPosition = Mappers.position.require(player)
        val playerSize = Mappers.size.require(player)
        enemyArray.forEach { enemy ->
            val enemyPosition = Mappers.position.require(enemy)
            val enemySize = Mappers.size.require(enemy)
            if (checkCollision(playerPosition, playerSize, enemyPosition, enemySize)) {
                warEngine.removeEntity(enemy)
            }
        }
    }

    companion object {
        private fun checkCollision(
            position1: PositionComponent,
            size1: SizeComponent,
            position2: PositionComponent,
            size2: SizeComponent,
        ): Boolean {
            val halfWidth1 = size1.width / 2f
            val halfHeight1 = size1.height / 2f
            val left1 = position1.x - halfWidth1
            val right1 = left1 + size1.width
            val bottom1 = position1.y - halfHeight1
            val top1 = bottom1 + size1.height
            val halfWidth2 = size2.width / 2f
            val halfHeight2 = size2.height / 2f
            val left2 = position2.x - halfWidth2
            val right2 = left2 + size2.width
            val bottom2 = position2.y - halfHeight2
            val top2 = bottom2 + size2.height
            return left1 < right2 && right1 > left2 && bottom1 < top2 && top1 > bottom2
        }
    }
}
