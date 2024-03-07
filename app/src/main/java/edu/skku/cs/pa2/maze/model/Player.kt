package edu.skku.cs.pa2.maze.model

import android.util.Log

class Player(
    var position: Position = Position(0, 0),
    var orientation: Direction = Direction.UP
) {
    fun move(direction: Direction) {
        position = Position(y = position.y + direction.dy, x = position.x + direction.dx)
        orientation = direction
        Log.d("Character", "new position: $position, direction: $direction")
    }

    fun turn(direction: Direction) {
        orientation = direction
    }
}
