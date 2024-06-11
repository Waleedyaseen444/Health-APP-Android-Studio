package com.project.sfmd_project


import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.sfmd_project.databinding.ActivityArticlesBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class Articles : AppCompatActivity(), Article_Adapter.OnItemClickListener {

    private lateinit var binding: ActivityArticlesBinding
    private lateinit var articleAdapter: Article_Adapter
    private val client = OkHttpClient()
    private val articlesList = ArrayList<Article_Data>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutParams = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        }
        window.attributes = layoutParams

        binding.back.setOnClickListener {
            val intent = Intent(this@Articles, Home::class.java)
            startActivity(intent)
        }

        binding.recycler.layoutManager = LinearLayoutManager(this)
        articleAdapter = Article_Adapter(ArrayList(), this)
        binding.recycler.adapter = articleAdapter

        // Call function to fetch articles
        fetchArticles()

        // Set up search button click listener
        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            if (query.isNotBlank()) {
                // Perform search
                performSearch(query)
            }
        }
    }

    private fun fetchArticles() {
        val apiKey = "361d2c73caee49e2b6b7388b1c1a5f9d"
        val url = "https://newsapi.org/v2/everything?q=health&apiKey=$apiKey"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val jsonObject = JSONObject(responseBody.string())
                    val articlesArray = jsonObject.getJSONArray("articles")

                    val articlesList = ArrayList<Article_Data>()
                    for (i in 0 until articlesArray.length()) {
                        val articleObject = articlesArray.getJSONObject(i)
                        val title = articleObject.getString("title")
                        val description = articleObject.getString("description")
                        val imageUrl = articleObject.getString("urlToImage")
                        val articleData = Article_Data(title, description, imageUrl)
                        articlesList.add(articleData)
                    }

                    runOnUiThread {
                        articleAdapter.updateList(articlesList)
                    }
                }
            }
        })
    }

    private fun performSearch(query: String) {
        val apiKey = "361d2c73caee49e2b6b7388b1c1a5f9d"
        val url = "https://newsapi.org/v2/everything?q=$query&apiKey=$apiKey"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val jsonObject = JSONObject(responseBody.string())
                    val articlesArray = jsonObject.getJSONArray("articles")

                    val articlesList = ArrayList<Article_Data>()
                    for (i in 0 until articlesArray.length()) {
                        val articleObject = articlesArray.getJSONObject(i)
                        val title = articleObject.getString("title")
                        val description = articleObject.getString("description")
                        val imageUrl = articleObject.getString("urlToImage")
                        val articleData = Article_Data(title, description, imageUrl)
                        articlesList.add(articleData)
                    }

                    runOnUiThread {
                        articleAdapter.updateList(articlesList)
                    }
                }
            }
        })
    }

    override fun onItemClick(position: Int) {
        val article = articleAdapter.getArticle(position)

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.description}")
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }


}
