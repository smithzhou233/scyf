package com.hngf.service.utils;

import com.hngf.service.sys.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * ID生成器
 * @author zhangfei
 * @date 2020-06-17
 */
@Component
public class IdKit {

    @Autowired
    private DictService dictService;

    private static IdKit idKit;

    @PostConstruct
    public void init(){
        idKit = this;
        idKit.dictService = this.dictService;
    }

    /**
     * 检查记录编号
     */
    private final static String CHECK_RECORD_NO = "check_record_no" ;
    /**
     * 组织机构表
     */
    private final static String TABLE_SYS_ORG = "sys_org" ;
    /**
     * 岗位表
     */
    private final static String TABLE_SYS_POSITION = "sys_position" ;
    /**
     * 隐患表
     */
    private final static String TABLE_SCYF_HIDDEN = "scyf_hidden" ;
    /**
     * 检查记录表
     */
    private final static String TABLE_SCYF_RISK_INSPECT_RECORD = "scyf_risk_inspect_record" ;
    /**
     * 检查任务定义表
     */
    private final static String TABLE_SCYF_INSPECT_SCHDULE_DEF = "scyf_inspect_schdule_def" ;
    /**
     * 风险点表
     */
    private final static String TABLE_SCYF_RISK_POINT = "scyf_risk_point" ;
    /**
     * 危险源表
     */
    private final static String TABLE_SCYF_RISK_SOURCE = "scyf_risk_source" ;

    /**
     * 获取ID
     * @param tableName
     * @return
     */
    public static Long getId(String tableName){
        return idKit.dictService.getTabId(tableName);
    }

    /**
     * 获取检查记录表ID
     * @return
     */
    public static Long getRiskInspectRecordId(){
        return idKit.dictService.getTabId(IdKit.TABLE_SCYF_RISK_INSPECT_RECORD);
    }

    /**
     * 获取检查记录编号
     * @return
     */
    public static Long getCheckRecordNo(){
        return idKit.dictService.getTabId(IdKit.CHECK_RECORD_NO);
    }

    /**
     * 获取隐患表ID
     * @return
     */
    public static Long getHiddenId(){
        return idKit.dictService.getTabId(IdKit.TABLE_SCYF_HIDDEN);
    }

    /**
     * 获取危险源表ID
     * @return
     */
    public static Long getRiskSourceId(){
        return idKit.dictService.getTabId(IdKit.TABLE_SCYF_RISK_SOURCE);
    }

    /**
     * 获取风险点表ID
     * @return
     */
    public static Long getRiskPointId(){
        return idKit.dictService.getTabId(IdKit.TABLE_SCYF_RISK_POINT);
    }

    /**
     * 获取岗位表ID
     * @return
     */
    public static Long getPositionId(){
        return idKit.dictService.getTabId(IdKit.TABLE_SYS_POSITION);
    }

    /**
     * 获取检查任务定义表ID
     * @return
     */
    public static Long getInspectSchduleDefId(){
        return idKit.dictService.getTabId(IdKit.TABLE_SCYF_INSPECT_SCHDULE_DEF);
    }
    /**
     * 获取组织机构表ID
     * @return
     */
    public static Long getOrgId(){
        return idKit.dictService.getTabId(IdKit.TABLE_SYS_ORG);
    }
}
