package com.hngf.web.controller.score;

import java.util.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

import com.hngf.entity.score.ScoreModel;
import com.hngf.service.score.ScoreModelService;
import com.hngf.common.utils.PageUtils;
import com.hngf.common.utils.R;
import com.hngf.common.validator.ValidatorUtils;
import com.hngf.common.utils.Constant;



/**
 * 绩效考核模式配置表
 *
 * @author hngf
 * @email 
 * @date 2020-05-27 10:11:53
 */
@Api(value="绩效考核模式配置表",tags = {"绩效考核模式配置表"})
@RestController
@RequestMapping("scyf/scoremodel")
public class ScoreModelController extends BaseController{
    @Autowired
    private ScoreModelService ScoreModelService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @RequiresPermissions("scyf:scoremodel:list")
    public R list(@RequestParam Map<String, Object> params){

        int pageNum = null!= params.get(Constant.PAGE_PAGENUM) ? Integer.parseInt(params.get(Constant.PAGE_PAGENUM).toString()) : 1;
        int pageSize = null != params.get(Constant.PAGE_PAGESIZE) ? Integer.parseInt(params.get(Constant.PAGE_PAGESIZE).toString()) : Constant.PAGE_SIZE;

        PageUtils page = ScoreModelService.queryPage(params,pageNum,pageSize,null);

        return R.ok().put("page", page);
    }
    /**
    * @Author: zyj
    * @Description:查询考核模式配置
    * @Param 
    * @Date 15:59 2020/6/11
    */
    @RequestMapping(value = "/getModelSetting",method = RequestMethod.GET)
    @RequiresPermissions("scyf:scoremodel:list")
    @ApiOperation(value = "绩效考核模式配置表", notes="绩效考核模式配置表查询")
    @ApiImplicitParam(name = "settingType", value = "配置类型,1为考核模式,2为积分分模式", required = false, paramType = "query", dataType = "String")
      public R getModelSetting(@RequestParam(defaultValue = "1") String settingType){
        try {
            long companyId = getCompanyId();
            Map<String, Object> data = new HashMap();
            List<ScoreModel> modelSettings = ScoreModelService.queryByModelSettingPage(companyId);
            if (!modelSettings.isEmpty()) {
                Map<String, Object> map = new HashMap();
                map.put("companyId", companyId);
                Calendar date = Calendar.getInstance();
                String year = String.valueOf(date.get(1));
                map.put("year", year);
                Iterator var9 = modelSettings.iterator();

                while(var9.hasNext()) {
                    ScoreModel modelSetting = (ScoreModel)var9.next();
                    if (modelSetting.getScoreModelType().equals(1)) {
                        data.put("setting1", modelSetting);
                        data.put("settingType1", modelSetting.getScoreModelType());
                        if (modelSetting.getScoreModelStatus().equals(1)) {
                            settingType="1";
                            data.put("startTimeStr", modelSetting.getStartTimeStr());
                            data.put("initScore", this.setThisYearInitScore(companyId, modelSetting.getScoreModelId(), year));
                        }
                    }

                    if (modelSetting.getScoreModelType().equals(2)) {
                        if (modelSetting.getScoreModelStatus().equals(1)) {
                            settingType = "2";
                        }
                        data.put("setting2", modelSetting);
                        data.put("settingType2", modelSetting.getScoreModelType());
                    }
                }
            }

            data.put("settingType", settingType);
            return R.ok().put("data",data);
        }catch (Exception e){
            return R.error("查询失败");
        }

      }
      /**
       * @Author: zyj
     * @Description:通过企业id,父级企业id,年份,查询初始化分数
     * @Param companyId 企业id  scoreParentId企业父级id year年份
     * @Date 16:19 2020/6/11
       */
      private Integer setThisYearInitScore(Long companyId, Long scoreParentId, String year){
          Integer InitScore=0;
           try {
               ScoreModel thisYearinitScore = ScoreModelService.getThisYearinitScore(companyId, scoreParentId, year);
               if (thisYearinitScore!=null){
                   InitScore=thisYearinitScore.getInitScore();
               }
               return InitScore;
           }catch (Exception e){
               return InitScore;
           }
      }
      /**
      * @Author: zyj
      * @Description:绩效考核模式配置表添加配置
      * @Param
      * @Date 16:59 2020/6/11
      */
       @PostMapping("/saveModelSetting")
       @RequiresPermissions("scyf:scoremodel:save")
       @ApiOperation(value = "绩效考核模式配置表", notes="绩效考核模式配置表添加配置")
       @ApiImplicitParams({
               @ApiImplicitParam(name = "scoreModelId", value = "主键id,取后台查询返回的scoreModelId", required = true, paramType = "query", dataType = "Long"),
               @ApiImplicitParam(name = "initScore", value = "初始化分数", required = true, paramType = "query", dataType = "int"),
               @ApiImplicitParam(name = "startTimeStr", value = "开始日期", required = false, paramType = "query", dataType = "string")
       })
      public R saveModelSetting(Long scoreModelId,Integer initScore,String startTimeStr){
           try {
               List<ScoreModel> modelSettingList = ScoreModelService.queryByModelSettingPage(getCompanyId());
               if (!modelSettingList.isEmpty()) {
                   Iterator var5 = modelSettingList.iterator();
                   while (var5.hasNext()) {
                       ScoreModel modelSetting = (ScoreModel) var5.next();
                       if (modelSetting.getScoreModelId().equals(scoreModelId)) {
                           modelSetting.setScoreModelStatus(1);
                           modelSetting.setInitScore(initScore);
                       } else {
                           modelSetting.setScoreModelStatus(0);
                       }

                       modelSetting.setStartTime((Date) null);
                       modelSetting.setEndTime((Date) null);
                       modelSetting.setStartTimeStr(startTimeStr);
                       modelSetting.setCreatedBy(getUserId());
                       ScoreModelService.updateByModelSetting(modelSetting);
                   }
               }
               return R.ok("添加成功");
           }catch (Exception e){
               return R.error("添加失败");
           }

      }
    /**
     * 信息
     */
    @RequestMapping(value="/info/{scoreModelId}",method = RequestMethod.GET)
    @RequiresPermissions("scyf:scoremodel:info")
    public R info(@PathVariable("scoreModelId") Long scoreModelId){
        ScoreModel ScoreModel = ScoreModelService.getById(scoreModelId);

        return R.ok().put("ScoreModel", ScoreModel);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("scyf:scoremodel:save")
    public R save(@RequestBody ScoreModel ScoreModel){

        ValidatorUtils.validateEntity(ScoreModel);
        ScoreModel.setDelFlag(0);
        ScoreModel.setCreatedTime(new Date());
        ScoreModel.setCreatedBy(getUserId());
        ScoreModelService.save(ScoreModel);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("scyf:scoremodel:update")
    public R update(@RequestBody ScoreModel ScoreModel){
        ValidatorUtils.validateEntity(ScoreModel);
        ScoreModel.setUpdatedTime(new Date());
        ScoreModel.setUpdatedBy(getUserId());
        ScoreModelService.update(ScoreModel);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method = RequestMethod.DELETE)
    @RequiresPermissions("scyf:scoremodel:delete")
    public R delete(@RequestBody Long[] scoreModelIds){
        ScoreModelService.removeByIds(Arrays.asList(scoreModelIds));

        return R.ok();
    }

}
