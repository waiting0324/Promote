<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="任務名稱" prop="jobName">
        <el-input
          v-model="queryParams.jobName"
          placeholder="請輸入任務名稱"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任務組名" prop="jobGroup">
        <el-select
          v-model="queryParams.jobGroup"
          placeholder="請任務組名"
          clearable
          size="small"
          style="width: 240px"
        >
          <el-option
            v-for="dict in jobGroupOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="執行狀態" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="請選擇執行狀態"
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
      <el-form-item label="執行時間">
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
          v-hasPermi="['monitor:job:remove']"
        >刪除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          @click="handleClean"
          v-hasPermi="['monitor:job:remove']"
        >清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['monitor:jobLog:export']"
        >匯出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="jobLogList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="日誌編號" width="80" align="center" prop="jobLogId" />
      <el-table-column label="任務名稱" align="center" prop="jobName" :show-overflow-tooltip="true" />
      <el-table-column label="任務組名" align="center" prop="jobGroup" :formatter="jobGroupFormat" :show-overflow-tooltip="true" />
      <el-table-column label="呼叫目標字串" align="center" prop="invokeTarget" :show-overflow-tooltip="true" />
      <el-table-column label="日誌資訊" align="center" prop="jobMessage" :show-overflow-tooltip="true" />
      <el-table-column label="執行狀態" align="center" prop="status" :formatter="statusFormat" />
      <el-table-column label="執行時間" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['monitor:job:query']"
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

    <!-- 排程日誌詳細 -->
    <el-dialog title="排程日誌詳細" :visible.sync="open" width="700px">
      <el-form ref="form" :model="form" label-width="100px" size="mini">
        <el-row>
          <el-col :span="12">
            <el-form-item label="日誌序號：">{{ form.jobLogId }}</el-form-item>
            <el-form-item label="任務名稱：">{{ form.jobName }}</el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任務分組：">{{ form.jobGroup }}</el-form-item>
            <el-form-item label="執行時間：">{{ form.createTime }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="呼叫方法：">{{ form.invokeTarget }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="日誌資訊：">{{ form.jobMessage }}</el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="執行狀態：">
              <div v-if="form.status == 0">正常</div>
              <div v-else-if="form.status == 1">失敗</div>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="異常資訊：" v-if="form.status == 1">{{ form.exceptionInfo }}</el-form-item>
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
import { listJobLog, delJobLog, exportJobLog, cleanJobLog } from "@/api/monitor/jobLog";

export default {
  name: "JobLog",
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
      // 排程日誌表格資料
      jobLogList: [],
      // 是否顯示彈出層
      open: false,
      // 日期範圍
      dateRange: [],
      // 表單引數
      form: {},
      // 執行狀態字典
      statusOptions: [],
      // 任務組名字典
      jobGroupOptions: [],
      // 查詢引數
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        jobName: undefined,
        jobGroup: undefined,
        status: undefined
      },
      // 表單引數
      form: {}
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_job_status").then(response => {
      this.statusOptions = response.data;
    });
    this.getDicts("sys_job_group").then(response => {
      this.jobGroupOptions = response.data;
    });
  },
  methods: {
    /** 查詢排程日誌列表 */
    getList() {
      this.loading = true;
      listJobLog(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.jobLogList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 執行狀態字典翻譯
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 任務組名字典翻譯
    jobGroupFormat(row, column) {
      return this.selectDictLabel(this.jobGroupOptions, row.jobGroup);
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
      this.ids = selection.map(item => item.jobLogId);
      this.multiple = !selection.length;
    },
    /** 詳細按鈕操作 */
    handleView(row) {
      this.open = true;
      this.form = row;
    },
    /** 刪除按鈕操作 */
    handleDelete(row) {
      const jobLogIds = this.ids;
      this.$confirm('是否確認刪除排程日誌編號為"' + jobLogIds + '"的資料項?', "警告", {
          confirmButtonText: "確定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delJobLog(jobLogIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("刪除成功");
        }).catch(function() {});
    },
    /** 清空按鈕操作 */
    handleClean() {
      this.$confirm("是否確認清空所有排程日誌資料項?", "警告", {
          confirmButtonText: "確定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return cleanJobLog();
        }).then(() => {
          this.getList();
          this.msgSuccess("清空成功");
        }).catch(function() {});
    },
    /** 匯出按鈕操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm("是否確認匯出所有排程日誌資料項?", "警告", {
          confirmButtonText: "確定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportJobLog(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>