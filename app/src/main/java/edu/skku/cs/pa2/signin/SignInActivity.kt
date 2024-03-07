package edu.skku.cs.pa2.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import edu.skku.cs.pa2.R
import edu.skku.cs.pa2.mazeselection.MazeSelectionActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class SignInActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val editTextView = findViewById<EditText>(R.id.user_name_edit_text)
        val buttonView = findViewById<Button>(R.id.sign_in_button)

        buttonView.setOnClickListener {
            val username = editTextView.text.toString()
            signIn(username)
        }
    }

    private fun signIn(username: String) {
        val domain = getString(R.string.domain)
        val port = getString(R.string.port)
        val url = "$domain:$port/users"

        val body = Gson().toJson(SignInRequest(username))
        val mediaType = "application/json; charset=utf-8".toMediaType()
        Log.i(localClassName, "Request body: $body")

        val request = Request.Builder().url(url).post(body.toRequestBody(mediaType)).build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.w(localClassName, "Network failure from url $url", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        Log.w(localClassName, "Network failure from url $url")
                        return
                    }

                    val str = response.body?.string() ?: ""
                    val data = Gson().fromJson(str, SignInResponse::class.java)
                    Log.i(localClassName, "Response data: $str")

                    if (data.success) {
                        val intent = Intent(applicationContext, MazeSelectionActivity::class.java).apply {
                            putExtra(MazeSelectionActivity.USER_NAME, username)
                        }
                        startActivity(intent)
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(applicationContext, "Wrong User Name", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }

    private data class SignInRequest(val username: String)
    private data class SignInResponse(val success: Boolean)
}
