package com.promote.project.promote.task;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.poi.excel.sax.Excel03SaxReader;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.StringUtils;
import com.promote.project.monitor.domain.SysOperLog;
import com.promote.project.monitor.service.ISysOperLogService;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.service.IProWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 排程任務
 *
 * @author 6592 曾培晃
 * @date 2020-04-21
 */
@Component("promoteTask")
@ConfigurationProperties(prefix = "ftp")
public class PromoteTask {

    @Autowired
    private IProWhitelistService proWhitelistService;

    @Autowired
    ISysOperLogService operLogServic;

    // 與FTP相關配置 開始
    private String host;

    private int port;

    private String username;

    private String password;

    private String remoteDir;

    private String localTempDir;

    private String localStoreDir;
    // 與FTP相關配置 結束

    private int totalSuccess;

    private int totalFail;

//    private int count = 0; //測試用


    /**
     * 從FTP上下載差異檔
     */
    public void ftpFetchFile() throws Exception {

        // FTP 連接
        Ftp ftp = new Ftp(host, port, username, password, Charset.forName("utf8"));

        // 讀取檔案列表
        List<String> files = ftp.ls(remoteDir);

        // 循環下載所有檔案
        for (String fileName : files) {
            ftp.download(remoteDir, fileName, FileUtil.file(localTempDir + "/" +  fileName));
        }

        // 刪除遠端資料
        ftp.delDir(remoteDir);

        // 關閉FTP連接
        ftp.close();

        // 開始將Excel檔案匯入資料庫
        File[] localFiles = FileUtil.ls(localTempDir);
        for (File localFile : localFiles) {
            // TODO 需要做商家或旅宿業者判斷
            this.dealDiffData(localFile.getPath());
        }

        // 將檔案從臨時資料夾轉到儲存資料夾
        FileUtil.copyContent(FileUtil.newFile(localTempDir), FileUtil.newFile(localStoreDir), true);

        // 刪除暫存文件
        FileUtil.clean(localTempDir);
    }


    /**
     * 處理白名單匯入及差異檔
     *
     * @param path 白名單路徑
     */
    public void dealDiffData(String path) {
        if (StringUtils.isNotEmpty(path)) {
            Map<String, Integer> pair = new HashMap<String, Integer>();
            //白名單Field與白名單Excel映射關係
            boolean isHostel = false;  //TODO 待確認檔名規則
            if (isHostel) {
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
                //店家
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
            //根據Excel版本的不同選用不同的方式處理
            if(path.indexOf(".xlsx") > -1){
                //Excel 2007
                Excel07SaxReader reader = new Excel07SaxReader(createRowHandler(pair));
                reader.read(path, 0);
            }else{
                //Excel 2003
                Excel03SaxReader reader = new Excel03SaxReader(createRowHandler(pair));
                reader.read(path, 0);
            }
            String mathodName = PromoteTask.class.getName() + ".dealDiffData(String path)";
            Date now = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime());
            if (totalSuccess > 0) {
                //成功筆數寫log
                SysOperLog successLog = new SysOperLog();
                successLog.setMethod(mathodName);
                successLog.setOperatorType(2);
                successLog.setOperName("SYSTEM");
                successLog.setJsonResult("執行" + (isHostel ? "旅宿業者" : "店家") + "白名單匯入- 共成功: " + totalSuccess + "筆");
                successLog.setOperTime(now);
                operLogServic.insertOperlog(successLog);
            }
            if (totalFail > 0) {
                //失敗筆數寫log
                SysOperLog failLog = new SysOperLog();
                failLog.setMethod(mathodName);
                failLog.setOperatorType(2);
                failLog.setOperName("SYSTEM");
                failLog.setErrorMsg("執行" + (isHostel ? "旅宿業者" : "店家") + "白名單匯入-共失敗: " + totalFail + "筆");
                failLog.setOperTime(now);
                operLogServic.insertOperlog(failLog);
            }
            totalSuccess = 0;
            totalFail = 0;
        }
    }


