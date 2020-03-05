package fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient

data class Patient(
    var uuid: String = "",
    val name: Name = Name("", "", ""),
    val disease: String = "",
    val age: String = ""
)