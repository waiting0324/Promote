package com.promote.project.promote.controller;

import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.common.utils.poi.ExcelUtil;
import com.promote.framework.aspectj.lang.annotation.Log;
import com.promote.framework.aspectj.lang.enums.BusinessType;
import com.promote.framework.security.service.TokenService;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.framework.web.page.TableDataInfo;
import com.promote.project.promote.domain.ConsumerInfo;
import com.promote.project.promote.domain.Coupon;
import com.promote.project.promote.domain.CouponConsume;
import com.promote.project.promote.service.IConsumerService;
import com.promote.project.promote.service.ICouponService;
import com.promote.project.promote.service.IFilesService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.service.ISysConfigService;
import com.promote.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 檔案Controller
 *
 * @author 6592 曾培晃
 * @date 2020-04-22
 */
@RestController
@RequestMapping("/file")
public class FilesController extends BaseController {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IFilesService filesService;

    /**
     *檔案查詢(實名認證、付款檔、白名單差異檔)
     *
     * @param request
     * @return
     */
    @PostMapping("/getFileList")
    public AjaxResult getFileList(@RequestBody Map<String, Object> request) {
        //檔案日期
        String fileDate = (String)request.get("fileDate");
        //檔案類型
        String fileType = (String)request.get("fileType");
        if(StringUtils.isEmpty(fileType)){
            return AjaxResult.error("未輸入檔案類型");
        }
        String configKey = null;
        switch (fileType){
            case "0":
                configKey = "file.path.zero";
                break;
            case "1":
                configKey = "file.path.one";
                break;
            case "2":
                configKey = "file.path.two";
                break;
            case "3":
                configKey = "file.path.three";
                break;
            case "4":
                configKey = "file.path.four";
                break;
            case "5":
                configKey = "file.path.five";
                break;
        }
        String path = configService.selectConfigByKey(configKey);
        if(StringUtils.isEmpty(path)){
            return AjaxResult.error("未輸入檔案類型");
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> fileList = filesService.getFileList(fileDate, path);
        if(StringUtils.isEmpty(fileList)){
            return AjaxResult.error("系統異常,請稍侯再試");
        }
        resultMap.put("fileInfo", fileList);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("result", resultMap);
        return ajax;
    }

}