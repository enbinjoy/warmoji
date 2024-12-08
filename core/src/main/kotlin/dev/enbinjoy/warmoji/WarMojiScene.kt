package dev.enbinjoy.warmoji

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.mazatech.gdx.SVGAssetsConfigGDX
import com.mazatech.gdx.SVGAssetsGDX
import dev.enbinjoy.kgdx.Scene
import java.util.zip.ZipInputStream
import kotlin.math.roundToInt

class WarMojiScene : Scene {
    private val spriteBatch: SpriteBatch = SpriteBatch()
    private val texture: Texture = createEmojiTexture()

    override fun resize(width: Int, height: Int) {
    }

    override fun resume() {
    }

    override fun render(deltaTime: Float) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)
        spriteBatch.begin()
        spriteBatch.draw(texture, 140f, 210f)
        spriteBatch.end()
    }

    override fun pause() {
    }

    override fun dispose() {
        texture.dispose()
        spriteBatch.dispose()
    }

    companion object {
        private fun createEmojiTexture(): Texture {
            val zipFileHandle = Gdx.files.internal("openmoji-svg-color.zip")
            ZipInputStream(zipFileHandle.read(DEFAULT_BUFFER_SIZE)).use { zipInputStream ->
                zipInputStream.nextEntry
                val xmlText = zipInputStream.bufferedReader().readText()
                zipInputStream.closeEntry()
                val svgAssetsConfigGDX = SVGAssetsConfigGDX(Gdx.graphics.backBufferWidth,
                    Gdx.graphics.backBufferHeight, Gdx.graphics.ppiX)
                val svgAssetsGDX = SVGAssetsGDX(svgAssetsConfigGDX)
                val svgDocument = svgAssetsGDX.createDocument(xmlText)
                val texture = svgAssetsGDX.createTexture(svgDocument, svgDocument.viewport.width.roundToInt(),
                    svgDocument.viewport.height.roundToInt())
                svgDocument.dispose()
                svgAssetsGDX.dispose()
                return texture
            }
        }
    }
}
