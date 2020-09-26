package com.hngf.dto.commonPost;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("知识库model")
public class CommonPostDto implements Serializable {
 private static final long serialVersionUID = 1L;
 /**
  * 主键id
  */
 @ApiModelProperty("主键id")
 private Long postId;
 /**
  * 所属公司ID
  */
 @ApiModelProperty("所属公司ID")
 private Long companyId;
 /**
  * 所属单位
  */
 @ApiModelProperty("所属单位id")
 private Long groupId;
 /**
  * 分类ID
  */
 @ApiModelProperty("分类ID")
 private Long categoryId;
 /**
  * 标题
  */
 @ApiModelProperty("标题")
 private String postTitle;
 /**
  * 文章-章节-条目(article/chapter/item)5作业指导书；6安全标准化；7安全教育 ；
  * 8 应急预案；9规章制度；10岗位应知信息卡;11 两单一卡
  */
 @ApiModelProperty("5作业指导书；6安全标准化；7安全教育 ；8 应急预案；9规章制度")
 private String postType;
 /**
  * 内容类型 富文本-richtext/纯文本-puretext
  */
 @ApiModelProperty("内容类型 富文本-richtext/纯文本-puretext")
 private String postContentType;
 /**
  * 作者
  */
 @ApiModelProperty("作者")
 private String postAuthor;
 /**
  * 来源
  */
 @ApiModelProperty("来源")
 private String postSource;
 /**
  * 描述
  */
 @ApiModelProperty("描述")
 private String postDesc;
 /**
  * 图片地址
  */
 @ApiModelProperty("图片地址")
 private String postPicUrl;
 /**
  * 详情访问地址
  */
 @ApiModelProperty("详情访问地址")
 private String viewUrl;
 /**
  * 内容
  */
 @ApiModelProperty("内容")
 private String postContent;
 /**
  * 是否发布
  */
 @ApiModelProperty("是否发布")
 private Integer isPublish;
 /**
  * 发布时间
  */
 @ApiModelProperty("发布时间")
 private Date publishTime;
 /**
  * 创建时间
  */
 @ApiModelProperty("创建时间")
 private Date createdTime;
 /**
  * 创建人
  */
 @ApiModelProperty("创建人ID")
 private Long createdBy;
 /**
  * 更新时间
  */
 @ApiModelProperty("更新时间")
 private Date updatedTime;
 /**
  * 更新人
  */
 private Long updatedBy;
 /**
  * 删除标记
  */
 private Integer delFlag;
 /**
  * 用户名称
  */
 @ApiModelProperty("用户名称")
 private  String userName;
 /**
  * 部门名称
  */
 @ApiModelProperty("部门名称")
 private String groupName;

}
