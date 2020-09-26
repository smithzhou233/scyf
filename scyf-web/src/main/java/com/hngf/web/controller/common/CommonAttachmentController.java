package com.hngf.web.controller.common;

import java.util.Arrays;
import java.util.Map;
import java.util.Date;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.hngf.web.controller.BaseController;

import com.hngf.entity.common.CommonAttachment;
import com.hngf.service.common.CommonAttachmentService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.utils.Constant;



/**
 * 业务通用附件表
 *
 * @author hngf
 * @email 
 * @date 2020-06-13 15:24:30
 */
@RestController
@RequestMapping("scyf/commonattachment")
public class CommonAttachmentController extends BaseController{
    @Autowired
    private CommonAttachmentService CommonAttachmentService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:commonattachment:list")
    public R list(@RequestParam Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = CommonAttachmentService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{attachmentId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:commonattachment:info")
    public R info(@PathVariable("attachmentId") Long attachmentId){
        CommonAttachment CommonAttachment = CommonAttachmentService.getById(attachmentId);

        return R.ok().put("data", CommonAttachment);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:commonattachment:save")
    public R save(@RequestBody CommonAttachment CommonAttachment){

        ValidatorUtils.validateEntity(CommonAttachment);
        CommonAttachment.setDelFlag(0);
        CommonAttachment.setCreatedTime(new Date());
        CommonAttachment.setCreatedBy(getUserId());
        CommonAttachmentService.save(CommonAttachment);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:commonattachment:update")
    public R update(@RequestBody CommonAttachment CommonAttachment){
        ValidatorUtils.validateEntity(CommonAttachment);
        CommonAttachment.setUpdatedTime(new Date());
        CommonAttachment.setUpdatedBy(getUserId());
        CommonAttachmentService.update(CommonAttachment);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:commonattachment:delete")
    public R delete(@RequestBody Long[] attachmentIds){
        CommonAttachmentService.removeByIds(Arrays.asList(attachmentIds));

        return R.ok();
    }

}
