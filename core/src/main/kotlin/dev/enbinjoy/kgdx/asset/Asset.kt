package dev.enbinjoy.kgdx.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import dev.enbinjoy.kgdx.game

sealed class Asset<T>(
    val name: String,
    val extension: String,
) {
    abstract val directory: String

    abstract val assetType: Class<T>

    abstract val params: AssetLoaderParameters<T>

    private fun assetDescriptor(): AssetDescriptor<T> {
        val fileName = "$directory/$name${if (extension.isEmpty()) "" else "."}$extension"
        return AssetDescriptor(fileName, assetType, params)
    }

    fun get(): T {
        val assetDescriptor = assetDescriptor()
        return if (game.assetManager.isLoaded(assetDescriptor)) {
            game.assetManager.get(assetDescriptor)
        } else {
            game.assetManager.load(assetDescriptor)
            game.assetManager.finishLoadingAsset(assetDescriptor)
        }
    }

    fun unload() {
        game.assetManager.unload(assetDescriptor().fileName)
    }
}
