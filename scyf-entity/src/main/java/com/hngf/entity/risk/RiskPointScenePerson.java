package com.hngf.entity.risk;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 风险点 现场人员
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
public class RiskPointScenePerson implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 风险点
	 */
	@ApiModelProperty(value = "风险点ID")
	private Long riskPointId;
	/**
	 * 公司ID
	 */
	@ApiModelProperty(value = "公司ID")
	private Long companyId;
	/**
	 * 账号ID
	 */
	@ApiModelProperty(value = " 账号ID")
	private Long userId;
	/**
	 * 1在线 0 下线
	 */
	@ApiModelProperty(value = "1在线 0 下线")
	private Integer status;
	/**
	 * 删除标记
	 */
	@ApiModelProperty(value = "删除标记")
	private Integer delFlag;

	//扩展字段
	//用户名
	@ApiModelProperty(value = "用户名")
	private String userName;
	//用户头像
	@ApiModelProperty(value = "用户头像")
	private String userPicture;
	//用户所属部门名称
	@ApiModelProperty(value = "用户所属部门名称")
	private String groupName;


}
