package br.com.transportae.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "e9b1c292e7abc6707a1b4aed4f914eedc4d76193be47d091c39d36b14e1af427";
    private final long MILISSEGUNDOS = 1000;
    private final long SEGUNDOS = 60;
    private final long MINUTOS = 60;
    private final long HORAS = 24;

    public <T> T extrairClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extrairClaims(token);
        return claimsResolver.apply(claims);
    }

    public String gerarToken(UserDetails userDetails) {
        return gerarToken(userDetails, new HashMap<>());
    }

    public String gerarToken(
        UserDetails userDetails,
        Map<String, Object> extraClaims
    ) {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .claim("authorities", userDetails.getAuthorities())
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + getUmDiaEmMilis()))
            .signWith(getLoginKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    private long getUmDiaEmMilis() {
        return 1 * HORAS * MINUTOS * SEGUNDOS * MILISSEGUNDOS;
    }

    public boolean isTokenValido(String token, UserDetails userDetails) {
        final String email = extrairEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpirado(token);
    }

    private boolean isTokenExpirado(String token) {
        return extrairExpiration(token).before(new Date());
    }

    public String extrairEmail(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    private Date extrairExpiration(String token) {
        return extrairClaim(token, Claims::getExpiration);
    }

    private Claims extrairClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getLoginKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getLoginKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
