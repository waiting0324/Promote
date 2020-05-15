package com.promote.project.promote.controller;

import com.promote.common.utils.SecurityUtils;
import com.promote.common.utils.StringUtils;
import com.promote.common.utils.poi.ExcelUtil;
import com.promote.framework.aspectj.lang.annotation.Log;
import com.promote.framework.aspectj.lang.enums.BusinessType;
import com.promote.framework.security.service.TokenService;
import com.promote.framework.web.controller.BaseController;
import com.promote.framework.web.domain.AjaxResult;
import com.promote.framework.web.page.TableDataInfo;
import com.promote.project.promote.domain.ConsumerInfo;
import com.promote.project.promote.domain.Coupon;
import com.promote.project.promote.domain.CouponConsume;
import com.promote.project.promote.service.IConsumerService;
import com.promote.project.promote.service.ICouponService;
import com.promote.project.promote.service.IFilesService;
import com.promote.project.system.domain.SysUser;
import com.promote.project.system.service.ISysConfigService;
import com.promote.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
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
//        path = "/temp/0/";
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
     * @param fileName 檔名
     * @param fileType 檔案類型
     * @param fileContent 檔案
     * @return 結果
     */
    @PostMapping("/uploadFile")
    public AjaxResult uploadFile(@RequestPart("fileName")String fileName,@RequestPart("fileType")String fileType,@RequestPart("fileContent")MultipartFile fileContent) {
        if(StringUtils.isEmpty(fileName) || StringUtils.isEmpty(fileType)){
            return AjaxResult.error("需輸入檔名及檔案類型");
        }
        if(fileContent == null){
            return AjaxResult.error("沒有上傳檔案");
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
//        path = "d:/tmp/0"; //測試用
        File file = new File(path);
        if(!file.exists()){
            return AjaxResult.error("檔案資料夾不存在");
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte[] buffers = new byte[1024];
            inputStream = fileContent.getInputStream();
            outputStream = new FileOutputStream(new File(path + "/" + fileName));
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
