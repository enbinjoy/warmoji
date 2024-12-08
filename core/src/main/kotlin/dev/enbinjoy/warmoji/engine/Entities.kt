package dev.enbinjoy.warmoji.engine

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import dev.enbinjoy.warmoji.warMoji

fun WarEngine.newPlayer(): Entity {
    return entity {
        component<PlayerComponent>()
        component<PositionComponent> {
            x = columns / 2f
            y = rows / 2f
        }
        component<SizeComponent> {
            width = 1f
            height = 1f
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

private fun WarEngine.entity(configure: Pair<WarEngine, Entity>.() -> Unit = {}): Entity {
    val entity = createEntity()
    val pair = this to entity
    pair.configure()
    addEntity(entity)
    return entity
}

private inline fun <reified T : Component> Pair<WarEngine, Entity>.component(configure: T.() -> Unit = {}): T {
    val (engine, entity) = this
    val component = engine.createComponent(T::class.java)
    component.configure()
    entity.add(component)
    return component
}
