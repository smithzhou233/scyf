/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.hngf.web.common.config;

import com.hngf.web.common.filter.ShiroLoginFilter;
import com.hngf.web.common.shiro.JedisSessionDAO;
import com.hngf.web.common.shiro.JedisSessionRepository;
import com.hngf.web.common.shiro.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro的配置文件
 */
@Component
@Configuration
@PropertySource(value = "classpath:application.yml")
public class ShiroConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;

    /**
     * 单机环境，session交给shiro管理
     *  @ConditionalOnProperty(prefix = "scyf", name = "cluster", havingValue = "false")
     */
    @Bean
    public DefaultWebSessionManager sessionManager(@Value("${scyf.globalSessionTimeout:3600}") long globalSessionTimeout){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionValidationInterval(globalSessionTimeout * 1000);
        sessionManager.setGlobalSessionTimeout(globalSessionTimeout * 1000);
       // sessionManager.setSessionValidationInterval(globalSessionTimeout);
       // sessionManager.setGlobalSessionTimeout(globalSessionTimeout);
        //sessionManager.setSessionIdCookie(rememberMeCookie());
        sessionManager.setSessionDAO(sessionDAO());
        return sessionManager;
    }

    /**
     * 集群环境，session交给spring-session管理
     */
    @Bean
    @ConditionalOnProperty(prefix = "scyf", name = "cluster", havingValue = "true")
    public ServletContainerSessionManager servletContainerSessionManager() {
        return new ServletContainerSessionManager();
    }

    @Bean("securityManager")
    public SecurityManager securityManager(UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
       // securityManager.setRememberMeManager(rememberMeManager());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(redisCacheManager());

        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager(30000));
        return securityManager;
    }

    //cookie管理
    /*@Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(1 * 60 * 60);
        return cookie;
    }*/
    @Bean
    public JedisSessionRepository sessionRepository() {
        return new JedisSessionRepository();
    }

  /*  //缓存管理
    @Bean
    public EhCacheManager cacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return cacheManager;
    }
*/

    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     * @return
     */
    @Bean
    public SessionDAO sessionDAO() {
     /*   RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;*/
         return new JedisSessionDAO(sessionRepository());

    }

    @Bean
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPassword(password);
        redisManager.setPort(port);
        redisManager.setTimeout(30000);
        return redisManager;
    }

    @Bean
  public RedisCacheManager redisCacheManager(){
      RedisCacheManager redisCacheManager = new RedisCacheManager();
      redisCacheManager.setRedisManager(redisManager());
      return redisCacheManager;
  }

    //记住我
   /* @Bean
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }*/


    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);




       // shiroFilter.setLoginUrl("/scyf");
        shiroFilter.setUnauthorizedUrl("/");

        Map<String, Filter> filters = new LinkedHashMap<>();
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        //设置默认登录名，默认为username
        formAuthenticationFilter.setUsernameParam("loginName");
        filters.put("authc", new ShiroLoginFilter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        //swagger文档直接放行
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/swagger-resources/**", "anon");

        //静态资源放行
        filterMap.put("/statics/**", "anon");
        filterMap.put("/source/**", "anon");
        //登录界面放行
        filterMap.put("/LoginIndex.html", "anon");
        //大屏接口直接放行，不需要登录
        filterMap.put("/bigscreen/**", "anon");
        //登录请求放行
        filterMap.put("/sys/login", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/captcha.jpg", "anon");
        //app下载请求放行
        filterMap.put("/sys/setting/download/app/android", "anon");

        //websocket放行
        filterMap.put("/ws/**", "anon");

        //其他路径，全部拦截，需要认证
        filterMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        shiroFilter.setUnauthorizedUrl("/403");//错误页面

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public static  LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
