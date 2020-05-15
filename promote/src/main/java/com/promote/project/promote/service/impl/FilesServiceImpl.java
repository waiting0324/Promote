package com.promote.project.promote.service.impl;

import cn.hutool.core.util.StrUtil;
import com.promote.common.constant.Constants;
import com.promote.common.constant.ConsumerConstants;
import com.promote.common.exception.CustomException;
import com.promote.common.exception.user.CaptchaException;
import com.promote.common.exception.user.CaptchaExpireException;
import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.framework.manager.AsyncManager;
import com.promote.framework.manager.factory.AsyncFactory;
import com.promote.framework.redis.RedisCache;
import com.promote.project.promote.domain.ConsumerInfo;
import com.promote.project.promote.domain.StoreInfo;
import com.promote.project.promote.mapper.ConsumerInfoMapper;
import com.promote.project.promote.service.ICommonService;
import com.promote.project.promote.service.IFilesService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.mapper.SysUserMapper;
import com.promote.project.system.service.ISysConfigService;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author 6550 劉威廷
 * @date 2020/4/23 下午 02:10
 * @description 檔案Service 實作
 */
@Service
public class FilesServiceImpl implements IFilesService {


    /**
     * 檔案查詢(實名認證、付款檔、白名單差異檔)
     *
     * @param path 路徑
     * @return
     */
    @Override
    public List<Map<String, Object>> getFileList(String fileDate,String path) {
        List<Map<String, Object>> list = null;
        String targetFileDate = null;
        if(StringUtils.isNotEmpty(fileDate)){
            targetFileDate = fileDate.replaceAll("/", "");
        }
        File folder = new File(path);
        if(folder.exists()){
            list = new ArrayList<Map<String, Object>>();
            File[] files = folder.listFiles();
            for(File file :files){
                if(file.isFile()){
                    String fileName = file.getName();
                    Map<String, Object> map = new LinkedHashMap<String, Object>();
                    if(StringUtils.isNotEmpty(fileDate)){
                        if(fileName.indexOf(targetFileDate) > -1){
                            map.put("fileName",fileName);
                            list.add(map);
                        }
                    }else{
                        map.put("fileName",fileName);
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 取得檔案
     *
     * @param path 路徑
     * @param fileName 檔名
     * @return
     */
    @Override
    public File getFile(String path, String fileName) {
        File result = null;
        File folder = new File(path);
        if(folder.exists()){
            File[] files = folder.listFiles();
            for(File file :files){
                if(fileName.equals(file.getName())){
                    result = file;
                    break;
                }
            }
        }else{
            throw new CustomException("檔案資料夾不存在");
        }
        return result;
    }

}