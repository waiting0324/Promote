package com.promote.framework.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * spring redis 工具類
 * 
 * @author ruoyi
 **/
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisCache
{
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 快取基本的物件，Integer、String、實體類等
     *
     * @param key 快取的鍵值
     * @param value 快取的值
     * @return 快取的物件
     */
    public <T> ValueOperations<String, T> setCacheObject(String key, T value)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.set(key, value);
        return operation;
    }

    /**
     * 快取基本的物件，Integer、String、實體類等
     *
     * @param key 快取的鍵值
     * @param value 快取的值
     * @param timeout 時間
     * @param timeUnit 時間顆粒度
     * @return 快取的物件
     */
    public <T> ValueOperations<String, T> setCacheObject(String key, T value, Integer timeout, TimeUnit timeUnit)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        operation.set(key, value, timeout, timeUnit);
        return operation;
    }

    /**
     * 獲得快取的基本物件。
     *
     * @param key 快取鍵值
     * @return 快取鍵值對應的資料
     */
    public <T> T getCacheObject(String key)
    {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 刪除單個物件
     *
     * @param key
     */
    public void deleteObject(String key)
    {
        redisTemplate.delete(key);
    }

    /**
     * 刪除集合物件
     *
     * @param collection
     */
    public void deleteObject(Collection collection)
    {
        redisTemplate.delete(collection);
    }

    /**
     * 快取List資料
     *
     * @param key 快取的鍵值
     * @param dataList 待快取的List資料
     * @return 快取的物件
     */
    public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList)
    {
        ListOperations listOperation = redisTemplate.opsForList();
        if (null != dataList)
        {
            redisTemplate.delete(key);
            int size = dataList.size();
            for (int i = 0; i < size; i++)
            {
                listOperation.leftPush(key, dataList.get(i));
            }
        }
        return listOperation;
    }

    /**
     * 獲得快取的list物件
     *
     * @param key 快取的鍵值
     * @return 快取鍵值對應的資料
     */
    public <T> List<T> getCacheList(String key)
    {
        List<T> dataList = new ArrayList<T>();
        ListOperations<String, T> listOperation = redisTemplate.opsForList();
        Long size = listOperation.size(key);

        for (int i = 0; i < size; i++)
        {
            dataList.add(listOperation.index(key, i));
        }
        return dataList;
    }

    /**
     * 快取Set
     *
     * @param key 快取鍵值
     * @param dataSet 快取的資料
     * @return 快取資料的物件
     */
    public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet)
    {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext())
        {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 獲得快取的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(String key)
    {
        Set<T> dataSet = new HashSet<T>();
        BoundSetOperations<String, T> operation = redisTemplate.boundSetOps(key);
        dataSet = operation.members();
        return dataSet;
    }

    /**
     * 快取Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    public <T> HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap)
    {
        HashOperations hashOperations = redisTemplate.opsForHash();
        if (null != dataMap)
        {
            for (Map.Entry<String, T> entry : dataMap.entrySet())
            {
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
        }
        return hashOperations;
    }

    /**
     * 獲得快取的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(String key)
    {
        Map<String, T> map = redisTemplate.opsForHash().entries(key);
        return map;
    }

    /**
     * 獲得快取的基本物件列表
     * 
     * @param pattern 字串字首
     * @return 物件列表
     */
    public Collection<String> keys(String pattern)
    {
        return redisTemplate.keys(pattern);
    }
}
