package dev.enbinjoy.kgdx.utils

import com.badlogic.gdx.graphics.Color

fun Color.copy(
    r: Float = this.r,
    g: Float = this.g,
    b: Float = this.b,
    a: Float = this.a,
): Color {
    return Color(r, g, b, a)
}
