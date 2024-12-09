package dev.enbinjoy.kgdx

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ScreenUtils
import dev.enbinjoy.kgdx.Scene.Companion.dispose
import dev.enbinjoy.kgdx.Scene.Companion.pause
import dev.enbinjoy.kgdx.Scene.Companion.render
import dev.enbinjoy.kgdx.Scene.Companion.resize
import dev.enbinjoy.kgdx.Scene.Companion.resume
import dev.enbinjoy.kgdx.dev.DevLogStage

val game: Game
    get() = Gdx.app.applicationListener as Game

abstract class Game : ApplicationListener, Disposable {
    private var canRender: Boolean = false
    private var resumed: Boolean = false

    private lateinit var devLogStage: DevLogStage

    var sceneList: List<Scene>? = null
        set(value) {
            val resumed = resumed
            if (resumed) {
                field?.pause()
            }
            field?.dispose()
            field = value
            field?.resize(Gdx.graphics.width, Gdx.graphics.height)
            if (resumed) {
                field?.resume()
            }
        }

    private fun sceneList(): List<Scene> {
        val sceneList = sceneList?.toMutableList() ?: mutableListOf()
        sceneList.add(devLogStage)
        return sceneList
    }

    override fun create() {
        devLogStage = DevLogStage()
        canRender = true
    }

    override fun resize(width: Int, height: Int) {
        sceneList().resize(width, height)
    }

    override fun resume() {
        canRender = true
    }

    override fun render() {
        if (!canRender) return
        if (!resumed) {
            resumed = true
            sceneList().resume()
        }
        ScreenUtils.clear(Color.CLEAR)
        sceneList().render(Gdx.graphics.deltaTime)
    }

    override fun pause() {
        if (resumed) {
            sceneList().pause()
            resumed = false
        }
        canRender = false
    }

    override fun dispose() {
        sceneList = null
        devLogStage.dispose()
    }

    fun devLog(message: String) {
        devLogStage.log(message)
    }
}
