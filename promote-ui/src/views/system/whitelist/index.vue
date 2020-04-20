<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="負責人姓名" prop="owner">
        <el-input
          v-model="queryParams.owner"
          placeholder="請輸入負責人姓名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="商家/旅宿 名稱" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="請輸入商家/旅宿 名稱"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="統編/身分證字號" prop="taxNo">
        <el-input
          v-model="queryParams.taxNo"
          placeholder="請輸入統編/身分證字號"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="旅宿業者預設帳號" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="請輸入旅宿業者預設帳號"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="旅宿業者預設密碼" prop="password">
        <el-input
          v-model="queryParams.password"
          placeholder="請輸入旅宿業者預設密碼"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地址 (帶上郵遞區號)" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="請輸入地址 (帶上郵遞區號)"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="電話" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="請輸入電話"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="電子信箱" prop="email">
        <el-input
          v-model="queryParams.email"
          placeholder="請輸入電子信箱"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="資料類型 (1旅宿業者 2商家)" prop="type">
        <el-select v-model="queryParams.type" placeholder="請選擇資料類型 (1旅宿業者 2商家)" clearable size="small">
          <el-option
            v-for="dict in typeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否為夜市攤商 ( 0否 1是 )" prop="isNMarket">
        <el-input
          v-model="queryParams.isNMarket"
          placeholder="請輸入是否為夜市攤商 ( 0否 1是 )"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否為市場攤商 ( 0否 1是 )" prop="isTMarket">
        <el-input
          v-model="queryParams.isTMarket"
          placeholder="請輸入是否為市場攤商 ( 0否 1是 )"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否為餐飲業 ( 0否 1是 )" prop="isFoodbeverage">
        <el-input
          v-model="queryParams.isFoodbeverage"
          placeholder="請輸入是否為餐飲業 ( 0否 1是 )"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否為藝文產業 ( 0否 1是 )" prop="isCulture">
        <el-input
          v-model="queryParams.isCulture"
          placeholder="請輸入是否為藝文產業 ( 0否 1是 )"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否為觀光工廠 ( 0否 1是 )" prop="isSightseeing">
        <el-input
          v-model="queryParams.isSightseeing"
          placeholder="請輸入是否為觀光工廠 ( 0否 1是 )"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
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
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:whitelist:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:whitelist:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:whitelist:remove']"
        >刪除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:whitelist:export']"
        >匯出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="whitelistList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="流水號" align="center" prop="id" />
      <el-table-column label="負責人姓名" align="center" prop="owner" />
      <el-table-column label="商家/旅宿 名稱" align="center" prop="name" />
      <el-table-column label="統編/身分證字號" align="center" prop="taxNo" />
      <el-table-column label="旅宿業者預設帳號" align="center" prop="username" />
      <el-table-column label="旅宿業者預設密碼" align="center" prop="password" />
      <el-table-column label="地址 (帶上郵遞區號)" align="center" prop="address" />
      <el-table-column label="電話" align="center" prop="phonenumber" />
      <el-table-column label="電子信箱" align="center" prop="email" />
      <el-table-column label="資料類型 (1旅宿業者 2商家)" align="center" prop="type" :formatter="typeFormat" />
      <el-table-column label="是否為夜市攤商 ( 0否 1是 )" align="center" prop="isNMarket" :formatter="isNMarketFormat" />
      <el-table-column label="是否為市場攤商 ( 0否 1是 )" align="center" prop="isTMarket" :formatter="isTMarketFormat" />
      <el-table-column label="是否為餐飲業 ( 0否 1是 )" align="center" prop="isFoodbeverage" :formatter="isFoodbeverageFormat" />
      <el-table-column label="是否為藝文產業 ( 0否 1是 )" align="center" prop="isCulture" :formatter="isCultureFormat" />
      <el-table-column label="是否為觀光工廠 ( 0否 1是 )" align="center" prop="isSightseeing" :formatter="isSightseeingFormat" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:whitelist:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:whitelist:remove']"
          >刪除</el-button>
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

    <!-- 新增或修改白名單對話方塊 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="負責人姓名" prop="owner">
          <el-input v-model="form.owner" placeholder="請輸入負責人姓名" />
        </el-form-item>
        <el-form-item label="商家/旅宿 名稱" prop="name">
          <el-input v-model="form.name" placeholder="請輸入商家/旅宿 名稱" />
        </el-form-item>
        <el-form-item label="統編/身分證字號" prop="taxNo">
          <el-input v-model="form.taxNo" placeholder="請輸入統編/身分證字號" />
        </el-form-item>
        <el-form-item label="旅宿業者預設帳號" prop="username">
          <el-input v-model="form.username" placeholder="請輸入旅宿業者預設帳號" />
        </el-form-item>
        <el-form-item label="旅宿業者預設密碼" prop="password">
          <el-input v-model="form.password" placeholder="請輸入旅宿業者預設密碼" />
        </el-form-item>
        <el-form-item label="地址 (帶上郵遞區號)" prop="address">
          <el-input v-model="form.address" placeholder="請輸入地址 (帶上郵遞區號)" />
        </el-form-item>
        <el-form-item label="電話" prop="phonenumber">
          <el-input v-model="form.phonenumber" placeholder="請輸入電話" />
        </el-form-item>
        <el-form-item label="電子信箱" prop="email">
          <el-input v-model="form.email" placeholder="請輸入電子信箱" />
        </el-form-item>
        <el-form-item label="資料類型 (1旅宿業者 2商家)">
          <el-select v-model="form.type" placeholder="請選擇資料類型 (1旅宿業者 2商家)">
            <el-option
              v-for="dict in typeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否為夜市攤商 ( 0否 1是 )" prop="isNMarket">
          <el-input v-model="form.isNMarket" placeholder="請輸入是否為夜市攤商 ( 0否 1是 )" />
        </el-form-item>
        <el-form-item label="是否為市場攤商 ( 0否 1是 )" prop="isTMarket">
          <el-input v-model="form.isTMarket" placeholder="請輸入是否為市場攤商 ( 0否 1是 )" />
        </el-form-item>
        <el-form-item label="是否為餐飲業 ( 0否 1是 )" prop="isFoodbeverage">
          <el-input v-model="form.isFoodbeverage" placeholder="請輸入是否為餐飲業 ( 0否 1是 )" />
        </el-form-item>
        <el-form-item label="是否為藝文產業 ( 0否 1是 )" prop="isCulture">
          <el-input v-model="form.isCulture" placeholder="請輸入是否為藝文產業 ( 0否 1是 )" />
        </el-form-item>
        <el-form-item label="是否為觀光工廠 ( 0否 1是 )" prop="isSightseeing">
          <el-input v-model="form.isSightseeing" placeholder="請輸入是否為觀光工廠 ( 0否 1是 )" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">確 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listWhitelist, getWhitelist, delWhitelist, addWhitelist, updateWhitelist, exportWhitelist } from "@/api/system/whitelist";

