package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import dev.enbinjoy.warmoji.engine.BulletComponent
import dev.enbinjoy.warmoji.engine.EnemyComponent
import dev.enbinjoy.warmoji.engine.Mappers
import dev.enbinjoy.warmoji.engine.MathUtils
import dev.enbinjoy.warmoji.engine.WarSystem
import dev.enbinjoy.warmoji.engine.require

class EnemyBulletCollisionSystem : WarSystem() {
    private lateinit var enemyArray: ImmutableArray<Entity>
    private lateinit var bulletArray: ImmutableArray<Entity>

    override fun addedToEngine() {
        super.addedToEngine()
        enemyArray = warEngine.getEntitiesFor(Family.all(EnemyComponent::class.java).get())
        bulletArray = warEngine.getEntitiesFor(Family.all(BulletComponent::class.java).get())
    }

    override fun tick(tickDeltaTime: Float) {
        super.tick(tickDeltaTime)
        enemyArray.forEach { enemy ->
            val enemyPosition = Mappers.position.require(enemy)
            val enemySize = Mappers.size.require(enemy)
            bulletArray.forEach { bullet ->
                val bulletPosition = Mappers.position.require(bullet)
                val bulletSize = Mappers.size.require(bullet)
                if (MathUtils.checkCollision(enemyPosition, enemySize, bulletPosition, bulletSize)) {
                    warEngine.postRemoveEntity(enemy)
                    warEngine.postRemoveEntity(bullet)
                }
            }
        }
    }
}
