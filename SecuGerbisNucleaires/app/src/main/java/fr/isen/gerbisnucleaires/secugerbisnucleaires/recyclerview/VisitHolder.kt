package fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import kotlinx.android.synthetic.main.activity_visit_item.view.*

class VisitHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(visit: Visit, clickListener: VisitAdapter.OnItemClickListener) {
        itemView.visitDate.text = SecuGerbis(visit.dateOfVisit).decrypt()
        itemView.visitTemperatureValue.text = SecuGerbis(visit.temperature).decrypt()
        itemView.visitTreatmentValue.text = SecuGerbis(visit.treatment).decrypt()
        itemView.visitPatientStateValue.text = SecuGerbis(visit.patientState).decrypt()

        itemView.setOnClickListener {
            clickListener.onItemClick(visit)
        }
    }
}