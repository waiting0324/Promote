package com.promote.project.system.controller;

import com.promote.common.constant.UserConstants;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.common.utils.poi.ExcelUtil;
import com.promote.framework.aspectj.lang.annotation.Log;
import com.promote.framework.aspectj.lang.enums.BusinessType;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.framework.web.page.TableDataInfo;
import com.promote.project.system.domain.SysConfig;
import com.promote.project.system.service.ISysConfigService;
import com.promote.project.system.service.ISysHostelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 旅宿業者 控制層
 *
 * @author 曾培晃
 */
@RestController
@RequestMapping("/hostel")
public class SysHostelController extends BaseController {
    @Autowired
    private ISysHostelService hostelService;

    /**
     * 旅宿業者註冊
     */
    @PostMapping("/regist")
    public AjaxResult regist(String acct, String pwd) {
        if (StringUtils.isNotEmpty(acct) && StringUtils.isNotEmpty(pwd) ){
            return toAjax(hostelService.regist(acct,pwd));
        }
        return AjaxResult.error("帳號or密碼未輸入值");
    }
}
