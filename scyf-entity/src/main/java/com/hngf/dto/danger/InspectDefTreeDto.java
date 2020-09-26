package com.hngf.dto.danger;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 检查定义表树形结构
 * @author zhangfei
 * @Date 2020/06/09
 */
@Data
public class InspectDefTreeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 名称
     */
    private String label;

    /**
     * 子节点
     */
    private List<InspectDefTreeDto> children;
}
