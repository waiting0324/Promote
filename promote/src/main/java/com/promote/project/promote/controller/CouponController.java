package com.promote.project.promote.controller;

import com.promote.common.utils.poi.ExcelUtil;
import com.promote.framework.aspectj.lang.annotation.Log;
import com.promote.framework.aspectj.lang.enums.BusinessType;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.framework.web.page.TableDataInfo;
import com.promote.project.promote.domain.Coupon;
import com.promote.project.promote.service.ICouponService;
import com.promote.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 抵用券Controller
 * 
 * @author 6550 劉威廷
 * @date 2020-04-22
 */
@RestController
@RequestMapping("/coupon")
public class CouponController extends BaseController
{
    @Autowired
    private ICouponService couponService;

    /**
     * 查詢抵用券列表
     */
    @PreAuthorize("@ss.hasPermi('system:coupon:list')")
    @GetMapping("/list")
    public TableDataInfo list(Coupon coupon)
    {
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
    public AjaxResult export(Coupon coupon)
    {
        List<Coupon> list = couponService.selectCouponList(coupon);
        ExcelUtil<Coupon> util = new ExcelUtil<Coupon>(Coupon.class);
        return util.exportExcel(list, "coupon");
    }

    /**
     * 獲取抵用券詳細資訊
     */
    @PreAuthorize("@ss.hasPermi('system:coupon:query')")
    @GetMapping(value = "/{sn}")
    public AjaxResult getInfo(@PathVariable("sn") String sn)
    {
        return AjaxResult.success(couponService.selectCouponById(sn));
    }

    /**
     * 新增抵用券
     */
    @PreAuthorize("@ss.hasPermi('system:coupon:add')")
    @Log(title = "抵用券", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Coupon coupon)
    {
        return toAjax(couponService.insertCoupon(coupon));
    }

    /**
     * 修改抵用券
     */
    @PreAuthorize("@ss.hasPermi('system:coupon:edit')")
    @Log(title = "抵用券", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Coupon coupon)
    {
        return toAjax(couponService.updateCoupon(coupon));
    }

    /**
     * 刪除抵用券
     */
    @PreAuthorize("@ss.hasPermi('system:coupon:remove')")
    @Log(title = "抵用券", businessType = BusinessType.DELETE)
	@DeleteMapping("/{sns}")
    public AjaxResult remove(@PathVariable String[] sns)
    {
        return toAjax(couponService.deleteCouponByIds(sns));
    }

    /**
     * 旅宿業者發送抵用券給消費者
     */
    @PostMapping("/send")
    public AjaxResult sendCoupon(SysUser user, String code) {
        AjaxResult ajax = AjaxResult.success();
        couponService.sendCoupon(user, code);

        return ajax;
    }
}
