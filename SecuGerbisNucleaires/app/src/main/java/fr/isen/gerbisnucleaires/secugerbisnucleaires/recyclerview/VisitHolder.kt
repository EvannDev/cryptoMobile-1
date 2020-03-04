package fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_visit_item.view.*

class VisitHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(visit: Visit, clickListener : VisitAdapter.OnItemClickListener) {
        itemView.visitDate.text = visit.dateOfVisit
        itemView.visitTemperatureValue.text = visit.temperature
        itemView.visitTreatmentValue.setText(visit.treatment)
        itemView.visitPatientStateValue.setText(visit.patientState)

        itemView.setOnClickListener {
            clickListener.onItemClick(visit)
        }
    }
}