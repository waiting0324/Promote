package com.promote.project.promote.controller;

import com.promote.common.constant.RoleConstants;
import com.promote.common.utils.SecurityUtils;
import com.promote.framework.security.service.TokenService;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.domain.WeeklySettlementDetail;
import com.promote.project.promote.service.IDailyConsumeService;
import com.promote.project.promote.service.IWeeklySettlementService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.promote.domain.WeeklySettlement;
import com.promote.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.text.*;

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

    @Autowired
    private IDailyConsumeService dailyConsumeService;

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
            weeklySettlement = new WeeklySettlement();
            weeklySettlement.setStoreId(userId);
            weeklySettlement.setId(Long.valueOf(weeklyTxId.get(i).toString()));
            weeklySettlement.setIsConfirm("1");
            weeklySettlementList.add(weeklySettlement);
        }
        int sum = weeklySettlementService.updateWeeklySettlement(weeklySettlementList);
        int code = 0;
        String msg = "";
        AjaxResult ajax = new AjaxResult();
        if(sum < 0){
            return AjaxResult.error("週結交易確認失敗");
        }else{
            return AjaxResult.success("週結交易確認成功");
        }
    }

    /**
     * 週結交易查詢
     *
     * @return 結果
     */
    @PostMapping("/weeklyTx")
    public AjaxResult weeklyTx(@RequestBody Map<String, Object> request) throws Exception{

        SysUser user = SecurityUtils.getLoginUser().getUser();
        Long userId = 0l;
        //判斷使用者是客服還是店家
        if(user.getRoles().get(0).getRoleId().equals(RoleConstants.SERVICE_ROLE_ID)){
            //使用者是客服
            SysUser queryUser = sysUserService.selectUserByUserName(user.getUsername());
            userId = queryUser.getUserId();
        }else{
            //使用者是店家
            userId = user.getUserId();
        }

        Date date = new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("E");
        String week = dateFm.format(date);
        String beginDay = "";
        String endDay = "";
        if("星期一".equals(week)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            //取7天前
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE,-7);
            Date firstDate = cal.getTime();
            beginDay = sdf.format(firstDate);
            //取1天前
            cal = Calendar.getInstance();
            cal.add(Calendar.DATE,-1);
            Date lastDate = cal.getTime();
            endDay = sdf.format(lastDate);
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            //取當週星期一
            System.out.println(getThisWeekMonday(date));
            Date monday = getThisWeekMonday(date);
            beginDay = sdf.format(monday);
            //取當前日期前一天
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE,-1);
            Date lastDate = cal.getTime();
            endDay = sdf.format(lastDate);
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("storeId", userId);
        params.put("beginDay", beginDay);
        beginDay = beginDay.replaceAll("/","-");
        params.put("endDay", endDay);
        endDay = endDay.replaceAll("/","-");

        List<Map<String, Object>> queryDailyConsumeForDayList =
                dailyConsumeService.queryDailyConsumeForDay(userId, beginDay, endDay);
        List<Map<String, Object>> weeklySettlementList =
                weeklySettlementService.queryWeeklySettlementList(userId);
        //計算統計金額參數
        int incomeTotal = 0;
        //每日消費統計
        for(int i =0; i < queryDailyConsumeForDayList.size(); i++){
            //計算統計金額參數
            String showAmount = queryDailyConsumeForDayList.get(i).get("showAmount").toString();
            incomeTotal += Integer.valueOf(showAmount);

            //取日期
            String showDate = queryDailyConsumeForDayList.get(i).get("showDate").toString();
            //日期換算成星期幾
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = sdf.parse(showDate);
            SimpleDateFormat dateToWeek = new SimpleDateFormat("E");
            String nowWeek = dateToWeek.format(newDate);
            //日期加上星期幾在放回去
            showDate += " ("+nowWeek.substring(2)+")";
            queryDailyConsumeForDayList.get(i).put("showDate", showDate);
        }
        //週結檔
        for(int i =0; i < weeklySettlementList.size(); i++){
            //statusDesc，前端顯示狀態，若isConfirm = 0，填入待確認。
            //statusDesc，前端顯示狀態，若isConfirm = 1，則判斷payment_status=0，銷帳處理中，payment_status=1，已入帳。
            //isConfirm 判斷Y/N
            System.out.println(weeklySettlementList.get(i));
            String isConfirm = weeklySettlementList.get(i).get("isConfirm").toString();
            String newIsConfirm = "";
            String statusDesc = "";
            if("1".equals(isConfirm)){
                newIsConfirm = "Y";
                String paymentStatus = weeklySettlementList.get(i).get("payment_status").toString();
                if("0".equals(paymentStatus)){
                    statusDesc = "銷帳處理中";
                }else if("1".equals(paymentStatus)){
                    statusDesc = "已入帳";
                }
            }else{
                newIsConfirm = "N";
                statusDesc = "待確認";
            }
            weeklySettlementList.get(i).put("isConfirm", newIsConfirm);
            weeklySettlementList.get(i).put("statusDesc", statusDesc);
            //計算統計金額參數
            String totalAmount = weeklySettlementList.get(i).get("totalAmount").toString();
            incomeTotal += Integer.valueOf(totalAmount);
        }

        AjaxResult ajax = AjaxResult.success();
        Map<String, Object> resultsMap = new HashMap<String, Object>();
        resultsMap.put("incomeTotal", incomeTotal);
        resultsMap.put("DailyTransaction", queryDailyConsumeForDayList);
        resultsMap.put("WeeklyTransaction", weeklySettlementList);
        ajax.put("results",resultsMap);
        //ajax.put("incomeTotal", incomeTotal);
        //ajax.put("DailyTransaction", queryDailyConsumeForDayList);
        //ajax.put("WeeklyTransaction", weeklySettlementList);

        return ajax;
    }

    /**
     * 週結明細查詢
     *
     * @return 結果
     */
    @PostMapping("/weeklyTxDtl")
    public AjaxResult weeklyTxDtl(@RequestBody Map<String, Object> request) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        Long userId = 0l;
        //判斷使用者是客服還是店家
        if(user.getRoles().get(0).getRoleId().equals(RoleConstants.SERVICE_ROLE_ID)){
            //使用者是客服
            SysUser queryUser = sysUserService.selectUserByUserName(user.getUsername());
            userId = queryUser.getUserId();
        }else{
            //使用者是店家
            userId = user.getUserId();
        }

        WeeklySettlementDetail weeklySettlementDteail = new WeeklySettlementDetail();
        weeklySettlementDteail.setStoreId(userId);

        String weeklyTxId = request.get("weeklyTxId").toString();
        weeklySettlementDteail.setMainId(Long.parseLong(weeklyTxId));

        List<Map<String, Object>> resultList = weeklySettlementService.queryWeeklySettlementDetailList(weeklySettlementDteail);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("resultList", resultList);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("result", resultMap);
        return ajax;
    }
    //取當週星期一
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

}
