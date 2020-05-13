package com.promote.framework.manager.factory;

import com.promote.common.constant.Constants;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.EmailUtils;
import com.promote.common.utils.LogUtils;
import com.promote.common.utils.ServletUtils;
import com.promote.common.utils.SmsUtil;
import com.promote.common.utils.ip.AddressUtils;
import com.promote.common.utils.ip.IpUtils;
import com.promote.common.utils.spring.SpringUtils;
import com.promote.project.monitor.domain.SysLogininfor;
import com.promote.project.monitor.domain.SysOperLog;
import com.promote.project.monitor.service.ISysLogininforService;
import com.promote.project.monitor.service.ISysOperLogService;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;
import java.util.TimerTask;

/**
 * 非同步工廠（產生任務用）
 *
 * @author ruoyi
 */
public class AsyncFactory {
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 記錄登陸資訊
     *
     * @param username 使用者名稱
     * @param status   狀態
     * @param message  訊息
     * @param args     列表
     * @return 任務task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
                                             final Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 列印資訊到日誌
                sys_user_logger.info(s.toString(), args);
                // 獲取客戶端作業系統
                String os = userAgent.getOperatingSystem().getName();
                // 獲取客戶端瀏覽器
                String browser = userAgent.getBrowser().getName();
                // 封裝物件
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                // 日誌狀態
                if (Constants.LOGIN_SUCCESS.equals(status) || Constants.LOGOUT.equals(status)) {
                    logininfor.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入資料
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * 操作日誌記錄
     *
     * @param operLog 操作日誌資訊
     * @return 任務task
     */
    public static TimerTask recordOper(final SysOperLog operLog) {
        return new TimerTask() {
            @Override
            public void run() {
                // 遠端查詢操作地點
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }

    /**
     * 發送電子信件
     *
     * @return 任務task
     */
    public static TimerTask sendEmail(final String toMail, final String title, final String msg) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    EmailUtils.sendEmail(toMail, title, msg);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 發送電子信件
     *
     * @return 任務task
     */
    public static TimerTask sendSms(final String mobile, final String msg) {

        if (mobile == null) {
            throw new CustomException("未輸入手機號碼");
        }

        return new TimerTask() {
            @Override
            public void run() {
                SmsUtil.sendSms(mobile, msg);
            }
        };
    }
}
