package edu.skku.cs.pa2.mazeselection

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import edu.skku.cs.pa2.R
import edu.skku.cs.pa2.maze.MazeActivity

class MazeListAdapter(val context: Context, val data: List<MazeInfoResponse>): BaseAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(idx: Int): Any {
        return data[idx]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(idx: Int, view: View?, viewGroup: ViewGroup?): View {
        val createdView = LayoutInflater.from(context).inflate(R.layout.maze_entry, null)

        val mazeNameTextView = createdView.findViewById<TextView>(R.id.maze_name_text)
        val mazeSizeTextView = createdView.findViewById<TextView>(R.id.maze_size_text)
        val mazeStartButton = createdView.findViewById<Button>(R.id.maze_start_button)

        val name = data[idx].name
        val size = data[idx].size
        mazeNameTextView.text = name
        mazeSizeTextView.text = size.toString()

        mazeStartButton.setOnClickListener {
            val intent = Intent(context, MazeActivity::class.java).apply {
                putExtra(MazeActivity.MAZE_NAME, name)
                putExtra(MazeActivity.MAZE_SIZE, size)
            }.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        return createdView
    }
}