package com.promote.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import com.promote.common.utils.spring.SpringUtils;

/**
 * 獲取i18n資原始檔
 * 
 * @author ruoyi
 */
public class MessageUtils
{
    /**
     * 根據訊息鍵和引數 獲取訊息 委託給spring messageSource
     *
     * @param code 訊息鍵
     * @param args 引數
     * @return 獲取國際化翻譯值
     */
    public static String message(String code, Object... args)
    {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
