package fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient
import kotlinx.android.synthetic.main.activity_patient_item.view.*

class PatientHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(patient: Patient, clickListener: PatientAdapter.OnItemClickListener) {
        itemView.patientName.text = (SecuGerbis(patient.name.title).decrypt() + " " + SecuGerbis(patient.name.name).decrypt() + " " + SecuGerbis(patient.name.firstName).decrypt())
        itemView.patientAge.text = (SecuGerbis(patient.age).decrypt() + " years old")
        itemView.patientDisease.text = SecuGerbis(patient.disease).decrypt()

        itemView.setOnClickListener {
            clickListener.onItemClick(patient)
        }
    }
}