package com.promote.project.promote.service;

import com.promote.project.promote.domain.HotelWhitelist;

import java.util.List;
import java.util.Map;

/**
 * 檔案Service介面
 *
 * @author 6592 曾培晃
 * @date 2020-05-06
 */
public interface IFilesService {

    /**
     * 檔案查詢(實名認證、付款檔、白名單差異檔)
     *
     * @param path 路徑
     * @return
     */
    List<Map<String,Object>> getFileList(String fileDate,String path);
}
