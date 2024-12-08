package dev.enbinjoy.warmoji.engine

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport

class WarRenderer(private val viewport: Viewport) : Disposable {
    private val shapeRenderer: ShapeRenderer = ShapeRenderer()

    private val spriteBatch: SpriteBatch = SpriteBatch()

    fun useShape(
        type: ShapeRenderer.ShapeType,
        color: Color = Color.WHITE,
        action: ShapeRenderer.() -> Unit,
    ) {
        shapeRenderer.projectionMatrix = viewport.camera.combined
        shapeRenderer.begin(type)
        shapeRenderer.color = color
        shapeRenderer.action()
        shapeRenderer.end()
    }

    fun useBatch(
        color: Color = Color.WHITE,
        action: SpriteBatch.() -> Unit,
    ) {
        spriteBatch.projectionMatrix = viewport.camera.combined
        spriteBatch.begin()
        spriteBatch.color = color
        spriteBatch.action()
        spriteBatch.end()
    }

    override fun dispose() {
        spriteBatch.dispose()
        shapeRenderer.dispose()
    }
}
