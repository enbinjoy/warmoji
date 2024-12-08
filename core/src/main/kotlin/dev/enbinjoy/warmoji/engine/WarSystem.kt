package dev.enbinjoy.warmoji.engine

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem

abstract class WarSystem : EntitySystem() {
    protected lateinit var warEngine: WarEngine
        private set

    final override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        warEngine = engine as WarEngine
        addedToEngine()
    }

    protected open fun addedToEngine() {
    }

    final override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine)
        removedFromEngine()
    }

    protected open fun removedFromEngine() {
    }

    open fun resize(width: Float, height: Float) {
    }

    open fun resume() {
    }

    open fun pause() {
    }

    @Deprecated("", ReplaceWith("warEngine"))
    final override fun getEngine(): Engine {
        return super.getEngine()
    }
}
