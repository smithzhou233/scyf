package com.hngf.entity.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hngf.common.validator.group.AddGroup;
import com.hngf.common.validator.group.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业基础信息表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:31:55
 */
@Data
public class Company implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 企业ID
	 */
	
	private Long companyId;
	/**
	 * 企业logo
	 */
	private  String companyPic;
	/**
	 * 企业名称
	 */
	@NotNull(message = "企业名称不能为空！", groups = {AddGroup.class, UpdateGroup.class})
	@Length(message = "企业名称长度范围1-100", min = 1,max =100 )
	private String companyName;
	/**
	 * 注册号
	 */
	@NotNull(message = "注册号不能为空！",  groups = UpdateGroup.class)
	@Length(message = "注册号长度范围1-100", min = 1,max =100 ,  groups = UpdateGroup.class)
	private String registerNumber;
	/**
	 * 组织机构代码
	 */
	@NotNull(message = "组织机构代码不能为空！",  groups = UpdateGroup.class)
	@Length(message = "组织机构代码长度范围1-100", min = 1,max =100 ,  groups = UpdateGroup.class)
	private String orgCode;
	/**
	 * 成立日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8" )
	private Date foundDate;
	/**
	 * 法定代表人
	 */
	@NotNull(message = "法定代表人不能为空！",  groups = UpdateGroup.class)
	@Length(message = "法定代表人长度范围1-100", min = 1,max =100 ,  groups = UpdateGroup.class)
	private String companyDeputy;
	/**
	 * 联系电话
	 */
	@NotNull(message = "联系电话不能为空！",  groups = UpdateGroup.class)
	@Length(message = "联系电话长度范围1-100", min = 1,max =100 ,  groups = UpdateGroup.class)
	private String companyMobile;
	/**
	 * 电子邮箱
	 */
	@Email(message = "邮箱格式错误！")
	private String compayEmail;
	/**
	 * 注册地址
	 */
	@NotNull(message = "注册地址不能为空！",  groups = {AddGroup.class, UpdateGroup.class})
	@Length(message = "注册地址长度范围1-200", min = 1,max =200,  groups = {AddGroup.class, UpdateGroup.class} )
	private String companyAddress;
	/**
	 * 邮政编码
	 */
	@Length(message = "邮政编码长度范围0-6", max = 6 )
	private String postalCode;
	/**
	 * 所属省
	 */
	private String provId;
	/**
	 * 所属市
	 */
	private String cityId;
	/**
	 * 所属县/区
	 */
	private String countyId;
	/**
	 * 所属乡/街道
	 */
	private String townId;
	/**
	 * 经济类型代码
	 */
	private Integer economicTypeCode;
	/**
	 * 行业类别代码
	 */
	@NotNull(message = "行业类别代码不能为空！",  groups = UpdateGroup.class)
	@Length(message = "行业类别代码长度范围1-32", min = 1,max =32 ,  groups = UpdateGroup.class)
	private String industryTypeCoe;
	/**
	 * 企业行政隶属关系
	 */
	private String companyAffiliation;
	/**
	 * 经营范围
	 */
	private String businessScope;
	/**
	 * 企业状态
	 */
	@NotNull(message = "企业状态不能为空！",  groups = UpdateGroup.class )
	@Range(message = "企业状态范围0-100", max = 100l)
	private Integer companyStatus;
	/**
	 * 企业位置经度
	 */
	@NotNull(message = "企业位置经度不能为空！",  groups = {AddGroup.class, UpdateGroup.class})
	@DecimalMin(message = "经度小于最小",value = "000.000000",  groups = {AddGroup.class, UpdateGroup.class})
	@DecimalMax(message = "经度超过最大",value = "999.999999",  groups = {AddGroup.class, UpdateGroup.class})
	private BigDecimal longitude;
	/**
	 * 企业位置纬度
	 */
	@NotNull(message = "企业位置纬度不能为空！",  groups = {AddGroup.class, UpdateGroup.class})
	@DecimalMin(message = "纬度小于最小",value = "000.000000",  groups = {AddGroup.class, UpdateGroup.class})
	@DecimalMax(message = "纬度超过最大",value = "999.999999",  groups = {AddGroup.class, UpdateGroup.class})
	private BigDecimal latitude;
	/**
	 * 企业经营区域（百度）
	 */
	private String businessScopeBaidu;
	/**
	 * 企业经营区域（3D）
	 */
	private String businessScope3d;
	/**
	 * 公司管理员帐号
	 */
	private Long companyAdminId;
	/**
	 * 公司对应的根群组ID
	 */
	private Long companyGroupId;
	/**
	 * 监管机构ID
	 */
	private Long supervisionId;
	/**
	 * 顶层集团公司id
	 */
	private Long companyRootId;
	/**
	 * 上级企业ID
	 */
	private Long companyParentId;
	/**
	 * 0 企业 1集团
	 */
	private Integer companyTypeId;
	/**
	 * 左
	 */
	private Integer companyLeft;
	/**
	 * 右
	 */
	private Integer companyRight;
	/**
	 * 企业级别
	 */
	private Integer companyLevel;
	/**
	 * 部署情况 1 未开始（红） 2 正在部署（黄） 3 部署完成 （绿）
	 */
	private Integer deployment;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 更新人
	 */
	private Long updatedBy;
	/**
	 * 删除标记
	 */
	private Integer delFlag;
	/**
	 * 企业证书
	 */
	private String companyCertificate;
	/**
	 * 企业资质
	 */
	private String companyAptitude;

	private String evaluateType;


	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyPic() {
		return companyPic;
	}

	public void setCompanyPic(String companyPic) {
		this.companyPic = companyPic;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getRegisterNumber() {
		return registerNumber;
	}

	public void setRegisterNumber(String registerNumber) {
		this.registerNumber = registerNumber;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public Date getFoundDate() {
		return foundDate;
	}

	public void setFoundDate(Date foundDate) {
		this.foundDate = foundDate;
	}

	public String getCompanyDeputy() {
		return companyDeputy;
	}

	public void setCompanyDeputy(String companyDeputy) {
		this.companyDeputy = companyDeputy;
	}

	public String getCompanyMobile() {
		return companyMobile;
	}

	public void setCompanyMobile(String companyMobile) {
		this.companyMobile = companyMobile;
	}

	public String getCompayEmail() {
		return compayEmail;
	}

	public void setCompayEmail(String compayEmail) {
		this.compayEmail = compayEmail;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getProvId() {
		return provId;
	}

	public void setProvId(String provId) {
		this.provId = provId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCountyId() {
		return countyId;
	}

	public void setCountyId(String countyId) {
		this.countyId = countyId;
	}

	public String getTownId() {
		return townId;
	}

	public void setTownId(String townId) {
		this.townId = townId;
	}

	public Integer getEconomicTypeCode() {
		return economicTypeCode;
	}

	public void setEconomicTypeCode(Integer economicTypeCode) {
		this.economicTypeCode = economicTypeCode;
	}

	public String getIndustryTypeCoe() {
		return industryTypeCoe;
	}

	public void setIndustryTypeCoe(String industryTypeCoe) {
		this.industryTypeCoe = industryTypeCoe;
	}

	public String getCompanyAffiliation() {
		return companyAffiliation;
	}

	public void setCompanyAffiliation(String companyAffiliation) {
		this.companyAffiliation = companyAffiliation;
	}

	public String getBusinessScope() {
		return businessScope;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}

	public Integer getCompanyStatus() {
		return companyStatus;
	}

	public void setCompanyStatus(Integer companyStatus) {
		this.companyStatus = companyStatus;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public String getBusinessScopeBaidu() {
		return businessScopeBaidu;
	}

	public void setBusinessScopeBaidu(String businessScopeBaidu) {
		this.businessScopeBaidu = businessScopeBaidu;
	}

	public String getBusinessScope3d() {
		return businessScope3d;
	}

	public void setBusinessScope3d(String businessScope3d) {
		this.businessScope3d = businessScope3d;
	}

	public Long getCompanyAdminId() {
		return companyAdminId;
	}

	public void setCompanyAdminId(Long companyAdminId) {
		this.companyAdminId = companyAdminId;
	}

	public Long getCompanyGroupId() {
		return companyGroupId;
	}

	public void setCompanyGroupId(Long companyGroupId) {
		this.companyGroupId = companyGroupId;
	}

	public Long getSupervisionId() {
		return supervisionId;
	}

	public void setSupervisionId(Long supervisionId) {
		this.supervisionId = supervisionId;
	}

	public Long getCompanyRootId() {
		return companyRootId;
	}

	public void setCompanyRootId(Long companyRootId) {
		this.companyRootId = companyRootId;
	}

	public Long getCompanyParentId() {
		return companyParentId;
	}

	public void setCompanyParentId(Long companyParentId) {
		this.companyParentId = companyParentId;
	}

	public Integer getCompanyTypeId() {
		return companyTypeId;
	}

	public void setCompanyTypeId(Integer companyTypeId) {
		this.companyTypeId = companyTypeId;
	}

	public Integer getCompanyLeft() {
		return companyLeft;
	}

	public void setCompanyLeft(Integer companyLeft) {
		this.companyLeft = companyLeft;
	}

	public Integer getCompanyRight() {
		return companyRight;
	}

	public void setCompanyRight(Integer companyRight) {
		this.companyRight = companyRight;
	}

	public Integer getCompanyLevel() {
		return companyLevel;
	}

	public void setCompanyLevel(Integer companyLevel) {
		this.companyLevel = companyLevel;
	}

	public Integer getDeployment() {
		return deployment;
	}

	public void setDeployment(Integer deployment) {
		this.deployment = deployment;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

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
		return "Company{" +
				"companyId=" + companyId +
				", companyPic='" + companyPic + '\'' +
				", companyName='" + companyName + '\'' +
				", registerNumber='" + registerNumber + '\'' +
				", orgCode='" + orgCode + '\'' +
				", foundDate=" + foundDate +
				", companyDeputy='" + companyDeputy + '\'' +
				", companyMobile='" + companyMobile + '\'' +
				", compayEmail='" + compayEmail + '\'' +
				", companyAddress='" + companyAddress + '\'' +
				", postalCode='" + postalCode + '\'' +
				", provId='" + provId + '\'' +
				", cityId='" + cityId + '\'' +
				", countyId='" + countyId + '\'' +
				", townId='" + townId + '\'' +
				", economicTypeCode=" + economicTypeCode +
				", industryTypeCoe='" + industryTypeCoe + '\'' +
				", companyAffiliation='" + companyAffiliation + '\'' +
				", businessScope='" + businessScope + '\'' +
				", companyStatus=" + companyStatus +
				", longitude=" + longitude +
				", latitude=" + latitude +
				", businessScopeBaidu='" + businessScopeBaidu + '\'' +
				", businessScope3d='" + businessScope3d + '\'' +
				", companyAdminId=" + companyAdminId +
				", companyGroupId=" + companyGroupId +
				", supervisionId=" + supervisionId +
				", companyRootId=" + companyRootId +
				", companyParentId=" + companyParentId +
				", companyTypeId=" + companyTypeId +
				", companyLeft=" + companyLeft +
				", companyRight=" + companyRight +
				", companyLevel=" + companyLevel +
				", deployment=" + deployment +
				", createdTime=" + createdTime +
				", createdBy=" + createdBy +
				", updatedTime=" + updatedTime +
				", updatedBy=" + updatedBy +
				", delFlag=" + delFlag +
				'}';
	}
}
