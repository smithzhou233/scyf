package com.hngf.entity.scyf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;

/**
 * 安全生产基本信息表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-22 16:52:31
 */
@Data
public class SecureProduc implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 企业id
	 */
	@ApiModelProperty("监控平台地址")
	private Long companyId;
	/**
	 * 主要负责人
	 */
	@Length(message = "主要负责人长度范围0-100", max = 100)
	private String majorName;
	/**
	 * 主要负责人固定电话号码
	 */
	@Length(message = "主要负责人固定电话号码长度范围0-100", max = 100)
	private String majorTelephone;
	/**
	 * 主要负责人移动电话号码
	 */
	@Length(message = "主要负责人移动电话号码长度范围0-100", max = 100)
	private String majorPhone;
	/**
	 * 主要负责人电子邮箱
	 */
	@Email(message = "主要负责人电子邮箱格式不正确")
	private String majorEmail;
	/**
	 * 安全负责人
	 */
	@Length(message = "安全负责人长度范围0-100",  max = 100)
	private String secureName;
	/**
	 * 安全负责人固定电话号码
	 */
	@Length(message = "安全负责人固定电话号码长度范围0-100",  max = 100)
	private String secureTelephone;
	/**
	 * 安全负责人移动电话号码
	 */
	@Length(message = "安全负责人移动电话号码长度范围0-100", max = 100)
	private String securePhone;
	/**
	 * 安全负责人电子邮箱
	 */
	@Email(message = "安全负责人电子邮箱格式错误")
	private String secureEmail;
	/**
	 * 安全机构设置情况
	 */
	@Range(message = "安全机构设置情况的值不能超过9",max = 9)
	private Integer secureSetting;
	/**
	 * 从业人员数量
	 */
	@Range(message = "从业人员数量长度范围1-10",max = 9999999999L)
	private Integer employeePersons;
	/**
	 * 特种作业人员数量
	 */
	@Range(message = "特种作业人员数量长度范围1-10",max = 9999999999L)
	private Integer specialWorkPersons;
	/**
	 * 专职安全生产管理人员数
	 */
	@Range(message = "专职安全生产管理人员数量长度范围1-8",max = 99999999L)
	private Integer fulltimeSecurePersons;
	/**
	 * 专职应急管理人员数
	 */
	@Range(message = "专职应急管理人员数量长度范围1-8",max = 99999999L)
	private Integer fulltimeEmergencyPersons;
	/**
	 * 注册安全工程师人员数
	 */
	@Range(message = "专职应急管理人员数量长度范围1-8",max = 99999999L)
	private Integer registerSecureEngineerPersons;
	/**
	 * 安全监管监察机构
	 */
	@Length(message = "安全监管监察机构的长度范围0-36", max = 36)
	private String secureSupervisePersons;
	/**
	 * 生产/经营地址
	 */
	@Length(message = "安全监管监察机构的长度范围0-500", max = 500)
	private String productionAddress;
	/**
	 * 规模情况
	 */
	@Range(message = "规模情况的值不能超过5",max= 5L )
	private Integer scaleCase;
	/**
	 * 企业规模
	 */
	@Range(message = "企业规模的值不能超过9",max= 9L )
	private Integer companyScale;
	/**
	 * 监管分类
	 */
	@Length(message = "监管分类的长度范围0-32", max = 32)
	private String superviseClassify;
	/**
	 * 隐患排查治理制度
	 */
	@Length(message = "隐患排查治理制度的长度范围0-220", max = 220)
	private String hiddenCheckGovern;
	/**
	 * 隐患排查治理计划
	 */
	@Length(message = "隐患排查治理计划的长度范围0-220", max = 220)
	private String hiddenCheckPlan;
	/**
	 * 创建时间
	 */
	@JsonIgnore
	private Date createdTime;
	/**
	 * 创建人
	 */
	@JsonIgnore
	private Long createdBy;
	/**
	 * 更新时间
	 */
	@JsonIgnore
	private Date updatedTime;
	/**
	 * 更新人
	 */
	@JsonIgnore
	private Long updatedBy;
	/**
	 * 删除标记
	 */
	@JsonIgnore
	private Integer delFlag;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public String getMajorTelephone() {
		return majorTelephone;
	}

	public void setMajorTelephone(String majorTelephone) {
		this.majorTelephone = majorTelephone;
	}

	public String getMajorPhone() {
		return majorPhone;
	}

	public void setMajorPhone(String majorPhone) {
		this.majorPhone = majorPhone;
	}

	public String getMajorEmail() {
		return majorEmail;
	}

	public void setMajorEmail(String majorEmail) {
		this.majorEmail = majorEmail;
	}

	public String getSecureName() {
		return secureName;
	}

	public void setSecureName(String secureName) {
		this.secureName = secureName;
	}

	public String getSecureTelephone() {
		return secureTelephone;
	}

	public void setSecureTelephone(String secureTelephone) {
		this.secureTelephone = secureTelephone;
	}

	public String getSecurePhone() {
		return securePhone;
	}

	public void setSecurePhone(String securePhone) {
		this.securePhone = securePhone;
	}

	public String getSecureEmail() {
		return secureEmail;
	}

	public void setSecureEmail(String secureEmail) {
		this.secureEmail = secureEmail;
	}

	public Integer getSecureSetting() {
		return secureSetting;
	}

	public void setSecureSetting(Integer secureSetting) {
		this.secureSetting = secureSetting;
	}

	public Integer getEmployeePersons() {
		return employeePersons;
	}

	public void setEmployeePersons(Integer employeePersons) {
		this.employeePersons = employeePersons;
	}

	public Integer getSpecialWorkPersons() {
		return specialWorkPersons;
	}

	public void setSpecialWorkPersons(Integer specialWorkPersons) {
		this.specialWorkPersons = specialWorkPersons;
	}

	public Integer getFulltimeSecurePersons() {
		return fulltimeSecurePersons;
	}

	public void setFulltimeSecurePersons(Integer fulltimeSecurePersons) {
		this.fulltimeSecurePersons = fulltimeSecurePersons;
	}

	public Integer getFulltimeEmergencyPersons() {
		return fulltimeEmergencyPersons;
	}

	public void setFulltimeEmergencyPersons(Integer fulltimeEmergencyPersons) {
		this.fulltimeEmergencyPersons = fulltimeEmergencyPersons;
	}

	public Integer getRegisterSecureEngineerPersons() {
		return registerSecureEngineerPersons;
	}

	public void setRegisterSecureEngineerPersons(Integer registerSecureEngineerPersons) {
		this.registerSecureEngineerPersons = registerSecureEngineerPersons;
	}

	public String getSecureSupervisePersons() {
		return secureSupervisePersons;
	}

	public void setSecureSupervisePersons(String secureSupervisePersons) {
		this.secureSupervisePersons = secureSupervisePersons;
	}

	public String getProductionAddress() {
		return productionAddress;
	}

	public void setProductionAddress(String productionAddress) {
		this.productionAddress = productionAddress;
	}

	public Integer getScaleCase() {
		return scaleCase;
	}

	public void setScaleCase(Integer scaleCase) {
		this.scaleCase = scaleCase;
	}

	public Integer getCompanyScale() {
		return companyScale;
	}

	public void setCompanyScale(Integer companyScale) {
		this.companyScale = companyScale;
	}

	public String getSuperviseClassify() {
		return superviseClassify;
	}

	public void setSuperviseClassify(String superviseClassify) {
		this.superviseClassify = superviseClassify;
	}

	public String getHiddenCheckGovern() {
		return hiddenCheckGovern;
	}

	public void setHiddenCheckGovern(String hiddenCheckGovern) {
		this.hiddenCheckGovern = hiddenCheckGovern;
	}

	public String getHiddenCheckPlan() {
		return hiddenCheckPlan;
	}

	public void setHiddenCheckPlan(String hiddenCheckPlan) {
		this.hiddenCheckPlan = hiddenCheckPlan;
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
}
