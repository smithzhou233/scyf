package com.hngf.common.enums;

/**
 * 大屏数据类型
 */
public enum BigScreenDataTypeEnum {

    riskPointCount("riskPointCount", "风险点统计"),
    riskPointList("riskPointList", "风险点列表"),
    todayCheck("todayCheck", "今日检查"),
    weeklyCheck("weeklyCheck", "周检查"),
    warningRiskPoint("warningRiskPoint", "预警风险点"),
    hiddenCount("hiddenCount", "隐患统计"),
    hiddenBulletin("hiddenBulletin", "隐患通报"),
    riskPointControlRecord("riskPointControlRecord", "风险点预警信息"),
    hidden("hidden", "隐患"),
    riskPoint("riskpoint", "风险点"),
    allIndustryStatistics("allIndustryStatistics","监管级统计查询"),//监管级统计查询;
    securityScore("securityScore","下级单位安全生产分值"),
    riskIndex("riskIndex","下级企业风险指数");

    public String idType;
    public String typeName;

    private BigScreenDataTypeEnum(String iType, String typeName) {
        this.idType = iType;
        this.typeName = typeName;
    }

    public String getIdType() {
        return this.idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}


