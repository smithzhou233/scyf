package com.hngf.dto.sys;

import com.hngf.entity.sys.Group;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 群组DTO
 * @author dell
 * @since 2020/5/22 15:25
 */
@ApiModel(value = "组织部门Tree")
@Data
public class GroupDetailDto extends Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "父级部门名称",required = true,position = 1, dataType = "String")
    private String groupParentName;
    @ApiModelProperty(value = "部门类型标题",required = true,position = 3, dataType = "String")
    private String groupTypeTitle;
    @ApiModelProperty(value = "根部门Id",required = true,position = 5, dataType = "long")
    private Long groupRootId;
}