    private RowHandler createRowHandler(Map<String, Integer> pair) {
        return new RowHandler() {
            @Override
            public void handle(int sheetIndex, int rowIndex, List<Object> rowlist) {
                //每一列都會呼叫此方法一次
                try{
                    if (rowIndex > 0 && rowlist != null) {
                        String id = rowlist.get(pair.get("id")).toString();
                        if(StringUtils.isEmpty(id)){
                            throw new Exception("ID不得為空");
                        }
                        boolean needInsert = false;
                        Class c = ProWhitelist.class;
                        Field[] fields = c.getDeclaredFields();
                        //白名單Model的屬性名
                        List<String> columnNameList = new ArrayList<String>();
                        //白名單Model的屬性類型
                        List<Class> columnTypeList = new ArrayList<Class>();
                        for (Field field : fields) {
                            String fieldName = field.getName();
                            if ("serialVersionUID".equals(fieldName) || "type".equals(fieldName)) {
                                continue;
                            }
                            columnNameList.add(fieldName);
                            columnTypeList.add(field.getType());
                        }
                        Integer type = pair.get("type");
                        //根據id及類型去查白名單table,有查到做update,沒查到做insert
                        ProWhitelist proWhitelist = proWhitelistService.selectProWhitelistByIdType(id,type.toString());
                        if (proWhitelist == null) {
                            needInsert = true;
                            proWhitelist = new ProWhitelist();
                        }
                        for (int i = 0; i < columnNameList.size(); i++) {
                            try {
                                String columnName = columnNameList.get(i);
                                String methodName = new StringBuilder("set").append(columnName.substring(0, 1).toUpperCase()).append(columnName.substring(1)).toString();
                                //取得白名單Model的setter方法
                                Method method = c.getMethod(methodName, columnTypeList.get(i));
                                if (method != null) {
                                    Integer index = pair.get(columnName);
                                    if (index == null) {
                                        continue;
                                    }
                                    Object value = rowlist.get(index);
                                    //設定Excel的值到白名單Model
                                    method.invoke(proWhitelist, StringUtils.isNotNull(value) ? value.toString().trim().replaceAll("\\u00A0+", "") : null);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        proWhitelist.setType(type != null ? type.toString() : null);
                        try {
                            if (needInsert) {
                                //新增
//                                if(count == 3){
//                                    //測試用
//                                    count++;
//                                    throw new Exception();
//                                }
                                proWhitelistService.insertProWhitelist(proWhitelist);

                            } else {
                                //更新
//                                if(count == 3){
//                                    //測試用
//                                    count++;
//                                    throw new Exception();
//                                }
                                proWhitelistService.updateProWhitelist(proWhitelist);
                            }
                            totalSuccess++;
//                            count++; //測試用
                        } catch (Exception e) {
                            //新增或更新失敗時寫log
                            totalFail++;
                            SysOperLog failLog = new SysOperLog();
                            failLog.setMethod(PromoteTask.class.getName() + ".dealDiffData(String path)");
                            failLog.setOperatorType(2);
                            failLog.setOperName("SYSTEM");
                            String errMsg = "";
                            if (type == 1) {
                                errMsg = "執行旅宿業者白名單" + (needInsert ? "新增" : "更新") + "失敗: ID = " + id;
                            } else if (type == 2) {
                                errMsg = "執行店家白名單" + (needInsert ? "新增" : "更新") + "失敗: ID = " + id;
                            }
                            failLog.setErrorMsg(errMsg);
                            failLog.setOperTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
                            operLogServic.insertOperlog(failLog);
                        }
                    }
                }catch(Exception e){
                    //DO NOTHING
//                    e.printStackTrace();
                }
            }
        };
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemoteDir() {
        return remoteDir;
    }

    public void setRemoteDir(String remoteDir) {
        this.remoteDir = remoteDir;
    }

    public String getLocalTempDir() {
        return localTempDir;
    }

    public void setLocalTempDir(String localTempDir) {
        this.localTempDir = localTempDir;
    }

    public String getLocalStoreDir() {
        return localStoreDir;
    }

    public void setLocalStoreDir(String localStoreDir) {
        this.localStoreDir = localStoreDir;
    }
}
