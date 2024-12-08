package dev.enbinjoy.warmoji.engine

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture

object PlayerComponent : Component

class PositionComponent : Component {
    var x: Float = 0f
    var y: Float = 0f
}

class SizeComponent : Component {
    var width: Float = 0f
    var height: Float = 0f
}

class SpeedComponent : Component {
    var value: Float = 0f
}

class DirectionComponent : Component {
    var x: Float = 0f
    var y: Float = 0f
}

class TextureComponent : Component {
    lateinit var value: Texture
}

object Mappers {
    val position: ComponentMapper<PositionComponent> = ComponentMapper.getFor(PositionComponent::class.java)
    val size: ComponentMapper<SizeComponent> = ComponentMapper.getFor(SizeComponent::class.java)
    val speed: ComponentMapper<SpeedComponent> = ComponentMapper.getFor(SpeedComponent::class.java)
    val direction: ComponentMapper<DirectionComponent> = ComponentMapper.getFor(DirectionComponent::class.java)
    val texture: ComponentMapper<TextureComponent> = ComponentMapper.getFor(TextureComponent::class.java)
}

fun <T : Component> ComponentMapper<T>.require(entity: Entity): T {
    return requireNotNull(get(entity))
}
