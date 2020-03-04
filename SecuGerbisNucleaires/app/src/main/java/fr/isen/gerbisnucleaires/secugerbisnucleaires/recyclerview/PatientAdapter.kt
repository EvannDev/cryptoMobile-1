package fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.gerbisnucleaires.secugerbisnucleaires.R
import fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient.Patient

class PatientAdapter(private val patients: ArrayList<Patient>, var itemClickListener: OnItemClickListener) : RecyclerView.Adapter<PatientHolder>() {
    override fun onBindViewHolder(holder: PatientHolder, position: Int) {
        holder.bind(patients[position], itemClickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientHolder {
        return PatientHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_patient_item, parent, false))
    }

    override fun getItemCount(): Int = patients.size

    interface OnItemClickListener {
        fun onItemClick(patient : Patient)
    }
}
