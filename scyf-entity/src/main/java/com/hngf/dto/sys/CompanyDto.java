package com.hngf.dto.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("企业信息model")
public class CompanyDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("企业Id")
    private Long companyId;//企业Id
    @ApiModelProperty("企业名称")
    private String companyName;//企业名称
    @ApiModelProperty("企业logo")
    private String companyPic;//企业logo
    @ApiModelProperty("注册号")
    private String registerNumber;//注册号
    @ApiModelProperty("织机构代码")
    private String orgCode;//组织机构代码
    @ApiModelProperty("成立时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8" )
    private Date foundDate;//成立时间
    @ApiModelProperty("法定代表人")
    private String companyDeputy;//法定代表人
    @ApiModelProperty("联系电话")
    private String companyMobile;//联系电话
    @ApiModelProperty("电子邮箱")
    private String compayEmail;//电子邮箱
    @ApiModelProperty("注册地址")
    private String companyAddress;//注册地址
    @ApiModelProperty("邮政编码")
    private String postalCode;//邮政编码
    @ApiModelProperty("经济类型编码")
    private Integer economicTypeCode;//经济类型编码
    @ApiModelProperty("行业类别代码")
    private String industryTypeCoe;//行业类别代码
    @ApiModelProperty("企业行政隶属关系")
    private String companyAffiliation;//企业行政隶属关系
    @ApiModelProperty("经营范围")
    private String businessScope;//经营范围
    @ApiModelProperty("企业状态")
    private Integer companyStatus;//企业状态
    @ApiModelProperty("企业位置经度")
    private BigDecimal longitude;//企业位置经度
    @ApiModelProperty("企业位置纬度")
    private BigDecimal latitude;//企业位置纬度
    @ApiModelProperty("企业经营区域(百度)")
    private String businessScopeBaidu;//企业经营区域(百度)
    @ApiModelProperty("企业经营区域(3D)")
    private String businessScope3d;//企业经营区域(3D)
    @ApiModelProperty("公司管理员账号")
    private Long companyAdminId;//公司管理员账号
    @ApiModelProperty("公司对应的根群组ID")
    private Long companyGroupId;//公司对应的根群组ID
    @ApiModelProperty("监管机构Id")
    private Long supervisionId;//监管机构Id
    @ApiModelProperty("顶层集团公司ID")
    private Long companyRootId;//顶层集团公司ID
    @ApiModelProperty("上级企业Id")
    private Long companyParentId;//上级企业Id
    @ApiModelProperty("企业类型")
    private Integer companyTypeId;//企业类型
    @ApiModelProperty("")
    private Integer companyLeft;//左
    @ApiModelProperty("")
    private Integer companyRight;//右
    @ApiModelProperty("企业级别")
    private Integer companyLevel;//企业级别
    @ApiModelProperty("部署情况 1 未开始（红） 2 正在部署（黄） 3 部署完成 （绿）")
    private Integer deployment;//部署情况 1 未开始（红） 2 正在部署（黄） 3 部署完成 （绿）
    @ApiModelProperty("主要负责人")
    private String majorName;//主要负责人
    @ApiModelProperty("主要负责人固定电话号码")
    private String majorTelephone;//主要负责人固定电话号码
    @ApiModelProperty("主要负责人移动电话号码")
    private String majorPhone;//主要负责人移动电话号码
    @ApiModelProperty("主要负责人电子邮箱")
    private String majorEmail;//主要负责人电子邮箱
    @ApiModelProperty("安全负责人")
    private String secureName;//安全负责人
    @ApiModelProperty("安全负责人固定电话号码")
    private String secureTelephone;//安全负责人固定电话号码
    @ApiModelProperty("安全负责人固定电话号码")
    private String securePhone;//安全负责人固定电话号码
    @ApiModelProperty("安全负责人电子邮箱")
    private String secureEmail;//安全负责人电子邮箱
    @ApiModelProperty("安全机构设置情况")
    private Integer secureSetting;//安全机构设置情况
    @ApiModelProperty("从业人员数量")
    private Integer employeePersons;//从业人员数量
    @ApiModelProperty("特殊作业人员数量")
    private Integer specialWorkPersons;//特殊作业人员数量
    @ApiModelProperty("专职安全生产管理人员数")
    private Integer fulltimeSecurePersons;//专职安全生产管理人员数
    @ApiModelProperty("专职应急管理人员数")
    private Integer fulltimeEmergencyPersons;//专职应急管理人员数
    @ApiModelProperty("注册安全工程师人员数")
    private Integer registerSecureEngineerPersons;//注册安全工程师人员数
    @ApiModelProperty("安全监管监察机构")
    private String secureSupervisePersons;//安全监管监察机构
    @ApiModelProperty("生产/经营地址")
    private String productionAddress;//生产/经营地址
    @ApiModelProperty("规模情况")
    private Integer scaleCase;//规模情况
    @ApiModelProperty("企业规模")
    private Integer companyScale;//企业规模
    @ApiModelProperty("监管分类")
    private String superviseClassify;//监管分类
    @ApiModelProperty("隐患排查治理制度")
    private String hiddenCheckGovern;//隐患排查治理制度
    @ApiModelProperty("隐患排查治理计划")
    private String hiddenCheckPlan;//隐患排查治理计划
    @ApiModelProperty("行业领域名称")
    private String industryName;//行业领域名称
    @ApiModelProperty("名称")
    private String itemName;//名称
    @ApiModelProperty("组织机构名称")
    private String orgName;//组织机构名称
    @ApiModelProperty("大屏跳转地址")
    private String webUrl;//大屏跳转地址
    @ApiModelProperty("账号登录名称")
    private String loginName;//账号登录名称
    @ApiModelProperty("手机号码")
    private String userMobile;//手机号码
    @ApiModelProperty("用户Id")
    private Long userId;//用户Id
    @ApiModelProperty("企业证书")
    private String companyCertificate;//企业证书
    @ApiModelProperty("企业资质")
    private String companyAptitude;//企业资质
    /**
     * 所属省
     */
    @ApiModelProperty("所属省")
    private String provId;
    /**
     * 所属市
     */
    @ApiModelProperty("所属市")
    private String cityId;
    /**
     * 所属县/区
     */
    @ApiModelProperty("所属县/区")
    private String countyId;
    /**
     * 存放子级数据
     */
    @ApiModelProperty("子级数据")
    private List<CompanyDto> children;

    @ApiModelProperty("公司管理员账号名称")
    private String companyAdminName;//公司管理员账号名称

    @ApiModelProperty("企业是否具有删除权限标识符，值是1的时候 无删除权限，不需要删除按钮，值是0 时，有删除权限")
    private int delPermissionsFlag = 0;

    @ApiModelProperty(value = "评价方式" )
    private String evaluateType ;
}
