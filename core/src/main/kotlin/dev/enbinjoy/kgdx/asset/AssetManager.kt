package dev.enbinjoy.kgdx.asset

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader

typealias GdxAssetManager = com.badlogic.gdx.assets.AssetManager

class AssetManager : GdxAssetManager(InternalFileHandleResolver(), false) {
    init {
        setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(fileHandleResolver))
        setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(fileHandleResolver))
    }
}
