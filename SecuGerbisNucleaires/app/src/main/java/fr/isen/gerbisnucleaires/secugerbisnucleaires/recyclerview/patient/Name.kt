package fr.isen.gerbisnucleaires.secugerbisnucleaires.recyclerview.patient

data class Name(
    val name: String,
    val firstName: String,
    val title: String
){
    constructor() : this("","","")
}