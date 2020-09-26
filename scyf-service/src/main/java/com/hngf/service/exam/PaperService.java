package com.hngf.service.exam;

import com.hngf.common.utils.PageUtils;
import com.hngf.dto.exam.PaperDto;
import com.hngf.entity.exam.Paper;
import com.hngf.vo.exam.PaperToUserVo;
import com.hngf.vo.exam.PaperVo;

import java.util.List;
import java.util.Map;

/**
 * 试卷表
 *
 * @author hngf
 * @email 
 * @date 2020-08-14 09:39:02
 */
public interface PaperService {

    PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order);

    /**
    * 根据ID查询
    */
    Paper getById(Long id);

    /**
    * 根据ID查询
    */
    PaperDto getDtoById(Long id);

    /**
    * 保存
    */
    void save(PaperVo paperVo , Long userId , Long companyId);

    /**
    * 更新
    */
    void update(PaperVo paperVo , Long userId ,Long companyId);

    /**
    * 批量删除
    */
    void removeByIds(List ids,Long updatedBy);

    /**
    * 删除
    */
    void removeById(Long id,Long updatedBy);

    /**
     * 试卷发布/撤销
     * status=0撤销，=1 或者 null 发布
     * @param paperToUserVo
     * @return
     */
    void updatePaperStatus(PaperToUserVo paperToUserVo, Long userId ,Long  groupId, Long companyId);
}

