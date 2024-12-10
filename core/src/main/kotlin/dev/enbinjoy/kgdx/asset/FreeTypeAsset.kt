package dev.enbinjoy.kgdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader

class FreeTypeAsset(
    name: String,
    extension: String,
    private val fontFileName: String,
) : Asset<BitmapFont>(
    name = name,
    extension = extension,
) {
    override val directory: String
        get() = "freetype"

    override val assetType: Class<BitmapFont>
        get() = BitmapFont::class.java

    override val params: AssetLoaderParameters<BitmapFont>
        get() = FreetypeFontLoader.FreeTypeFontLoaderParameter().also {
            it.fontFileName = "$directory/$fontFileName${if (extension.isEmpty()) "" else "."}$extension"
        }
}
