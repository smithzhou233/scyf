package com.hngf.api.common.utils;

import com.hngf.entity.sys.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JsonWebToken工具类
 */
@ConfigurationProperties(prefix = "scyf.jwt")
@Component
public class JwtUtil {
    private Logger logger = LoggerFactory.getLogger(getClass());

    // 秘钥
    private String secret;
    // 过期时间
    private long expire ;
    //token参数名称
    private String header ;

    /**
     * 生成 JWT
     */
    public String generateToken(User user) {
        if (user == null || user.getUserId() == null || user.getLoginName() == null) {
            return null;
        }
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        return Jwts.builder()
                .setSubject(user.getLoginName())//subject中放入loginName
                .claim("userId", user.getUserId())//放入userId
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 校验 JWT
     */
    public Claims checkJWT(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("token验证失败");
            return null;
        }
    }

    /**
     * token是否过期
     * @return  true：过期
     */
    public boolean isExpired(Date expiration) {
        return expiration.before(new Date());
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
