package fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient
import kotlinx.android.synthetic.main.activity_patient_item.view.*

class PatientHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindValue(patient: Patient) {
        itemView.patientTitle.text = patient.name.title
        itemView.patientName.text = (patient.name.name + " " + patient.name.firstName)
        itemView.patientDisease.text = patient.disease
    }
}