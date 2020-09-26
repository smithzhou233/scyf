package com.hngf.entity.exam;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 试卷类型分数
 * 
 * @author hngf
 * @email 
 * @date 2020-08-14 09:39:02
 */
@Data
@ApiModel("试卷类型分数")
public class PaperMark implements Serializable {

	private static final long serialVersionUID = 1916689302605407040L;
	/**
	 * 试卷类型分数id
	 */
    @ApiModelProperty(value = "试卷类型分数id" )
    private Long paperMarkId;
	/**
	 * 试卷id
	 */
    @ApiModelProperty(value = "试卷id"  )
    private Long paperId;
	/**
	 * 1单选2多选3判断
	 *
	 */
    @ApiModelProperty(value = "1单选2多选3判断"  )
    private Integer questionsType;
	/**
	 * 分数
	 */
    @ApiModelProperty(value = "分数"  )
    private Integer mark;
	/**
	 * 多选题答案少选分值
	 */
    @ApiModelProperty(value = "多选题答案少选分值"  )
    private Integer lessMark;
	/**
	 * 多选题答案多选分值
	 */
    @ApiModelProperty(value = "多选题答案多选分值"  )
    private Integer moreMark;
	/**
	 * 部门id
	 */
    @ApiModelProperty(value = "部门id"  )
    private Long groupId;
	/**
	 * 企业id
	 */
    @ApiModelProperty(value = "企业id"  )
    private Long companyId;
	/**
	 * 创建人
	 */
    @ApiModelProperty(value = "创建人"  )
    private Long createdBy;
	/**
	 * 创建时间
	 */
    @ApiModelProperty(value = "创建时间"  )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
	/**
	 * 修改人
	 */
    @ApiModelProperty(value = "修改人"  )
    private Long updatedBy;
	/**
	 * 修改时间
	 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
	/**
	 * 删除标记 0正常 1删除
	 */
    private Integer delFlag;

	/**
	 * 部门名称
	 */
	@ApiModelProperty(value = "部门名称" )
	private String groupName;
	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称" )
	private String companyName;

	/**
	 * 新增 初始化
	 * @param createdBy
	 * @param companyId
	 * @param delFlag
	 */
	public void insertPrefix(Long createdBy, Long companyId, Integer delFlag){
		this.setCreatedBy(createdBy);
		this.setCreatedTime(new Date());
		this.setDelFlag(delFlag);
		this.setCompanyId(companyId);
	}

	/**
	 * 修改 初始化
	 * @param updatedBy
	 */
	public void updatePrefix(Long updatedBy){
		this.setUpdatedBy(updatedBy);
		this.setUpdatedTime(new Date());
	}

}
