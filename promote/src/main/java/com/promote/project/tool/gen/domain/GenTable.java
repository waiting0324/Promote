package com.promote.project.tool.gen.domain;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.ArrayUtils;
import com.promote.common.constant.GenConstants;
import com.promote.common.utils.StringUtils;
import com.promote.framework.web.domain.BaseEntity;

/**
 * 業務表 gen_table
 * 
 * @author ruoyi
 */
public class GenTable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 編號 */
    private Long tableId;

    /** 表名稱 */
    @NotBlank(message = "表名稱不能為空")
    private String tableName;

    /** 表描述 */
    @NotBlank(message = "表描述不能為空")
    private String tableComment;

    /** 實體類名稱(首字母大寫) */
    @NotBlank(message = "實體類名稱不能為空")
    private String className;

    /** 使用的模板（crud單表操作 tree樹表操作） */
    private String tplCategory;

    /** 生成包路徑 */
    @NotBlank(message = "生成包路徑不能為空")
    private String packageName;

    /** 生成模組名 */
    @NotBlank(message = "生成模組名不能為空")
    private String moduleName;

    /** 生成業務名 */
    @NotBlank(message = "生成業務名不能為空")
    private String businessName;

    /** 生成功能名 */
    @NotBlank(message = "生成功能名不能為空")
    private String functionName;

    /** 生成作者 */
    @NotBlank(message = "作者不能為空")
    private String functionAuthor;

    /** 主鍵資訊 */
    private GenTableColumn pkColumn;

    /** 表列資訊 */
    @Valid
    private List<GenTableColumn> columns;

    /** 其它生成選項 */
    private String options;

    /** 樹編碼欄位 */
    private String treeCode;

    /** 樹父編碼欄位 */
    private String treeParentCode;

    /** 樹名稱欄位 */
    private String treeName;

    public Long getTableId()
    {
        return tableId;
    }

    public void setTableId(Long tableId)
    {
        this.tableId = tableId;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableComment()
    {
        return tableComment;
    }

    public void setTableComment(String tableComment)
    {
        this.tableComment = tableComment;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getTplCategory()
    {
        return tplCategory;
    }

    public void setTplCategory(String tplCategory)
    {
        this.tplCategory = tplCategory;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public String getModuleName()
    {
        return moduleName;
    }

    public void setModuleName(String moduleName)
    {
        this.moduleName = moduleName;
    }

    public String getBusinessName()
    {
        return businessName;
    }

    public void setBusinessName(String businessName)
    {
        this.businessName = businessName;
    }

    public String getFunctionName()
    {
        return functionName;
    }

    public void setFunctionName(String functionName)
    {
        this.functionName = functionName;
    }

    public String getFunctionAuthor()
    {
        return functionAuthor;
    }

    public void setFunctionAuthor(String functionAuthor)
    {
        this.functionAuthor = functionAuthor;
    }

    public GenTableColumn getPkColumn()
    {
        return pkColumn;
    }

    public void setPkColumn(GenTableColumn pkColumn)
    {
        this.pkColumn = pkColumn;
    }

    public List<GenTableColumn> getColumns()
    {
        return columns;
    }

    public void setColumns(List<GenTableColumn> columns)
    {
        this.columns = columns;
    }

    public String getOptions()
    {
        return options;
    }

    public void setOptions(String options)
    {
        this.options = options;
    }

    public String getTreeCode()
    {
        return treeCode;
    }

    public void setTreeCode(String treeCode)
    {
        this.treeCode = treeCode;
    }

    public String getTreeParentCode()
    {
        return treeParentCode;
    }

    public void setTreeParentCode(String treeParentCode)
    {
        this.treeParentCode = treeParentCode;
    }

    public String getTreeName()
    {
        return treeName;
    }

    public void setTreeName(String treeName)
    {
        this.treeName = treeName;
    }

    public boolean isTree()
    {
        return isTree(this.tplCategory);
    }

    public static boolean isTree(String tplCategory)
    {
        return tplCategory != null && StringUtils.equals(GenConstants.TPL_TREE, tplCategory);
    }

    public boolean isCrud()
    {
        return isCrud(this.tplCategory);
    }

    public static boolean isCrud(String tplCategory)
    {
        return tplCategory != null && StringUtils.equals(GenConstants.TPL_CRUD, tplCategory);
    }

    public boolean isSuperColumn(String javaField)
    {
        return isSuperColumn(this.tplCategory, javaField);
    }

    public static boolean isSuperColumn(String tplCategory, String javaField)
    {
        if (isTree(tplCategory))
        {
            return StringUtils.equalsAnyIgnoreCase(javaField,
                    ArrayUtils.addAll(GenConstants.TREE_ENTITY, GenConstants.BASE_ENTITY));
        }
        return StringUtils.equalsAnyIgnoreCase(javaField, GenConstants.BASE_ENTITY);
    }
}