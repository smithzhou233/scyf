package com.hngf.dto.sys;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ElementUIDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 名称
     */
    private String label;
    /**
     * 菜单类型
     */
    private Integer type;
    /**
     * 矢量图标
     */
    private String icon;

    /**
     * 排序序号
     */
    private Integer sort;
    /**
     * 子节点
     */
    private List<ElementUIDto> children;
}
