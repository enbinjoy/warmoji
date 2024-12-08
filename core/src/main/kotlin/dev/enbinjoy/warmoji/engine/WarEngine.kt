package dev.enbinjoy.warmoji.engine

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import dev.enbinjoy.kgdx.Scene
import dev.enbinjoy.warmoji.engine.system.BackgroundRenderSystem
import dev.enbinjoy.warmoji.engine.system.CameraSystem
import dev.enbinjoy.warmoji.engine.system.InputSystem
import dev.enbinjoy.warmoji.engine.system.MovementSystem
import dev.enbinjoy.warmoji.engine.system.TextureRenderSystem

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

    private var resized: Boolean = false

    override fun resize(width: Int, height: Int) {
        val oldWidth = viewport.worldWidth
        val oldHeight = viewport.worldHeight
        viewport.update(width, height)
        val newWidth = viewport.worldWidth
        val newHeight = viewport.worldHeight
        if (resized && oldWidth == newWidth && oldHeight == newHeight) return
        if (!resized) {
            resized = true
        }
        systems.forEach { system ->
            system as WarSystem
            system.resize(newWidth, newHeight)
        }
    }

    override fun resume() {
        systems.forEach { system ->
            system as WarSystem
            system.resume()
        }
    }

    override fun render(deltaTime: Float) {
        update(deltaTime)
    }

    override fun pause() {
        systems.reversed().forEach { system ->
            system as WarSystem
            system.pause()
        }
    }

    override fun dispose() {
        removeAllSystems()
        removeAllEntities()

        batch.dispose()
        shapeRenderer.dispose()
    }

    companion object {
        private const val ROWS = 25
        private const val COLUMNS = 25
    }
}
