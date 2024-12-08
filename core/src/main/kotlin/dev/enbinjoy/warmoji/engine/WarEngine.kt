package dev.enbinjoy.warmoji.engine

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import dev.enbinjoy.kgdx.Scene
import dev.enbinjoy.kgdx.utils.copy
import dev.enbinjoy.warmoji.warMoji
import kotlin.math.max
import kotlin.random.Random

class WarEngine : PooledEngine(), Scene {
    private class BackgroundTile(
        val texture: Texture,
        val x: Float,
        val y: Float,
        val scale: Float,
        val rotation: Float,
    )

    private val viewport: ScreenViewport = object : ScreenViewport() {
        override fun update(screenWidth: Int, screenHeight: Int, centerCamera: Boolean) {
            unitsPerPixel = max(TILES_PER_SCREEN / screenWidth, TILES_PER_SCREEN / screenHeight)
            super.update(screenWidth, screenHeight, centerCamera)
        }
    }

    init {
        viewport.camera.position.x = COLUMNS / 2f
        viewport.camera.position.y = ROWS / 2f
        viewport.camera.update()
    }

    private val shapeRenderer: ShapeRenderer = ShapeRenderer().also {
        it.setColor(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
    }

    private val batch: SpriteBatch = SpriteBatch()

    private val backgroundTileList: List<BackgroundTile> = createBackgroundTileList()

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun resume() {
    }

    override fun render(deltaTime: Float) {
        val directionX = when {
            Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isTouched(0) && Gdx.input.getX(0) < Gdx.graphics.width / 3 -> -1
            Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A) ||
                Gdx.input.isTouched(0) && Gdx.input.getX(0) > Gdx.graphics.width * 2 / 3 -> 1
            else -> 0
        }
        val directionY = when {
            Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W) ||
                Gdx.input.isTouched(0) && Gdx.input.getY(0) > Gdx.graphics.height * 2 / 3 -> -1
            Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S) ||
                Gdx.input.isTouched(0) && Gdx.input.getY(0) < Gdx.graphics.height / 3 -> 1
            else -> 0
        }
        viewport.camera.position.x = (viewport.camera.position.x + directionX * CAMERA_SPEED * deltaTime)
            .coerceIn(0f, COLUMNS.toFloat())
        viewport.camera.position.y = (viewport.camera.position.y + directionY * CAMERA_SPEED * deltaTime)
            .coerceIn(0f, ROWS.toFloat())
        viewport.camera.update()

        ScreenUtils.clear(Color.CLEAR)
        shapeRenderer.projectionMatrix = viewport.camera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.rect(0f, 0f, COLUMNS.toFloat(), ROWS.toFloat())
        shapeRenderer.end()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        batch.color = Color.WHITE.copy(a = BACKGROUND_EMOJI_ALPHA)
        backgroundTileList.forEach { backgroundTile ->
            batch.draw(backgroundTile.texture, backgroundTile.x, backgroundTile.y, 0.5f, 0.5f, 1f, 1f,
                backgroundTile.scale, backgroundTile.scale, backgroundTile.rotation, 0, 0,
                backgroundTile.texture.width, backgroundTile.texture.height, false, true)
        }
        batch.end()
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
        private const val BACKGROUND_EMOJI_COUNT = 5
        private const val BACKGROUND_EMOJI_DENSITY = 0.2f
        private const val BACKGROUND_EMOJI_OFFSET = 0.5f
        private const val BACKGROUND_EMOJI_SCALE_MIN = 0.25f
        private const val BACKGROUND_EMOJI_SCALE_MAX = 0.75f
        private const val BACKGROUND_EMOJI_ALPHA = 0.2f
        private const val CAMERA_SPEED = 4f

        private fun createBackgroundTileList(): List<BackgroundTile> {
            val textureList = warMoji.openMojiManager.textureList.shuffled().take(BACKGROUND_EMOJI_COUNT)
            val backgroundTileList = mutableListOf<BackgroundTile>()
            for (y in 1 until ROWS - 1) {
                for (x in 1 until COLUMNS - 1) {
                    if (Random.nextFloat() > BACKGROUND_EMOJI_DENSITY) continue
                    val backgroundTile = BackgroundTile(
                        texture = textureList.random(),
                        x = x + (if (Random.nextBoolean()) 1f else -1f) * Random.nextFloat() * BACKGROUND_EMOJI_OFFSET,
                        y = y + (if (Random.nextBoolean()) 1f else -1f) * Random.nextFloat() * BACKGROUND_EMOJI_OFFSET,
                        scale = Random.nextFloat() * (BACKGROUND_EMOJI_SCALE_MAX - BACKGROUND_EMOJI_SCALE_MIN) +
                            BACKGROUND_EMOJI_SCALE_MIN,
                        rotation = Random.nextFloat() * 360f,
                    )
                    backgroundTileList.add(backgroundTile)
                }
            }
            return backgroundTileList
        }
    }
}
