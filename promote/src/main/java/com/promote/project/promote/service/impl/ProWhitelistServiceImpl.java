package com.promote.project.promote.service.impl;

import com.promote.common.utils.DateUtils;
import com.promote.common.utils.StringUtils;
import com.promote.project.promote.domain.ProWhitelist;
import com.promote.project.promote.mapper.ProWhitelistMapper;
import com.promote.project.promote.service.IProWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public ProWhitelist selectProWhitelistById(String id) {
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
        proWhitelist.setCreateTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
        return proWhitelistMapper.insertProWhitelist(proWhitelist);
    }

    /**
     * 修改白名單
     *
     * @param proWhitelist 白名單
     * @return 結果
     */
    @Override
    @Transactional
    public int updateProWhitelist(ProWhitelist proWhitelist) {
        proWhitelist.setUpdateTime(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", DateUtils.getTime()));
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
     * 將旅店資料匯入白名單
     *
     * @param file    資料
     * @param version Excel版本
     */
//    @Override
//    @Transactional
//    public void importHostelData(InputStream file, String version) {
//        //白名單欄位與白名單Excel之欄位對應順序
//        Map<String,Integer> hostelMap = new ConcurrentHashMap<String,Integer>();
////        hostelMap.put("owner",-1);
//        hostelMap.put("name",2);
//        hostelMap.put("taxNo",22);
//        hostelMap.put("username",26);
////        hostelMap.put("password",-1);
//        hostelMap.put("address",4);
//        hostelMap.put("phonenumber",6);
//        hostelMap.put("email",27);
////        hostelMap.put("isNMarket",-1);
////        hostelMap.put("isTMarket",-1);
////        hostelMap.put("isFoodbeverage",-1);
////        hostelMap.put("isCulture",-1);
////        hostelMap.put("isSightseeing",-1);
//        hostelMap.put("type",1);
//        if ("2007".equals(version)) {
//            Excel07SaxReader reader = new Excel07SaxReader(createRowHandler(hostelMap));
//            reader.read(file,1);
//        } else if ("2003".equals(version)) {
//            Excel03SaxReader reader = new Excel03SaxReader(createRowHandler(hostelMap));
//            reader.read(file,1);
//        }
//    }


    /**
     * 將商家資料匯入白名單
     *
     * @param file    資料
     * @param version Excel版本
     */
//    @Override
//    @Transactional
//    public void importStoreData(InputStream file, String version) {
//        //白名單欄位與白名單Excel之欄位對應順序
//        Map<String,Integer> storeMap = new ConcurrentHashMap<String,Integer>();
//        storeMap.put("owner",5);
//        storeMap.put("name",1);
//        storeMap.put("taxNo",2);
////        storeMap.put("username",-1);
////        storeMap.put("password",-1);
//        storeMap.put("address",3);
//        storeMap.put("phonenumber",6);
////        storeMap.put("email",-1);
//        storeMap.put("isNMarket",7);
//        storeMap.put("isTMarket",8);
//        storeMap.put("isFoodbeverage",10);
//        storeMap.put("isCulture",11);
//        storeMap.put("isSightseeing",12);
//        storeMap.put("type",2);
//        if ("2007".equals(version)) {
//            Excel07SaxReader reader = new Excel07SaxReader(createRowHandler(storeMap));
//            reader.read(file,1);
//        } else if ("2003".equals(version)) {
//            Excel03SaxReader reader = new Excel03SaxReader(createRowHandler(storeMap));
//            reader.read(file,1);
//        }
//    }

    /**
     * 根據代號及資料類型查找白名單資料
     *
     * @param id 代號
     * @param type 資料類型
     * @return 白名單
     */
    @Override
    public ProWhitelist selectProWhitelistByIdType(String id, String type) {
        return proWhitelistMapper.selectProWhitelistByIdType(id,type);
    }

    /**
     * 根據統編/身分證字號查找白名單資料
     *
     * @param taxNo 統編/身分證字號
     * @return 白名單
     */
    @Override
    public ProWhitelist selectProWhitelistByTaxNo(String taxNo) {
        // 查詢白名單
        List<ProWhitelist> whitelists = proWhitelistMapper.selectProWhitelistByTaxNo(taxNo);

        ProWhitelist white = whitelists.get(0);
        // 清除關鍵訊息
        white.setUsername(null);
        white.setPassword(null);

        return white;
    }

    /**
     *根據資料類型及統編/身分證字號查找白名單資料
     *
     * @param type 資料類型 (1旅宿業者 2商家)
     * @param taxNo 統編/身分證字號
     * @return 結果
     */
    @Override
    public List<Map<String,Object>> getByTypeTaxNo(String type, String taxNo) {
        return proWhitelistMapper.getByTypeTaxNo(type, taxNo);
    }


//    private RowHandler createRowHandler(Map<String,Integer> dataMap) {
//        return new RowHandler() {
//            @Override
//            public void handle(int sheetIndex, int rowIndex, List<Object> rowlist) {
//                if (rowIndex > 0 && rowlist != null) {
//                    Class c = ProWhitelist.class;
////                    String[] columnName = {"owner","name","taxNo","username","password","address","phonenumber","email","isNMarket","isTMarket","isFoodbeverage","isCulture","isSightseeing"};
////                    Class[] columnType={String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class};
//                    Field[] fields = c.getDeclaredFields();
//                    List<String> columnNameList = new ArrayList<String>();
//                    List<Class> columnTypeList = new ArrayList<Class>();
//                    for(Field field : fields){
//                        String fieldName = field.getName();
//                        if("serialVersionUID".equals(fieldName) || "id".equals(fieldName) || "type".equals(fieldName)){
//                            continue;
//                        }
//                        columnNameList.add(fieldName);
//                        columnTypeList.add(field.getType());
//                    }
//
//                    ProWhitelist proWhitelist = new ProWhitelist();
//                    for (int i = 0; i < columnNameList.size(); i++) {
//                        try {
//                            String columnName = columnNameList.get(i);
//                            String methodName = new StringBuilder("set").append(columnName.substring(0, 1).toUpperCase()).append(columnName.substring(1)).toString();
//                            Method method = c.getMethod(methodName, columnTypeList.get(i));
//                            if (method != null) {
//                                Integer index = dataMap.get(columnName);
//                                if(index == null){
//                                    continue;
//                                }
//                                Object value = rowlist.get(index);
//                                method.invoke(proWhitelist, StringUtils.isNotNull(value) ? value.toString() : null);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    Date now = DateUtils.dateTime("yyyy-MM-dd",DateUtils.getDate());
//                    proWhitelist.setType(dataMap.get("type") != null ? dataMap.get("type").toString() : null);
//                    proWhitelist.setCreateTime(now);
//                    proWhitelist.setUpdateTime(now);
//                    proWhitelistMapper.insertProWhitelist(proWhitelist);
//                }
//            }
//        };
//    }
}
