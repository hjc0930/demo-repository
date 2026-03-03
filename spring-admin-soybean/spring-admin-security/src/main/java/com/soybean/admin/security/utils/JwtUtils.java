package com.soybean.admin.security.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * JWT工具类
 * 负责JWT的生成、解析和验证
 */
@Slf4j
@Component
public class JwtUtils {

    /**
     * JWT签名密钥
     */
    @Value("${jwt.secret:abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ}")
    private String secret;

    /**
     * Access Token过期时间（毫秒）默认30分钟
     */
    @Value("${jwt.access-expiration:1800000}")
    private Long accessExpiration;

    /**
     * Refresh Token过期时间（毫秒）默认7天
     */
    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ACCESS_TOKEN_PREFIX = "access:";
    private static final String REFRESH_TOKEN_PREFIX = "refresh:";

    public JwtUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 生成密钥
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 生成Access Token
     *
     * @param subject   主题（通常是用户ID）
     * @param claims    额外的声明
     * @return Access Token
     */
    public String generateAccessToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, accessExpiration, ACCESS_TOKEN_PREFIX);
    }

    /**
     * 生成Refresh Token
     *
     * @param subject   主题（通常是用户ID）
     * @param claims    额外的声明
     * @return Refresh Token
     */
    public String generateRefreshToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, refreshExpiration, REFRESH_TOKEN_PREFIX);
    }

    /**
     * 生成Token
     */
    private String generateToken(String subject, Map<String, Object> claims, Long expiration, String prefix) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        String token = Jwts.builder()
            .subject(subject)
            .claims(claims)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSignKey())
            .compact();

        // 将token存储到Redis中
        String redisKey = prefix + token;
        redisTemplate.opsForValue().set(redisKey, subject, expiration, TimeUnit.MILLISECONDS);

        return token;
    }

    /**
     * 从Token中获取主题
     */
    public String getSubjectFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            log.error("获取Token主题失败", e);
            return null;
        }
    }

    /**
     * 从Token中获取所有声明
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return parseToken(token);
        } catch (Exception e) {
            log.error("获取Token声明失败", e);
            return null;
        }
    }

    /**
     * 验证Token是否有效
     */
    public boolean validateToken(String token) {
        try {
            // 检查token是否在黑名单中
            if (isTokenBlacklisted(token)) {
                return false;
            }

            // 检查token是否在Redis中存在
            String redisKeyAccess = ACCESS_TOKEN_PREFIX + token;
            String redisKeyRefresh = REFRESH_TOKEN_PREFIX + token;
            Boolean hasAccess = redisTemplate.hasKey(redisKeyAccess);
            Boolean hasRefresh = redisTemplate.hasKey(redisKeyRefresh);

            if (Boolean.FALSE.equals(hasAccess) && Boolean.FALSE.equals(hasRefresh)) {
                return false;
            }

            // 解析token验证签名
            parseToken(token);
            return true;
        } catch (Exception e) {
            log.error("Token验证失败", e);
            return false;
        }
    }

    /**
     * 刷新Token
     *
     * @param refreshToken 刷新Token
     * @return 新的Access Token
     */
    public String refreshAccessToken(String refreshToken) {
        try {
            Claims claims = parseToken(refreshToken);
            String subject = claims.getSubject();

            // 删除旧的refresh token
            String oldRefreshKey = REFRESH_TOKEN_PREFIX + refreshToken;
            redisTemplate.delete(oldRefreshKey);

            // 生成新的access token
            return generateAccessToken(subject, claims);
        } catch (Exception e) {
            log.error("刷新Token失败", e);
            throw new RuntimeException("刷新Token失败");
        }
    }

    /**
     * 将Token加入黑名单（用于登出）
     */
    public void blacklistToken(String token) {
        try {
            Claims claims = parseToken(token);
            long expiration = claims.getExpiration().getTime() - System.currentTimeMillis();

            if (expiration > 0) {
                String blacklistKey = "token:blacklist:" + token;
                redisTemplate.opsForValue().set(blacklistKey, "1", expiration, TimeUnit.MILLISECONDS);
            }

            // 删除Redis中的token
            String accessKey = ACCESS_TOKEN_PREFIX + token;
            String refreshKey = REFRESH_TOKEN_PREFIX + token;
            redisTemplate.delete(accessKey);
            redisTemplate.delete(refreshKey);
        } catch (Exception e) {
            log.error("Token黑名单添加失败", e);
        }
    }

    /**
     * 检查Token是否在黑名单中
     */
    private boolean isTokenBlacklisted(String token) {
        String blacklistKey = "token:blacklist:" + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey));
    }

    /**
     * 解析Token
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
            .verifyWith(getSignKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    /**
     * 获取Token过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检查Token是否即将过期（在5分钟内）
     */
    public boolean isTokenExpiringSoon(String token) {
        Date expiration = getExpirationDateFromToken(token);
        if (expiration == null) {
            return true;
        }

        long timeToExpiry = expiration.getTime() - System.currentTimeMillis();
        return timeToExpiry < (5 * 60 * 1000); // 5分钟
    }
}
