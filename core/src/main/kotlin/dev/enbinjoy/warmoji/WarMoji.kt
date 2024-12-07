package dev.enbinjoy.warmoji

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.mazatech.gdx.SVGAssetsConfigGDX
import com.mazatech.gdx.SVGAssetsGDX
import java.util.zip.ZipInputStream
import kotlin.math.roundToInt

class WarMoji : ApplicationAdapter() {
    private lateinit var spriteBatch: SpriteBatch
    private lateinit var texture: Texture

    override fun create() {
        super.create()
        spriteBatch = SpriteBatch()
        texture = createEmojiTexture()
    }

    override fun render() {
        super.render()
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)
        spriteBatch.begin()
        spriteBatch.draw(texture, 140f, 210f)
        spriteBatch.end()
    }

    override fun dispose() {
        texture.dispose()
        spriteBatch.dispose()
        super.dispose()
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
