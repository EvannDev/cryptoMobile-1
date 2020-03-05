package fr.isen.gerbisnucleaires.secugerbisnucleaires.dataclass

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

data class SecuGerbis(val data: String) {

    private fun getKey(): String {
        return "a125fe14d825a58b"
    }

    fun encrypt(): String {
        val key = getKey().toByteArray(Charsets.UTF_8)
        val ivs = getKey().toByteArray(Charsets.UTF_8)
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        val secretKeySpec = SecretKeySpec(key, "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, IvParameterSpec(ivs))
        val cipherContent = cipher.doFinal(data.toByteArray(Charsets.UTF_8))

        return Base64.getEncoder().encodeToString(cipherContent)
    }

    fun decrypt(): String {
        val key = getKey().toByteArray(Charsets.UTF_8)
        val ivs = getKey().toByteArray(Charsets.UTF_8)
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        val secretKeySpec = SecretKeySpec(key, "AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(ivs))

        val decodeData = Base64.getDecoder().decode(data)

        return cipher.doFinal(decodeData).toString(Charsets.UTF_8)
    }
}