export default {
  name: "Whitelist",
  data() {
    return {
      // 遮罩層
      loading: true,
      // 選中陣列
      ids: [],
      // 非單個禁用
      single: true,
      // 非多個禁用
      multiple: true,
      // 總條數
      total: 0,
      // 白名單表格資料
      whitelistList: [],
      // 彈出層標題
      title: "",
      // 是否顯示彈出層
      open: false,
      // 資料類型 (1旅宿業者 2商家)字典
      typeOptions: [],
      // 是否為夜市攤商 ( 0否 1是 )字典
      isNMarketOptions: [],
      // 是否為市場攤商 ( 0否 1是 )字典
      isTMarketOptions: [],
      // 是否為餐飲業 ( 0否 1是 )字典
      isFoodbeverageOptions: [],
      // 是否為藝文產業 ( 0否 1是 )字典
      isCultureOptions: [],
      // 是否為觀光工廠 ( 0否 1是 )字典
      isSightseeingOptions: [],
      // 查詢引數
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        owner: undefined,
        name: undefined,
        taxNo: undefined,
        username: undefined,
        password: undefined,
        address: undefined,
        phonenumber: undefined,
        email: undefined,
        type: undefined,
        isNMarket: undefined,
        isTMarket: undefined,
        isFoodbeverage: undefined,
        isCulture: undefined,
        isSightseeing: undefined,
      },
      // 表單引數
      form: {},
      // 表單校驗
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("pro_whitelist_type").then(response => {
      this.typeOptions = response.data;
    });
    this.getDicts("sys_yes_no").then(response => {
      this.isNMarketOptions = response.data;
    });
    this.getDicts("sys_yes_no").then(response => {
      this.isTMarketOptions = response.data;
    });
    this.getDicts("sys_yes_no").then(response => {
      this.isFoodbeverageOptions = response.data;
    });
    this.getDicts("sys_yes_no").then(response => {
      this.isCultureOptions = response.data;
    });
    this.getDicts("sys_yes_no").then(response => {
      this.isSightseeingOptions = response.data;
    });
  },
  methods: {
    /** 查詢白名單列表 */
    getList() {
      this.loading = true;
      listWhitelist(this.queryParams).then(response => {
        this.whitelistList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 資料類型 (1旅宿業者 2商家)字典翻譯
    typeFormat(row, column) {
      return this.selectDictLabel(this.typeOptions, row.type);
    },
    // 是否為夜市攤商 ( 0否 1是 )字典翻譯
    isNMarketFormat(row, column) {
      return this.selectDictLabel(this.isNMarketOptions, row.isNMarket);
    },
    // 是否為市場攤商 ( 0否 1是 )字典翻譯
    isTMarketFormat(row, column) {
      return this.selectDictLabel(this.isTMarketOptions, row.isTMarket);
    },
    // 是否為餐飲業 ( 0否 1是 )字典翻譯
    isFoodbeverageFormat(row, column) {
      return this.selectDictLabel(this.isFoodbeverageOptions, row.isFoodbeverage);
    },
    // 是否為藝文產業 ( 0否 1是 )字典翻譯
    isCultureFormat(row, column) {
      return this.selectDictLabel(this.isCultureOptions, row.isCulture);
    },
    // 是否為觀光工廠 ( 0否 1是 )字典翻譯
    isSightseeingFormat(row, column) {
      return this.selectDictLabel(this.isSightseeingOptions, row.isSightseeing);
    },
    // 取消按鈕
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表單重置
    reset() {
      this.form = {
        id: undefined,
        owner: undefined,
        name: undefined,
        taxNo: undefined,
        username: undefined,
        password: undefined,
        address: undefined,
        phonenumber: undefined,
        email: undefined,
        type: undefined,
        isNMarket: undefined,
        isTMarket: undefined,
        isFoodbeverage: undefined,
        isCulture: undefined,
        isSightseeing: undefined,
        updateTime: undefined,
        createTime: undefined
      };
      this.resetForm("form");
    },
    /** 搜尋按鈕操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按鈕操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多選框選中資料
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按鈕操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增白名單";
    },
    /** 修改按鈕操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWhitelist(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改白名單";
      });
    },
    /** 提交按鈕 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateWhitelist(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addWhitelist(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          }
        }
      });
    },
    /** 刪除按鈕操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否確認刪除白名單編號為"' + ids + '"的資料項?', "警告", {
          confirmButtonText: "確定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delWhitelist(ids);
        }).then(() => {
          this.getList();
          this.msgSuccess("刪除成功");
        }).catch(function() {});
    },
    /** 匯出按鈕操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否確認匯出所有白名單資料項?', "警告", {
          confirmButtonText: "確定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportWhitelist(queryParams);
        }).then(response => {
          this.download(response.msg);
        }).catch(function() {});
    }
  }
};
</script>