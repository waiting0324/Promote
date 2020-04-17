<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="表名稱" prop="tableName">
        <el-input
          v-model="queryParams.tableName"
          placeholder="請輸入表名稱"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表描述" prop="tableComment">
        <el-input
          v-model="queryParams.tableComment"
          placeholder="請輸入表描述"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="建立時間">
        <el-date-picker
          v-model="dateRange"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="開始日期"
          end-placeholder="結束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜尋</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-download"
          size="mini"
          @click="handleGenTable"
          v-hasPermi="['tool:gen:code']"
        >生成</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          icon="el-icon-upload"
          size="mini"
          @click="openImportTable"
          v-hasPermi="['tool:gen:import']"
        >匯入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleEditTable"
          v-hasPermi="['tool:gen:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['tool:gen:remove']"
        >刪除</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tableList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="序號" align="center" prop="tableId" width="50px" />
      <el-table-column
        label="表名稱"
        align="center"
        prop="tableName"
        :show-overflow-tooltip="true"
        width="130"
      />
      <el-table-column
        label="表描述"
        align="center"
        prop="tableComment"
        :show-overflow-tooltip="true"
        width="130"
      />
      <el-table-column
        label="實體"
        align="center"
        prop="className"
        :show-overflow-tooltip="true"
        width="130"
      />
      <el-table-column label="建立時間" align="center" prop="createTime" width="160" />
      <el-table-column label="更新時間" align="center" prop="updateTime" width="160" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            icon="el-icon-view"
            @click="handlePreview(scope.row)"
            v-hasPermi="['tool:gen:preview']"
          >預覽</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-edit"
            @click="handleEditTable(scope.row)"
            v-hasPermi="['tool:gen:edit']"
          >編輯</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['tool:gen:remove']"
          >刪除</el-button>
          <el-button
            type="text"
            size="small"
            icon="el-icon-download"
            @click="handleGenTable(scope.row)"
            v-hasPermi="['tool:gen:code']"
          >生成程式碼</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!-- 預覽介面 -->
    <el-dialog :title="preview.title" :visible.sync="preview.open" width="80%" top="5vh">
      <el-tabs v-model="preview.activeName">
        <el-tab-pane
          v-for="(value, key) in preview.data"
          :label="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
          :name="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
          :key="key"
        >
          <pre>{{ value }}</pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <import-table ref="import" @ok="handleQuery" />
  </div>
</template>

<script>
import { listTable, previewTable, delTable } from "@/api/tool/gen";
import importTable from "./importTable";
import { downLoadZip } from "@/utils/zipdownload";
export default {
  name: "Gen",
  components: { importTable },
  data() {
    return {
      // 遮罩層
      loading: true,
      // 唯一識別符號
      uniqueId: "",
      // 選中陣列
      ids: [],
      // 選中表陣列
      tableNames: [],
      // 非單個禁用
      single: true,
      // 非多個禁用
      multiple: true,
      // 總條數
      total: 0,
      // 表資料
      tableList: [],
      // 日期範圍
      dateRange: "",
      // 查詢引數
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tableName: undefined,
        tableComment: undefined
      },
      // 預覽引數
      preview: {
        open: false,
        title: "程式碼預覽",
        data: {},
        activeName: "domain.java"
      }
    };
  },
  created() {
    this.getList();
  },
  activated() {
    const time = this.$route.query.t;
    if (time != null && time != this.uniqueId) {
      this.uniqueId = time;
      this.resetQuery();
    }
  },
  methods: {
    /** 查詢表集合 */
    getList() {
      this.loading = true;
      listTable(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.tableList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 搜尋按鈕操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 生成程式碼操作 */
    handleGenTable(row) {
      const tableNames = row.tableName || this.tableNames;
      if (tableNames == "") {
        this.msgError("請選擇要生成的資料");
        return;
      }
      downLoadZip("/tool/gen/batchGenCode?tables=" + tableNames, "ruoyi");
    },
    /** 開啟匯入表彈窗 */
    openImportTable() {
      this.$refs.import.show();
    },
    /** 重置按鈕操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 預覽按鈕 */
    handlePreview(row) {
      previewTable(row.tableId).then(response => {
        this.preview.data = response.data;
        this.preview.open = true;
      });
    },
    // 多選框選中資料
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.tableId);
      this.tableNames = selection.map(item => item.tableName);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    /** 修改按鈕操作 */
    handleEditTable(row) {
      const tableId = row.tableId || this.ids[0];
      this.$router.push({ path: "/gen/edit", query: { tableId: tableId } });
    },
    /** 刪除按鈕操作 */
    handleDelete(row) {
      const tableIds = row.tableId || this.ids;
      this.$confirm('是否確認刪除表編號為"' + tableIds + '"的資料項?', "警告", {
        confirmButtonText: "確定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
          return delTable(tableIds);
      }).then(() => {
          this.getList();
          this.msgSuccess("刪除成功");
      }).catch(function() {});
    }
  }
};
</script>