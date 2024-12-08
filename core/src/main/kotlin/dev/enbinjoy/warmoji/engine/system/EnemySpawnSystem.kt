package dev.enbinjoy.warmoji.engine.system

import dev.enbinjoy.warmoji.engine.WarSystem
import dev.enbinjoy.warmoji.engine.newEnemy
import kotlin.random.Random

class EnemySpawnSystem : WarSystem() {
    override fun tick(tickDeltaTime: Float) {
        super.tick(tickDeltaTime)
        if (Random.nextFloat() < tickDeltaTime) {
            warEngine.newEnemy()
        }
    }
}
