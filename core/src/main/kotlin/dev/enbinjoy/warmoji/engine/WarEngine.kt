package dev.enbinjoy.warmoji.engine

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import dev.enbinjoy.kgdx.Scene
import dev.enbinjoy.warmoji.engine.system.BackgroundRenderSystem
import dev.enbinjoy.warmoji.engine.system.CameraSystem
import dev.enbinjoy.warmoji.engine.system.InputSystem
import dev.enbinjoy.warmoji.engine.system.MovementSystem
import dev.enbinjoy.warmoji.engine.system.TextureRenderSystem

val EntitySystem.warEngine: WarEngine
    get() = engine as WarEngine

class WarEngine : PooledEngine(), Scene {
    val viewport: WarViewport = WarViewport()

    val rows: Int = ROWS
    val columns: Int = COLUMNS

    val shapeRenderer: ShapeRenderer = ShapeRenderer()

    val batch: SpriteBatch = SpriteBatch()

    init {
        newPlayer()

        addSystem(InputSystem())
        addSystem(MovementSystem())
        addSystem(CameraSystem())
        addSystem(BackgroundRenderSystem())
        addSystem(TextureRenderSystem())
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun resume() {
    }

    override fun render(deltaTime: Float) {
        update(deltaTime)
    }

    override fun pause() {
    }

    override fun dispose() {
        batch.dispose()
        shapeRenderer.dispose()
    }

    companion object {
        private const val ROWS = 25
        private const val COLUMNS = 25
    }
}
