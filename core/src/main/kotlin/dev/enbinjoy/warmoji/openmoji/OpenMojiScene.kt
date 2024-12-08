package dev.enbinjoy.warmoji.openmoji

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import dev.enbinjoy.kgdx.Scene
import dev.enbinjoy.warmoji.warMoji
import kotlin.math.ceil
import kotlin.math.sqrt

class OpenMojiScene : Scene {
    private class RenderData(
        val size: Float,
        val tileList: List<Tile>,
    ) {
        class Tile(
            val texture: Texture,
            val x: Float,
            val y: Float,
        )
    }

    private val camera: OrthographicCamera = OrthographicCamera()

    private val batch: SpriteBatch = SpriteBatch()

    private lateinit var renderData: RenderData

    override fun resize(width: Int, height: Int) {
        camera.setToOrtho(true, width.toFloat(), height.toFloat())
        renderData = createRenderData(width, height)
    }

    override fun resume() {
    }

    override fun render(deltaTime: Float) {
        ScreenUtils.clear(Color.WHITE)
        batch.projectionMatrix = camera.combined
        batch.begin()
        renderData.tileList.forEach { tile ->
            batch.draw(tile.texture, tile.x, tile.y, renderData.size, renderData.size)
        }
        batch.end()
    }

    override fun pause() {
    }

    override fun dispose() {
        batch.dispose()
    }

    companion object {
        private fun createRenderData(width: Int, height: Int): RenderData {
            if (width <= 0 || height <= 0) {
                return RenderData(0f, emptyList())
            }
            val textureList = warMoji.openMojiManager.textureList
            var columns = ceil(sqrt(textureList.size.toFloat() / height * width)).toInt() - 1
            var rows: Int
            do {
                ++columns
                rows = ceil(textureList.size.toFloat() / columns).toInt()
            } while (width.toFloat() / columns * rows > height)
            val size = width.toFloat() / columns
            val tileList = textureList.mapIndexed { index, texture ->
                val x = index % columns * size
                val y = index / columns * size
                RenderData.Tile(texture, x, y)
            }
            return RenderData(size, tileList)
        }
    }
}
