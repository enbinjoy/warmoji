package dev.enbinjoy.kgdx.dev

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import dev.enbinjoy.kgdx.LifecycleStage
import kotlin.math.max

class DevLogStage : LifecycleStage(object : ScreenViewport() {
    override fun update(screenWidth: Int, screenHeight: Int, centerCamera: Boolean) {
        unitsPerPixel = max(UNITS / screenWidth, UNITS / screenHeight)
        super.update(screenWidth, screenHeight, centerCamera)
    }
}) {
    private val bitmapFont: BitmapFont = BitmapFont()

    private val label: Label = Label(null, Label.LabelStyle(bitmapFont, null)).also {
        it.setAlignment(Align.bottomLeft)
        addActor(it)
    }

    private val logList: MutableList<String> = mutableListOf()

    fun log(message: String) {
        logList.add(message)
    }

    override fun act(delta: Float) {
        super.act(delta)
        label.setText(logList.joinToString("\n"))
        logList.clear()
    }

    override fun dispose() {
        bitmapFont.dispose()
        super.dispose()
    }

    companion object {
        private const val UNITS = 720f
    }
}
