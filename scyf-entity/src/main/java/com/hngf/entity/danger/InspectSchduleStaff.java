package com.hngf.entity.danger;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 检查任务定义 人员表
 * 
 * @author 闫姗姗
 * @email 
 * @date 2020-06-9
 */
@Data
public class InspectSchduleStaff implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * ID
	 */
	private Long staffId;
	/**
	 * 检查定义ID
	 */
	private Long schduleDefId;
	/**
	 * 所属企业
	 */
	private Long companyId;
	/**
	 * 所属部门
	 */
	private Long groupId;
	/**
	 * 人员编号
	 */
	private Long staffNo;
	/**
	 * 人员姓名
	 */
	private String staffName;
	/**
	 * 类型：执行人0、参与人1
	 */
	private Integer inspectMode;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新者Id
	 */
	private Long updatedBy;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 删除标记 0正常 1删除
	 */
	private Integer delFlag;
}
