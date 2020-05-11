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
import com.promote.project.promote.domain.HotelWhitelist;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.domain.StoreWhitelist;
import com.promote.project.promote.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
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

    @Autowired
    ICouponService couponService;

    @Autowired
    IDailyConsumeService dailyConsumeService;

    @Autowired
    HotelWhitelistService hotelWhitelistService;

    @Autowired
    StoreWhitelistService storeWhitelistService;

    // 與FTP相關配置 開始
    private String host;

    private int port;

    private String username;

    private String password;

    private String remoteDir;

    private String localTempDir;

    private String localStoreDir;
    // 與FTP相關配置 結束

    //白名單成功筆數
    private Integer proWhitelistSuccessCnt = 0;
    //白名單失敗筆數
    private Integer proWhitelistFailCnt = 0;
    //旅宿,店家白名單成功筆數
    private Integer whitelistSuccessCnt = 0;
    //旅宿,店家白名單失敗筆數
    private Integer whitelistFailCnt = 0;

//    private int count = 0; //測試用


    /**
     * 從FTP上下載差異檔
     */
    public void ftpFetchFile() throws Exception {

        // FTP 連接
        Ftp ftp = new Ftp(host, port, username, password, Charset.forName("utf8"));

        // 讀取檔案列表
        List<String> hostelFiles = ftp.ls(remoteDir + "hostellist");
        List<String> storeFiles = ftp.ls(remoteDir + "storelist");

        // 循環下載所有檔案
        for (String fileName : hostelFiles) {
            ftp.download(remoteDir, fileName, FileUtil.file(localTempDir + "/hostellist/" + fileName));
        }
        for (String fileName : storeFiles) {
            ftp.download(remoteDir, fileName, FileUtil.file(localTempDir + "/storelist/" + fileName));
        }

        // 刪除遠端資料
        //ftp.delDir(remoteDir);

        // 關閉FTP連接
        ftp.close();

        // 開始將Excel檔案匯入資料庫

        // 旅宿業者
        File[] localFiles = FileUtil.ls(localTempDir + "/hostellist");
        for (File localFile : localFiles) {
//            this.dealDiffData(localFile.getPath(), true);
            this.dealWhitelistFile(localFile.getPath(), true);
        }

        // 商家
        localFiles = FileUtil.ls(localTempDir + "/storelist");
        for (File localFile : localFiles) {
//            this.dealDiffData(localFile.getPath(), false);
            this.dealWhitelistFile(localFile.getPath(), false);
        }

        // 將檔案從臨時資料夾轉到儲存資料夾
        FileUtil.copyContent(FileUtil.newFile(localTempDir), FileUtil.newFile(localStoreDir), true);

        // 刪除暫存文件
        FileUtil.clean(localTempDir);
    }


    /**
     * 處理白名單匯入及差異檔
     *
     * @param path     白名單路徑
     * @param isHostel 是否為旅宿業者
     */
    public void dealWhitelistFile(String path, Boolean isHostel) {
        if (StringUtils.isNotEmpty(path)) {
            InputStreamReader inputStream = null;
            BufferedReader reader = null;
            Map<String, Integer> pair = new HashMap<String, Integer>();
            //白名單Field與白名單Excel映射關係
            if (isHostel) {
                //旅宿
                pair.put("id", 0);
                pair.put("name", 2);
                pair.put("taxNo", 22);
                pair.put("username", 35);
                pair.put("password", 36);
                pair.put("address", 4);
                pair.put("phonenumber", 6);
                pair.put("email", 27);
                pair.put("type", 1);
                pair.put("latitude", 11);
                pair.put("longitude", 10);
            } else {
                //店家
                pair.put("id", 0);
                pair.put("owner", 7);
                pair.put("name", 1);
                pair.put("taxNo", 3);
                pair.put("address", 5);
                pair.put("phonenumber", 8);
                pair.put("type", 2);
                pair.put("isNMarket", 9);
                pair.put("isTMarket", 10);
                pair.put("isFoodbeverage", 12);
                pair.put("isCulture", 13);
                pair.put("isSightseeing", 14);
            }
            try {
                inputStream = new InputStreamReader(new FileInputStream(path),"UTF-8");//檔案讀取路徑
                reader = new BufferedReader(inputStream);
                String line = null; //每一行
                int lineIndex = 0;
                //讀取開始
                while ((line = reader.readLine()) != null) {
                    if(lineIndex == 0){
                        //標題不處理
                        lineIndex++;
                        continue;
                    }
                    handlerRow(pair,Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 處理每一行數據
     *
     * @param pair 白名單Field與白名單檔映射關係
     * @param rowlist 每一行數據
     */
    private void handlerRow(Map<String, Integer> pair,List<String> rowlist) {
        try{
            Integer type = pair.get("type");
            String id = rowlist.get(pair.get("id")).toString();
            if (StringUtils.isEmpty(id)) {
                throw new Exception("ID不得為空");
            }
            Class proWhitelistCls = ProWhitelist.class;
            Class whitelistCls = type == 1 ? HotelWhitelist.class : StoreWhitelist.class;
            Field[] proWhitelistFields = proWhitelistCls.getDeclaredFields();
            Field[] whitelistFields = whitelistCls.getDeclaredFields();
            //白名單Model的屬性名
            List<String> proWhitelistFieldName = new ArrayList<String>();
            List<String> whitelistFieldName = new ArrayList<String>();
            //白名單Model的屬性類型
            List<Class> proWhitelistFieldType = new ArrayList<Class>();
            List<Class> whitelistFieldType = new ArrayList<Class>();
            //取得白名單屬姓名及屬性類型
            for (Field field : proWhitelistFields) {
                String fieldName = field.getName();
                if ("serialVersionUID".equals(fieldName) || "type".equals(fieldName)) {
                    continue;
                }
                proWhitelistFieldName.add(fieldName);
                proWhitelistFieldType.add(field.getType());
            }
            //取得旅宿or店家白名單屬姓名及屬性類型
            for (Field field : whitelistFields) {
                String fieldName = field.getName();
                if ("serialVersionUID".equals(fieldName)) {
                    continue;
                }
                whitelistFieldName.add(fieldName);
                whitelistFieldType.add(field.getType());
            }
            //根據id及類型去查白名單table,有查到做update,沒查到做insert
            ProWhitelist proWhitelist = proWhitelistService.selectProWhitelistByIdType(id, type.toString());
            HotelWhitelist hotelWhitelist = null;
            StoreWhitelist storeWhitelist = null;
            if (type == 1) {
                hotelWhitelist = hotelWhitelistService.getHotelWhitelistById(id);
            } else if (type == 2) {
                storeWhitelist = storeWhitelistService.getStoreWhitelistById(id);
            }
            boolean needInsertProWhitelist = false;
            boolean needInsertWhitelist = false;
            if (proWhitelist == null) {
                needInsertProWhitelist = true;
                proWhitelist = new ProWhitelist();
            }
            if (type == 1 && StringUtils.isNull(hotelWhitelist)) {
                needInsertWhitelist = true;
                hotelWhitelist = new HotelWhitelist();
            }
            if (type == 2 && StringUtils.isNull(storeWhitelist)) {
                needInsertWhitelist = true;
                storeWhitelist = new StoreWhitelist();
            }
            //處理白名單
            for (int i = 0; i < proWhitelistFieldName.size(); i++) {
                try {
                    String columnName = proWhitelistFieldName.get(i);
                    String methodName = new StringBuilder("set").append(columnName.substring(0, 1).toUpperCase()).append(columnName.substring(1)).toString();
                    //取得白名單Model的setter方法
                    Class typeClass = proWhitelistFieldType.get(i);
                    Method method = proWhitelistCls.getMethod(methodName, typeClass);
                    if (method != null) {
                        Integer index = pair.get(columnName);
                        if (index == null) {
                            continue;
                        }
                        String tempValue = rowlist.get(index);
                        //設定Excel的值到白名單Model
                        String value = StringUtils.isNotNull(tempValue) ? tempValue.toString().trim() : null;
//                        String value = StringUtils.isNotNull(tempValue) ? tempValue.toString().trim().replaceAll("\\u00A0+", "") : null;
                        switch (typeClass.getName()) {
                            case "java.lang.Double":
                                method.invoke(proWhitelist, StringUtils.isNotNull(value) ? Double.parseDouble(value) : null);
                                break;
                            case "java.lang.String":
                                method.invoke(proWhitelist, value);
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            proWhitelist.setType(type != null ? type.toString() : null);
            try {
                if (needInsertProWhitelist) {
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
                proWhitelistSuccessCnt++;
//                            count++; //測試用
            } catch (Exception e) {
                //新增或更新失敗時寫log
                proWhitelistFailCnt++;
                SysOperLog failLog = new SysOperLog();
                failLog.setTitle("定時任務");
                failLog.setMethod(PromoteTask.class.getName() + ".dealDiffData(String path)");
                failLog.setOperatorType(1);
                failLog.setOperName("SYSTEM");
                failLog.setErrorMsg("執行白名單" + (needInsertProWhitelist ? "新增" : "更新") + "失敗: " + (type == 1 ? "旅宿業者代號= " : "店家代碼= ") + id);
                failLog.setOperTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
                operLogServic.insertOperlog(failLog);
            }
            //處理旅宿or店家白名單
            for (int i = 0; i < whitelistFieldName.size(); i++) {
                try {
                    String columnName = whitelistFieldName.get(i);
                    String methodName = new StringBuilder("set").append(columnName.substring(0, 1).toUpperCase()).append(columnName.substring(1)).toString();
                    //取得旅宿or店家白名單Model的setter方法
                    Class typeClass = whitelistFieldType.get(i);
                    Method method = whitelistCls.getMethod(methodName, typeClass);
                    if (method != null) {
                        String tempValue = rowlist.get(i);
                        //設定Excel的值到白名單Model
                        String value = StringUtils.isNotNull(tempValue) ? tempValue.toString().trim() : null;
//                        String value = StringUtils.isNotNull(tempValue) ? tempValue.toString().trim().replaceAll("\\u00A0+", "") : null;
                        switch (typeClass.getName()) {
                            case "java.lang.Double":
                                method.invoke(type == 1 ? hotelWhitelist : storeWhitelist, StringUtils.isNotNull(value) ? Double.parseDouble(value) : null);
                                break;
                            case "java.lang.String":
                                method.invoke(type == 1 ? hotelWhitelist : storeWhitelist, value);
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                if (needInsertWhitelist) {
                    //新增
//                                if(count == 3){
//                                    //測試用
//                                    count++;
//                                    throw new Exception();
//                                }
                    int result = type == 1 ? hotelWhitelistService.insertHotelWhitelist(hotelWhitelist) : storeWhitelistService.insertStoreWhitelist(storeWhitelist);
                } else {
                    //更新
//                                if(count == 4){
//                                    //測試用
//                                    count++;
//                                    throw new Exception();
//                                }
                    int result = type == 1 ? hotelWhitelistService.updateHotelWhitelist(hotelWhitelist) : storeWhitelistService.updateStoreWhitelist(storeWhitelist);
                }
                whitelistSuccessCnt++;
//                            count++; //測試用
            } catch (Exception e) {
                //新增或更新失敗時寫log
                whitelistFailCnt++;
                SysOperLog failLog = new SysOperLog();
                failLog.setTitle("定時任務");
                failLog.setMethod(PromoteTask.class.getName() + ".dealDiffData(String path)");
                failLog.setOperatorType(1);
                failLog.setOperName("SYSTEM");
                String errMsg = "";
                if (type == 1) {
                    errMsg = "執行旅宿白名單(hotel_whitelist)" + (needInsertWhitelist ? "新增" : "更新") + "失敗: 旅宿業者代號 = " + id;
                } else if (type == 2) {
                    errMsg = "執行店家白名單(store_whitelist)" + (needInsertWhitelist ? "新增" : "更新") + "失敗: 店家代碼 = " + id;
                }
                failLog.setErrorMsg(errMsg);
                failLog.setOperTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
                operLogServic.insertOperlog(failLog);
            }
        }catch(Exception e){
            //DO NOTHING
        }
    }



    /**
     * 處理白名單匯入及差異檔
     *
     * @param path 白名單路徑
     */
    public void dealDiffData(String path, Boolean isHostel) {
        if (StringUtils.isNotEmpty(path)) {
            Map<String, Integer> pair = new HashMap<String, Integer>();
            //白名單Field與白名單Excel映射關係
            if (isHostel) {
                //旅宿
                pair.put("id", 0);
                pair.put("name", 2);
                pair.put("taxNo", 22);
                pair.put("username", 35);
                pair.put("password", 36);
                pair.put("address", 4);
                pair.put("phonenumber", 6);
                pair.put("email", 27);
                pair.put("type", 1);
                pair.put("latitude", 11);
                pair.put("longitude", 10);
            } else {
                //店家
                pair.put("id", 0);
                pair.put("owner", 7);
                pair.put("name", 1);
                pair.put("taxNo", 3);
                pair.put("address", 5);
                pair.put("phonenumber", 8);
                pair.put("type", 2);
                pair.put("isNMarket", 9);
                pair.put("isTMarket", 10);
                pair.put("isFoodbeverage", 12);
                pair.put("isCulture", 13);
                pair.put("isSightseeing", 14);
            }
            //根據Excel版本的不同選用不同的方式處理
            if (path.indexOf(".xlsx") > -1) {
                //Excel 2007
                Excel07SaxReader reader = new Excel07SaxReader(createRowHandler(pair));
                reader.read(path, 0);
            } else {
                //Excel 2003
                Excel03SaxReader reader = new Excel03SaxReader(createRowHandler(pair));
                reader.read(path, 0);
            }
            String mathodName = PromoteTask.class.getName() + ".dealDiffData(String path)";
            Date now = DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime());
            if (proWhitelistSuccessCnt > 0) {
                //白名單成功筆數寫log
                SysOperLog successLog = new SysOperLog();
                successLog.setTitle("定時任務");
                successLog.setMethod(mathodName);
                successLog.setOperatorType(1);
                successLog.setOperName("SYSTEM");
                successLog.setJsonResult("執行白名單(pro_whitelist)匯入- 共成功: " + proWhitelistSuccessCnt + "筆");
                successLog.setOperTime(now);
                operLogServic.insertOperlog(successLog);
            }
            if (proWhitelistFailCnt > 0) {
                //白名單失敗筆數寫log
                SysOperLog failLog = new SysOperLog();
                failLog.setTitle("定時任務");
                failLog.setMethod(mathodName);
                failLog.setOperatorType(1);
                failLog.setOperName("SYSTEM");
                failLog.setErrorMsg("執行白名單(pro_whitelist)匯入-共失敗: " + proWhitelistFailCnt + "筆");
                failLog.setOperTime(now);
                operLogServic.insertOperlog(failLog);
            }

            if (whitelistSuccessCnt > 0) {
                //旅宿or店家白名單成功筆數寫log
                SysOperLog successLog = new SysOperLog();
                successLog.setTitle("定時任務");
                successLog.setMethod(mathodName);
                successLog.setOperatorType(1);
                successLog.setOperName("SYSTEM");
                successLog.setJsonResult("執行" + (isHostel ? "旅宿白名單(hotel_whitelist)匯入- 共成功: " : "店家白名單(store_whitelist)匯入- 共成功: ") + whitelistSuccessCnt + "筆");
                successLog.setOperTime(now);
                operLogServic.insertOperlog(successLog);
            }
            if (whitelistFailCnt > 0) {
                //旅宿or店家白名單失敗筆數寫log
                SysOperLog failLog = new SysOperLog();
                failLog.setTitle("定時任務");
                failLog.setMethod(mathodName);
                failLog.setOperatorType(1);
                failLog.setOperName("SYSTEM");
                failLog.setErrorMsg("執行" + (isHostel ? "旅宿白名單(hotel_whitelist)匯入- 共失敗: " : "店家白名單(store_whitelist)匯入- 共失敗: ") + whitelistFailCnt + "筆");
                failLog.setOperTime(now);
                operLogServic.insertOperlog(failLog);
            }
            //成功,失敗Count清成0
            proWhitelistSuccessCnt = 0;
            proWhitelistFailCnt = 0;
            whitelistSuccessCnt = 0;
            whitelistFailCnt = 0;
        }
    }

    private RowHandler createRowHandler(Map<String, Integer> pair) {
        return new RowHandler() {
            @Override
            public void handle(int sheetIndex, int rowIndex, List<Object> rowlist) {
                //每一列都會呼叫此方法一次
                try {
                    if (rowIndex > 0 && rowlist != null) {
                        //取得類型-旅宿or店家
                        Integer type = pair.get("type");
                        String id = rowlist.get(pair.get("id")).toString();
                        if (StringUtils.isEmpty(id)) {
                            throw new Exception("ID不得為空");
                        }
                        Class proWhitelistCls = ProWhitelist.class;
                        Class whitelistCls = type == 1 ? HotelWhitelist.class : StoreWhitelist.class;
                        Field[] proWhitelistFields = proWhitelistCls.getDeclaredFields();
                        Field[] whitelistFields = whitelistCls.getDeclaredFields();
                        //白名單Model的屬性名
                        List<String> proWhitelistFieldName = new ArrayList<String>();
                        List<String> whitelistFieldName = new ArrayList<String>();
                        //白名單Model的屬性類型
                        List<Class> proWhitelistFieldType = new ArrayList<Class>();
                        List<Class> whitelistFieldType = new ArrayList<Class>();
                        //取得白名單屬姓名及屬性類型
                        for (Field field : proWhitelistFields) {
                            String fieldName = field.getName();
                            if ("serialVersionUID".equals(fieldName) || "type".equals(fieldName)) {
                                continue;
                            }
                            proWhitelistFieldName.add(fieldName);
                            proWhitelistFieldType.add(field.getType());
                        }
                        //取得旅宿or店家白名單屬姓名及屬性類型
                        for (Field field : whitelistFields) {
                            String fieldName = field.getName();
                            if ("serialVersionUID".equals(fieldName)) {
                                continue;
                            }
                            whitelistFieldName.add(fieldName);
                            whitelistFieldType.add(field.getType());
                        }
                        //根據id及類型去查白名單table,有查到做update,沒查到做insert
                        ProWhitelist proWhitelist = proWhitelistService.selectProWhitelistByIdType(id, type.toString());
                        HotelWhitelist hotelWhitelist = null;
                        StoreWhitelist storeWhitelist = null;
                        if (type == 1) {
                            hotelWhitelist = hotelWhitelistService.getHotelWhitelistById(id);
                        } else if (type == 2) {
                            storeWhitelist = storeWhitelistService.getStoreWhitelistById(id);
                        }
                        boolean needInsertProWhitelist = false;
                        boolean needInsertWhitelist = false;
                        if (proWhitelist == null) {
                            needInsertProWhitelist = true;
                            proWhitelist = new ProWhitelist();
                        }
                        if (type == 1 && StringUtils.isNull(hotelWhitelist)) {
                            needInsertWhitelist = true;
                            hotelWhitelist = new HotelWhitelist();
                        }
                        if (type == 2 && StringUtils.isNull(storeWhitelist)) {
                            needInsertWhitelist = true;
                            storeWhitelist = new StoreWhitelist();
                        }
                        //處理白名單
                        for (int i = 0; i < proWhitelistFieldName.size(); i++) {
                            try {
                                String columnName = proWhitelistFieldName.get(i);
                                String methodName = new StringBuilder("set").append(columnName.substring(0, 1).toUpperCase()).append(columnName.substring(1)).toString();
                                //取得白名單Model的setter方法
                                Class typeClass = proWhitelistFieldType.get(i);
                                Method method = proWhitelistCls.getMethod(methodName, typeClass);
                                if (method != null) {
                                    Integer index = pair.get(columnName);
                                    if (index == null) {
                                        continue;
                                    }
                                    Object tempValue = rowlist.get(index);
                                    //設定Excel的值到白名單Model
                                    String value = StringUtils.isNotNull(tempValue) ? tempValue.toString().trim().replaceAll("\\u00A0+", "") : null;
                                    switch (typeClass.getName()) {
                                        case "java.lang.Double":
                                            method.invoke(proWhitelist, StringUtils.isNotNull(value) ? Double.parseDouble(value) : null);
                                            break;
                                        case "java.lang.String":
                                            method.invoke(proWhitelist, value);
                                            break;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        proWhitelist.setType(type != null ? type.toString() : null);
                        try {
                            if (needInsertProWhitelist) {
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
                            proWhitelistSuccessCnt++;
//                            count++; //測試用
                        } catch (Exception e) {
                            //新增或更新失敗時寫log
                            proWhitelistFailCnt++;
                            SysOperLog failLog = new SysOperLog();
                            failLog.setTitle("定時任務");
                            failLog.setMethod(PromoteTask.class.getName() + ".dealDiffData(String path)");
                            failLog.setOperatorType(1);
                            failLog.setOperName("SYSTEM");
                            failLog.setErrorMsg("執行白名單" + (needInsertProWhitelist ? "新增" : "更新") + "失敗: " + (type == 1 ? "旅宿業者代號= " : "店家代碼= ") + id);
                            failLog.setOperTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
                            operLogServic.insertOperlog(failLog);
                        }
                        //處理旅宿or店家白名單
                        for (int i = 0; i < whitelistFieldName.size(); i++) {
                            try {
                                String columnName = whitelistFieldName.get(i);
                                String methodName = new StringBuilder("set").append(columnName.substring(0, 1).toUpperCase()).append(columnName.substring(1)).toString();
                                //取得旅宿or店家白名單Model的setter方法
                                Class typeClass = whitelistFieldType.get(i);
                                Method method = whitelistCls.getMethod(methodName, typeClass);
                                if (method != null) {
                                    Object tempValue = rowlist.get(i);
                                    //設定Excel的值到白名單Model
                                    String value = StringUtils.isNotNull(tempValue) ? tempValue.toString().trim().replaceAll("\\u00A0+", "") : null;
                                    switch (typeClass.getName()) {
                                        case "java.lang.Double":
                                            method.invoke(type == 1 ? hotelWhitelist : storeWhitelist, StringUtils.isNotNull(value) ? Double.parseDouble(value) : null);
                                            break;
                                        case "java.lang.String":
                                            method.invoke(type == 1 ? hotelWhitelist : storeWhitelist, value);
                                            break;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            if (needInsertWhitelist) {
                                //新增
//                                if(count == 3){
//                                    //測試用
//                                    count++;
//                                    throw new Exception();
//                                }
                                int result = type == 1 ? hotelWhitelistService.insertHotelWhitelist(hotelWhitelist) : storeWhitelistService.insertStoreWhitelist(storeWhitelist);
                            } else {
                                //更新
//                                if(count == 4){
//                                    //測試用
//                                    count++;
//                                    throw new Exception();
//                                }
                                int result = type == 1 ? hotelWhitelistService.updateHotelWhitelist(hotelWhitelist) : storeWhitelistService.updateStoreWhitelist(storeWhitelist);
                            }
                            whitelistSuccessCnt++;
//                            count++; //測試用
                        } catch (Exception e) {
                            //新增或更新失敗時寫log
                            whitelistFailCnt++;
                            SysOperLog failLog = new SysOperLog();
                            failLog.setTitle("定時任務");
                            failLog.setMethod(PromoteTask.class.getName() + ".dealDiffData(String path)");
                            failLog.setOperatorType(1);
                            failLog.setOperName("SYSTEM");
                            String errMsg = "";
                            if (type == 1) {
                                errMsg = "執行旅宿白名單(hotel_whitelist)" + (needInsertWhitelist ? "新增" : "更新") + "失敗: 旅宿業者代號 = " + id;
                            } else if (type == 2) {
                                errMsg = "執行店家白名單(store_whitelist)" + (needInsertWhitelist ? "新增" : "更新") + "失敗: 店家代碼 = " + id;
                            }
                            failLog.setErrorMsg(errMsg);
                            failLog.setOperTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
                            operLogServic.insertOperlog(failLog);
                        }


                    }
                } catch (Exception e) {
                    //DO NOTHING
//                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 批次寫入每日消費統計檔
     *
     * @param span 增減幾日(ex.1:加1天,-1:減一天)
     */
    public void insertDailyConsume(Integer span) {
        span = StringUtils.isNull(span) ? 0 : span;
        //當前日期
        Calendar begin = Calendar.getInstance();
        begin.add(Calendar.DATE, span);
        //設定時
        begin.set(Calendar.HOUR_OF_DAY, 0);
        //設定分
        begin.set(Calendar.MINUTE, 0);
        //設定秒
        begin.set(Calendar.SECOND, 0);
        //開始時間
        String beginDate = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", begin.getTime());
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DATE, span);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        //結束時間
        String endDate = DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", end.getTime());
        List<Map<String, Object>> storeTotalAmtList = couponService.getTotalAmtByStoreId(beginDate, endDate);
        if (StringUtils.isNotNull(storeTotalAmtList) && storeTotalAmtList.size() > 0) {
            for (Map<String, Object> storeTotalAmtMap : storeTotalAmtList) {
                Long storeId = (Long) storeTotalAmtMap.get("storeId");
                Long totalAmt = (Long) storeTotalAmtMap.get("totalAmt");
                try {
                    dailyConsumeService.insertDailyConsume(DateUtils.dateTime("yyyy-MM-dd", beginDate), storeId, totalAmt);
                } catch (Exception e) {
                    SysOperLog failLog = new SysOperLog();
                    failLog.setTitle("定時任務");
                    failLog.setBusinessType(1);
                    failLog.setMethod(PromoteTask.class.getName() + ".insertDailyConsume(Integer span)");
                    failLog.setOperatorType(1);
                    failLog.setOperName("SYSTEM");
                    failLog.setErrorMsg("新增每日消費統計檔失敗- 店家ID:" + storeId);
                    failLog.setOperTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
                    operLogServic.insertOperlog(failLog);
                }
            }
        }
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
