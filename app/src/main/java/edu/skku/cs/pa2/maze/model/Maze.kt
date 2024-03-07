package edu.skku.cs.pa2.maze.model

import android.util.Log
import java.util.*

class Maze(val cells: List<List<MazeCell>>) {

    val size = cells.size
    var hintPosition: Position? = null
    var goalPosition = Position(size - 1, size - 1)

    fun hasWall(at: Position, direction: Direction): Boolean {
        val cell = cells[at.y][at.x]
        return cell.hasWall(direction)
    }

    fun findHint(from: Position) {
        if (from == goalPosition) return

        val queue: LinkedList<PositionPair> = LinkedList()
        val visited = mutableSetOf<Position>()

        val cell = cells[from.y][from.x]
        Direction.values().forEach {
            if (!cell.hasWall(it)) {
                queue.add(PositionPair(
                    startPosition = Position(from.y + it.dy, from.x + it.dx),
                    curPosition = Position(from.y + it.dy, from.x + it.dx),
                ))
            }
        }

        while (!queue.isEmpty()) {
            val pair = queue.removeFirst()
            if (pair.curPosition == goalPosition) {
                Log.d("maze", "Hint found, Position: $hintPosition")
                hintPosition = pair.startPosition
                return
            }
            if (visited.contains(pair.curPosition)) continue

            visited.add(pair.curPosition)
            val currentCell = cells[pair.curPosition.y][pair.curPosition.x]
            Direction.values().forEach {
                if (!currentCell.hasWall(it)) {
                    queue.add(PositionPair(
                        startPosition = pair.startPosition,
                        curPosition = Position(
                            pair.curPosition.y + it.dy,
                            pair.curPosition.x + it.dx
                        )
                    ))
                }
            }
        }
    }

    data class PositionPair(val startPosition: Position, val curPosition: Position)
}

class MazeCell(val walls: List<MazeWall>) {
    fun hasWall(at: MazeWall) = walls.contains(at)
    fun hasWall(at: Direction) = walls.contains(
        when (at) {
            Direction.UP -> MazeWall.TOP
            Direction.LEFT -> MazeWall.LEFT
            Direction.DOWN -> MazeWall.BOTTOM
            Direction.RIGHT -> MazeWall.RIGHT
        }
    )
}

enum class MazeWall(val value: Int) {
    TOP(8), LEFT(4), BOTTOM(2), RIGHT(1)
}