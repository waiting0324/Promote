package com.promote.project.promote.service.impl;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import cn.hutool.poi.excel.sax.Excel03SaxReader;
import cn.hutool.poi.excel.sax.Excel07SaxReader;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.promote.project.promote.mapper.ProWhitelistMapper;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.service.IProWhitelistService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 白名單Service業務層處理
 *
 * @author 6550 劉威廷
 * @date 2020-04-20
 */
@Service
public class ProWhitelistServiceImpl implements IProWhitelistService {
    @Autowired
    private ProWhitelistMapper proWhitelistMapper;


    /**
     * 查詢白名單
     *
     * @param id 白名單ID
     * @return 白名單
     */
    @Override
    public ProWhitelist selectProWhitelistById(Long id) {
        return proWhitelistMapper.selectProWhitelistById(id);
    }

    /**
     * 查詢白名單列表
     *
     * @param proWhitelist 白名單
     * @return 白名單
     */
    @Override
    public List<ProWhitelist> selectProWhitelistList(ProWhitelist proWhitelist) {
        return proWhitelistMapper.selectProWhitelistList(proWhitelist);
    }

    /**
     * 新增白名單
     *
     * @param proWhitelist 白名單
     * @return 結果
     */
    @Override
    public int insertProWhitelist(ProWhitelist proWhitelist) {
        proWhitelist.setCreateTime(DateUtils.getNowDate());
        return proWhitelistMapper.insertProWhitelist(proWhitelist);
    }

    /**
     * 修改白名單
     *
     * @param proWhitelist 白名單
     * @return 結果
     */
    @Override
    public int updateProWhitelist(ProWhitelist proWhitelist) {
        proWhitelist.setUpdateTime(DateUtils.getNowDate());
        return proWhitelistMapper.updateProWhitelist(proWhitelist);
    }

    /**
     * 批量刪除白名單
     *
     * @param ids 需要刪除的白名單ID
     * @return 結果
     */
    @Override
    public int deleteProWhitelistByIds(Long[] ids) {
        return proWhitelistMapper.deleteProWhitelistByIds(ids);
    }

    /**
     * 刪除白名單資訊
     *
     * @param id 白名單ID
     * @return 結果
     */
    @Override
    public int deleteProWhitelistById(Long id) {
        return proWhitelistMapper.deleteProWhitelistById(id);
    }

    /**
     * 匯入旅宿業者白名單資料
     *
     * @param file    資料
     * @param version Excel版本
     */
    @Override
    @Transactional
    public void importHostel(InputStream file, String version) {
        //白名單欄位與白名單Excel之欄位對應順序
        Map<String,Integer> hostelMap = new ConcurrentHashMap<String,Integer>();
        hostelMap.put("owner",-1);
        hostelMap.put("name",2);
        hostelMap.put("taxNo",22);
        hostelMap.put("username",26);
        hostelMap.put("password",-1);
        hostelMap.put("address",4);
        hostelMap.put("phonenumber",6);
        hostelMap.put("email",27);
        hostelMap.put("isNMarket",-1);
        hostelMap.put("isTMarket",-1);
        hostelMap.put("isFoodbeverage",-1);
        hostelMap.put("isCulture",-1);
        hostelMap.put("isSightseeing",-1);
        hostelMap.put("type",1);
        if ("2007".equals(version)) {
            Excel07SaxReader reader = new Excel07SaxReader(createRowHandler(hostelMap));
            reader.read(file);
        } else if ("2003".equals(version)) {
            Excel03SaxReader reader = new Excel03SaxReader(createRowHandler(hostelMap));
            reader.read(file);
        }
    }

    private RowHandler createRowHandler(Map<String,Integer> hostelMap) {
        return new RowHandler() {
            @Override
            public void handle(int sheetIndex, int rowIndex, List<Object> rowlist) {
                if (rowIndex > 0 && rowlist != null) {
                    Class c = ProWhitelist.class;
                    String[] columnName = {"owner","name","taxNo","username","password","address","phonenumber","email","isNMarket","isTMarket","isFoodbeverage","isCulture","isSightseeing"};
                    Class[] columnType={String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class};
                    ProWhitelist proWhitelist = new ProWhitelist();
                    for (int i = 0; i < columnName.length; i++) {
                        try {
                            String methodName = new StringBuilder("set").append(columnName[i].substring(0, 1).toUpperCase()).append(columnName[i].substring(1)).toString();
                            Method method = c.getMethod(methodName, columnType[i]);
                            if (method != null) {
                                Integer index = hostelMap.get(columnName[i]);
                                if(index == null || index == -1){
                                    continue;
                                }
                                Object value = rowlist.get(index);
                                method.invoke(proWhitelist, StringUtils.isNotNull(value) ? value.toString() : null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Date now = DateUtils.dateTime("yyyy-MM-dd",DateUtils.getDate());
                    proWhitelist.setType(hostelMap.get("type") != null ? hostelMap.get("type").toString() : null);
                    proWhitelist.setCreateTime(now);
                    proWhitelist.setUpdateTime(now);
                    proWhitelistMapper.insertProWhitelist(proWhitelist);
                }
            }
        };
    }
}
