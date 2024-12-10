package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import dev.enbinjoy.warmoji.engine.BreathingComponent
import dev.enbinjoy.warmoji.engine.WarSystem

class BreathingSystem : WarSystem() {
    private lateinit var entityArray: ImmutableArray<Entity>

    override fun init() {
        super.init()
        entityArray = warEngine.getEntitiesFor(Family.all(BreathingComponent::class.java).get())
    }

    override fun update(isTick: Boolean, deltaTime: Float) {
        super.update(isTick, deltaTime)
        entityArray.forEach { entity ->
            val breathing = entity.getComponent(BreathingComponent::class.java)
            breathing.stateTime += deltaTime
            var value = ((breathing.stateTime % (breathing.duration * 2f)) + breathing.duration * 2f) %
                (breathing.duration * 2f) / breathing.duration
            if (value > 1f) {
                value = 2f - value
            }
            breathing.scaleX = 1f + breathing.scalePercent * value
            breathing.scaleY = 1f - breathing.scalePercent * value
        }
    }
}
