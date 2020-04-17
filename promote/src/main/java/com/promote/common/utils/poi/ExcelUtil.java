package com.promote.common.utils.poi;

import com.promote.common.core.text.Convert;
import com.promote.common.exception.CustomException;
import com.promote.common.utils.DateUtils;
import com.promote.common.utils.StringUtils;
import com.promote.common.utils.reflect.ReflectUtils;
import com.promote.framework.aspectj.lang.annotation.Excel;
import com.promote.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.promote.framework.aspectj.lang.annotation.Excel.Type;
import com.promote.framework.aspectj.lang.annotation.Excels;
import com.promote.framework.config.RuoYiConfig;
import com.promote.framework.web.domain.AjaxResult;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Excel相關處理
 * 
 * @author ruoyi
 */
public class ExcelUtil<T>
{
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * Excel sheet最大行數，預設65536
     */
    public static final int sheetSize = 65536;

    /**
     * 工作表名稱
     */
    private String sheetName;

    /**
     * 匯出型別（EXPORT:匯出資料；IMPORT：匯入模板）
     */
    private Type type;

    /**
     * 工作薄物件
     */
    private Workbook wb;

    /**
     * 工作表物件
     */
    private Sheet sheet;

    /**
     * 樣式列表
     */
    private Map<String, CellStyle> styles;

    /**
     * 匯入匯出資料列表
     */
    private List<T> list;

    /**
     * 註解列表
     */
    private List<Object[]> fields;

    /**
     * 實體物件
     */
    public Class<T> clazz;

