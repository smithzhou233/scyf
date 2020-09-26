package com.hngf.dto.risk;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("检查项内容")
public class InspectItemContentDto implements java.io.Serializable{

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
     * 检查结果 1通过；2不通过；3发现隐患；4未涉及；
     */
    @ApiModelProperty("检查结果 1通过；2不通过；3发现隐患；4未涉及；")
    private Integer checkResult;
    /**
     * 检查记录日志ID
     */
    @ApiModelProperty("检查记录日志ID")
    private Long checkRecordLogId;
    /**
     * 检查记录编号
     */
    @ApiModelProperty("检查记录编号")
    private Long checkRecordNo;
}
