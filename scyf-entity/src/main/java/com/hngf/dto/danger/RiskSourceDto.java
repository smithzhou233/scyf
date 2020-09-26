package com.hngf.dto.danger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("危险源model")
public class RiskSourceDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("危险源ID")
    private Integer riskDangerId;
    @ApiModelProperty("企业ID")
    private Integer companyId;
    @ApiModelProperty("父级危险源ID")
    private Integer parentRiskDangerId;
    @ApiModelProperty("危险源类型（1 设备设施 2 作业活动）")
    private Integer riskDangerType;
    @ApiModelProperty("危险源名称")
    private String riskDangerName;
    @ApiModelProperty("标识")
    private String label;
    @ApiModelProperty("风险源等级（1、红色；2、橙色；3、黄色；4、蓝色）")
    private Integer riskDangerLevel;
    @ApiModelProperty("危险源编码")
    private String riskDangerCode;
    @ApiModelProperty("危险源图标")
    private String riskDangerImg;
    @ApiModelProperty("0：正常；1：锁定")
    private Integer riskDangerStatus;
    @ApiModelProperty("行业ID-外键")
    private Integer industryId;
    @ApiModelProperty("0固定 1移动")
    private Integer isFixed;
    @ApiModelProperty("是否叶子节点 1 叶子节点")
    private Integer isLeaf;
    @ApiModelProperty("节点左侧索引")
    private Integer nodeLeft;
    @ApiModelProperty("节点右侧索引")
    private Integer nodeRight;
    @ApiModelProperty("节点层级")
    private Integer nodeLevel;
    @ApiModelProperty("根节点")
    private Integer rootNode;
    @ApiModelProperty("添加时间")
    private Date createdTime;
    @ApiModelProperty("创建人")
    private Integer createdBy;
    @ApiModelProperty("修改时间")
    private Date updatedTime;
    @ApiModelProperty("修改人")
    private Integer updatedBy;
    @ApiModelProperty("删除标识")
    private Integer delFlag;
    @ApiModelProperty("风险数量")
    private Integer riskCount;
    @ApiModelProperty("")
    private Integer riskCountParent;
    @ApiModelProperty("判断是否添加子危险源 0时添加子危险源")
    private Integer useCount;
    /**
     * 存放子级数据
     */
    @ApiModelProperty("子集")
    private List<RiskSourceDto> children;
}
