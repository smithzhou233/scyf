package com.hngf.web.controller.sys;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hngf.web.controller.BaseController;

import com.hngf.entity.sys.Message;
import com.hngf.service.sys.MessageService;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 消息管理
 * @author zhangfei
 * @email 
 * @date 2020-06-23
 */
@RestController
@RequestMapping("sys/message")
@Api(value="消息管理",tags = {"消息管理"})
public class MessageController extends BaseController{
    @Autowired
    private MessageService messageService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="列表",response = Message.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "action", value = "1收件箱、2发件箱", required = true, paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "msgStatus", value = "0未读；1已读；", required = false, paramType = "query", dataType = "int")
    })
    public R list(@RequestParam() Integer action){
        Map<String, Object> params = new HashMap<>();
        params.put("action", action);
        params.put("userId", getUserId());
        //params.put("msgStatus", 0);
        params.put("companyId", getCompanyId());
        return R.ok().put("data", messageService.getList(params));
    }

    /**
     * 查询未读消息总数
     */
    @GetMapping("/getCount")
    @ApiOperation(value="查询未读消息总数")
    public R getCount(){
        Map<String, Object> params = new HashMap<>();
        params.put("userId", getUserId());
        params.put("msgStatus", 0);
        params.put("companyId", getCompanyId());
        return R.ok().put("data", messageService.getMessageCount(params));
    }

    /**
     * 消息设为已读
     */
    @GetMapping("/setRead")
    @ApiOperation(value="消息设为已读")
    @ApiImplicitParam(name = "msgId", value = "消息ID(不传时，全部消息设为已读)", required = false, paramType = "query", dataType = "int")
    public R setRead(@RequestParam(required = false)Long msgId){
        Map<String, Object> params = new HashMap<>();
        params.put("companyId", getCompanyId());
        params.put("userId", getUserId());
        params.put("updatedTime", new Date());
        params.put("msgId", msgId);
        messageService.setRead(params);
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{msgId}",method = RequestMethod.GET)
    public R info(@PathVariable("msgId") Long msgId){
        Message Message = messageService.getById(msgId);

        return R.ok().put("data", Message);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody Message Message){

        ValidatorUtils.validateEntity(Message);
        Message.setDelFlag(0);
        Message.setCreatedTime(new Date());
        Message.setCreatedBy(getUserId());
        messageService.save(Message);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody Message Message){
        ValidatorUtils.validateEntity(Message);
        Message.setUpdatedTime(new Date());
        Message.setUpdatedBy(getUserId());
        messageService.update(Message);
        
        return R.ok();
    }
}
