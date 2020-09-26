package com.hngf.service.device.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.hngf.common.utils.Constant;
import com.hngf.entity.risk.Risk;
import com.hngf.entity.risk.RiskCtrl;
import com.hngf.entity.risk.RiskMeasure;
import com.hngf.entity.risk.RiskSource;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hngf.common.utils.PageUtils;

import com.hngf.mapper.device.DeviceInfoMapper;
import com.hngf.entity.device.DeviceInfo;
import com.hngf.service.device.DeviceInfoService;


@Service("DeviceInfoService")
public class DeviceInfoServiceImpl implements DeviceInfoService {

    @Autowired
    private DeviceInfoMapper DeviceInfoMapper;
    /**
     * @Author: zyj
     * @Description:查看设备管理列表
     * @Param  companyId 企业id  groupId 群组id  deviceTypeId 设备类型id  deviceStatus 设备状态  keyword 关键词
     * @Date 15:51 2020/6/16
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params,Integer pageNum,Integer pageSize,String order) {
        //利用PageHelper分页查询 注意：这个一定要放查询语句的前一行,否则无法进行分页,因为它对紧随其后第一个sql语句有效
        if(StringUtils.isNotBlank(order)){
            PageHelper.startPage(pageNum, pageSize,order);
        }else{
            PageHelper.startPage(pageNum, pageSize);
        }
        List<DeviceInfo> list = DeviceInfoMapper.findList(params);
        PageInfo<DeviceInfo> pageInfo = new PageInfo<DeviceInfo>(list);
        return new PageUtils(list,(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getPageSize(),pageInfo.getPrePage() + 1);
    }

    @Override
    public DeviceInfo getById(Long id){
        return DeviceInfoMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DeviceInfo DeviceInfo) {
        DeviceInfoMapper.add(DeviceInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DeviceInfo DeviceInfo) {
        DeviceInfoMapper.update(DeviceInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByIds(List ids) {
        DeviceInfoMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(Long id) {
        DeviceInfoMapper.deleteById(id);
    }

    @Override
    public void importExcel(String path,Long companyId,Long groupId,Long userId) throws Exception {
        //读取excel文件
        ExcelReader reader = ExcelUtil.getReader(path);
        //获取excel表格内容
        List<List<Object>> readAll = reader.read();
        for(int i = 1; i < readAll.size(); ++i) {
                List<Object> one = (List)readAll.get(i);
                JSONArray data = new JSONArray(one);
                String riskCode = data.getString(0);
                String riskCode1 = one.size()>1 ?data.getString(1):"";
                String riskCode2 = one.size()>2 ?data.getString(2):"";
                String riskCode3 = one.size()>3 ?data.getString(3):"";
                String riskCode4 = one.size()>4 ? data.getString(4):"";
                String riskCode5 = one.size()>5 ?data.getString(5):"";
                DeviceInfo deviceInfo=new DeviceInfo();
                deviceInfo.setDeviceName(riskCode);
                deviceInfo.setDeviceNumber(riskCode1);
                deviceInfo.setDeviceBrand(riskCode2);
                deviceInfo.setDeviceModel(riskCode3);
                deviceInfo.setDevicePosition(riskCode4);
                deviceInfo.setSecurityNumber(riskCode5);
                deviceInfo.setCompanyId(companyId);
                deviceInfo.setGroupId(groupId);
                deviceInfo.setDeviceStatus(0);
                deviceInfo.setDelFlag(0);
                deviceInfo.setCreatedBy(userId);
                deviceInfo.setCreatedTime(new Date());
              DeviceInfoMapper.add(deviceInfo);
        }
    }

    /**
     * 导出设备信息
     * @param cId
     * @return
     */
    @Override
    public XSSFWorkbook exportExcel(Long cId) {
        XSSFWorkbook wb = null;

        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = null;
            resource = resourceLoader.getResource("classpath:excel/device.xlsx");
            wb = new XSSFWorkbook(resource.getInputStream());
            XSSFSheet sheet = wb.getSheetAt(0);
            Map<String, Object> params =new HashMap<>();
            params.put("companyId",cId);
            List<DeviceInfo> allRiskList = DeviceInfoMapper.findList(params);
            for(int r = 0; r < allRiskList.size(); ++r) {

                XSSFRow row = sheet.createRow(r + 1);
                this.createCell(row, 0, allRiskList.get(r).getDeviceName());
                this.createCell(row, 1, allRiskList.get(r).getDeviceNumber());
                this.createCell(row, 2, allRiskList.get(r).getDeviceBrand());
                this.createCell(row, 3, allRiskList.get(r).getDeviceModel());
                this.createCell(row, 4, allRiskList.get(r).getDevicePosition());
                this.createCell(row, 5, allRiskList.get(r).getSecurityNumber());
            }
        } catch (Exception var34) {
            var34.printStackTrace();
        }

        return wb;
    }

    private XSSFCell createCell(XSSFRow row, int colIndex, String cellVal) {
        XSSFCell cell = row.createCell(colIndex);
        setCellFont(cell);
        if (cellVal != null) {
            cell.setCellValue(cellVal);
        }

        return cell;
    }
    public static void setCellFont(XSSFCell cell) {
        XSSFCellStyle style = cell.getCellStyle();
        style.getAlignmentEnum();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        XSSFFont font = style.getFont();
        font.setFontHeightInPoints((short)16);
        font.setFontName("仿宋_GB2312");
        style.getBorderBottomEnum();
        style.setBorderTop(BorderStyle.THIN);
        style.getBorderBottomEnum();
        style.setBorderBottom(BorderStyle.THIN);
        style.getBorderBottomEnum();
        style.setBorderLeft(BorderStyle.THIN);
        style.getBorderBottomEnum();
        style.setBorderRight(BorderStyle.THIN);
    }
}
