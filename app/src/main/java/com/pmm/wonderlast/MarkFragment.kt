package com.pmm.wonderlast

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MarkFragment : Fragment(), GalleryAdapter.OnMarkClickListener {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter
    private val dataList = mutableListOf<GalleryItem>()
    private lateinit var auth: FirebaseAuth
    private lateinit var textNoDestinations : TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mark, container, false)
    }
    override fun onMarkClick(position: Int) {
        val selectedItem = dataList[position]
        val newMarkedStatus = !selectedItem.isMarked
        updateMarkStatusInDatabase(selectedItem.id, newMarkedStatus, position)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textNoDestinations = view.findViewById(R.id.textNoDestinations)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = GalleryAdapter(requireContext(), dataList)
        adapter.setOnMarkClickListener(this)
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
        collectionRef.whereEqualTo("markedBy.$userId", true)
            .get()
            .addOnSuccessListener { documents ->
//                oldDataList.clear()
//                oldDataList.addAll(dataList)

                dataList.clear()
                for (document in documents) {
                    val id = document.id
                    val imageUrl = document.getString("imageUrl") ?: ""
                    val title = document.getString("title") ?: ""
                    val description = document.getString("location") ?: ""
                    val isMarked = document.getBoolean("markedBy.$userId") ?: true
                    val rating = document.getString("rating") ?: ""
                    val galleryItem = GalleryItem(id, imageUrl, title, description, isMarked, rating)
                    dataList.add(galleryItem)
                }
                if (dataList.isEmpty()) {
                    textNoDestinations.visibility = View.VISIBLE
                } else {
                    textNoDestinations.visibility = View.GONE
                }
                adapter.notifyDataSetChanged()
//                oldDataList.clear()
//                oldDataList.addAll(dataList)
//
//                if (oldDataList.isEmpty()) {
//                    adapter.notifyDataSetChanged()
//                } else {
//                    for (i in 0 until dataList.size) {
//                        if (i < oldDataList.size && dataList[i].isMarked != oldDataList[i].isMarked) {
//                            adapter.notifyItemChanged(i) // Perbarui item yang berubah
//                        }
//                    }
//                }

//                if (oldDataList.isEmpty()) {
//                    adapter.notifyDataSetChanged()
//                } else {
//                    val diffResult = calculateDiff(oldDataList, dataList)
//                    diffResult.dispatchUpdatesTo(adapter)
//                }
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
}
