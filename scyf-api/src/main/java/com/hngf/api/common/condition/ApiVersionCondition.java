package com.hngf.api.common.condition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

/**
 * 版本号匹配筛选器
 */
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition>{

	
	//路径中版本的前缀， 这里用 /v[1-9]/的形式
	private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("/v(\\d+)/.*");
	
    private int apiVersion;
    
    public ApiVersionCondition(int apiVersion) {
		this.apiVersion = apiVersion;
	}

    
    //将不同的筛选条件合并
	@Override
	public ApiVersionCondition combine(ApiVersionCondition apiVersionCondition) {
		// 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiVersionCondition(apiVersionCondition.getApiVersion());
	}

	//不同筛选条件比较,用于排序
	@Override
	public int compareTo(ApiVersionCondition apiVersionCondition, HttpServletRequest request) {
        // 优先匹配最新的版本号
        return apiVersionCondition.getApiVersion() - this.apiVersion;
	}

	//根据request查找匹配到的筛选条件
	@Override
	public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURI());
        if (m.find()) {
            Integer version = Integer.valueOf(m.group(1));
            if (version >= this.apiVersion) {
                return this;
            }
        }
        return null;
	}

	public int getApiVersion() {
		return apiVersion;
	}
}
