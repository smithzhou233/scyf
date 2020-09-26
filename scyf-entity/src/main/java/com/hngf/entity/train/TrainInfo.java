package com.hngf.entity.train;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author hngf
 * @email 
 * @date 2020-05-26 11:59:17
 */
@Data
public class TrainInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 培训内容ID
	 */
	
	private Long trainInfoId;
	/**
	 * 培训计划ID
	 */
	@NotNull(message = "培训计划类型不能为空！")
	private Long trainPlanId;
	/**
	 * 公司ID
	 */
	private Long companyId;
	/**
	 * 群组ID
	 */
	private Long groupId;
	/**
	 * 培训主题
	 */
	@NotNull(message = "培训主题不能为空！")
	@Length(message = "主题长度范围1-64", min = 1,max = 64 )
	private String trainInfoName;
	/**
	 * 受训群组
	 */
	@NotNull(message = "受训群组不能为空！")
	@Length(message = "受训部门过多", min = 1,max = 1000 )
	private String trainGroupIds;
	/**
	 * 培训类型
	 */
	@NotNull(message = "培训类型不能为空！")
	@Range(message = "培训类型范围1-9999", min = 1,max = 9999 )
	private Long trainType;
	/**
	 * 培训人数
	 */
	@Range(message = "培训人数长度1-11位",  max=99999999999L)
	private Long trainNum;
	/**
	 * 开始时间
	 */
	@NotNull(message = "培训开始时间不能为空！")
	@Future(message = "需要一个将来日期")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date startDate;
	/**
	 * 结束时间
	 */
	@NotNull(message = "培训结束时间不能为空！")
	@Future(message = "需要一个将来日期")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date endDate;
	/**
	 * 培训课时
	 */
	@Range(message = "课时长度范围0-3",max = 999)
	private Integer trainInfoLesson;
	/**
	 * 培训地点
	 */
	@NotNull(message = "培训地点不能为空！")
	@Length(message = "地址长度范围1-250", min=1, max = 250 )
	private String trainInfoAddress;
	/**
	 * 培训主要内容
	 */
	@NotNull(message = "培训计划类型不能为空！")
	private String trainInfoContent;
	/**
	 * 添加时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date createdTime;
	/**
	 * 创建人
	 */
	private Long createdBy;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
	private Date updatedTime;
	/**
	 * 更新人
	 */
	private Long updatedBy;
	/**
	 * 删除标记
	 */
	private Integer delFlag;

	public void addPrefix(Long createdBy, Long companyId, Long groupId){
		this.setCompanyId(companyId);
		this.setGroupId(groupId);
		this.setCreatedBy(createdBy);
		this.setUpdatedBy(createdBy);
		this.setCreatedTime(new Date());
		this.setUpdatedTime(new Date());
		this.setDelFlag(0);

	}
	public void updatePrefix(Long updatedBy){
		this.setUpdatedBy(updatedBy);
		this.setUpdatedTime(new Date());
	}

}
