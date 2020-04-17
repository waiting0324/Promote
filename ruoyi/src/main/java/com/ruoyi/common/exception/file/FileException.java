package com.ruoyi.common.exception.file;

import com.ruoyi.common.exception.BaseException;

/**
 * 檔案資訊異常類
 * 
 * @author ruoyi
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
