package edu.skku.cs.pa2.maze.view

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout.LayoutParams
import edu.skku.cs.pa2.R
import edu.skku.cs.pa2.maze.model.Maze
import edu.skku.cs.pa2.maze.model.MazeWall
import edu.skku.cs.pa2.maze.model.Player
import edu.skku.cs.pa2.maze.model.Position

class MazeGridAdapter(
    val context: Context,
    val maze: Maze,
    val player: Player,
): BaseAdapter() {

    private val MARGIN_SIZE = 3.toDp()

    override fun getCount(): Int {
        return maze.size * maze.size
    }

    override fun getItem(idx: Int): Any {
        val y = idx / maze.size
        val x = idx % maze.size
        return maze.cells[y][x]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(idx: Int, view: View?, parent: ViewGroup?): View {
        val createdView = LayoutInflater.from(context).inflate(R.layout.maze_cell, null)

        val cellSize = 350 / maze.size
        val layoutParams = LayoutParams((cellSize + 1).toDp(), cellSize.toDp())
        layoutParams.gravity = Gravity.CENTER
        createdView.layoutParams = layoutParams

        val backgroundView = createdView.findViewById<ImageView>(R.id.cell_background)
        val backgroundLayoutParams = backgroundView.layoutParams as ViewGroup.MarginLayoutParams

        val y = idx / maze.size
        val x = idx % maze.size
        val cell = maze.cells[y][x]
        backgroundLayoutParams.setMargins(
            if (cell.hasWall(MazeWall.LEFT)) MARGIN_SIZE else 0,
            if (cell.hasWall(MazeWall.TOP)) MARGIN_SIZE else 0,
            if (cell.hasWall(MazeWall.RIGHT)) MARGIN_SIZE else 0,
            if (cell.hasWall(MazeWall.BOTTOM)) MARGIN_SIZE else 0,
        )

        val imageView = createdView.findViewById<ImageView>(R.id.cell_image)

        when (Position(y, x)) {
            player.position -> {
                imageView.setImageResource(R.drawable.user)
                imageView.rotation = player.orientation.degree
            }
            maze.goalPosition -> imageView.setImageResource(R.drawable.goal)
            maze.hintPosition -> imageView.setImageResource(R.drawable.hint)
        }

        return createdView
    }

    private fun Int.toDp(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}