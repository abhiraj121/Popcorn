package com.abhirajsharma.popcorn

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhirajsharma.popcorn.Extras.About
import com.abhirajsharma.popcorn.Extras.Feedback
import com.abhirajsharma.popcorn.Extras.Settings
import com.abhirajsharma.popcorn.Search_gen.SearchAdapter
import com.abhirajsharma.popcorn.Search_gen.SearchItemResponse
import com.abhirajsharma.popcorn.api_fetching.Client
import com.abhirajsharma.popcorn.movies_gen.MoviesFrag
import com.abhirajsharma.popcorn.shows_gen.ShowsFrag
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val THE_MOVIE_DB_API_TOKEN = "c669377735364869546a41a514a03c0f"
    var clicked = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //toolbar
        setSupportActionBar(toolbar)
        //NavDrawer
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        //For opening movies page default
        if (savedInstanceState==null){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragments_container,MoviesFrag()) //MoviesFrag()
                .commit()
            navView.setCheckedItem(R.id.nav_movies) //nav_movies
        }
    }

    //For search bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_menu,menu)
        val sv = menu!!.findItem(R.id.nav_search2).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))

        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity,"Searching for $query",Toast.LENGTH_SHORT).show()
//                sv.onActionViewCollapsed()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                loadSearchItem(newText.toString())
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun loadSearchItem(itemToBeSearched:String) {
        val apiService: com.abhirajsharma.popcorn.api_fetching.Service = Client().getMyClient()!!.create(
            com.abhirajsharma.popcorn.api_fetching.Service::class.java)
        val call: Call<SearchItemResponse> = apiService.getSearchItem(THE_MOVIE_DB_API_TOKEN, itemToBeSearched)

        call.enqueue(object : Callback<SearchItemResponse?> {
            override fun onResponse(call: Call<SearchItemResponse?>?, response: Response<SearchItemResponse?>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val movies = response.body()!!.results
                        val secondAdapter = SearchAdapter(this@MainActivity, movies)
                        val secondManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                        searchRecyclerView.layoutManager = secondManager
                        searchRecyclerView.adapter = secondAdapter
                    }
                }
            }
            override fun onFailure(call: Call<SearchItemResponse?>?, t: Throwable) {
                Toast.makeText(this@MainActivity, "No such item exists", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        if (searchRecyclerView != null) {
            if (clicked == 0) {
                searchRecyclerView.setVisibility(View.GONE)
                clicked = 1
            } else {
                finishAffinity()
            }
        } else {
            finishAffinity()
        }
    }

    //For items selected in navigation drawer
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> {
                Toast.makeText(this, "Authentication needs to be completed.", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_movies -> {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragments_container, MoviesFrag())
                    .commit()
            }
            R.id.nav_tvshows -> {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragments_container, ShowsFrag())
                    .commit()
            }
            R.id.nav_settings -> {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragments_container, Settings()
                    )
                    .commit()
            }
            R.id.nav_about -> {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragments_container, About()
                    )
                    .commit()
            }
            R.id.nav_feedback -> {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragments_container, Feedback()
                    )
                    .commit()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
