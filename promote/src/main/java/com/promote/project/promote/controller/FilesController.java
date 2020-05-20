package com.promote.project.promote.controller;

import com.promote.common.utils.StringUtils;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.project.promote.service.IFilesService;
import com.promote.project.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 檔案Controller
 *
 * @author 6592 曾培晃
 * @date 2020-04-22
 */
@RestController
@RequestMapping("/file")
public class FilesController extends BaseController {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private IFilesService filesService;

    /**
     *檔案查詢(實名認證、付款檔、白名單差異檔)
     *
     * @param request
     * @return
     */
    @PostMapping("/getFileList")
    public AjaxResult getFileList(@RequestBody Map<String, Object> request) {
        //檔案日期
        String fileDate = (String)request.get("fileDate");
        //檔案類型
        String fileType = (String)request.get("fileType");
        if(StringUtils.isEmpty(fileType)){
            return AjaxResult.error("未輸入檔案類型");
        }
        String configKey = null;
        switch (fileType){
            case "0":
                configKey = "file.path.zero";
                break;
            case "1":
                configKey = "file.path.one";
                break;
            case "2":
                configKey = "file.path.two";
                break;
            case "3":
                configKey = "file.path.three";
                break;
            case "4":
                configKey = "file.path.four";
                break;
            case "5":
                configKey = "file.path.five";
                break;
        }
        String path = configService.selectConfigByKey(configKey);
        if(StringUtils.isEmpty(path)){
            return AjaxResult.error("未輸入檔案類型");
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> fileList = filesService.getFileList(fileDate, path);
        if(StringUtils.isEmpty(fileList)){
            return AjaxResult.error("系統異常,請稍侯再試");
        }
        resultMap.put("fileInfo", fileList);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("result", resultMap);
        return ajax;
    }

    /**
     *檔案下載(實名認證、付款檔、白名單差異檔)
     *
     * @param request
     * @return 結果
     */
    @PostMapping("/downloadFile")
    public AjaxResult downloadFile(@RequestBody Map<String, Object> request, HttpServletResponse response) {
        //檔名
        String fileName = (String)request.get("fileName");
        //檔案類型
        String fileType = (String)request.get("fileType");
        if(StringUtils.isEmpty(fileName) || StringUtils.isEmpty(fileType)){
            return AjaxResult.error("需輸入檔名及檔案類型");
        }
        HttpHeaders headers = new HttpHeaders();
        String configKey = null;
        switch (fileType){
            case "0":
                configKey = "file.path.zero";
                break;
            case "1":
                configKey = "file.path.one";
                break;
            case "2":
                configKey = "file.path.two";
                break;
            case "3":
                configKey = "file.path.three";
                break;
            case "4":
                configKey = "file.path.four";
                break;
            case "5":
                configKey = "file.path.five";
                break;
        }
        String path = configService.selectConfigByKey(configKey);
        File file = filesService.getFile(path, fileName);
        if(file == null){
            AjaxResult.error("無此檔案");
        }
        response.reset();
        response.setContentLength((int) file.length());
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        FileInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try{
            byte[] buffers = new byte[1024];
            inputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();
            int length = 0;
            while ((length = inputStream.read(buffers)) > 0){
                outputStream.write(buffers, 0, length);
            }
        }catch(Exception e){
            return AjaxResult.error("系統異常,請聯繫管理員");
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return AjaxResult.success();
    }

    /**
     * 檔案上傳(實名認證、付款檔、白名單差異檔)
     *
     * @param fileType 檔案類型
     * @param file 檔案
     * @return 結果
     */
    @PostMapping("/uploadFile")
    public AjaxResult uploadFile(@RequestPart("fileType")String fileType,@RequestPart("file")MultipartFile file) {
        if(StringUtils.isEmpty(fileType)){
            return AjaxResult.error("需輸入檔案類型");
        }
        if(file == null){
            return AjaxResult.error("沒有檔案上傳");
        }
        String configKey = null;
        switch (fileType){
            case "0":
                configKey = "file.path.zero";
                break;
            case "1":
                configKey = "file.path.one";
                break;
            case "2":
                configKey = "file.path.two";
                break;
            case "3":
                configKey = "file.path.three";
                break;
            case "4":
                configKey = "file.path.four";
                break;
            case "5":
                configKey = "file.path.five";
                break;
        }
        String path = configService.selectConfigByKey(configKey);
        File dirs = new File(path);
        if(!dirs.exists()){
            //創建目錄
            dirs.mkdirs();
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte[] buffers = new byte[1024];
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(new File(path + "/" + file.getOriginalFilename()));
            int length = 0;
            while ((length = inputStream.read(buffers)) > 0){
                outputStream.write(buffers, 0, length);
            }
        } catch (IOException e) {
            return AjaxResult.error("系統異常,請聯繫管理員");
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return AjaxResult.success();
    }
}
