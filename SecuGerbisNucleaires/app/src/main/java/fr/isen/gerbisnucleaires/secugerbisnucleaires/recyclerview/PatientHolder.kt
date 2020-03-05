package fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient
import kotlinx.android.synthetic.main.activity_patient_item.view.*

class PatientHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(patient: Patient, clickListener : PatientAdapter.OnItemClickListener) {
        itemView.patientName.text = (patient.name.title + " " + patient.name.name + " " + patient.name.firstName)
        itemView.patientAge.text =  (patient.age.toString() + " years old")
        itemView.patientDisease.text = patient.disease

        itemView.setOnClickListener {
            clickListener.onItemClick(patient)
        }
    }
}