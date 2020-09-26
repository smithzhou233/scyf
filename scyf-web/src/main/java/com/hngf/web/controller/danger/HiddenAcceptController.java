package com.hngf.web.controller.danger;

import java.util.Arrays;
import java.util.Map;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hngf.entity.danger.HiddenAccept;
import com.hngf.service.danger.HiddenAcceptService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 隐患整改验收记录
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@RestController
@RequestMapping("scyf/hiddenaccept")
public class HiddenAcceptController {
    @Autowired
    private HiddenAcceptService HiddenAcceptService;

    /**
     * 信息
     */
    @RequestMapping(value="/info/{hiddenAcceptId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:hiddenaccept:info")
    public R info(@PathVariable("hiddenAcceptId") Long hiddenAcceptId){
        HiddenAccept HiddenAccept = HiddenAcceptService.getById(hiddenAcceptId);

        return R.ok().put("HiddenAccept", HiddenAccept);
    }


}
