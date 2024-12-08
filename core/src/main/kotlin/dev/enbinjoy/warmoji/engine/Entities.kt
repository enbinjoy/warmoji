package dev.enbinjoy.warmoji.engine

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import dev.enbinjoy.warmoji.warMoji
import kotlin.random.Random

fun WarEngine.newPlayer(): Entity {
    return entity {
        component<PlayerComponent>()
        component<PositionComponent> {
            x = columns / 2f
            y = rows / 2f
            outOfBoundsBehavior = PositionComponent.OutOfBoundsBehavior.CLAMP
        }
        component<SizeComponent> {
            width = 1f
            height = 1f
        }
        component<AttackSpeedComponent> {
            value = 1f
        }
        component<AttackRangeComponent> {
            value = 5f
        }
        component<SpeedComponent> {
            value = 4f
        }
        component<DirectionComponent> {
            x = 0f
            y = 0f
        }
        component<TextureComponent> {
            value = warMoji.openMojiManager.textureList.random()
        }
    }
}

fun WarEngine.newEnemy(): Entity {
    return entity {
        component<EnemyComponent>()
        component<PositionComponent> {
            x = Random.nextFloat() * columns
            y = Random.nextFloat() * rows
            outOfBoundsBehavior = PositionComponent.OutOfBoundsBehavior.CLAMP
        }
        component<SizeComponent> {
            width = 1f
            height = 1f
        }
        component<SpeedComponent> {
            value = 3f
        }
        component<DirectionComponent> {
            x = 0f
            y = 0f
        }
        component<TextureComponent> {
            value = warMoji.openMojiManager.textureList.random()
        }
    }
}

fun WarEngine.newBullet(
    positionX: Float,
    positionY: Float,
    directionX: Float,
    directionY: Float,
): Entity {
    return entity {
        component<BulletComponent>()
        component<PositionComponent> {
            x = positionX
            y = positionY
            outOfBoundsBehavior = PositionComponent.OutOfBoundsBehavior.REMOVE
        }
        component<SizeComponent> {
            width = 0.5f
            height = 0.5f
        }
        component<SpeedComponent> {
            value = 12f
        }
        component<DirectionComponent> {
            x = directionX
            y = directionY
        }
        component<TextureComponent> {
            value = warMoji.openMojiManager.textureList.random()
        }
    }
}

private fun WarEngine.entity(configure: Pair<WarEngine, Entity>.() -> Unit = {}): Entity {
    val entity = createEntity()
    val pair = this to entity
    pair.configure()
    addEntity(entity)
    return entity
}

private inline fun <reified T : Component> Pair<WarEngine, Entity>.component(configure: T.() -> Unit = {}): T {
    val (engine, entity) = this
    val component = engine.component<T>(configure)
    entity.add(component)
    return component
}
