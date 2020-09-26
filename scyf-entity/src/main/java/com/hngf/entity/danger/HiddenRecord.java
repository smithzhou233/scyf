package com.hngf.entity.danger;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 隐患表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@Data
public class HiddenRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	
	private Long hiddenRecordId;
	/**
	 * 隐患ID
	 */
	private Long hiddenId;
	/**
	 * 所属单位
	 */
	private Long companyId;
	/**
	 * 组织机构ID
	 */
	private Long groupId;
	/**
	 * 危险点ID
	 */
	private Long dengerousPointId;
	/**
	 * 检查表ID
	 */
	private Long inspectDefId;
	/**
	 * 任务ID
	 */
	private Long inspectScheduleId;
	/**
	 * 检查记录Id
	 */
	private Long inspectRecordId;
	/**
	 * 检查项ID/风险ID
	 */
	private Long inspectItemId;
	/**
	 * 检查内容ID/风险管控措施ID
	 */
	private Long inspectContentId;
	/**
	 * 隐患类型ID
	 */
	private Long hiddenCatId;
	/**
	 * 隐患等级：1重大，2较大，3一般，4较低；
【二级取：1,3】
【三级取：1,2,3】
【四级取：1,2,3,4】
	 */
	private Integer hiddenLevel;
	/**
	 * 隐患名称
	 */
	private String hiddenTitle;
	/**
	 * 隐患描述
	 */
	private String hiddenDesc;
	/**
	 * 风险等级
	 */
	private Integer riskLevel;
	/**
	 * 整改单位
	 */
	private String hiddenRectifyDept;
	/**
	 * 整改部门
	 */
	private Long hiddenRetifyGroup;
	/**
	 * 隐患原来整改人
	 */
	private Long hiddenQuondamRetifyBy;
	/**
	 * 整改人
	 */
	private Long hiddenRetifyBy;
	/**
	 * 整改期限
	 */
	private Date hiddenRetifyDeadline;
	/**
	 * 验收单位
	 */
	private Long hiddenAcceptedGroup;
	/**
	 * 验收人
	 */
	private Long hiddenAcceptedBy;
	/**
	 * 评审单位
	 */
	private Long hiddenReviewGroup;
	/**
	 * 评审人
	 */
	private Long hiddenReviewBy;
	/**
	 * 发生时间
	 */
	private Date happenedTime;
	/**
	 * 结束时间
	 */
	private Date finishedTime;
	/**
	 * 隐患状态

1: 已提交；
2：已评审；
3：已整改；
4：已验收；
5：已删除；
6 : 已撤销；
	 */
	private Integer status;
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
	 * 删除标记
	 */
	private Integer delFlag;

}
