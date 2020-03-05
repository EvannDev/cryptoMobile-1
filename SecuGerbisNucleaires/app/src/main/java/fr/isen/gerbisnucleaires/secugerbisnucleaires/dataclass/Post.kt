package fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass

data class Post(
    var uid: String,
    var firstname: String,
    var lastname: String,
    var email: String,
    var phone: String,
    var starCount: Int = 0,
    var stars: MutableMap<String, Boolean> = HashMap()
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "firstname" to firstname,
            "lastname" to lastname,
            "email" to email,
            "phone" to phone,
            "starCount" to starCount,
            "stars" to stars
        )
    }
}