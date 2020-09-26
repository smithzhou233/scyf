package com.hngf.entity.risk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hngf.entity.danger.Hidden;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 风险点索引表
 * 
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:23
 */
@Data
@JsonIgnoreProperties({"delFlag"})
@ApiModel("风险点model")
public class RiskPoint implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@ApiModelProperty(value = "ID")
	private Long riskPointId;
	/**
	 * 所属企业ID
	 */
	@ApiModelProperty(value = "所属企业ID")
	private Long companyId;
	/**
	 * 风险点名称
	 */
	@ApiModelProperty(value = "风险点名称")
	@NotBlank(message = "风险点名称不能为空")
	@Length(message = "风险点名称长度范围1-100", min=1, max = 100)
	private String riskPointName;
	/**
	 * 责任部门
	 */
	@ApiModelProperty(value = "责任部门")
	@NotNull(message = "责任部门必选")
	private Long dutyGroupId;
	/**
	 * 责任部门负责人
	 */
	@ApiModelProperty(value = "责任部门负责人")
	private Long dutyPerson;
	/**
	 *  电子NFC标签:0未绑定；1为绑定
	 */
	@ApiModelProperty(value = "电子NFC标签:0未绑定；1为绑定")
	@Range(message = "电子NFC标签 值不能超过9" , max = 9)
	private Integer nfcBind;
	/**
	 * 作业岗位绑定二维码信息
	 */
	@ApiModelProperty(value = "作业岗位绑定二维码信息")
	@Length(message = "作业岗位绑定二维码信息长度范围0-90", max = 90)
	private String qrcodeBind;
	/**
	 * 风险点的等级
	 */
	@ApiModelProperty(value = "风险点的等级")
	@Range(message = "风险点的等级值不能超过9", max = 9L)
	private Integer riskPointLevel;
	/**
	 * 是否重大危险源
	 */
	@ApiModelProperty(value = "是否重大危险源")
	private Integer isGreatDangerSrc;
	/**
	 * 标签列表
	 */
	@ApiModelProperty(value = "标签列表")
	private String riskTagList;
	/**
	 * 是否有摄像头0无1有
	 */
	@ApiModelProperty(value = "是否有摄像头0无1有")
	private Integer isIpCamera;
	/**
	 * IP摄像头地址
	 */
	@ApiModelProperty(value = "IP摄像头地址")
	private String ipCameraUrl;
	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String riskPointDesc;
	/**
	 * 是否激活：1激活；0未激活
	 */
	@ApiModelProperty(value = "是否激活：1激活；0未激活")
	private Integer isActive;
	/**
	 * 开始时间
	 */
	@Future(message = "开始时间应当为当前时间之后的时间")
	@ApiModelProperty(value = "开始时间")
	private Date startTime;
	/**
	 * 结束时间
	 */
	@Future(message = "结束时间应当为当前时间之后的时间")
	@ApiModelProperty(value = "结束时间")
	private Date endTime;
	/**
	 * 图片
	 */
	@Length(message = "图片范围长度：1-200", max = 220)
	@ApiModelProperty(value = "图片")
	private String riskPointImg;
	/**
	 * 主危险id
	 */
	@ApiModelProperty(value = "主危险id")
	private Long dangerSrcId;
	/**
	 * 主危险名称
	 */
	@ApiModelProperty(value = "主危险名称")
	private String dangerSrcName;
	/**
	 * 危险源数量
	 */
	@ApiModelProperty(value = " 危险源数量")
	private Integer dangerSourceNumber;
	/**
	 * 隐患数量
	 */
	@ApiModelProperty(value = "隐患数量")
	private Integer hdangerNumber;
	/**
	 * 风险数量
	 */
	@ApiModelProperty(value = "风险数量")
	private Integer riskNumber;
	/**
	 * 风险点类型（1 设备设施 2 作业活动）
	 */
	@ApiModelProperty(value = "风险点类型（1 设备设施 2 作业活动）")
	private Integer riskPointType;
	/**
	 * 是否失控1失控0受控
	 */
	@ApiModelProperty(value = "是否失控1失控0受控")
	private Integer isOutOfControl;
	/**
	 * 风险点位置
	 */
	@ApiModelProperty(value = "风险点位置")
	private String riskPointPlaces;
	/**
	 * 添加时间
	 */
	@ApiModelProperty(value = " 添加时间")
	private Date createdTime;
	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Long createdBy;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updatedTime;
	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private Long updatedBy;
	/**
	 * 删除标志  0 正常 1删除
	 */
	@ApiModelProperty(value = "删除标志  0 正常 1删除")
	private Integer delFlag;


	//扩展字段，与数据库字段无关

	/**
	 * 责任人ID，多个之间逗号分隔
	 */
	@ApiModelProperty(value = "责任人ID，多个之间逗号分隔")
	private String dutyPersons ;
	/**
	 * 责任人名称，多个之间逗号分隔
	 */
	@ApiModelProperty(value = "责任人名称，多个之间逗号分隔")
	private String dutyPersonNames ;

	/**
	 * 责任部门名称
	 */
	@ApiModelProperty(value = "责任部门名称")
	private String dutyGroupName ;
	/**
	 * 父级部门名称
	 */
	@ApiModelProperty(value = "父级部门名称")
	private String parentGroupName ;
	/**
	 * 所属企业名称
	 */
	@ApiModelProperty(value = "所属企业名称")
	private String companyName ;

	/**
	 * 激活开始时间
	 */
	@ApiModelProperty(value = "激活开始时间")
	private String activeStartTime;

	/**
	 * 激活结束时间
	 */
	@ApiModelProperty(value = "激活结束时间")
	private String activeEndTime;

	private Integer riskPointMinLevel;
	private Integer riskPointMinLevelSon;

	/**
	 * 地图ID
	 */
	@ApiModelProperty(value = "地图ID")
	private Long mapId;

	/**
	 * 地图数量
	 */
	@ApiModelProperty(value = "地图数量")
	private Long mapCount;
	@ApiModelProperty(value = "地图画点坐标")
	private String drawIds;

	/**
	 * 风险点涉及的危险源
	 */
	@ApiModelProperty(value = "风险点涉及的危险源ID")
	private String dangerSourceIds;

	/**
	 * 坐标
	 */
	@ApiModelProperty(value = "坐标")
	private String latlng;

	/**
	 * 风险检查表ID，多个之间逗号间隔
	 */
	@ApiModelProperty(value = "风险检查表ID")
	private String checkedTable;

	/**
	 * 以下List属性 为查询info接口时，返回更多信息使用
	 */
	//风险点警告信息
	@ApiModelProperty(value = "风险点警告信息")
	private List<RiskPointControlRecordLog> riskPointWarn;
	@ApiModelProperty(value = "未处理隐患")
	private List<Hidden> hiddenDangerList;
	@ApiModelProperty(value = "风险点检查记录")
	private List<RiskInspectRecord> riskCheckedRecord;
	@ApiModelProperty(value = "风险点现场人员")
	private List<RiskPointScenePerson> sitePersonnel;

}
