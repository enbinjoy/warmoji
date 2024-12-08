package dev.enbinjoy.warmoji.engine.system

import dev.enbinjoy.warmoji.engine.WarSystem
import dev.enbinjoy.warmoji.engine.newEnemy
import kotlin.random.Random

class EnemySpawnSystem : WarSystem() {
    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        if (Random.nextFloat() < deltaTime) {
            warEngine.newEnemy()
        }
    }
}
