package com.hngf.entity.risk;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 风险点地图标记数据
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Data
public class RiskPointMap implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "主键ID")
	private Long pointMapId;
	/**
	 * 企业地图背景
	 */
	@ApiModelProperty(value = "企业地图背景Id")
	private Long mapId;
	/**
	 * 企业id
	 */
	@ApiModelProperty(value = "企业id")
	private Long companyId;
	/**
	 * 风险点ID
	 */
	@ApiModelProperty(value = "风险点ID")
	private Long riskPointId;
	/**
	 * 风险点类型（1 设备设施 2 作业活动）
	 */
	@ApiModelProperty(value = "风险点类型（1 设备设施 2 作业活动）")
	private Integer riskPointType;
	/**
	 * 标注名称
	 */
	@ApiModelProperty(value = "标注名称")
	private String mapName;
	/**
	 * 标注数据
	 */
	@ApiModelProperty(value = "标注数据")
	private String mapData;
	/**
	 * 标注类型
	 */
	@ApiModelProperty(value = "标注类型")
	private Integer mapType;
	/**
	 * 添加时间
	 */
	@ApiModelProperty(value = "添加时间")
	private Date createTime;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Long createBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updatedTime;
	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private Long updatedBy;
	/**
	 * 删除标志  0 正常 1删除
	 */
	@ApiModelProperty(value = "删除标志  0 正常 1删除")
	private Integer delFlag;

}