    public ExcelUtil(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    public void init(List<T> list, String sheetName, Type type)
    {
        if (list == null)
        {
            list = new ArrayList<T>();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.type = type;
        createExcelField();
        createWorkbook();
    }

    /**
     * 對excel表單預設第一個索引名轉換成list
     * 
     * @param is 輸入流
     * @return 轉換後集合
     */
    public List<T> importExcel(InputStream is) throws Exception
    {
        return importExcel(StringUtils.EMPTY, is);
    }

    /**
     * 對excel表單指定表格索引名轉換成list
     * 
     * @param sheetName 表格索引名
     * @param is 輸入流
     * @return 轉換後集合
     */
    public List<T> importExcel(String sheetName, InputStream is) throws Exception
    {
        this.type = Type.IMPORT;
        this.wb = WorkbookFactory.create(is);
        List<T> list = new ArrayList<T>();
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName))
        {
            // 如果指定sheet名,則取指定sheet中的內容.
            sheet = wb.getSheet(sheetName);
        }
        else
        {
            // 如果傳入的sheet名不存在則預設指向第1個sheet.
            sheet = wb.getSheetAt(0);
        }

        if (sheet == null)
        {
            throw new IOException("檔案sheet不存在");
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if (rows > 0)
        {
            // 定義一個map用於存放excel列的序號和field.
            Map<String, Integer> cellMap = new HashMap<String, Integer>();
            // 獲取表頭
            Row heard = sheet.getRow(0);
            for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++)
            {
                Cell cell = heard.getCell(i);
                if (StringUtils.isNotNull(cell != null))
                {
                    String value = this.getCellValue(heard, i).toString();
                    cellMap.put(value, i);
                }
                else
                {
                    cellMap.put(null, i);
                }
            }
            // 有資料時才處理 得到類的所有field.
            Field[] allFields = clazz.getDeclaredFields();
            // 定義一個map用於存放列的序號和field.
            Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
            for (int col = 0; col < allFields.length; col++)
            {
                Field field = allFields[col];
                Excel attr = field.getAnnotation(Excel.class);
                if (attr != null && (attr.type() == Type.ALL || attr.type() == type))
                {
                    // 設定類的私有欄位屬性可訪問.
                    field.setAccessible(true);
                    Integer column = cellMap.get(attr.name());
                    fieldsMap.put(column, field);
                }
            }
            for (int i = 1; i < rows; i++)
            {
                // 從第2行開始取資料,預設第一行是表頭.
                Row row = sheet.getRow(i);
                T entity = null;
                for (Map.Entry<Integer, Field> entry : fieldsMap.entrySet())
                {
                    Object val = this.getCellValue(row, entry.getKey());

                    // 如果不存在例項則新建.
                    entity = (entity == null ? clazz.newInstance() : entity);
                    // 從map中得到對應列的field.
                    Field field = fieldsMap.get(entry.getKey());
                    // 取得型別,並根據物件型別設定值.
                    Class<?> fieldType = field.getType();
                    if (String.class == fieldType)
                    {
                        String s = Convert.toStr(val);
                        if (StringUtils.endsWith(s, ".0"))
                        {
                            val = StringUtils.substringBefore(s, ".0");
                        }
                        else
                        {
                            val = Convert.toStr(val);
                        }
                    }
                    else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType))
                    {
                        val = Convert.toInt(val);
                    }
                    else if ((Long.TYPE == fieldType) || (Long.class == fieldType))
                    {
                        val = Convert.toLong(val);
                    }
                    else if ((Double.TYPE == fieldType) || (Double.class == fieldType))
                    {
                        val = Convert.toDouble(val);
                    }
                    else if ((Float.TYPE == fieldType) || (Float.class == fieldType))
                    {
                        val = Convert.toFloat(val);
                    }
                    else if (BigDecimal.class == fieldType)
                    {
                        val = Convert.toBigDecimal(val);
                    }
                    else if (Date.class == fieldType)
                    {
                        if (val instanceof String)
                        {
                            val = DateUtils.parseDate(val);
                        }
                        else if (val instanceof Double)
                        {
                            val = DateUtil.getJavaDate((Double) val);
                        }
                    }
                    if (StringUtils.isNotNull(fieldType))
                    {
                        Excel attr = field.getAnnotation(Excel.class);
                        String propertyName = field.getName();
                        if (StringUtils.isNotEmpty(attr.targetAttr()))
                        {
                            propertyName = field.getName() + "." + attr.targetAttr();
                        }
                        else if (StringUtils.isNotEmpty(attr.readConverterExp()))
                        {
                            val = reverseByExp(String.valueOf(val), attr.readConverterExp());
                        }
                        ReflectUtils.invokeSetter(entity, propertyName, val);
                    }
                }
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 對list資料來源將其裡面的資料匯入到excel表單
     * 
     * @param list 匯出資料集合
     * @param sheetName 工作表的名稱
     * @return 結果
     */
    public AjaxResult exportExcel(List<T> list, String sheetName)
    {
        this.init(list, sheetName, Type.EXPORT);
        return exportExcel();
    }

    /**
     * 對list資料來源將其裡面的資料匯入到excel表單
     * 
     * @param sheetName 工作表的名稱
     * @return 結果
     */
    public AjaxResult importTemplateExcel(String sheetName)
    {
        this.init(null, sheetName, Type.IMPORT);
        return exportExcel();
    }

    /**
     * 對list資料來源將其裡面的資料匯入到excel表單
     * 
     * @return 結果
     */
    public AjaxResult exportExcel()
    {
        OutputStream out = null;
        try
        {
            // 取出一共有多少個sheet.
            double sheetNo = Math.ceil(list.size() / sheetSize);
            for (int index = 0; index <= sheetNo; index++)
            {
                createSheet(sheetNo, index);

                // 產生一行
                Row row = sheet.createRow(0);
                int column = 0;
                // 寫入各個欄位的列頭名稱
                for (Object[] os : fields)
                {
                    Excel excel = (Excel) os[1];
                    this.createCell(excel, row, column++);
                }
                if (Type.EXPORT.equals(type))
                {
                    fillExcelData(index, row);
                }
            }
            String filename = encodingFilename(sheetName);
            out = new FileOutputStream(getAbsoluteFile(filename));
            wb.write(out);
            return AjaxResult.success(filename);
        }
        catch (Exception e)
        {
            log.error("匯出Excel異常{}", e.getMessage());
            throw new CustomException("匯出Excel失敗，請聯絡網站管理員！");
        }
        finally
        {
            if (wb != null)
            {
                try
                {
                    wb.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 填充excel資料
     * 
     * @param index 序號
     * @param row 單元格行
     */
    public void fillExcelData(int index, Row row)
    {
        int startNo = index * sheetSize;
        int endNo = Math.min(startNo + sheetSize, list.size());
        for (int i = startNo; i < endNo; i++)
        {
            row = sheet.createRow(i + 1 - startNo);
            // 得到匯出物件.
            T vo = (T) list.get(i);
            int column = 0;
            for (Object[] os : fields)
            {
                Field field = (Field) os[0];
                Excel excel = (Excel) os[1];
                // 設定實體類私有屬性可訪問
                field.setAccessible(true);
                this.addCell(excel, row, vo, field, column++);
            }
        }
    }

    /**
     * 建立表格樣式
     * 
     * @param wb 工作薄物件
     * @return 樣式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb)
    {
        // 寫入各條記錄,每條記錄對應excel表中的一行
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        return styles;
    }

    /**
     * 建立單元格
     */
    public Cell createCell(Excel attr, Row row, int column)
    {
        // 建立列
        Cell cell = row.createCell(column);
        // 寫入列資訊
        cell.setCellValue(attr.name());
        setDataValidation(attr, row, column);
        cell.setCellStyle(styles.get("header"));
        return cell;
    }

    /**
     * 設定單元格資訊
     * 
     * @param value 單元格值
     * @param attr 註解相關
     * @param cell 單元格資訊
     */
    public void setCellVo(Object value, Excel attr, Cell cell)
    {
        if (ColumnType.STRING == attr.cellType())
        {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(StringUtils.isNull(value) ? attr.defaultValue() : value + attr.suffix());
        }
        else if (ColumnType.NUMERIC == attr.cellType())
        {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(Integer.parseInt(value + ""));
        }
    }

    /**
     * 建立表格樣式
     */
    public void setDataValidation(Excel attr, Row row, int column)
    {
        if (attr.name().indexOf("注：") >= 0)
        {
            sheet.setColumnWidth(column, 6000);
        }
        else
        {
            // 設定列寬
            sheet.setColumnWidth(column, (int) ((attr.width() + 0.72) * 256));
            row.setHeight((short) (attr.height() * 20));
        }
        // 如果設定了提示資訊則滑鼠放上去提示.
        if (StringUtils.isNotEmpty(attr.prompt()))
        {
            // 這裡預設設了2-101列提示.
            setXSSFPrompt(sheet, "", attr.prompt(), 1, 100, column, column);
        }
        // 如果設定了combo屬性則本列只能選擇不能輸入
        if (attr.combo().length > 0)
        {
            // 這裡預設設了2-101列只能選擇不能輸入.
            setXSSFValidation(sheet, attr.combo(), 1, 100, column, column);
        }
    }

    /**
     * 新增單元格
     */
    public Cell addCell(Excel attr, Row row, T vo, Field field, int column)
    {
        Cell cell = null;
        try
        {
            // 設定行高
            row.setHeight((short) (attr.height() * 20));
            // 根據Excel中設定情況決定是否匯出,有些情況需要保持為空,希望使用者填寫這一列.
            if (attr.isExport())
            {
                // 建立cell
                cell = row.createCell(column);
                cell.setCellStyle(styles.get("data"));

                // 用於讀取物件中的屬性
                Object value = getTargetValue(vo, field, attr);
                String dateFormat = attr.dateFormat();
                String readConverterExp = attr.readConverterExp();
                if (StringUtils.isNotEmpty(dateFormat) && StringUtils.isNotNull(value))
                {
                    cell.setCellValue(DateUtils.parseDateToStr(dateFormat, (Date) value));
                }
                else if (StringUtils.isNotEmpty(readConverterExp) && StringUtils.isNotNull(value))
                {
                    cell.setCellValue(convertByExp(String.valueOf(value), readConverterExp));
                }
                else
                {
                    // 設定列型別
                    setCellVo(value, attr, cell);
                }
            }
        }
        catch (Exception e)
        {
            log.error("匯出Excel失敗{}", e);
        }
        return cell;
    }

    /**
     * 設定 POI XSSFSheet 單元格提示
     * 
     * @param sheet 表單
     * @param promptTitle 提示標題
     * @param promptContent 提示內容
     * @param firstRow 開始行
     * @param endRow 結束行
     * @param firstCol 開始列
     * @param endCol 結束列
     */
    public void setXSSFPrompt(Sheet sheet, String promptTitle, String promptContent, int firstRow, int endRow,
            int firstCol, int endCol)
    {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createCustomConstraint("DD1");
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        dataValidation.createPromptBox(promptTitle, promptContent);
        dataValidation.setShowPromptBox(true);
        sheet.addValidationData(dataValidation);
    }

    /**
     * 設定某些列的值只能輸入預製的資料,顯示下拉框.
     * 
     * @param sheet 要設定的sheet.
     * @param textlist 下拉框顯示的內容
     * @param firstRow 開始行
     * @param endRow 結束行
     * @param firstCol 開始列
     * @param endCol 結束列
     * @return 設定好的sheet.
     */
    public void setXSSFValidation(Sheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol)
    {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 載入下拉列表內容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textlist);
        // 設定資料有效性載入在哪個單元格上,四個引數分別是：起始行、終止行、起始列、終止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 資料有效性物件
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        // 處理Excel相容性問題
        if (dataValidation instanceof XSSFDataValidation)
        {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        }
        else
        {
            dataValidation.setSuppressDropDownArrow(false);
        }

        sheet.addValidationData(dataValidation);
    }

    /**
     * 解析匯出值 0=男,1=女,2=未知
     * 
     * @param propertyValue 引數值
     * @param converterExp 翻譯註解
     * @return 解析後值
     * @throws Exception
     */
    public static String convertByExp(String propertyValue, String converterExp) throws Exception
    {
        try
        {
            String[] convertSource = converterExp.split(",");
            for (String item : convertSource)
            {
                String[] itemArray = item.split("=");
                if (itemArray[0].equals(propertyValue))
                {
                    return itemArray[1];
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return propertyValue;
    }

    /**
     * 反向解析值 男=0,女=1,未知=2
     * 
     * @param propertyValue 引數值
     * @param converterExp 翻譯註解
     * @return 解析後值
     * @throws Exception
     */
    public static String reverseByExp(String propertyValue, String converterExp) throws Exception
    {
        try
        {
            String[] convertSource = converterExp.split(",");
            for (String item : convertSource)
            {
                String[] itemArray = item.split("=");
                if (itemArray[1].equals(propertyValue))
                {
                    return itemArray[0];
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return propertyValue;
    }

    /**
     * 編碼檔名
     */
    public String encodingFilename(String filename)
    {
        filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        return filename;
    }

    /**
     * 獲取下載路徑
     * 
     * @param filename 檔名稱
     */
    public String getAbsoluteFile(String filename)
    {
        String downloadPath = RuoYiConfig.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    /**
     * 獲取bean中的屬性值
     * 
     * @param vo 實體物件
     * @param field 欄位
     * @param excel 註解
     * @return 最終的屬性值
     * @throws Exception
     */
    private Object getTargetValue(T vo, Field field, Excel excel) throws Exception
    {
        Object o = field.get(vo);
        if (StringUtils.isNotEmpty(excel.targetAttr()))
        {
            String target = excel.targetAttr();
            if (target.indexOf(".") > -1)
            {
                String[] targets = target.split("[.]");
                for (String name : targets)
                {
                    o = getValue(o, name);
                }
            }
            else
            {
                o = getValue(o, target);
            }
        }
        return o;
    }

    /**
     * 以類的屬性的get方法方法形式獲取值
     * 
     * @param o
     * @param name
     * @return value
     * @throws Exception
     */
    private Object getValue(Object o, String name) throws Exception
    {
        if (StringUtils.isNotEmpty(name))
        {
            Class<?> clazz = o.getClass();
            String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            Method method = clazz.getMethod(methodName);
            o = method.invoke(o);
        }
        return o;
    }

    /**
     * 得到所有定義欄位
     */
    private void createExcelField()
    {
        this.fields = new ArrayList<Object[]>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : tempFields)
        {
            // 單註解
            if (field.isAnnotationPresent(Excel.class))
            {
                putToField(field, field.getAnnotation(Excel.class));
            }

            // 多註解
            if (field.isAnnotationPresent(Excels.class))
            {
                Excels attrs = field.getAnnotation(Excels.class);
                Excel[] excels = attrs.value();
                for (Excel excel : excels)
                {
                    putToField(field, excel);
                }
            }
        }
    }

    /**
     * 放到欄位集合中
     */
    private void putToField(Field field, Excel attr)
    {
        if (attr != null && (attr.type() == Type.ALL || attr.type() == type))
        {
            this.fields.add(new Object[] { field, attr });
        }
    }

    /**
     * 建立一個工作簿
     */
    public void createWorkbook()
    {
        this.wb = new SXSSFWorkbook(500);
    }

    /**
     * 建立工作表
     * 
     * @param sheetNo sheet數量
     * @param index 序號
     */
    public void createSheet(double sheetNo, int index)
    {
        this.sheet = wb.createSheet();
        this.styles = createStyles(wb);
        // 設定工作表的名稱.
        if (sheetNo == 0)
        {
            wb.setSheetName(index, sheetName);
        }
        else
        {
            wb.setSheetName(index, sheetName + index);
        }
    }

    /**
     * 獲取單元格值
     * 
     * @param row 獲取的行
     * @param column 獲取單元格列號
     * @return 單元格值
     */
    public Object getCellValue(Row row, int column)
    {
        if (row == null)
        {
            return row;
        }
        Object val = "";
        try
        {
            Cell cell = row.getCell(column);
            if (cell != null)
            {
                if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA)
                {
                    val = cell.getNumericCellValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell))
                    {
                        val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式轉換
                    }
                    else
                    {
                        if ((Double) val % 1 > 0)
                        {
                            val = new DecimalFormat("0.00").format(val);
                        }
                        else
                        {
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                }
                else if (cell.getCellTypeEnum() == CellType.STRING)
                {
                    val = cell.getStringCellValue();
                }
                else if (cell.getCellTypeEnum() == CellType.BOOLEAN)
                {
                    val = cell.getBooleanCellValue();
                }
                else if (cell.getCellTypeEnum() == CellType.ERROR)
                {
                    val = cell.getErrorCellValue();
                }

            }
        }
        catch (Exception e)
        {
            return val;
        }
        return val;
    }
}