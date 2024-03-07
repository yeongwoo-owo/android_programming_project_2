package edu.skku.cs.pa2.maze

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import edu.skku.cs.pa2.R
import edu.skku.cs.pa2.maze.data.MazeResponse
import edu.skku.cs.pa2.maze.model.Direction
import edu.skku.cs.pa2.maze.model.Maze
import edu.skku.cs.pa2.maze.model.Player
import edu.skku.cs.pa2.maze.view.MazeGridAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.util.concurrent.Future

class MazeActivity : AppCompatActivity() {

    companion object {
        const val MAZE_NAME = "name"
        const val MAZE_SIZE = "size"
    }

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze)

        val mazeName = intent.getStringExtra(MAZE_NAME)
        val mazeSize = intent.getIntExtra(MAZE_SIZE, -1)

        val domain = getString(R.string.domain)
        val port = getString(R.string.port)
        val url = "$domain:$port/maze/map?name=$mazeName"

        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.w(localClassName, "Network failure from url $url", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.w(localClassName, "Network failure from url $url")
                    return
                }

                val str = response.body?.string() ?: ""
                val data = Gson().fromJson(str, MazeResponse::class.java)

                val player = Player()
                val maze = Maze(data.parse())
                var moveCount = 0
                val turnView = findViewById<TextView>(R.id.turn_text)
                var hasHint = true

                CoroutineScope(Dispatchers.Main).launch {
                    val mazeGridView = findViewById<GridView>(R.id.maze_grid)
                    mazeGridView.numColumns = mazeSize
                    val adapter = MazeGridAdapter(
                        context = applicationContext,
                        maze = maze,
                        player = player,
                    )
                    mazeGridView.adapter = adapter

                    val leftButton = findViewById<Button>(R.id.left_button)
                    leftButton.setOnClickListener {
                        if (!maze.hasWall(player.position, Direction.LEFT)) {
                            player.move(Direction.LEFT)
                            turnView.text = "Turn: ${++moveCount}"
                            if (maze.hintPosition == player.position) {
                                maze.hintPosition = null
                            }
                            adapter.notifyDataSetChanged()
                        } else {
                            player.turn(Direction.LEFT)
                            adapter.notifyDataSetChanged()
                        }
                        if (maze.goalPosition == player.position) {
                            Toast.makeText(this@MazeActivity, "Finish!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    val upButton = findViewById<Button>(R.id.up_button)
                    upButton.setOnClickListener {
                        if (!maze.hasWall(player.position, Direction.UP)) {
                            player.move(Direction.UP)
                            turnView.text = "Turn: ${++moveCount}"
                            if (maze.hintPosition == player.position) {
                                maze.hintPosition = null
                            }
                            adapter.notifyDataSetChanged()
                        } else {
                            player.turn(Direction.UP)
                            adapter.notifyDataSetChanged()
                        }
                        if (maze.goalPosition == player.position) {
                            Toast.makeText(this@MazeActivity, "Finish!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    val downButton = findViewById<Button>(R.id.down_button)
                    downButton.setOnClickListener {
                        if (!maze.hasWall(player.position, Direction.DOWN)) {
                            player.move(Direction.DOWN)
                            turnView.text = "Turn: ${++moveCount}"
                            if (maze.hintPosition == player.position) {
                                maze.hintPosition = null
                            }
                            adapter.notifyDataSetChanged()
                        } else {
                            player.turn(Direction.DOWN)
                            adapter.notifyDataSetChanged()
                        }
                        if (maze.goalPosition == player.position) {
                            Toast.makeText(this@MazeActivity, "Finish!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    val rightButton = findViewById<Button>(R.id.right_button)
                    rightButton.setOnClickListener {
                        if (!maze.hasWall(player.position, Direction.RIGHT)) {
                            player.move(Direction.RIGHT)
                            turnView.text = "Turn: ${++moveCount}"
                            if (maze.hintPosition == player.position) {
                                maze.hintPosition = null
                            }
                            adapter.notifyDataSetChanged()
                        } else {
                            player.turn(Direction.RIGHT)
                            adapter.notifyDataSetChanged()
                        }
                        if (maze.goalPosition == player.position) {
                            Toast.makeText(this@MazeActivity, "Finish!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    val hintButton = findViewById<Button>(R.id.hint_button)
                    hintButton.setOnClickListener {
                        if (hasHint) {
                            hasHint = false
                            maze.findHint(player.position)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }
}