package dev.enbinjoy.warmoji.engine

import com.badlogic.gdx.utils.viewport.ScreenViewport
import kotlin.math.max

class WarViewport : ScreenViewport() {
    override fun update(screenWidth: Int, screenHeight: Int, centerCamera: Boolean) {
        unitsPerPixel = max(TILES_PER_SCREEN / screenWidth, TILES_PER_SCREEN / screenHeight)
        super.update(screenWidth, screenHeight, centerCamera)
    }

    companion object {
        private const val TILES_PER_SCREEN = 12f
    }
}
