package com.ruoyi.common.exception.user;

import com.ruoyi.common.exception.BaseException;

/**
 * 使用者資訊異常類
 * 
 * @author ruoyi
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
