package com.hngf.common.enums;

/**
 * 账号类型
 * @author zhangfei
 * @since 2020/5/23 15:16
 */
public enum AccountType {

    ACCOUNT_TYPE_NOT_EXIST(0, "无类型"),
    ACCOUNT_TYPE_SYSTEM(1, "系统帐号"),
    ACCOUNT_TYPE_ORG(2, "政府帐号"),
    ACCOUNT_TYPE_COMPANY(3, "公司帐号"),
    ACCOUNT_TYPE_GROUP(4, "集团帐号");

    private Integer idType;
    private String typeName;

    private AccountType(Integer iType, String typeName) {
        this.idType = iType;
        this.typeName = typeName;
    }

    public Integer getIdType() {
        return this.idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static AccountType valueOf(Integer value) {
        if (value != null) {
            AccountType[] accountTypes = values();
            for(int i = 0; i < accountTypes.length; ++i) {
                AccountType fsEnum = accountTypes[i];
                if (fsEnum.getIdType().equals(value)) {
                    return fsEnum;
                }
            }
        }
        return null;
    }
}
