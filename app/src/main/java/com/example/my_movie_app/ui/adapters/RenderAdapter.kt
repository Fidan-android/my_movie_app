package com.example.my_movie_app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.my_movie_app.R
import com.example.my_movie_app.api.models.CategoryModel
import com.example.my_movie_app.api.models.CinemaModel
import com.example.my_movie_app.api.models.FilmModel


class RenderAdapter<T>(private val viewType: Int, private val delegate: IItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), IRenderAdapter<T> {

    interface IItemClickListener {
        fun onClick(position: Int)
    }

    private var renderList: MutableList<T> = mutableListOf()

    override fun getItemViewType(position: Int) = viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> FilmViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.film_cell, parent, false)
            )
            1 -> CategoryViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.category_cell, parent, false)
            )
            2 -> CinemaViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.cinema_cell, parent, false)
            )
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FilmViewHolder -> holder.onBind(
                renderList[position] as FilmModel,
                delegate::onClick
            )
            is CategoryViewHolder -> holder.onBind(
                renderList[position] as CategoryModel,
                delegate::onClick
            )
            is CinemaViewHolder -> holder.onBind(
                renderList[position] as CinemaModel,
                delegate::onClick
            )
        }
    }

    override fun getItemCount() = renderList.size

    override fun onUpdateRenderList(list: MutableList<T>) {
        val temp = list.size
        renderList.clear()
        notifyItemRangeRemoved(0, temp)
        renderList.addAll(list)
        notifyItemRangeInserted(0, temp)
    }

    open class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val rootView: LinearLayout = itemView.findViewById(R.id.rootView)
        private val logoFilm: AppCompatImageView = itemView.findViewById(R.id.logoFilm)
        private val nameFilm: AppCompatTextView = itemView.findViewById(R.id.nameFilm)
        private val ratingFilm: AppCompatTextView = itemView.findViewById(R.id.ratingFilm)

        open fun onBind(model: FilmModel, onClick: (Int) -> Unit) {
            nameFilm.text = model.title
            ratingFilm.text = model.stars
            Glide
                .with(itemView.context)
                .load(
                    GlideUrl(
                        "http://n931333e.beget.tech/api/v3/img/" + model.image,
                        LazyHeaders.Builder()
                            .addHeader("User-Agent", "Mozilla/5.0")
                            .build()
                    )
                )
                .centerCrop()
                .error(R.drawable.logo)
                .into(logoFilm)

            rootView.setOnClickListener {
                onClick(adapterPosition)
            }
        }
    }

    open class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val rootView: LinearLayout = itemView.findViewById(R.id.rootView)
        private val categoryName: AppCompatTextView = itemView.findViewById(R.id.categoryName)

        open fun onBind(model: CategoryModel, onClick: (Int) -> Unit) {
            categoryName.text = model.categoryName
            rootView.setOnClickListener {
                onClick(adapterPosition)
            }
        }
    }

    open class CinemaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val rootView: LinearLayout = itemView.findViewById(R.id.rootView)
        private val bgCinema: AppCompatImageView = itemView.findViewById(R.id.bgCinema)
        private val cinemaName: AppCompatTextView = itemView.findViewById(R.id.cinemaName)
        private val cinemaAddress: AppCompatTextView = itemView.findViewById(R.id.cinemaAddress)

        open fun onBind(model: CinemaModel, onClick: (Int) -> Unit) {
            Glide
                .with(itemView.context)
                .load(
                    GlideUrl(
                        "http://n931333e.beget.tech/api/v3/img/" + model.imageUrl,
                        LazyHeaders.Builder()
                            .addHeader("User-Agent", "Mozilla/5.0")
                            .build()
                    )
                )
                .centerCrop()
                .error(R.drawable.logo)
                .into(bgCinema)
            cinemaName.text = model.cinemaName
            cinemaAddress.text = model.address
            rootView.setOnClickListener {
                onClick(adapterPosition)
            }
        }
    }
}