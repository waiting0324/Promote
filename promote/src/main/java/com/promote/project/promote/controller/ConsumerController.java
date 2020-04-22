package com.promote.project.promote.controller;

import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.service.IConsumerService;
import com.promote.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 6550 劉威廷
 * @date 2020/4/22 下午 02:19
 * @description 消費者 Controller
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController extends BaseController {

    @Autowired
    IConsumerService consumerService;

    @PreAuthorize("@ss.hasRole('hostel')")
    @GetMapping("/identity/{identity}")
    public AjaxResult selectByIdentity(@PathVariable("identity") String identity) {

        AjaxResult ajax = AjaxResult.success();
        SysUser user = consumerService.selectByIdentity(identity);
        ajax.put("user", user);

        return ajax;
    }



}
