package fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.gerbisnucleaires.secugerbisnucleaires.R
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.Nurse

class NurseAdapter(private val nurses: ArrayList<Nurse>, var itemClickListener: OnItemClickListener) : RecyclerView.Adapter<NurseHolder>(){

        override fun onBindViewHolder(holder: NurseHolder, position: Int) {
            holder.bind(nurses[position], itemClickListener)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NurseHolder {
            return NurseHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_nurse_item, parent, false))
        }

        override fun getItemCount(): Int = nurses.size

    interface OnItemClickListener {
        fun onItemClick(nurse: Nurse)
    }

}