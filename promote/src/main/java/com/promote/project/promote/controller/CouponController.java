package com.promote.project.promote.controller;

import cn.hutool.core.util.StrUtil;
import com.promote.common.constant.RoleConstants;
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
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抵用券Controller
 *
 * @author 6550 劉威廷
 * @date 2020-04-22
 */
@RestController
@RequestMapping("/coupon")
public class CouponController extends BaseController {

    @Autowired
    private ICouponService couponService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IConsumerService consumerService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查詢抵用券列表
     */
    @PreAuthorize("@ss.hasPermi('system:coupon:list')")
    @GetMapping("/list")
    public TableDataInfo list(Coupon coupon) {
        startPage();
        List<Coupon> list = couponService.selectCouponList(coupon);
        return getDataTable(list);
    }

    /**
     * 匯出抵用券列表
     */
    @PreAuthorize("@ss.hasPermi('system:coupon:export')")
    @Log(title = "抵用券", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Coupon coupon) {
        List<Coupon> list = couponService.selectCouponList(coupon);
        ExcelUtil<Coupon> util = new ExcelUtil<Coupon>(Coupon.class);
        return util.exportExcel(list, "coupon");
    }

    /**
     * 獲取抵用券詳細資訊
     */
    @PreAuthorize("@ss.hasPermi('system:coupon:query')")
    @GetMapping(value = "/{sn}")
    public AjaxResult getInfo(@PathVariable("sn") String sn) {
        return AjaxResult.success(couponService.selectCouponById(sn));
    }

    /**
     * 新增抵用券
     */
    @PreAuthorize("@ss.hasPermi('system:coupon:add')")
    @Log(title = "抵用券", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Coupon coupon) {
        return toAjax(couponService.insertCoupon(coupon));
    }

    /**
     * 修改抵用券
     */
    @PreAuthorize("@ss.hasPermi('system:coupon:edit')")
    @Log(title = "抵用券", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Coupon coupon) {
        return toAjax(couponService.updateCoupon(coupon));
    }

    /**
     * 刪除抵用券
     */
    @PreAuthorize("@ss.hasPermi('system:coupon:remove')")
    @Log(title = "抵用券", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sns}")
    public AjaxResult remove(@PathVariable String[] sns) {
        return toAjax(couponService.deleteCouponByIds(sns));
    }

    /**
     * 旅宿業者發送抵用券給消費者
     */
    @PreAuthorize("@ss.hasRole('hostel')")
    @PostMapping("/apply")
    public AjaxResult applyCoupon(@RequestBody Map<String, String> request) {

        // 參數取得
        String username = request.get("username");
        String mobile = request.get("mobile");
        String couponType = request.get("couponType");
        String code = request.get("code");

        // 參數封裝
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setMobile(mobile);
        ConsumerInfo consumerInfo = new ConsumerInfo();
        consumerInfo.setCouponType(couponType);
        user.setConsumer(consumerInfo);


        AjaxResult ajax = AjaxResult.success("恭喜你，已完成抵用券申請，通知簡訊已由系統傳送簡訊到您的手機。");

        // 發放抵用券
        Map<String, Object> result = couponService.applyCoupon(user, code);
        ajax.put("result", result);

        return ajax;
    }


    /**
     * 消費者取得可使用的抵用券
     *
     * @param storeId 商家的user_id
     * @return 結果
     */
    @PostMapping("/preScan")
    public AjaxResult getConsumerCoupon(@RequestBody Map<String, Long> request) {
        Long storeId = request.get("userId");

        Map result = couponService.getConsumerCoupon(storeId, SecurityUtils.getLoginUser().getUser());

        AjaxResult ajax = AjaxResult.success();
        ajax.put("result", result);

        return ajax;
    }

