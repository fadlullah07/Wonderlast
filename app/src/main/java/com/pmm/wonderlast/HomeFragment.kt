package com.pmm.wonderlast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment:Fragment() , GalleryAdapter.OnMarkClickListener{
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter
    private val dataList = mutableListOf<GalleryItem>()
    private val oldDataList = mutableListOf<GalleryItem>()
    private lateinit var auth: FirebaseAuth
    private lateinit var bottomNavigationView : BottomNavigationView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)

    }
    override fun onMarkClick(position: Int) {
        val selectedItem = dataList[position]
        val newMarkedStatus = !selectedItem.isMarked
        updateMarkStatusInDatabase(selectedItem.id, newMarkedStatus, position)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = GalleryAdapter(requireContext(), dataList)
        adapter.setOnMarkClickListener(this)
        bottomNavigationView = view.findViewById(R.id.footbar)
        recyclerView.adapter = adapter
        auth = FirebaseAuth.getInstance()

        swipeRefreshLayout.setOnRefreshListener {
            fetchDataFromFirestore()
        }
        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("data_destinasi")
        val userId = auth.currentUser?.providerId

        collectionRef.get()
            .addOnSuccessListener { documents ->
                oldDataList.clear()
                oldDataList.addAll(dataList)

                dataList.clear()
                for (document in documents) {
                    val id = document.id
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val title = document.getString("title") ?: ""
                    val description = document.getString("location") ?: ""
                    val isMarked = document.getBoolean("markedBy.$userId") ?: false
                    val rating = document.getString("rating") ?: ""
                    val galleryItem = GalleryItem(id, imageUrl, title, description, isMarked, rating)
                    dataList.add(galleryItem)
                }

                if (oldDataList.isEmpty()) {
                    adapter.notifyDataSetChanged()
                } else {
                    val diffResult = calculateDiff(oldDataList, dataList)
                    diffResult.dispatchUpdatesTo(adapter)
                }

                swipeRefreshLayout.isRefreshing = false
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error: ${exception.message}")
                swipeRefreshLayout.isRefreshing = false
            }
    }

    private fun updateMarkStatusInDatabase(itemId: String, newMarkedStatus: Boolean, position: Int) {
        val db = FirebaseFirestore.getInstance()
        val userId = auth.currentUser?.providerId
        val itemRef = db.collection("data_destinasi").document(itemId)

        itemRef.update("markedBy.$userId", newMarkedStatus)
            .addOnSuccessListener {
                Log.d("Firestore", "isMarked status updated in database")
                dataList[position].isMarked = newMarkedStatus
                adapter.notifyItemChanged(position)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating isMarked status: $e")
            }
    }

    private fun calculateDiff(oldList: List<GalleryItem>, newList: List<GalleryItem>): DiffUtil.DiffResult {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldList.size
            override fun getNewListSize(): Int = newList.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition].id == newList[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldList[oldItemPosition] == newList[newItemPosition]
        }
        return DiffUtil.calculateDiff(diffCallback)
    }
}
