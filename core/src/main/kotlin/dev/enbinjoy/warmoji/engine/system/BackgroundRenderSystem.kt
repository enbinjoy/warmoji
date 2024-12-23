package dev.enbinjoy.warmoji.engine.system

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import dev.enbinjoy.kgdx.util.copy
import dev.enbinjoy.warmoji.engine.WarSystem
import dev.enbinjoy.warmoji.warMoji
import kotlin.random.Random

class BackgroundRenderSystem : WarSystem() {
    private class Tile(
        val texture: Texture,
        val x: Float,
        val y: Float,
        val scale: Float,
        val rotation: Float,
    )

    private lateinit var backgroundColor: Color

    private lateinit var tileList: List<Tile>

    override fun start() {
        super.start()
        backgroundColor = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f)
        val textureList = warMoji.openMojiManager.textureList.shuffled().take(EMOJI_COUNT)
        val tileList = mutableListOf<Tile>()
        for (y in 1 until warEngine.rows - 1) {
            for (x in 1 until warEngine.columns - 1) {
                if (Random.nextFloat() > EMOJI_DENSITY) continue
                val tile = Tile(
                    texture = textureList.random(),
                    x = x + (if (Random.nextBoolean()) 1f else -1f) * Random.nextFloat() * EMOJI_OFFSET,
                    y = y + (if (Random.nextBoolean()) 1f else -1f) * Random.nextFloat() * EMOJI_OFFSET,
                    scale = Random.nextFloat() * (EMOJI_SCALE_MAX - EMOJI_SCALE_MIN) + EMOJI_SCALE_MIN,
                    rotation = Random.nextFloat() * 360f,
                )
                tileList.add(tile)
            }
        }
        this.tileList = tileList
    }

    override fun update(isTick: Boolean, deltaTime: Float) {
        super.update(isTick, deltaTime)
        warEngine.renderer.useShape(
            type = ShapeRenderer.ShapeType.Filled,
            color = backgroundColor,
        ) {
            rect(0f, 0f, warEngine.columns.toFloat(), warEngine.rows.toFloat())
        }
        warEngine.renderer.useBatch(
            color = Color.WHITE.copy(a = EMOJI_ALPHA),
        ) {
            tileList.forEach { tile ->
                draw(tile.texture, tile.x, tile.y, 0.5f, 0.5f, 1f, 1f, tile.scale, tile.scale, tile.rotation, 0, 0,
                    tile.texture.width, tile.texture.height, false, true)
            }
        }
    }

    companion object {
        private const val EMOJI_COUNT = 5
        private const val EMOJI_DENSITY = 0.2f
        private const val EMOJI_OFFSET = 0.5f
        private const val EMOJI_SCALE_MIN = 0.25f
        private const val EMOJI_SCALE_MAX = 0.75f
        private const val EMOJI_ALPHA = 0.2f
    }
}
