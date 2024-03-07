package edu.skku.cs.pa2.mazeselection

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.skku.cs.pa2.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class MazeSelectionActivity : AppCompatActivity() {

    companion object {
        const val USER_NAME = "username"
    }

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maze_selection)

        val username = intent.getStringExtra(USER_NAME)

        val usernameTextView = findViewById<TextView>(R.id.user_name_text)
        usernameTextView.text = username

        val mazeListView = findViewById<ListView>(R.id.maze_list)

        val domain = getString(R.string.domain)
        val port = getString(R.string.port)
        val url = "$domain:$port/maps"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException) {
                Log.w(localClassName, "Network failure from url $url", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.w(localClassName, "Network failure from url $url")
                    return
                }

                val str = response.body?.string() ?: ""
                val typeToken = object : TypeToken<List<MazeInfoResponse>>() {}.type
                val data = Gson().fromJson<List<MazeInfoResponse>>(str, typeToken)

                CoroutineScope(Dispatchers.Main).launch {
                    val adapter = MazeListAdapter(applicationContext, data)
                    mazeListView.adapter = adapter
                }
            }
        })
    }
}