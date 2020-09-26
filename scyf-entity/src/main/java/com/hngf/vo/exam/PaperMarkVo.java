package com.hngf.vo.exam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 试卷类型分数
 * 
 * @author lxf
 * @email 
 * @date 2020-08-14 09:39:02
 */
@Data
@ApiModel("试卷类型分数")
public class PaperMarkVo implements Serializable {

	private static final long serialVersionUID = 6696581737987910849L;

	/**
	 * 试卷类型分数id
	 */
	@ApiModelProperty(value = "试卷类型分数id" , position =1 )
	private Long paperMarkId;

	/**
	 * 试卷id
	 */
	@ApiModelProperty(value = "试卷id" , position =2 )
	private Long paperId;

	/**
	 * 1单选2多选3判断
	 *
	 */
    @ApiModelProperty(value = "1单选2多选3判断"  , position =3 )
    private Integer questionsType;
	/**
	 * 分数
	 */
    @ApiModelProperty(value = "分数" , position = 4  )
    private Integer mark;
	/**
	 * 多选题答案少选分值
	 */
    @ApiModelProperty(value = "多选题答案少选分值"  , position =5 )
    private Integer lessMark;
	/**
	 * 多选题答案多选分值
	 */
    @ApiModelProperty(value = "多选题答案多选分值"  , position = 6 )
    private Integer moreMark;
	/**
	 * 试卷类型的试题Id的集合
	 */
	@ApiModelProperty(value = "试卷类型的试题 " , position = 7 )
	private List<Long> questionsIdList ;

}
