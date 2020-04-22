package com.promote.project.promote.service.impl;

import com.promote.common.exception.CustomException;
import com.promote.common.utils.StringUtils;
import com.promote.project.promote.service.IConsumerService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 6550 劉威廷
 * @date 2020/4/22 下午 02:39
 * @description 消費者 服務層
 */
@Service
public class ConsumerServiceImpl implements IConsumerService {


    @Autowired
    SysUserMapper userMapper;

    @Override
    public SysUser selectByIdentity(String identity) {

        // 根據身分證查詢消費者
        SysUser sysUser = userMapper.selectConsumerByIdentity(identity);

        if (StringUtils.isNull(sysUser)) {
            throw new CustomException("該消費者尚未進行註冊");
        }

        return sysUser;
    }
}
