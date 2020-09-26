package com.hngf.dto.sys;

import com.hngf.entity.sys.GroupMemberPosition;
import lombok.Data;

/**
 * 群组成员岗位表
 * @author dell
 * @since 2020/5/23 17:30
 */
@Data
public class GroupMemberPositionDto extends GroupMemberPosition {
    private Integer grantGroupLeft;
    private Integer grantGroupRight;
    private String grantGroupName;
    private String positionTitle;
    private String positionDesc;
    private String accountRealName;
    private String groupName;
    private Long groupLeft;
    private Long groupRight;


}
