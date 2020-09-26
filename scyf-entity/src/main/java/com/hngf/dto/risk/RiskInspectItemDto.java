package com.hngf.dto.risk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("检查项定义")
public class RiskInspectItemDto implements java.io.Serializable{

    private static final long serialVersionUID = 1L;
    /**
     * 检查项ID
     */
    @ApiModelProperty("检查项ID")
    private Long checkItemDefId;
    /**
     * 检查表ID
     */
    @ApiModelProperty("检查表ID")
    private Long checkDefId;
    /**
     * 检查项名称
     */
    @ApiModelProperty("检查项名称")
    private String checkItemDefName;
    /**
     * 风险等级
     */
    @ApiModelProperty("")
    private Integer riskLevel;
    /**
     * 具体内容描述
     */
    @ApiModelProperty("具体内容描述")
    private String checkItemDefDesc;
    /**
     * 检查方法
     */
    @ApiModelProperty("检查方法")
    private String checkItemDefMethod;
    /**
     * 检查依据
     */
    @ApiModelProperty("检查依据")
    private String checkItemDefRule;
    /**
     * 处罚依据
     */
    @ApiModelProperty("处罚依据")
    private String punishItemDefRule;

    /**
     * 检查项内容
     */
    @ApiModelProperty("检查项内容")
    List<InspectItemContentDto> inspectItemes;

    @ApiModelProperty("检查结果")
    private String checkResult;
}
