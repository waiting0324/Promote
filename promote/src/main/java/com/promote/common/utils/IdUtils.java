package com.promote.common.utils;

import com.promote.common.core.lang.UUID;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ID生成器工具類
 * 
 * @author ruoyi
 */
public class IdUtils
{
    /**
     * 獲取隨機UUID
     * 
     * @return 隨機UUID
     */
    public static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * 簡化的UUID，去掉了橫線
     * 
     * @return 簡化的UUID，去掉了橫線
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 獲取隨機UUID，使用效能更好的ThreadLocalRandom生成UUID
     * 
     * @return 隨機UUID
     */
    public static String fastUUID()
    {
        return UUID.fastUUID().toString();
    }

    /**
     * 簡化的UUID，去掉了橫線，使用效能更好的ThreadLocalRandom生成UUID
     * 
     * @return 簡化的UUID，去掉了橫線
     */
    public static String fastSimpleUUID()
    {
        return UUID.fastUUID().toString(true);
    }


    /**
     * 產生抵用券序號
     *
     * @param storeId 旅宿唯一KEY
     * @param funds 足額發效的補助機構
     * @return
     */
    public static Map<String, LinkedList<String>> generateTicketNo(String storeId, String[] funds) {

        /*
          S:中企
          T:中辦
          B:商業司
          C:文化部
          storeId + (yyMMDDHHmmssSSS - (storeId * storeId)) + S + seq
        */
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyddHHmmssSSS");
        BigDecimal a = new BigDecimal(sdf.format(d));
        BigDecimal b = new BigDecimal(Integer.parseInt(storeId) * Integer.parseInt(storeId));

        String prefixNo = storeId + a.subtract(b);
        //  String[] funds = new String[] {"S", "T", "B", "C"};
        Map<String, LinkedList<String>> map = new HashMap<>();
        LinkedList<String> tickets = null;
        for (int i=0; i<funds.length; i++) {
            tickets = new LinkedList<String>();
            for (int j=1; j<= 4; j++) {
                tickets.add(prefixNo + funds[i] + j);
            }
            map.put(funds[i], tickets);
        }
        return map;
    }
}
