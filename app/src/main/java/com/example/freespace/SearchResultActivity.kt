package com.example.freespace
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freespace.R
import com.example.freespace.databinding.ActivitySearchBinding
import com.example.freespace.databinding.ActivitySearchResultBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SearchResultActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchResultBinding
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: MyPlaceAdapter
    lateinit var rdb: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    private fun init() {
        val intent = intent
        val placeName = intent.getStringExtra("placeName")

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rdb = FirebaseDatabase.getInstance().getReference("PlaceDB/Place")
        val query = rdb.orderByChild("pname").equalTo(placeName)//query문 찾아봐야함
        val option = FirebaseRecyclerOptions.Builder<Place>().setQuery(query, Place::class.java).build()
        adapter = MyPlaceAdapter(option)
        adapter.itemClickListener = object : MyPlaceAdapter.OnItemClickListener {
            override fun OnItemClick(view: View, position: Int) {
                val intent = Intent(this@SearchResultActivity, SaturationActivity::class.java)
                intent.putExtra("string", adapter.getItem(position).pName.toString())
                intent.putExtra("int",
                    (adapter.getItem(position).pNum.toString().toDouble() /
                            adapter.getItem(position).pMaxNum.toString().toDouble() * 100).toInt()
                )
                startActivity(intent)
            }
        }
        binding.apply {
            resultRecyclerView.layoutManager = layoutManager
            resultRecyclerView.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}