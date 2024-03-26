package tech.benhack.ui.helpers

import android.content.Context
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import tech.benhack.ui.R
import java.util.*

class TokenUtil {
    fun generateToken(context:Context, email: String, provider: String): String {
        val secretKey = context.getString(R.string.tokens_secret_key)
        val expireDate = Date(System.currentTimeMillis() + 2592000000 ) // 1 month in millis

        return Jwts.builder()
            .setSubject(email)
            .claim("provider", provider)
            .setExpiration(expireDate)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun validateToken(context:Context, token: String):  Map<String, Any>? {
        val secretKey = context.getString(R.string.tokens_secret_key)

        return try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
        } catch (e: Exception) {
            null
        }
    }
}

