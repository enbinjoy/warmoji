package dev.enbinjoy.kgdx.dev

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import dev.enbinjoy.kgdx.LifecycleStage
import dev.enbinjoy.kgdx.asset.FreeTypeAsset
import kotlin.math.max

class DevLogStage : LifecycleStage(object : ScreenViewport() {
    override fun update(screenWidth: Int, screenHeight: Int, centerCamera: Boolean) {
        unitsPerPixel = max(UNITS / screenWidth, UNITS / screenHeight)
        super.update(screenWidth, screenHeight, centerCamera)
    }
}) {
    private val freeTypeAsset: FreeTypeAsset = FreeTypeAsset(
        name = "dev",
        extension = "ttf",
        fontFileName = "noto_sans_mono",
    )

    private val label: Label = Label(null, Label.LabelStyle(freeTypeAsset.get(), null)).also {
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

    companion object {
        private const val UNITS = 720f
    }
}
