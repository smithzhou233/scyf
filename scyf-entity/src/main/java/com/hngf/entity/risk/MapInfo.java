package com.hngf.entity.risk;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业地图信息表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-26 16:06:37
 */
@Data
@ApiModel("企业地图model")
public class MapInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 地图ID
	 */
	@ApiModelProperty("地图ID")
	private Long mapId;
	/**
	 * 公司ID
	 */
	@ApiModelProperty("公司ID")
	private Long companyId;
	/**
	 * 群组ID
	 */
	@ApiModelProperty("群组ID")
	private Long groupId;
	/**
	 * 地图名称
	 */
	@ApiModelProperty("地图名称")
	private String mapName;
	/**
	 * 地图类别
	 */
	@ApiModelProperty("地图类别")
	private Integer mapType;
	/**
	 * 地图标志
	 */
	@ApiModelProperty("地图标志")
	private String mapLogo;
	/**
	 * 地图背景
	 */
	@ApiModelProperty("地图背景")
	private String mapBackground;
	/**
	 * 描述
	 */
	@ApiModelProperty("描述")
	private String mapRemark;
	/**
	 * 状态:1已发布，0未发布
	 */
	@ApiModelProperty("状态:1已发布，0未发布")
	private Integer status;
	/**
	 * 1 四色风险地图 2 示意图
	 */
	@ApiModelProperty("1 四色风险地图 2 示意图")
	private Integer useType;
	/**
	 * 地图服务地址
	 */
	@ApiModelProperty("地图服务地址")
	private String mapUrl;
	/**
	 * 全图范围最小X
	 */
	@ApiModelProperty(value = "全图范围最小X")
	private String xMin;
	/**
	 * 全图范围最小Y
	 */
	@ApiModelProperty(value = "全图范围最小Y")
	private String yMin;
	/**
	 * 全图范围最大X
	 */
	@ApiModelProperty(value = "全图范围最大X")
	private String xMax;
	/**
	 * 全图范围最大Y
	 */
	@ApiModelProperty(value = "全图范围最大Y")
	private String yMax;
	/**
	 * 空间参考
	 */
	@ApiModelProperty("空间参考")
	private String spatialReference;
	/**
	 * 添加时间
	 */
	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createdTime;
	/**
	 * 创建人
	 */
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private Long createdBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updatedTime;
	/**
	 * 更新人
	 */
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	private Long updatedBy;
	/**
	 * 删除标记
	 */
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private Integer delFlag;

}
