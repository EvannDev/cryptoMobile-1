package fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.gerbisnucleaires.secugerbisnucleaires.R
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient

class PatientAdapter(private val patients: ArrayList<Patient>) : RecyclerView.Adapter<PatientHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientHolder {
        return PatientHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_patient_item, parent, false))
    }

    override fun getItemCount(): Int = patients.size

    override fun onBindViewHolder(holder: PatientHolder, position: Int) {
        holder.bindValue(patients[position])
    }
}