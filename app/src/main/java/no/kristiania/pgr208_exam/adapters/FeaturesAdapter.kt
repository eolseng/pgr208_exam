package no.kristiania.pgr208_exam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.feature_card.view.*
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.entities.Feature

class FeaturesAdapter(
    var features: List<Feature> = emptyList(),
    val featureClickListener: OnFeatureClickListener
) : RecyclerView.Adapter<FeaturesAdapter.FeatureViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feature_card, parent, false)
        return FeatureViewHolder(view)
    }

    override fun getItemCount(): Int {
        return features.size
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        holder.bindFeatureToViewHolder(features[position], featureClickListener)
    }

    internal fun setFeatures(features: List<Feature>) {
        this.features = features
        notifyDataSetChanged()
    }

    class FeatureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindFeatureToViewHolder(feature: Feature, clickListener: OnFeatureClickListener) {
            itemView.feature_name.text = feature.properties.name
            itemView.setOnClickListener {
                clickListener.onFeatureClicked(feature)
            }
            itemView.location_button.setOnClickListener{
                clickListener.onLocationClicked(feature)
            }
        }
    }

    interface OnFeatureClickListener {
        fun onFeatureClicked(feature: Feature)
        fun onLocationClicked(feature: Feature)
    }
}