    /**
     * 正掃(消費者掃商家)
     *
     * @return 結果
     */
    @PreAuthorize("@ss.hasRole('consumer')")
    @PostMapping("/postiveScan")
    public AjaxResult postiveScan(@RequestBody Map<String, Object> request) {

        //抵用券序號
        List<String> couponIds = (List<String>) request.get("couponIds");

        //商家的user_id
        Integer storeId = (Integer) request.get("storeId");

        if (StringUtils.isNull(couponIds) || couponIds.size() == 0) {
            return AjaxResult.error("未輸入抵用券");
        }
        if (StringUtils.isNull(storeId)) {
            return AjaxResult.error("未掃描商家");
        }

        // 使用抵用券
        Integer amount = couponService.postiveScan(couponIds, storeId.longValue());

        return new AjaxResult(1000, StrUtil.format("{} {}元 抵用成功", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss")), amount));
    }


    /**
     * 消費紀錄列表
     *
     * @return 結果
     */
    @GetMapping("/consumption")
    public AjaxResult consumption() {
        List<CouponConsume> list = couponService.consumption();
        return AjaxResult.success(list);
    }

    /**
     * 抵用券發放紀錄查詢
     *
     * @return 結果
     */
    @PostMapping("/getCouponOverview")
    public AjaxResult getCouponOverview(@RequestBody(required = false) Map<String, Object> request) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        Long roleId = user.getRoles().get(0).getRoleId();
        Long consumerId = null;
        if (RoleConstants.CONSUMER_ROLE_ID.equals(roleId)) {
            consumerId = user.getUserId();
        } else if (RoleConstants.SERVICE_ROLE_ID.equals(roleId)) {
            String indentity = (String) request.get("indentity");
            String username = (String) request.get("username");
            if (StringUtils.isEmpty(indentity) && StringUtils.isEmpty(username)) {
                AjaxResult.error("請輸入身分證號/居留證號或消費者帳號");
            }
            SysUser sysUser = sysUserService.getByUnameIndentity(username, indentity);
            consumerId = sysUser.getUserId();
        }
        Map<String, Object> couponOverview = couponService.overviewCoupons(consumerId);
        if(StringUtils.isNull(couponOverview)){
            return new AjaxResult(2200,"尚未申請抵用券");
        }
        AjaxResult ajax = AjaxResult.success();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("couponOverview", couponOverview);
        ajax.put("result", resultMap);
        return ajax;
    }

    /**
     * 重置列印狀態
     *
     * @return 結果
     */
    @PostMapping("/resetPrint")
    public AjaxResult resetPrint(@RequestBody Map<String, Object> request) {
        List<SysUser> sysYserList = sysUserService.selectConsumerByIdentity(request.get("identity").toString());

        if (sysYserList.size() > 0) {
            String userId = sysYserList.get(0).getUserId().toString();
            ConsumerInfo consumerInfo = new ConsumerInfo();
            consumerInfo.setUserId(Long.parseLong(userId));
            consumerInfo.setConsumerStat("3");
            int sum = couponService.updateConsumerStart(consumerInfo);

            if (sum < 0) {
                return AjaxResult.error("修改失敗");
            }
        } else {
            return AjaxResult.error("無此消費者");
        }

        return AjaxResult.success();
    }

    /**
     * 抵用券消費記錄查詢(WEB介面用)
     *
     * @return 結果
     */
    @PostMapping("/transactionHistory")
    public AjaxResult transactionHistory(@RequestBody Map<String, Object> request) {
        //紙本或電子
        String couponType = (String) request.get("couponType");
        //抵用券類型
        String storeType = (String) request.get("storeType");
        //查詢起日
        String startDate = (String) request.get("startDate");
        //查詢迄日
        String endDate = (String) request.get("endDate");
        if (StringUtils.isEmpty(couponType) || StringUtils.isEmpty(storeType) || StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return AjaxResult.error("需輸入紙本/電子,抵用券類型,查詢起日,查詢迄日");
        }
        String rows = (String) request.get("rows");
        rows = StringUtils.isEmpty(rows) ? "10" : rows;
        String page = (String) request.get("page");
        page = StringUtils.isEmpty(page) ? "1" : page;
        SysUser user = SecurityUtils.getLoginUser().getUser();
        Long userId = user.getUserId();
        AjaxResult ajax = AjaxResult.success();
        //判斷角色
        Long roleId = user.getRoles().get(0).getRoleId();
        Map<String, Object> map = null;
        if (RoleConstants.STORE_ROLE_ID.equals(roleId)) {
            //店家
            map = couponService.transactionHistory(userId, "S", storeType, startDate, endDate, rows, page, couponType);
        } else if (RoleConstants.CONSUMER_ROLE_ID.equals(roleId)) {
            //消費者
            map = couponService.transactionHistory(userId, "C", storeType, startDate, endDate, rows, page, couponType);
        } else if (RoleConstants.SERVICE_ROLE_ID.equals(roleId)) {
            //客服
            String username = (String) request.get("username");
            String indentity = (String) request.get("indentity");
            //查店家或查消費者
            String target = (String) request.get("role");
            if (StringUtils.isEmpty(target)) {
                return AjaxResult.error("需輸入要查詢店家或消費者");
            }
            if (StringUtils.isEmpty(username) && StringUtils.isEmpty(indentity)) {
                return AjaxResult.error("需輸入身分證號/居留證號或帳號");
            }
            SysUser sysUser = sysUserService.getByUnameIndentity(username, indentity);
            roleId = sysUser.getRoles().get(0).getRoleId();
            if (RoleConstants.STORE_ROLE_ID.equals(roleId)) {
                map = couponService.transactionHistory(sysUser.getUserId(), "S", storeType, startDate, endDate, rows, page, couponType);
            } else if (RoleConstants.CONSUMER_ROLE_ID.equals(roleId)) {
                map = couponService.transactionHistory(sysUser.getUserId(), "C", storeType, startDate, endDate, rows, page, couponType);
            }
        } else {
            return AjaxResult.error("目前登入者無權進行抵用券消費記錄查詢");
        }
        if (StringUtils.isNotEmpty(map)) {
            ajax.put("result", map);
            return ajax;
        }
        return AjaxResult.error("查無資料");
    }

    /**
     * 以證號末四碼及兌換碼查詢抵用券
     *
     * @return 結果
     */
    @PostMapping("/getPrintCoupon")
    public AjaxResult getPrintCoupon(@RequestBody Map<String, Object> request) {
        String indentity = "%" + request.get("indentity").toString();
        String printCode = request.get("printCode").toString();

        List<Map<String, Object>> getPrintCouponList = couponService.getPrintCoupon(indentity, printCode);
        System.out.println(getPrintCouponList);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("couponInfo", getPrintCouponList);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("result", resultMap);
        return ajax;
    }

    /**
     * 以證號末四碼及兌換碼查詢抵用券
     *
     * @return 結果
     */
    @PostMapping("/updatePrintCoupon")
    public AjaxResult updatePrintCoupon(@RequestBody Map<String, Object> request) {

        String printCode = request.get("printCode").toString();
        int sum = couponService.updatePrintCoupon(printCode);
        if (sum < 0) {
            return AjaxResult.error();
        }
        //int sum = 0;
        return AjaxResult.success();
    }
}
