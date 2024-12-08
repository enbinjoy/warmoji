package dev.enbinjoy.warmoji.engine

import kotlin.math.sqrt

object MathUtils {
    fun checkCollision(
        position1: PositionComponent,
        size1: SizeComponent,
        position2: PositionComponent,
        size2: SizeComponent,
    ): Boolean {
        val halfWidth1 = size1.width / 2f
        val halfHeight1 = size1.height / 2f
        val left1 = position1.x - halfWidth1
        val right1 = left1 + size1.width
        val bottom1 = position1.y - halfHeight1
        val top1 = bottom1 + size1.height
        val halfWidth2 = size2.width / 2f
        val halfHeight2 = size2.height / 2f
        val left2 = position2.x - halfWidth2
        val right2 = left2 + size2.width
        val bottom2 = position2.y - halfHeight2
        val top2 = bottom2 + size2.height
        return left1 < right2 && right1 > left2 && bottom1 < top2 && top1 > bottom2
    }

    fun direction(
        positionFrom: PositionComponent,
        positionTo: PositionComponent,
        direction: DirectionComponent,
    ): DirectionComponent {
        val distanceX = positionTo.x - positionFrom.x
        val distanceY = positionTo.y - positionFrom.y
        val distance = sqrt(distanceX * distanceX + distanceY * distanceY)
        direction.x = if (distance == 0f) 0f else distanceX / distance
        direction.y = if (distance == 0f) 0f else distanceY / distance
        return direction
    }
}
