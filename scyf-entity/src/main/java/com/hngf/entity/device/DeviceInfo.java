package com.hngf.entity.device;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.util.Date;

/**
 * 设备信息表
 * 
 * @author hngf
 * @email 
 * @date 2020-06-16 15:10:13
 */
@Data
public class DeviceInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	
	private Long deviceId;
	/**
	 * 公司ID
	 */
	private Long companyId;
	/**
	 * 群组ID 
	 */
	@NotNull(message = "使用部门Id 不能为空！")
	private Long groupId;
	/**
	 * 设备类型:1生产2消防3办公
	 */
	@Length(message = "设备类型长度范围0-2", max = 2)
	private String deviceTypeId;
	/**
	 * 风险点ID
	 */
	private Long riskPointId;
	/**
	 * 设备名称
	 */
	@NotNull(message = "设备名称不能为空！")
	@Length(message = "设备名称长度范围1-200", min=1, max = 200)
	private String deviceName;
	/**
	 * 设备序列号
	 */
	@Length(message = "设备序列号长度范围0-36", max = 36)
	private String deviceNumber;
	/**
	 * 设备型号
	 */
	@Length(message = "设备型号长度范围0-200", max = 200)
	private String deviceModel;
	/**
	 * 设备品牌
	 */
	@Length(message = "设备名称长度范围0-200", max = 200)
	private String deviceBrand;
	/**
	 * 安装位置
	 */
	@Length(message = "安装位置长度范围0-200", max = 200)
	private String devicePosition;
	/**
	 * 责任人
	 */
	@Length(message = "责任人长度范围0-1000", max = 1000)
	private String responsible;
	/**
	 * 设备备注
	 */
	@Length(message = "设备备注长度范围0-1000", max = 1000)
	private String deviceRemark;
	/**
	 * 0正常1未使用
	 */
	@Range(message = "设备状态", max = 2L )
	private Integer deviceStatus;
	/**
	 * 备用字段
	 */
	private String backup2;
	/**
	 * 备用字段
	 */
	private String backup3;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date createdTime;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date updatedTime;
	/**
	 * 创建人
	 */
	@JsonIgnore
	private Long createdBy;
	/**
	 * 更新人
	 */
	@JsonIgnore
	private Long updatedBy;
	/**
	 * 删除标记0正常1删除
	 */
	@JsonIgnore
	private Integer delFlag;
	/**
	 * 责任人电话
	 */
	private Long responsiblePhone;
	/**
	 * 操作人员
	 */
	@Length(message = "安a置长度范围0-1000", max = 1000)
	private String operatingPersonnel;
	/**
	 * 验收人
	 */
	private String deviceAcceptor;
	/**
	 * 验收日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date acceptorTime;
	/**
	 * 安全许可证号
	 */
	private String securityNumber;
	/**
	 * 发证日期
	 */
	@NotNull(message = "发证日期不能为空")
	@Past(message = "需要一个过去日期")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date securityTime;
	/**
	 * 安装单位
	 */
	private String deviceUnit;
	/**
	 * 安装日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date deviceTime;
	/**
	 * 投用日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date proactivelyTime;
	/**
	 * 退场日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date exitTime;
	/**
	 * 是否有设备概况信息（单选：0是/1否）
	 */
	private Integer deviceInfo;
	/**
	 * 是否有设备人员配置信息牌（单选：0是/1否）
	 */
	private Integer devicePersonnelInfo;
	/**
	 * 是否有告知及使用登记信息牌（单选：0是/1否）
	 */
	private Integer deviceRegisterInfo;
	/**
	 * 是否有按照验收合格信息牌（单选：0是/1否）
	 */
	private Integer deviceAcceptanceInfo;
	/**
	 * 使用部门
	 */
    private String groupName;

    public void addPrefixInit(Long createdBy, Long companyId){
    	this.setCompanyId(companyId);
    	this.setCreatedBy(createdBy);
    	this.setUpdatedBy(createdBy);
    	this.setCreatedTime(new Date());
    	this.setUpdatedTime(new Date());
    	this.setDelFlag(0);
	}
}
