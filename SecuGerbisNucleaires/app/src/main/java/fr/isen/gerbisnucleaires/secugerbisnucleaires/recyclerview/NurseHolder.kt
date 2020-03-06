package fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.Nurse
import fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass.SecuGerbis
import kotlinx.android.synthetic.main.activity_nurse_item.view.*

class NurseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(nurse: Nurse, clickListener: NurseAdapter.OnItemClickListener) {
        itemView.nurseName.text = (SecuGerbis(nurse.firstName).decrypt() + " " + SecuGerbis(nurse.lastName).decrypt())
        itemView.nurseEmailValue.text = (SecuGerbis(nurse.email).decrypt())

        itemView.setOnClickListener {
            clickListener.onItemClick(nurse)

        }
    }
}