package com.hngf.common.config;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import redis.clients.jedis.JedisPool;

import redis.clients.jedis.JedisPoolConfig;



import java.io.IOException;

import java.io.InputStreamReader;

import java.util.Properties;



/**

 * @ClassName JedisConfig

 * @Author yupanpan

 * @Date 2019/10/10 16:46

 */

@Component
@Configuration
public class JedisConfig {

    private static final Logger logger= LoggerFactory.getLogger(JedisConfig.class);



    private static JedisPool jedisPool = null;



    private static String redisHost;

    private static String redisPassword;



    private static void initialPool(){

        try {

            JedisPoolConfig config = new JedisPoolConfig();

            //最大空闲连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数

            config.setMaxIdle(500);

            config.setMinIdle(10);

            //最大连接数, 应用自己评估，不要超过ApsaraDB for Redis每个实例最大的连接数

            config.setMaxTotal(6000);

            config.setTestOnBorrow(true);

            config.setTestOnReturn(false);

            jedisPool = new JedisPool(config, redisHost, 6379, 3000, redisPassword);

        } catch (Exception e) {

            if(jedisPool != null){

                jedisPool.close();

            }

            logger.error("初始化Redis连接池失败", e);

        }

    }



    /**

     * 初始化Redis连接池

     */

    static {

        Properties prop=new Properties();

        try {

            //类初始化加载器优先于@Value注解顺序

            prop.load(new InputStreamReader(JedisConfig.class.getClassLoader().getResourceAsStream("application.yml"), "UTF-8"));

            redisHost = (String)prop.get("host");

            redisPassword = (String)prop.get("password");

            initialPool();

        } catch (IOException e) {

            logger.error("获取application.properties文件redis连接信息失败",e);

        }

    }



    /**

     * 在多线程环境同步初始化

     */

    private static synchronized void poolInit() {

        if (jedisPool == null) {

            initialPool();

        }

    }





    /**

     * 同步获取Jedis实例

     *

     * @return Jedis

     */

    public Jedis getJedis() {

        if (jedisPool == null) {

            poolInit();

        }

        Jedis jedis = null;

        try {

            if (jedisPool != null) {

                jedis = jedisPool.getResource();

            }

        } catch (Exception e) {

            logger.error("同步获取Jedis实例失败" + e.getMessage(), e);

            returnResource(jedis);

        }



        return jedis;

    }



    /**

     * 释放jedis资源

     *

     * @param jedis

     */

    @SuppressWarnings("deprecation")

    public static void returnResource(final Jedis jedis) {

        if (jedis != null && jedisPool != null) {

            jedis.close();

        }

    }

}
