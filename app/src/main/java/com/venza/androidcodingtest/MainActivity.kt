package com.venza.androidcodingtest

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.venza.androidcodingtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var vm: UserViewModel

    lateinit var adapter: RecyclerViewAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var userRv: RecyclerView
    lateinit var progressBar: ProgressBar
    var isLoading = false

    val numberList: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userRv = findViewById(R.id.user_rv)
        layoutManager = LinearLayoutManager(userRv.context)
        userRv.layoutManager = layoutManager
        userRv.setHasFixedSize(true)
        progressBar = findViewById(R.id.pg_bar)

        userRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val total = adapter.itemCount

                    if (!isLoading) {
                        if ((visibleItemCount + pastVisibleItem) >= total) {
                            getListOfUser()
                        }
                    }

                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        getListOfUser()

        val searchIcon = binding.userSearch.findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)

        val cancelIcon = binding.userSearch.findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.BLACK)

        val textView = binding.userSearch.findViewById<TextView>(R.id.search_src_text)
        textView.setTextColor(Color.BLACK)

        binding.userSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })

    }

    fun getListOfUser() {
        isLoading = true
        progressBar.visibility = View.VISIBLE

        for (i in 0..10) {
            numberList.add("Item " + i.toString())
            Log.d("testing", numberList.toString())
        }
        Handler().postDelayed({
            vm =  ViewModelProvider(this).get(UserViewModel::class.java)
            vm.getAllUsers()
            vm.postModelListLiveData?.observe(this, Observer {
                if (it!=null){
                    //adapter.setData(it as ArrayList<UserModel>)
                    adapter = RecyclerViewAdapter(it as ArrayList<UserModel>)

                    userRv.adapter = adapter
                }else{
                    showToast("Something went wrong")
                }
            })
            isLoading = false
            progressBar.visibility = View.GONE

        }, 3000)


    }


    private fun showToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

}