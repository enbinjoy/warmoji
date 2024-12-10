package dev.enbinjoy.warmoji.engine

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import dev.enbinjoy.kgdx.Scene
import dev.enbinjoy.warmoji.engine.system.BackgroundRenderSystem
import dev.enbinjoy.warmoji.engine.system.BreathingSystem
import dev.enbinjoy.warmoji.engine.system.BulletSpawnSystem
import dev.enbinjoy.warmoji.engine.system.CameraSystem
import dev.enbinjoy.warmoji.engine.system.DebugRenderSystem
import dev.enbinjoy.warmoji.engine.system.DevLogSystem
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

    private var started: Boolean = false
    private var resumed: Boolean = false

    init {
        addAllSystems()
        addAllEntities()
    }

    private fun addAllSystems() {
        addSystem(InputSystem())
        addSystem(EnemyAISystem())
        addSystem(MovementSystem())
        addSystem(PlayerEnemyCollisionSystem())
        addSystem(EnemyBulletCollisionSystem())
        addSystem(EnemySpawnSystem())
        addSystem(BulletSpawnSystem())
        addSystem(BreathingSystem())
        addSystem(CameraSystem())
        addSystem(BackgroundRenderSystem())
        addSystem(TextureRenderSystem())
        addSystem(DebugRenderSystem())
        addSystem(DevLogSystem())
    }

    private fun addAllEntities() {
        newPlayer()
    }

    private var needRestart: Boolean = false

    fun restart() {
        needRestart = true
    }

    private var resized: Boolean = false

    override fun resize(width: Int, height: Int) {
        val oldWidth = viewport.worldWidth
        val oldHeight = viewport.worldHeight
        viewport.update(width, height)
        val newWidth = viewport.worldWidth
        val newHeight = viewport.worldHeight
        if (resized && oldWidth == newWidth && oldHeight == newHeight) return
        if (resized) {
            resize(newWidth, newHeight)
        } else {
            resized = true
            resize(newWidth, newHeight)
            start()
        }
    }

    private fun resize(width: Float, height: Float) {
        systems.forEach { system ->
            system as WarSystem
            system.resize(width, height)
        }
    }

    private fun start() {
        if (started) return
        started = true
        systems.forEach { system ->
            system as WarSystem
            system.start()
        }
    }

    override fun resume() {
        if (resumed) return
        resumed = true
        systems.forEach { system ->
            system as WarSystem
            system.resume()
        }
    }

    val tickDeltaTime: Float = TICK_DELTA_TIME

    private var tickAccumulator: Float = 0f

    var isTick: Boolean = false
        private set

    override fun render(deltaTime: Float) {
        val limitedDeltaTime = deltaTime.coerceAtMost(LIMITED_DELTA_TIME)
        tickAccumulator += limitedDeltaTime
        isTick = if (tickAccumulator >= tickDeltaTime) {
            tickAccumulator -= tickDeltaTime
            true
        } else {
            false
        }
        update(limitedDeltaTime)
        postRunnableList.forEach { it() }
        postRunnableList.clear()
        if (needRestart) {
            needRestart = false
            pause()
            stop()
            removeAllEntities()
            addAllEntities()
            resize(viewport.worldWidth, viewport.worldHeight)
            start()
            resume()
        }
    }

    override fun pause() {
        if (!resumed) return
        systems.reversed().forEach { system ->
            system as WarSystem
            system.pause()
        }
        resumed = false
    }

    private fun stop() {
        if (!started) return
        systems.reversed().forEach { system ->
            system as WarSystem
            system.stop()
        }
        started = false
    }

    override fun dispose() {
        stop()
        removeAllEntities()
        removeAllSystems()
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

        private const val LIMITED_DELTA_TIME = 1f / 30f
        private const val TICK_DELTA_TIME = 1f / 30f
    }
}
