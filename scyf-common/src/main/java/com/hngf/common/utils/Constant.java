package com.hngf.common.utils;

/**
 * 常量
 * @author hngf
 * @since 2020/5/20 15:43
 */
public class Constant {
    /** 超级管理员ID */
    public static final int SUPER_ADMIN = 1;

    /** 默认每页显示多少条数据*/
    public static final int PAGE_SIZE = 10;
    /** 每页显示20条数据*/
    public static final int PAGE_SIZE_20 = 20;
    /** 每页显示1000条数据*/
    public static final int PAGE_DB_SIZE = 1000;

    /**
     * 当前页码
     */
    public static final String PAGE_PAGENUM = "pageNum";
    /**
     * 每页显示记录数
     */
    public static final String PAGE_PAGESIZE = "pageSize";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "order";
    /**
     * 降序
     */
    public static final String ORDER_DESC = "desc";
    /**
     *  升序
     */
    public static final String ORDER_ASC = "asc";

    /**
     * 用户初始化密码
     */
    public static final String USER_PASSWORD = "888888" ;

    /** 数据权限过滤 */
    public static final String SQL_FILTER = "sql_filter";

    /**
     * 用户ID名称
     */
    public static final String USER_ID = "userId";
    /**
     * 用户登录名
     */
    public static final String USER_LOGIN_NAME = "loginName";

    /**
     *  绩效考核打分规则   配置编号
     */
    public static final Integer SCORE_SETTING_SCHDULE_YQ_1 = 1;
    public static final Integer SCORE_SETTING_HDANGER_ZGCS_2 = 2;
    public static final Integer SCORE_SETTING_SCHDULE_JCWC_3 = 3;
    public static final Integer SCORE_SETTING_HDANGER_ZGWC_4 = 4;
    public static final Integer SCORE_SETTING_HDANGER_YSWC_5 = 5;
    public static final Integer SCORE_SETTING_HDANGER_PSWC_6 = 6;
    /**
     *  绩效考核打分规则   配置类型
     */
    public static final Integer SCORE_TYPE_ADDING_1 = 1;
    public static final Integer SCORE_TYPE_DEDCUT_2 = 2;


    /**
     * 增加风险表 风险类型
     */
    public static final Integer RISK_TYPE_CHANGGUI = 1;

    /**
     * 增加隐患巡查 巡查类型
     */
    public static final Integer CLASSIFY_TYPE_SCHDULE_4 = 4;
    public static final Integer CLASSIFY_TYPE_HDANGER_1 = 1;
    public static final Integer CLASSIFY_TYPE_CHECKEDDEF_2 = 2;

    /**
     * 隐患是否有评审的开关，1评审；0不评审
     */
    public static final Integer IS_HDANGER_REVIEW_YES = 1;
    public static final Integer IS_HDANGER_REVIEW_ONT = 0;

    /**
     *巡检设备开关  ：0不扫码（false）；1扫码（true）
     */
    public static final Integer IS_CHECK_DEVICE_ON_YES = 1;
    public static final Integer IS_CHECK_DEVICE_ON_ONT = 0;
    /**
     * 隐患评审是否通过，1通过；0不通过
     */
    public static final Integer REVIEW_YES = 1;
    public static final Integer REVIEW_ONT = 0;
    /**
     * 是否显示检查地址
     */
    public static final Integer IS_SHOW_CHECK_ADDRESS_YES = 1;
    public static final Integer IS_SHOW_CHECK_ADDRESS_ONT = 0;


    /**
     * 隐患相关常量
     */
    public static final Integer CHECK_RESULT_WJC = 0;
    public static final Integer CHECK_RESULT_TG = 1;
    public static final Integer CHECK_RESULT_BTG = 2;
    public static final Integer CHECK_RESULT_CZYH = 3;
    public static final Integer CHECK_RESULT_BSJ = 4;
    public static final Integer CHECK_RESULT_FINISH = 5;

    public static final Integer DANGER_NOT = 0;
    public static final Integer DANGER_DPS = 1;
    public static final Integer DANGER_DZG = 2;
    public static final Integer DANGER_DYS = 3;
    public static final Integer DANGER_YSTG = 4;
    public static final Integer DANGER_YSBTG = 5;
    public static final Integer DANGER_YCX = 6;
    public static final Integer DANGER_DELETE = 7;
    public static final Integer DANGER_WAITHAND = 9;

    public static final Integer ATTACH_HDANGER = 1;
    public static final Integer ATTACH_ACCEPT = 2;
    public static final Integer ATTACH_RETIFY = 3;
    public static final Integer ATTACH_MEASURE = 4;

    public static final Integer HIDDEN_DANGER_YTJ = 1;
    public static final Integer HIDDEN_DANGER_YPS = 2;
    public static final Integer HIDDEN_DANGER_YZG = 3;
    public static final Integer HIDDEN_DANGER_YYS = 4;
    public static final Integer HIDDEN_DANGER_YCX = 6;
    public static final Integer HIDDEN_DANGER_DEL = 7;

    /**
     * 检查项常量
     */
    public static final Integer OWNER_TYPE_0 = 0;
    public static final Integer OWNER_TYPE_1 = 1;
    public static final Integer OWNER_TYPE_2 = 2;
    public static final Integer OWNER_TYPE_3 = 3;
    public static final Integer OWNER_TYPE_4 = 4;
    public static final Integer OWNER_TYPE_5 = 5;

    /**
     * 风险常量
     */
    public static final Integer RISK_TYPE_ZHUANYE = 2;

    /**
     * 保存风险点设备设施/作业活动时用到的
     */
    public static final Integer MAP_DOT = 1;
    public static final Integer MAP_THREAD = 2;
    public static final Integer MAP_REGION = 3;
    public static final Integer MAP_CAMERA = 4;

    //检查任务用到
    public static final Integer RISK_POINT_ALARM_HDANGER = 1;
    public static final Integer RISK_POINT_ALARM_TASK = 2;
    public static final Integer RISK_POINT_ALARM_TRANSDUCER = 3;

    /**
     * 指导书常量
     */
    public static final Integer KNOWLEDGE = 0;
    /**
     * 作业指导书
     */
    public static final Integer WORK_INSTRUCTION = 5;
    /**
	 * 菜单类型
	 */
    public enum MenuType {
    	/**
    	 * 分类
    	 */
    	CLASSIFY(1),
        /**
         * 目录
         */
    	CATALOG(2),
        /**
         * 菜单
         */
        MENU(3),
        /**
         * 按钮
         */
        BUTTON(4);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }


    }
    public interface SESSION_USER {
        String ID = "userinfo_id";
        String MOBILE = "userinfo_mobile";
        String NICKNAME = "userinfo_nick_name";
        String MENU_LIST = "user_menu_list";
        String ROLE_IDS = "user_role_ids";
        String USERNAME = "user_name"; // 平台管理端用户名
        String IP_ADDR = "ip_addr"; // 登录用户当前IP
        String ROLE_NAMES = "user_role_names";//用户角色
    }
    /**
     * 正常状态
     */
    public static final Integer STATUS_NORMAL = 0;

    /**
     * 停止状态
     */
    public static final Integer STATUS_DISABLE = -1;

    /**
     * 删除标志
     */
    public static final Integer DEL_FLAG_1 = 1;

    /**
     * 未删除
     */
    public static final Integer DEL_FLAG_0 = 0;

    /**
     * 领导岗位（临时使用）  多个岗位用“，”隔开
     */
    public static final String LEADER_POSITON_IDS="87,88";
}
