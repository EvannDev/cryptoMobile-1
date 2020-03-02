package fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.gerbisnucleaires.secugerbisnucleaires.R

class VisitAdapter(private val visits: ArrayList<Visit>, var itemClickListener: OnItemClickListener) : RecyclerView.Adapter<VisitHolder>() {
    override fun onBindViewHolder(holder: VisitHolder, position: Int) {
        holder.bind(visits[position], itemClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitHolder {
        return VisitHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_visit_item, parent, false))
    }

    override fun getItemCount(): Int = visits.size

    interface OnItemClickListener {
        fun onItemClick(visit : Visit)
    }
}