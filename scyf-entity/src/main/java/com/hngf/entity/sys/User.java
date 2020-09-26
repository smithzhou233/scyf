package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户基础信息表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 09:57:11
 */
@JsonIgnoreProperties({"salt","delFlag"})/**生成JSON时，过滤掉一些字段*/
@Data
@ApiModel("用户")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 账号ID
	 */
	@ApiModelProperty("账号ID")
	private Long userId;
	/**
	 * 账号登录名称
	 */
	@NotBlank(message = "账号登录名不能为空")
	@ApiModelProperty("账号登录名称")
	private String loginName;
	/**
	 * 用户名称
	 */
	@ApiModelProperty("用户名称")
	private String userName;
	/**
	 * 手机号码
	 */
	@ApiModelProperty("手机号码")
	private String userMobile;
	/**
	 * 邮箱
	 */
	@ApiModelProperty("邮箱")
	private String userEmail;
	/**
	 * 单位ID
	 */
	@ApiModelProperty("公司ID")
	private Long companyId;
	/**
	 * 账号状态，0正常，1锁定
	 */
	@ApiModelProperty("账号状态，0正常，1锁定")
	private Integer userStatus;
	/**
	 * 密码
	 */
	@ApiModelProperty("密码")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	/**
	 * 密码加盐
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String salt;
	/**
	 * 记录tocken
	 */
	@ApiModelProperty("记录token")
	private String rememberToken;
	/**
	 * 帐户类型：1. 系统帐号； 2.政府帐号   3.公司帐号. 4. 集团帐号
	 */
	@ApiModelProperty("帐户类型：1. 系统帐号； 2.政府帐号   3.公司帐号. 4. 集团帐号")
	private Integer userType;
	/**
	 * 最后登录IP
	 */
	@ApiModelProperty("最后登录IP")
	private String userLastIp;
	/**
	 * 本地化
	 */
	@ApiModelProperty("本地化")
	private String userLocalize;
	/**
	 * 头像
	 */
	@ApiModelProperty("头像")
	private String userPicture;
	/**
	 * 地址
	 */
	@ApiModelProperty("地址")
	private String userAddress;
	/**
	 * 单位名称
	 */
	@ApiModelProperty("公司名称")
	private String companyName;
	/**
	 * 性别
	 */
	@ApiModelProperty("性别")
	private Integer userSex;
	/**
	 * 用户简介
	 */
	@ApiModelProperty("用户简介")
	private String userIntro;
	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	private Integer userOrder;
	/**
	 * 极光推送id
	 */
	@ApiModelProperty("极光推送id")
	private String jPushId;
	/**
	 * 微信openId
	 */
	@ApiModelProperty("微信openId")
	private String wxOpenId;
	/**
	 * 用户职称
	 */
	@ApiModelProperty("用户职称")
	private String userRank;
	/**
	 * 身份证号
	 */
	@ApiModelProperty("身份证号")
	private String userIdcard;
	/**
	 * 工种
	 */
	@ApiModelProperty("工种")
	private String userWorkType;
	/**
	 * rongyun会话token
	 */
	private String chatSessionToken;
	/**
	 * 创建时间
	 */
	@ApiModelProperty("创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createdTime;
	/**
	 * 修改时间
	 */
	@ApiModelProperty("修改时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updatedTime;
	/**
	 * 创建人
	 */
	@ApiModelProperty("创建人ID")
	private Long createdBy;
	/**
	 * 修改人
	 */
	@ApiModelProperty("修改人ID")
	private Long updatedBy;
	/**
	 * 删除标记
	 */
	private Integer delFlag;

	//扩展字段，需要关联操作其他表数据
	/**
	 * 所属角色ID
	 */
	@ApiModelProperty("所属角色ID")
	private Long roleId;
	/**
	 * 所属角色名称
	 */
	@ApiModelProperty("所属角色名称")
	private String roleName;
	/**
	 * 所属岗位
	 */
	@ApiModelProperty("所属岗位id")
	private Long positionId;
	/**
	 * 所属岗位名称
	 */
	@ApiModelProperty("所属岗位名称")
	private String positionTitle;

	//扩展字段，与数据库业务无关

	//所在部门ID
	@ApiModelProperty("所在部门ID")
	private Long groupId;
	//所在部门名称
	@ApiModelProperty("所在部门名称")
	private String groupName;
	/**
	 * 公司对应的根群组ID
	 */
	@ApiModelProperty("公司对应的根群组ID")
	private Long companyGroupId;
	/**
	 * 监管机构ID
	 */
	@ApiModelProperty("监管机构ID")
	private Long supervisionId;
	/**
	 * 顶层集团公司id
	 */
	@ApiModelProperty("顶层集团公司id")
	private Long companyRootId;
	/**
	 * 上级企业ID
	 */
	@ApiModelProperty("上级企业ID")
	private Long companyParentId;

	/**
	 * 组织机构ID
	 */
	@ApiModelProperty("组织机构ID")
	private Long orgId;
	/**
	 * 组织机构名称
	 */
	@ApiModelProperty("组织机构名称")
	private String orgName;
	/**
	 * 新组织ID,位编码
	 */
	@ApiModelProperty("新组织ID,位编码")
	private Long orgGroupId;
	/**
	 * 左
	 */
	private Integer orgLeft;
	/**
	 * 右
	 */
	private Integer orgRight;

	/**
	 * 授权部门ID
	 */
	@ApiModelProperty("授权部门ID")
	private String grantGroupId;
	/**
	 * 授权部门名称
	 */
	@ApiModelProperty("授权部门名称")
	private String grantGroupName;
	/**
	 * 授权部门岗位ID
	 */
	@ApiModelProperty("授权部门岗位ID")
	private Long groupMemberPositionId;
	/**
	 * 部门授权表主键ID
	 */
	@ApiModelProperty("部门授权表主键ID")
	private Long groupMemberPositionGrantId;

	/**
	 * 现场人员列表中选中状态: 1选择,0 未选择
	 */
	@ApiModelProperty("现场人员列表中选中状态: 1选择,0 未选择")
	private Long ifChecked;

	@ApiModelProperty("监控平台地址")
	private String screen;

	@ApiModelProperty("注册Id")
	private String registrationId;

	public void insertPrefix(Long userId){
		this.setCreatedBy(userId );
		this.setCreatedTime(new Date());
		this.setUpdatedBy(userId );
		this.setUpdatedTime(new Date());
		this.setDelFlag(0);
	}

	public void updatePrefix(Long userId){
		this.setUpdatedBy(userId );
		this.setUpdatedTime(new Date());
	}

	@Override
	public String toString() {
		return "User{" +
				"userId=" + userId +
				", loginName='" + loginName + '\'' +
				", userName='" + userName + '\'' +
				", userMobile='" + userMobile + '\'' +
				", userEmail='" + userEmail + '\'' +
				", companyId=" + companyId +
				", userStatus=" + userStatus +
				", rememberToken='" + rememberToken + '\'' +
				", userType=" + userType +
				", userLastIp='" + userLastIp + '\'' +
				", userLocalize='" + userLocalize + '\'' +
				", userPicture='" + userPicture + '\'' +
				", userAddress='" + userAddress + '\'' +
				", companyName='" + companyName + '\'' +
				", userSex=" + userSex +
				", userIntro='" + userIntro + '\'' +
				", userOrder=" + userOrder +
				", userRank='" + userRank + '\'' +
				", userIdcard='" + userIdcard + '\'' +
				", userWorkType='" + userWorkType + '\'' +
				", createdTime=" + createdTime +
				", updatedTime=" + updatedTime +
				", createdBy=" + createdBy +
				", updatedBy=" + updatedBy +
				", delFlag=" + delFlag +
				", roleId=" + roleId +
				", positionId=" + positionId +
				", groupId=" + groupId +
				'}';
	}
}
