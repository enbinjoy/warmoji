package dev.enbinjoy.warmoji.engine

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import dev.enbinjoy.kgdx.Scene
import dev.enbinjoy.warmoji.engine.system.BackgroundRenderSystem
import dev.enbinjoy.warmoji.engine.system.BulletSpawnSystem
import dev.enbinjoy.warmoji.engine.system.CameraSystem
import dev.enbinjoy.warmoji.engine.system.DebugRenderSystem
import dev.enbinjoy.warmoji.engine.system.EnemyAISystem
import dev.enbinjoy.warmoji.engine.system.EnemyBulletCollisionSystem
import dev.enbinjoy.warmoji.engine.system.EnemySpawnSystem
import dev.enbinjoy.warmoji.engine.system.InputSystem
import dev.enbinjoy.warmoji.engine.system.MovementSystem
import dev.enbinjoy.warmoji.engine.system.PlayerEnemyCollisionSystem
import dev.enbinjoy.warmoji.engine.system.TextureRenderSystem

class WarEngine : PooledEngine(), Scene {
    val viewport: WarViewport = WarViewport()

    val renderer: WarRenderer = WarRenderer(viewport)

    val rows: Int = ROWS
    val columns: Int = COLUMNS

    init {
        addEntitiesAndSystems()
    }

    private fun addEntitiesAndSystems() {
        newPlayer()

        addSystem(InputSystem())
        addSystem(EnemyAISystem())
        addSystem(MovementSystem())
        addSystem(PlayerEnemyCollisionSystem())
        addSystem(EnemyBulletCollisionSystem())
        addSystem(EnemySpawnSystem())
        addSystem(BulletSpawnSystem())
        addSystem(CameraSystem())
        addSystem(BackgroundRenderSystem())
        addSystem(TextureRenderSystem())
        addSystem(DebugRenderSystem())
    }

    private fun removeEntitiesAndSystems() {
        removeAllSystems()
        removeAllEntities()
    }

    fun newWar() {
        postRunnable {
            removeEntitiesAndSystems()
            addEntitiesAndSystems()
        }
    }

    private var resized: Boolean = false

    override fun resize(width: Int, height: Int) {
        val oldWidth = viewport.worldWidth
        val oldHeight = viewport.worldHeight
        viewport.update(width, height)
        val newWidth = viewport.worldWidth
        val newHeight = viewport.worldHeight
        if (resized && oldWidth == newWidth && oldHeight == newHeight) return
        if (!resized) {
            resized = true
        }
        systems.forEach { system ->
            system as WarSystem
            system.resize(newWidth, newHeight)
        }
    }

    override fun resume() {
        systems.forEach { system ->
            system as WarSystem
            system.resume()
        }
    }

    override fun render(deltaTime: Float) {
        update(deltaTime)
        postRunnableList.forEach { it() }
        postRunnableList.clear()
    }

    override fun pause() {
        systems.reversed().forEach { system ->
            system as WarSystem
            system.pause()
        }
    }

    override fun dispose() {
        removeEntitiesAndSystems()
        renderer.dispose()
    }

    private val postRunnableList: MutableList<() -> Unit> = mutableListOf()

    fun postRunnable(runnable: () -> Unit) {
        postRunnableList.add(runnable)
    }

    fun postRemoveEntity(entity: Entity) {
        postRunnable {
            removeEntity(entity)
        }
    }

    companion object {
        private const val ROWS = 25
        private const val COLUMNS = 25
    }
}
