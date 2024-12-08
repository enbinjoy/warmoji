package dev.enbinjoy.warmoji.engine

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.ScreenViewport
import dev.enbinjoy.kgdx.Scene
import dev.enbinjoy.warmoji.engine.system.BackgroundRenderSystem
import dev.enbinjoy.warmoji.engine.system.CameraSystem
import dev.enbinjoy.warmoji.engine.system.InputSystem
import dev.enbinjoy.warmoji.engine.system.MovementSystem
import dev.enbinjoy.warmoji.engine.system.TextureRenderSystem
import kotlin.math.max

val EntitySystem.warEngine: WarEngine
    get() = engine as WarEngine

class WarEngine : PooledEngine(), Scene {
    val viewport: ScreenViewport = object : ScreenViewport() {
        override fun update(screenWidth: Int, screenHeight: Int, centerCamera: Boolean) {
            unitsPerPixel = max(TILES_PER_SCREEN / screenWidth, TILES_PER_SCREEN / screenHeight)
            super.update(screenWidth, screenHeight, centerCamera)
        }
    }

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
        private const val TILES_PER_SCREEN = 12f
        private const val ROWS = 25
        private const val COLUMNS = 25
    }
}
