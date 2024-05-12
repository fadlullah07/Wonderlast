package com.pmm.wonderlast

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GalleryAdapter(private val context: Context, private val dataList: List<GalleryItem>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    private var onMarkClickListener: OnMarkClickListener? = null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val imageLocation: TextView = itemView.findViewById(R.id.locationInfo)
        val markIcon: ImageView = itemView.findViewById(R.id.mark_icon)
        val ratingValue: TextView = itemView.findViewById(R.id.rating_value)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_wrapper, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]

        // Set gambar
        Glide.with(context)
            .load(item.imageUrl)
            .placeholder(R.drawable.landscape_placeholder_svgrepo_com) // Placeholder jika gambar tidak tersedia
            .error(R.drawable.image_error_placeholder) // Placeholder jika terjadi kesalahan
            .into(holder.imageView)


        // Set informasi gambar
        holder.textViewTitle.text = item.title
        holder.imageLocation.text = item.location
        holder.ratingValue.text = item.rating
        val markIconRes = if (item.isMarked) R.drawable.mark_active else R.drawable.mark_inactive
        holder.markIcon.setImageResource(markIconRes)

        // Tambahkan click listener untuk icon tandai
        holder.markIcon.setOnClickListener {
            // Panggil listener saat ikon tandai diklik dan kirimkan posisi item
            onMarkClickListener?.onMarkClick(position)
            item.toggle()
            // Perbarui status isMarked di dalam data set
            dataList[position].isMarked = item.isMarked
            // Perbarui ikon tandai
            val newMarkIconRes = if (item.isMarked) R.drawable.mark_active else R.drawable.mark_inactive
            holder.markIcon.setImageResource(newMarkIconRes)
            // Tampilkan pesan Toast
            val message = if (item.isMarked) "${item.title} is marked" else "${item.title} is unmarked"
            showToast(message)
        }
    }
    override fun getItemCount(): Int {
        return dataList.size
    }
    interface OnMarkClickListener {
        fun onMarkClick(position: Int)
    }

    fun setOnMarkClickListener(listener: OnMarkClickListener) {
        onMarkClickListener = listener
    }
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}
