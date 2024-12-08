package dev.enbinjoy.kgdx

import com.badlogic.gdx.utils.Disposable

interface Scene : Disposable {
    fun resize(width: Int, height: Int)

    fun resume()

    fun render(deltaTime: Float)

    fun pause()

    companion object {
        fun List<Scene>.resize(width: Int, height: Int) {
            forEach { it.resize(width, height) }
        }

        fun List<Scene>.resume() {
            forEach { it.resume() }
        }

        fun List<Scene>.render(deltaTime: Float) {
            forEach { it.render(deltaTime) }
        }

        fun List<Scene>.pause() {
            asReversed().forEach { it.pause() }
        }

        fun List<Scene>.dispose() {
            asReversed().forEach { it.dispose() }
        }
    }
}
