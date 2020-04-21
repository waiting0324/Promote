package com.promote.project.promote.task;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.StringUtils;
import com.promote.common.utils.spring.SpringUtils;
import com.promote.project.monitor.domain.SysOperLog;
import com.promote.project.monitor.service.ISysOperLogService;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.mapper.ProWhitelistMapper;
import com.promote.project.promote.service.IProWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 排程任務
 *
 * @author 6592 曾培晃
 * @date 2020-04-21
 */
@Component("promoteTask")
public class PromoteTask {

    @Autowired
    private ProWhitelistMapper proWhitelistMapper;

    @Autowired
    private IProWhitelistService proWhitelistService;

    @Autowired
    ISysOperLogService operLogServic;

    private static String host;

    private static String port;

    private static String ftpUser;

    private static String ftpPwd;

    private static String directory;

    private static String ftpPath;

    private static String fileName;

    private static String outputPath;

    private int totalSuccess;

    private int totalFail;


    /**
     * 從FTP上下載差異檔
     *
     * @throws Exception
     */
    public void downloadDiffData() throws Exception {
        Ftp ftp = new Ftp(host);
        ftp.cd(directory);
        ftp.download(ftpPath, fileName, FileUtil.file("d:/test2.jpg"));
        dealDiffData("TODO");
        ftp.delFile("TODO");
        ftp.close();
    }


    /**
     * 處理差異檔
     *
     * @param path 差異檔路徑
     */
    private void dealDiffData(String path) {
        if (StringUtils.isNotEmpty(path)) {
            //        Map<String, Integer> pair = new ConcurrentHashMap<String, Integer>();
            Map<String, Integer> pair = new HashMap<String, Integer>();
            //白名單Field與白名單Excel映射關係
            if (true) {
                //旅宿
                pair.put("id", 0);
                pair.put("name", 2);
                pair.put("taxNo", 22);
//            pair.put("username", 26); TODO待確認
//            pair.put("password", ); TODO待確認
                pair.put("address", 4);
                pair.put("phonenumber", 6);
                pair.put("email", 27);
                pair.put("type", 1);
            } else {
                pair.put("id", 0);
                pair.put("owner", 5);
                pair.put("name", 1);
                pair.put("taxNo", 2);
//            pair.put("username", 26); TODO待確認
//            pair.put("password", ); TODO待確認
                pair.put("address", 3);
                pair.put("phonenumber", 6);
                pair.put("type", 2);
                pair.put("isNMarket", 7);
                pair.put("isTMarket", 8);
                pair.put("isFoodbeverage", 10);
                pair.put("isCulture", 11);
                pair.put("isSightseeing", 12);
            }
            Excel07SaxReader reader = new Excel07SaxReader(createRowHandler(pair));
            reader.read(path, 1);
            String mathodName = PromoteTask.class.getName() + ".dealDiffData(String path)";
            Date now = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime());
            if (totalSuccess > 0) {
                SysOperLog successLog = new SysOperLog();
                successLog.setMethod(mathodName);
                successLog.setOperatorType(2);
                successLog.setOperName("SYSTEM");
                successLog.setJsonResult("執行白名單匯入- 共成功: " + totalSuccess + "筆");
                successLog.setOperTime(now);
                operLogServic.insertOperlog(successLog);
            }
            if (totalFail > 0) {
                SysOperLog failLog = new SysOperLog();
                failLog.setMethod(mathodName);
                failLog.setOperatorType(2);
                failLog.setOperName("SYSTEM");
                failLog.setErrorMsg("執行白名單匯入-共失敗: " + totalFail + "筆");
                failLog.setOperTime(now);
                operLogServic.insertOperlog(failLog);
            }
        }
    }


    private RowHandler createRowHandler(Map<String, Integer> pair) {
        return new RowHandler() {
            @Override
            public void handle(int sheetIndex, int rowIndex, List<Object> rowlist) {
                if (rowIndex > 0 && rowlist != null) {
                    boolean needInsert = false;
                    Class c = ProWhitelist.class;
                    Field[] fields = c.getDeclaredFields();
                    List<String> columnNameList = new ArrayList<String>();
                    List<Class> columnTypeList = new ArrayList<Class>();
                    for (Field field : fields) {
                        String fieldName = field.getName();
                        if ("serialVersionUID".equals(fieldName) || "type".equals(fieldName)) {
                            continue;
                        }
                        columnNameList.add(fieldName);
                        columnTypeList.add(field.getType());
                    }
                    ProWhitelist proWhitelist = null;
                    Integer type = pair.get("type");
                    String code = (String) rowlist.get(pair.get("id"));

                    if (type != null) {
                        if (type == 1) {
                            code = (String) rowlist.get(pair.get("旅宿業者代號"));
                            proWhitelist = proWhitelistMapper.selectProWhitelistByCode(code);
                        } else {
                            code = (String) rowlist.get(pair.get("店家代碼"));
                        }
                    }
                    //                    String code = (String) rowlist.get(dataMap.get("旅宿業者代號"));
//                    ProWhitelist proWhitelist = proWhitelistMapper.selectProWhitelistByCode(code);
                    if (proWhitelist == null) {
                        needInsert = true;
                        proWhitelist = new ProWhitelist();
//                        proWhitelist.setCreateTime(now);
                    }
                    for (int i = 0; i < columnNameList.size(); i++) {
                        try {
                            String columnName = columnNameList.get(i);
                            String methodName = new StringBuilder("set").append(columnName.substring(0, 1).toUpperCase()).append(columnName.substring(1)).toString();
                            Method method = c.getMethod(methodName, columnTypeList.get(i));
                            if (method != null) {
                                Integer index = pair.get(columnName);
                                if (index == null) {
                                    continue;
                                }
                                Object value = rowlist.get(index);
                                method.invoke(proWhitelist, StringUtils.isNotNull(value) ? value.toString() : null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//                    proWhitelist.setUpdateTime(now);
                    proWhitelist.setType(type != null ? type.toString() : null);
                    try {
                        if (needInsert) {
                            proWhitelistService.insertProWhitelist(proWhitelist);
                        } else {
                            proWhitelistService.updateProWhitelist(proWhitelist);
                        }
                        totalSuccess++;
                    } catch (Exception e) {
                        totalFail++;
                        SysOperLog failLog = new SysOperLog();
                        failLog.setMethod(PromoteTask.class.getName() + ".dealDiffData(String path)");
                        failLog.setOperatorType(2);
                        failLog.setOperName("SYSTEM");
                        String errMsg = "";
                        if (type == 1) {
                            errMsg = "執行旅宿業者白名單" + (needInsert ? "新增" : "更新") + "失敗:";
                        } else if (type == 2) {
                            errMsg = "執行店家白名單" + (needInsert ? "新增" : "更新") + "失敗:";
                        }
                        failLog.setErrorMsg(errMsg);
                        failLog.setOperTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
                        operLogServic.insertOperlog(failLog);
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
