package dev.enbinjoy.warmoji.engine.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import dev.enbinjoy.warmoji.engine.Mappers
import dev.enbinjoy.warmoji.engine.PlayerComponent
import dev.enbinjoy.warmoji.engine.require
import kotlin.math.sqrt

class InputSystem : EntitySystem() {
    private lateinit var player: Entity

    override fun addedToEngine(engine: Engine) {
        super.addedToEngine(engine)
        player = engine.getEntitiesFor(Family.all(PlayerComponent::class.java).get()).single()
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        val directionX = when {
            Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) ||
                Gdx.input.isTouched(0) && Gdx.input.getX(0) < Gdx.graphics.width / 3 -> -1f
            Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.A) ||
                Gdx.input.isTouched(0) && Gdx.input.getX(0) > Gdx.graphics.width * 2 / 3 -> 1f
            else -> 0f
        }
        val directionY = when {
            Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W) ||
                Gdx.input.isTouched(0) && Gdx.input.getY(0) > Gdx.graphics.height * 2 / 3 -> -1f
            Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S) ||
                Gdx.input.isTouched(0) && Gdx.input.getY(0) < Gdx.graphics.height / 3 -> 1f
            else -> 0f
        }
        val playerDirection = Mappers.direction.require(player)
        playerDirection.x = when {
            directionX == 0f -> 0f
            directionY == 0f -> directionX
            else -> directionX / sqrt(2f)
        }
        playerDirection.y = when {
            directionY == 0f -> 0f
            directionX == 0f -> directionY
            else -> directionY / sqrt(2f)
        }
    }
}
