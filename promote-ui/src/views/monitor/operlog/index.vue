<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="系統模組" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="請輸入系統模組"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作人員" prop="operName">
        <el-input
          v-model="queryParams.operName"
          placeholder="請輸入操作人員"
          clearable
          style="width: 240px;"
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="型別" prop="businessType">
        <el-select
          v-model="queryParams.businessType"
          placeholder="操作型別"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="狀態" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="操作狀態"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="操作時間">
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
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['monitor:operlog:remove']"
        >刪除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          @click="handleClean"
          v-hasPermi="['monitor:operlog:remove']"
        >清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:config:export']"
        >匯出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日誌編號" align="center" prop="operId" />
      <el-table-column label="系統模組" align="center" prop="title" />
      <el-table-column label="操作型別" align="center" prop="businessType" :formatter="typeFormat" />
      <el-table-column label="請求方式" align="center" prop="requestMethod" />
      <el-table-column label="操作人員" align="center" prop="operName" />
      <el-table-column label="主機" align="center" prop="operIp" width="130" :show-overflow-tooltip="true" />
      <el-table-column label="操作地點" align="center" prop="operLocation" />
      <el-table-column label="操作狀態" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="操作日期" align="center" prop="operTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.operTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row,scope.index)"
            v-hasPermi="['monitor:operlog:query']"
          >詳細</el-button>
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

    <!-- 操作日誌詳細 -->
    <el-dialog title="操作日誌詳細" :visible.sync="open" width="700px">
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="操作模組：">{{ form.title }} / {{ typeFormat(form) }}</el-form-item>
            <el-form-item
              label="登入資訊："
            >{{ form.operName }} / {{ form.operIp }} / {{ form.operLocation }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="請求地址：">{{ form.operUrl }}</el-form-item>
            <el-form-item label="請求方式：">{{ form.requestMethod }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="操作方法：">{{ form.method }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="請求引數：">{{ form.operParam }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="返回引數：">{{ form.jsonResult }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作狀態：">
              <div v-if="form.status === 0">正常</div>
              <div v-else-if="form.status === 1">失敗</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作時間：">{{ parseTime(form.operTime) }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="異常資訊：" v-if="form.status === 1">{{ form.errorMsg }}</el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">關 閉</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { list, delOperlog, cleanOperlog, exportOperlog } from "@/api/monitor/operlog";

export default {
  name: "Operlog",
  data() {
    return {
      // 遮罩層
      loading: true,
      // 選中陣列
      ids: [],
      // 非多個禁用
      multiple: true,
      // 總條數
      total: 0,
      // 表格資料
      list: [],
      // 是否顯示彈出層
      open: false,
      // 型別資料字典
      typeOptions: [],
      // 型別資料字典
      statusOptions: [],
      // 日期範圍
      dateRange: [],
      // 表單引數
      form: {},
      // 查詢引數
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        operName: undefined,
        businessType: undefined,
        status: undefined
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_oper_type").then(response => {
      this.typeOptions = response.data;
    });
    this.getDicts("sys_common_status").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查詢登入日誌 */
    getList() {
      this.loading = true;
      list(this.addDateRange(this.queryParams, this.dateRange)).then( response => {
          this.list = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 操作日誌狀態字典翻譯
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 操作日誌型別字典翻譯
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.businessType);
    },
    /** 搜尋按鈕操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按鈕操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多選框選中資料
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.operId)
      this.multiple = !selection.length
    },
    /** 詳細按鈕操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    /** 刪除按鈕操作 */
    handleDelete(row) {
      const operIds = row.operId || this.ids;
      this.$confirm('是否確認刪除日誌編號為"' + operIds + '"的資料項?', "警告", {
          confirmButtonText: "確定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delOperlog(operIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("刪除成功");
        }).catch(function() {});
    },
    /** 清空按鈕操作 */
    handleClean() {
        this.$confirm('是否確認清空所有操作日誌資料項?', "警告", {
          confirmButtonText: "確定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return cleanOperlog();
        }).then(() => {
          this.getList();
          this.msgSuccess("清空成功");
        }).catch(function() {});
    },
    /** 匯出按鈕操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否確認匯出所有操作日誌資料項?', "警告", {
          confirmButtonText: "確定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportOperlog(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>

