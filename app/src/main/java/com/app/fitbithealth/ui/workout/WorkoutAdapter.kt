package com.app.fitbithealth.ui.workout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.fitbithealth.R
import com.app.fitbithealth.databinding.ItemProgressBinding
import com.app.fitbithealth.databinding.ItemWorkoutBinding
import com.app.fitbithealth.model.ActivitiesModel

/**
 * Activities listing RecyclerView Adapter
 * @param mDataList contains list of Activities
 */
class WorkoutAdapter(private val mDataList: ArrayList<ActivitiesModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        /**
         * Used for storing loading indicator type
         */
        const val TYPE_LOAD_MORE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOAD_MORE -> {
                LoadMoreViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_progress, parent, false
                    )
                )
            }
            else -> {
                WorkoutViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_workout, parent, false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int = mDataList[position].doLoadMore

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is WorkoutViewHolder) {
            holder.mBinding.apply {
                with(mDataList[position]) {
                    holder.mBinding.activities = this
                    holder.mBinding.tvCalories.text =
                        String.format(holder.itemView.context.getString(R.string.text_calories), calories)
                }
            }

        }
    }

    override fun getItemCount(): Int = mDataList.size

    /**
     * to clear the all the list data
     */
    fun clearActivitiesData() {
        mDataList.clear()
        notifyDataSetChanged()
    }

    /**
     * To store and display more pagination data
     * @param list list of activities data
     */
    fun updateMoreActivities(list: ArrayList<ActivitiesModel>) {
        val startIndex = mDataList.size
        mDataList.addAll(list)
        notifyItemRangeChanged(startIndex, mDataList.size)
    }

    /**
     * This view holder class is used for activities
     */
    inner class WorkoutViewHolder(itemView: ItemWorkoutBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
    }

    /**
     * This view holder class is used for loading indicator
     */
    class LoadMoreViewHolder(itemView: ItemProgressBinding) :
        RecyclerView.ViewHolder(itemView.root)

}