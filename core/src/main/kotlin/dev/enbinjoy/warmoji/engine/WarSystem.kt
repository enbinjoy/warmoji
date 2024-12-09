package dev.enbinjoy.warmoji.engine

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem

/**
 * Systems should not hold disposable resources.
 */
abstract class WarSystem : EntitySystem() {
    protected lateinit var warEngine: WarEngine
        private set

    final override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        warEngine = engine as WarEngine
        init()
    }

    protected open fun init() {
    }

    final override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
    }

    final override fun update(deltaTime: Float) {
        super.update(deltaTime)
        if (warEngine.isTick) {
            tick(warEngine.tickDeltaTime)
            updateWithTick()
        } else {
            updateWithoutTick(deltaTime)
        }
    }

    protected open fun tick(tickDeltaTime: Float) {
    }

    protected open fun updateWithTick() {
    }

    protected open fun updateWithoutTick(deltaTime: Float) {
    }

    open fun resize(width: Float, height: Float) {
    }

    open fun start() {
    }

    open fun resume() {
    }

    open fun pause() {
    }

    open fun stop() {
    }

    @Deprecated("", ReplaceWith("warEngine"))
    final override fun getEngine(): Engine {
        return super.getEngine()
    }
}
