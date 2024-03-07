package edu.skku.cs.pa2.maze.model

enum class Direction(val degree: Float, val dy: Int, val dx: Int) {
    LEFT(270f, 0, -1),
    UP(0f, -1, 0),
    DOWN(180f, 1, 0),
    RIGHT(90f, 0, 1)
}
