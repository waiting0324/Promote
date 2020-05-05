package com.promote.project.promote.controller;

import com.promote.common.utils.MessageUtils;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.common.utils.poi.ExcelUtil;
import com.promote.framework.aspectj.lang.annotation.Log;
import com.promote.framework.aspectj.lang.enums.BusinessType;
import com.promote.framework.security.service.TokenService;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.framework.web.page.TableDataInfo;
import com.promote.project.promote.domain.Coupon;
import com.promote.project.promote.domain.CouponConsume;
import com.promote.project.promote.service.ICouponService;
import com.promote.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/send")
    public AjaxResult sendCoupon(@RequestBody SysUser user) {
        Map<String, Object> params = user.getParams();
        String code = (String) params.get("code");
        if (StringUtils.isNotEmpty(code)) {
            couponService.sendCoupon(user, code);
            return AjaxResult.success();
        }
        return AjaxResult.error(MessageUtils.message("user.jcaptcha.not.exist"));
    }

    /**
     * 消費者取得可使用的抵用券
     *
     * @param storeId 商家的user_id
     * @return 結果
     */
    @GetMapping("/consumer/{storeId}")
    public AjaxResult getConsumerCoupon(@PathVariable("storeId") Long storeId) {
        return AjaxResult.success(couponService.getConsumerCoupon(storeId, SecurityUtils.getLoginUser().getUser()));
    }

    /**
     * 正掃(消費者掃商家)
     *
     * @return 結果
     */
//    @PreAuthorize("@ss.hasRole('consumer')")
    @PostMapping("/postiveScan")
    public AjaxResult postiveScan(@RequestBody Map<String, Object> request) {

        //抵用券序號
        List<String> couponIds = (List<String>) request.get("couponIds");

        //商家的user_id
        Long storeId = Long.parseLong((String)request.get("storeId"));
        if (StringUtils.isNull(couponIds) || couponIds.size() == 0) {
            return AjaxResult.error("未輸入抵用券");
        }
        if (StringUtils.isNull(storeId)) {
            return AjaxResult.error("未掃描商家");
        }
        couponService.postiveScan(couponIds, storeId);
        return AjaxResult.success();
    }


    /**
     * 反掃(商家掃消費者)
     *
     * @param coupon 抵用券發放記錄檔物件
     * @return 結果
     */
    @PostMapping("/reverseScan")
    public AjaxResult reverseScan(@RequestBody Coupon coupon) {
        String id = coupon.getId();
        couponService.reverseScan(id, SecurityUtils.getLoginUser().getUser());
        return AjaxResult.success();
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
     * 抵用券總覽
     *
     * @return 結果
     */
    @GetMapping("/overview")
    public AjaxResult overview() {
        AjaxResult ajax = couponService.overviewCoupons();
        return ajax;
    }
}
