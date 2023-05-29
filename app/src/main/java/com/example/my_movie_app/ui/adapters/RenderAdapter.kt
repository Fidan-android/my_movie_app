package com.example.my_movie_app.ui.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.my_movie_app.R
import com.example.my_movie_app.api.models.FavouriteFilmModel
import com.example.my_movie_app.api.models.FilmModel
import com.example.my_movie_app.api.models.GenreModel
import com.example.my_movie_app.api.models.StaffModel


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
            1 -> FavouriteFilmsViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.film_cell, parent, false)
            )
            2 -> GenreViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.genre_cell, parent, false)
            )
            3 -> StaffViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.staff_cell, parent, false)
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
            is FavouriteFilmsViewHolder -> holder.onBind(
                renderList[position] as FavouriteFilmModel,
                delegate::onClick
            )
            is GenreViewHolder -> holder.onBind(
                renderList[position] as GenreModel,
                delegate::onClick
            )
            is StaffViewHolder -> holder.onBind(
                renderList[position] as StaffModel,
                delegate::onClick
            )
        }
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = renderList.size

    override fun onUpdateItems(list: MutableList<T>) {
        val diffCallback = CustomDiffUtil(viewType, renderList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        renderList.clear()
        renderList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    open class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val rootView: LinearLayout = itemView.findViewById(R.id.rootView)
        private val logoFilm: AppCompatImageView = itemView.findViewById(R.id.logoFilm)
        private val nameFilm: AppCompatTextView = itemView.findViewById(R.id.nameFilm)

        open fun onBind(model: FilmModel, onClick: (Int) -> Unit) {
            nameFilm.text = model.nameRu
            nameFilm.isSelected = true
            Glide
                .with(itemView.context)
                .load(model.posterUrlPreview)
                .centerCrop()
                .error(R.drawable.logo)
                .into(logoFilm)

            rootView.setOnClickListener {
                onClick(model.kinopoiskId ?: model.filmId ?: 0)
            }
        }
    }

    open class FavouriteFilmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val rootView: LinearLayout = itemView.findViewById(R.id.rootView)
        private val logoFilm: AppCompatImageView = itemView.findViewById(R.id.logoFilm)
        private val nameFilm: AppCompatTextView = itemView.findViewById(R.id.nameFilm)

        open fun onBind(model: FavouriteFilmModel, onClick: (Int) -> Unit) {
            nameFilm.text = model.nameRu
            nameFilm.gravity = Gravity.CENTER
            Glide
                .with(itemView.context)
                .load(model.posterUrlPreview)
                .centerCrop()
                .error(R.drawable.logo)
                .into(logoFilm)

            rootView.setOnClickListener {
                onClick(model.kinopoiskId ?: model.filmId ?: 0)
            }
        }
    }

    open class GenreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val rootView: LinearLayout = itemView.findViewById(R.id.rootView)
        private val genreName: AppCompatTextView = itemView.findViewById(R.id.genreName)

        open fun onBind(model: GenreModel, onClick: (Int) -> Unit) {
            genreName.text = model.genre
            rootView.setOnClickListener {
                onClick(model.id ?: 0)
            }
        }
    }

    open class StaffViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val posterStaff: AppCompatImageView = itemView.findViewById(R.id.posterStaff)
        private val nameStaff: AppCompatTextView = itemView.findViewById(R.id.nameStaff)

        open fun onBind(model: StaffModel, onClick: (Int) -> Unit) {
            nameStaff.text = model.nameRu
            Glide
                .with(itemView.context)
                .load(model.posterUrl)
                .centerCrop()
                .error(R.drawable.logo)
                .into(posterStaff)
        }
    }
}