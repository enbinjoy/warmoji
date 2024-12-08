package dev.enbinjoy.warmoji.openmoji

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable
import com.mazatech.gdx.SVGAssetsConfigGDX
import com.mazatech.gdx.SVGAssetsGDX
import dev.enbinjoy.kgdx.utils.fromJson
import java.util.zip.ZipInputStream
import kotlin.math.roundToInt

class OpenMojiManager : Disposable {
    val textureList: List<Texture> = createTextureList()

    override fun dispose() {
        textureList.forEach { it.dispose() }
    }

    companion object {
        private fun createTextureList(): List<Texture> {
            val textureMap = mutableMapOf<String, Texture>()
            val svgAssetsConfigGDX = SVGAssetsConfigGDX(Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight,
                Gdx.graphics.ppiX)
            val svgAssetsGDX = SVGAssetsGDX(svgAssetsConfigGDX)
            val zipFileHandle = Gdx.files.internal("openmoji-svg-color.zip")
            ZipInputStream(zipFileHandle.read(DEFAULT_BUFFER_SIZE)).use { zipInputStream ->
                while (true) {
                    val zipEntry = zipInputStream.nextEntry ?: break
                    val hexcode = zipEntry.name.removeSuffix(".svg")
                    val xmlText = zipInputStream.bufferedReader().readText()
                    zipInputStream.closeEntry()
                    val svgDocument = svgAssetsGDX.createDocument(xmlText)
                    val texture = svgAssetsGDX.createTexture(svgDocument, svgDocument.viewport.width.roundToInt(),
                        svgDocument.viewport.height.roundToInt())
                    svgDocument.dispose()
                    textureMap[hexcode] = texture
                }
            }
            svgAssetsGDX.dispose()
            val jsonFileHandle = Gdx.files.internal("openmoji.json")
            val openMojiList = jsonFileHandle.readString().fromJson<List<OpenMoji>>()
            val orderMap = openMojiList.associate { it.hexcode to (it.order.toIntOrNull() ?: Int.MAX_VALUE) }
            return textureMap.toSortedMap { o1, o2 ->
                val order1 = orderMap.getValue(o1)
                val order2 = orderMap.getValue(o2)
                order1.compareTo(order2).takeIf { it != 0 } ?: o1.compareTo(o2)
            }.values.toList()
        }
    }
}
