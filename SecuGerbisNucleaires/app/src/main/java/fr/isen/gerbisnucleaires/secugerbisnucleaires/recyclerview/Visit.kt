package fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview

data class Visit (
    var uuid : String = "",
    val patientId : String = "",
    val temperature : String = "",
    val treatment : String = "",
    val patientState : String = "",
    val dateOfVisit : String = ""
)