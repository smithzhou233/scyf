package com.hngf.vo.exam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 用户试卷表
 * 
 * @author lxf
 * @email 
 * @date 2020-08-15 11:57:10
 */
@Data
@ApiModel("用户交卷信息")
public class UserPaperVo implements Serializable {

	private static final long serialVersionUID = -5605061277165939283L;

	/**
	 * 用户试卷Id
	 */
	@ApiModelProperty(value = "用户试卷Id" , position = 1 )
	private Long userPaperId;

	/**
	 * 试卷id
	 */
    @ApiModelProperty(value = "试卷id" , position = 3 )
    private Long paperId;
	/**
	 * 试题答案
	 */
    @ApiModelProperty(value = "试题答案" , position = 5 )
    private Map<Long,String> paperQuestionsMap;

}
