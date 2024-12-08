package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.graphics.Color
import dev.enbinjoy.warmoji.engine.Mappers
import dev.enbinjoy.warmoji.engine.PositionComponent
import dev.enbinjoy.warmoji.engine.SizeComponent
import dev.enbinjoy.warmoji.engine.TextureComponent
import dev.enbinjoy.warmoji.engine.require
import dev.enbinjoy.warmoji.engine.warEngine

class TextureRenderSystem : EntitySystem() {
    private lateinit var entityArray: ImmutableArray<Entity>

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        entityArray = engine.getEntitiesFor(Family.all(
            PositionComponent::class.java,
            SizeComponent::class.java,
            TextureComponent::class.java,
        ).get())
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        warEngine.batch.projectionMatrix = warEngine.viewport.camera.combined
        warEngine.batch.begin()
        warEngine.batch.color = Color.WHITE
        entityArray.forEach { entity ->
            val position = Mappers.position.require(entity)
            val size = Mappers.size.require(entity)
            val texture = Mappers.texture.require(entity)
            val halfWidth = size.width / 2f
            val halfHeight = size.height / 2f
            warEngine.batch.draw(texture.value, position.x - halfWidth, position.y - halfHeight, size.width,
                size.height, 0, 0, texture.value.width, texture.value.height, false, true)
        }
        warEngine.batch.end()
    }
}
