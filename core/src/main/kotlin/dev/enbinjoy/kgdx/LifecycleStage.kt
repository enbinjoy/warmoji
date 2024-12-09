package dev.enbinjoy.kgdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport

abstract class LifecycleStage : Stage, Scene {
    constructor(viewport: Viewport = defaultViewport()) : super(viewport)

    constructor(viewport: Viewport = defaultViewport(), batch: Batch) : super(viewport, batch)

    private var resized: Boolean = false

    protected open val centerCameraOnResize: Boolean = true

    override fun resize(width: Int, height: Int) {
        diffSize {
            viewport.update(width, height, centerCameraOnResize)
        }
    }

    protected open fun resize(width: Float, height: Float) {
    }

    final override fun setViewport(viewport: Viewport) {
        diffSize {
            super.setViewport(viewport)
        }
    }

    private fun diffSize(updateViewport: () -> Unit) {
        val oldWidth = width
        val oldHeight = height
        updateViewport()
        val newWidth = width
        val newHeight = height
        if (resized && oldWidth == newWidth && oldHeight == newHeight) {
            return
        }
        if (!resized) {
            resized = true
        }
        resize(newWidth, newHeight)
    }

    override fun resume() {
    }

    final override fun render(deltaTime: Float) {
        act(deltaTime)
        viewport.apply()
        draw()
    }

    override fun pause() {
    }

    @Deprecated("", ReplaceWith("act(delta)"))
    final override fun act() {
        super.act()
    }

    companion object {
        private fun defaultViewport(): Viewport {
            return StretchViewport(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        }
    }
}
