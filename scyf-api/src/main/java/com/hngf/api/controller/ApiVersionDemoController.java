package com.hngf.api.controller;

import com.hngf.api.common.annotation.ApiVersion;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 关于@ApiVersion注解的使用，参考此类即可
 */
@RestController
@ApiVersion()
@RequestMapping("app/{version}/my/")
public class ApiVersionDemoController {

	@RequestMapping("test")
    public String test() {
        return "OK! v2";
    }
	
	@RequestMapping("/test")
    @ApiVersion(2)// 方法会覆盖类的版本
    public String v2test() {
        return "OK!method: v2";
    }
	@RequestMapping("/test")
	@ApiVersion(3)// 方法会覆盖类的版本
	public String v3test() {
		return "OK!method: v3";
	}
	@RequestMapping("/test")
	@ApiVersion(4)// 方法会覆盖类的版本
	public String v4test() {
		return "OK!method: v4";
	}
}
