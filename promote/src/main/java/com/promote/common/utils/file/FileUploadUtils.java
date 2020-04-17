package com.promote.common.utils.file;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import com.promote.common.constant.Constants;
import com.promote.common.exception.file.FileNameLengthLimitExceededException;
import com.promote.common.exception.file.FileSizeLimitExceededException;
import com.promote.common.exception.file.InvalidExtensionException;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.StringUtils;
import com.promote.common.utils.security.Md5Utils;
import com.promote.framework.config.RuoYiConfig;

/**
 * 檔案上傳工具類
 * 
 * @author ruoyi
 */
public class FileUploadUtils
{
    /**
     * 預設大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 預設的檔名最大長度 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * 預設上傳的地址
     */
    private static String defaultBaseDir = RuoYiConfig.getProfile();

    private static int counter = 0;

    public static void setDefaultBaseDir(String defaultBaseDir)
    {
        FileUploadUtils.defaultBaseDir = defaultBaseDir;
    }

    public static String getDefaultBaseDir()
    {
        return defaultBaseDir;
    }

    /**
     * 以預設配置進行檔案上傳
     *
     * @param file 上傳的檔案
     * @return 檔名稱
     * @throws Exception
     */
    public static final String upload(MultipartFile file) throws IOException
    {
        try
        {
            return upload(getDefaultBaseDir(), file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 根據檔案路徑上傳
     *
     * @param baseDir 相對應用的基目錄
     * @param file 上傳的檔案
     * @return 檔名稱
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException
    {
        try
        {
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * 檔案上傳
     *
     * @param baseDir 相對應用的基目錄
     * @param file 上傳的檔案
     * @param extension 上傳檔案型別
     * @return 返回上傳成功的檔名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 檔名太長
     * @throws IOException 比如讀寫檔案出錯時
     * @throws InvalidExtensionException 檔案校驗異常
     */
    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
        int fileNamelength = file.getOriginalFilename().length();
        if (fileNamelength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH)
        {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file, allowedExtension);

        String fileName = extractFilename(file);

        File desc = getAbsoluteFile(baseDir, fileName);
        file.transferTo(desc);
        String pathFileName = getPathFileName(baseDir, fileName);
        return pathFileName;
    }

    /**
     * 編碼檔名
     */
    public static final String extractFilename(MultipartFile file)
    {
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        fileName = DateUtils.datePath() + "/" + encodingFilename(fileName) + "." + extension;
        return fileName;
    }

    private static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException
    {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists())
        {
            desc.createNewFile();
        }
        return desc;
    }

    private static final String getPathFileName(String uploadDir, String fileName) throws IOException
    {
        int dirLastIndex = RuoYiConfig.getProfile().length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
        return pathFileName;
    }

    /**
     * 編碼檔名
     */
    private static final String encodingFilename(String fileName)
    {
        fileName = fileName.replace("_", " ");
        fileName = Md5Utils.hash(fileName + System.nanoTime() + counter++);
        return fileName;
    }

    /**
     * 檔案大小校驗
     *
     * @param file 上傳的檔案
     * @return
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws InvalidExtensionException
     */
    public static final void assertAllowed(MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, InvalidExtensionException
    {
        long size = file.getSize();
        if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE)
        {
            throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);
        }

        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension))
        {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION)
            {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension,
                        fileName);
            }
            else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION)
            {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,
                        fileName);
            }
            else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION)
            {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,
                        fileName);
            }
            else
            {
                throw new InvalidExtensionException(allowedExtension, extension, fileName);
            }
        }

    }

    /**
     * 判斷MIME型別是否是允許的MIME型別
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension)
    {
        for (String str : allowedExtension)
        {
            if (str.equalsIgnoreCase(extension))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 獲取檔名的字尾
     * 
     * @param file 表單檔案
     * @return 字尾名
     */
    public static final String getExtension(MultipartFile file)
    {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension))
        {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }
}