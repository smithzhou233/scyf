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

import com.hngf.entity.danger.HiddenRetify;
import com.hngf.service.danger.HiddenRetifyService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;



/**
 * 隐患整改记录
 *
 * @author hngf
 * @email 
 * @date 2020-05-20 17:36:24
 */
@RestController
@RequestMapping("scyf/hiddenretify")
public class HiddenRetifyController {
    @Autowired
    private HiddenRetifyService HiddenRetifyService;

    /**
     * 信息
     */
    @RequestMapping(value="/info/{hiddenRetifyId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:hiddenretify:info")
    public R info(@PathVariable("hiddenRetifyId") Long hiddenRetifyId){
        HiddenRetify HiddenRetify = HiddenRetifyService.getById(hiddenRetifyId);

        return R.ok().put("HiddenRetify", HiddenRetify);
    }


}
