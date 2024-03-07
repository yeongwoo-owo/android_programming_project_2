package edu.skku.cs.pa2.maze.data

import android.util.Log
import edu.skku.cs.pa2.maze.model.MazeCell
import edu.skku.cs.pa2.maze.model.MazeWall

data class MazeResponse(val maze: String) {

    fun parse(): List<List<MazeCell>> {
        val lines = maze.split("\n")
        val size = lines[0].toInt()
        val rows = lines.subList(1, size + 1)
        Log.d("MazeResponse", rows.toString())

        return rows.map { row ->
            row.dropLast(1).split(" ").map {
                MazeCell(parseMazeCellShape(it.toInt()))
            }
        }
    }

    private fun parseMazeCellShape(n: Int): List<MazeWall> {
        return MazeWall.values().filter { (n / it.value).isOdd() }
    }

    private fun Int.isOdd(): Boolean {
        return this % 2 == 1;
    }
}