package com.hngf.entity.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import java.io.Serializable;

/**
 * 系统信息表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-22 09:58:36
 */
@ApiModel(value = "系统信息")
@Data
public class Info implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "信息Id", dataType = "long", position = 1 )
	private Long sysId;
	/**
	 * 名称
	 */
	@ApiModelProperty(value = "系统信息名称", dataType = "String",position = 3)
	@Length(message = "名称长度范围0-64", max = 64 )
	private String sysName;
	/**
	 * 编号
	 */
	@ApiModelProperty(value = "系统信息编号", dataType = "String",position = 5)
	@Length(message = "编号长度范围0-64", max = 64 )
	private String sysCode;
	/**
	 * logo
	 */
	@ApiModelProperty(value = "系统信息logo", dataType = "String",position = 7)
	@Length(message = "logo长度范围0-200", max = 200 )
	private String sysLogo;
	/**
	 * 描述信息
	 */
	@ApiModelProperty(value = "系统信息描述", dataType = "String",position = 9)
	private String sysDesc;
	/**
	 * 风险评价法 1 LEC  2 LS 
	 */
	@ApiModelProperty(value = "风险评价法 1 LEC  2 LS ", dataType = "int",position = 11)
	@Range(message = "最大值不能超过9",max = 9L)
	private Integer riskJudgeMethod;
	/**
	 * 大屏跳转地址
	 */
	@ApiModelProperty(value = "大屏跳转地址 ", dataType = "String",position = 13)
	@Length(message = "大屏跳转地址范围0-220", max = 220)
	private String webUrl;
	/**
	 * 企业ID
	 */
	@ApiModelProperty(value = "企业ID ", dataType = "long",position = 15 )
	private Long companyId;
	/**
	 * 扫码检查控制：0不扫码（false）；1扫码（true）。
	 */
	@ApiModelProperty("扫码检查控制：0不扫码（false）；1扫码（true）")
	@Max(message = "0不扫码（false）；1扫码（true）", value = 9)
	private Integer checkDeviceOn;
	/**
	 * 隐患是否有评审的开关，1评审；0不评审
	 */
	@ApiModelProperty("隐患是否有评审的开关，1评审；0不评审")
	@Max(message = "隐患是否有评审的开关，1评审；0不评审", value = 9)
	private Integer hdangerreviewOn;
	/**
	 * 是否显示风险点检查地址：1是；0否；
	 */
	@ApiModelProperty("是否显示风险点检查地址：1是；0否；")
	@Max(message = "是否显示风险点检查地址：1是；0否", value = 9)
	private Integer isShowCheckAddress;
	/**
	 * 隐患流程：1无整改方案；2重大隐患评审时递交整改方案
	 */
	@ApiModelProperty("隐患流程：1无整改方案；2重大隐患评审时递交整改方案")
	@Max(message = "隐患流程：1无整改方案；2重大隐患评审时递交整改方案", value = 9)
	private Integer hdangerWorkFlow;
	/**
	 * 隐患分布图
	 */
	@ApiModelProperty("隐患分布图")
	@Length(message = "隐患分布图 长度范围0-255", max = 255 )
	private String facilityMap;
	/**
	 * 删除标记0正常1删除
	 */
	@ApiModelProperty(hidden = true )
	private Integer delFlag;
	/**
	 *  是否自动督导 1. 是  0.否
	 */
	@ApiModelProperty("是否自动督导 1. 是  0.否")
	@Max(message = "是否自动督导 1. 是  0.否",value = 9)
	private Integer autoSupervise;

	/**
	 * 督导人Id
	 */
	@ApiModelProperty("督导人Id")
	@Range(message = "督导人Id不能超过99999999999L", max = 99999999999L)
	private Long superviserId;

	/**
	 * 督导临期期限 （小时）
	 */
	@ApiModelProperty("督导临期期限 （小时）")
	@Max(message = "督导临期期限 （小时）不能超过999",value =999)
	private Integer superviserAdvent;

	/**
	 * 系统信息类型0集团企业 1监管机构，默认0集团企业
	 * 当前用户 根据userType 的值 来确定 用户类型，用来判断 所属 是 监管机构还是 集团企业 ：2 -监管机构， 3、4-集团企业
	 */
	@ApiModelProperty(value = "系统信息类型0集团企业 1监管机构，默认0集团企业")
	@Max(message = "系统信息类型0集团企业 1监管机构",value =9)
	private Integer infoType ;
	
	public Info(Long companyId, Integer infoType){
		this.setCompanyId(companyId);
		this.setInfoType(infoType);
		this.setWebUrl("");
		this.setSysCode("");
		this.setSysLogo("");
		this.setSysDesc("");
		this.setSysName("");
		this.setDelFlag(0);
		this.setRiskJudgeMethod(2);
		this.setCheckDeviceOn(0);
		this.setHdangerreviewOn(1);
		this.setIsShowCheckAddress(0);
		this.setFacilityMap("");
	}

	@Override
	public String toString() {
		return "Info{" +
				"sysId=" + sysId +
				", sysName='" + sysName + '\'' +
				", sysCode='" + sysCode + '\'' +
				", sysLogo='" + sysLogo + '\'' +
				", sysDesc='" + sysDesc + '\'' +
				", riskJudgeMethod=" + riskJudgeMethod +
				", webUrl='" + webUrl + '\'' +
				", companyId=" + companyId +
				", checkDeviceOn=" + checkDeviceOn +
				", hdangerreviewOn=" + hdangerreviewOn +
				", isShowCheckAddress=" + isShowCheckAddress +
				", hdangerWorkFlow=" + hdangerWorkFlow +
				", facilityMap='" + facilityMap + '\'' +
				", autoSupervise=" + autoSupervise +
				", superviserId=" + superviserId +
				", superviserAdvent=" + superviserAdvent +
				", delFlag=" + delFlag +
				'}';
	}
}
