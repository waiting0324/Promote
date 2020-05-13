package com.promote.project.promote.controller;

import com.promote.common.utils.SecurityUtils;
import com.promote.framework.security.service.TokenService;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.service.IWeeklySettlementService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.promote.domain.WeeklySettlement;
import com.promote.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 抵用券Controller
 *
 * @author 6550 劉威廷
 * @date 2020-04-22
 */
@RestController
@RequestMapping("/payment")
public class paymentController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IWeeklySettlementService weeklySettlementService;

    /**
     * 重置列印狀態
     *
     * @return 結果
     */
    @PostMapping("/weeklyTxConfirm")
    public AjaxResult weeklyTxConfirm(@RequestBody Map<String, Object> request) {

        List<WeeklySettlement> weeklySettlementList = new ArrayList<WeeklySettlement>();
        WeeklySettlement weeklySettlement = new WeeklySettlement();
        SysUser user = SecurityUtils.getLoginUser().getUser();
        Long userId = user.getUserId();
        List<Object> weeklyTxId = (List<Object>)request.get("weeklyTxId");
        for(int i = 0; i < weeklyTxId.size(); i++){
            weeklySettlement.setStoreId(userId);
            weeklySettlement.setId(Long.valueOf(weeklyTxId.get(i).toString()));
            weeklySettlementList.add(weeklySettlement);
            weeklySettlementService.updateWeeklySettlement(weeklySettlementList);
        }

        return AjaxResult.success();
    }

}
