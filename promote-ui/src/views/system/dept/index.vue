<template>
  <div class="app-container">
    <el-form :inline="true">
      <el-form-item label="部門名稱">
        <el-input
          v-model="queryParams.deptName"
          placeholder="請輸入部門名稱"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="狀態">
        <el-select v-model="queryParams.status" placeholder="部門狀態" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
        >搜尋</el-button>
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:dept:add']"
        >新增</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="deptList"
      row-key="deptId"
      default-expand-all
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="deptName" label="部門名稱" width="260"></el-table-column>
      <el-table-column prop="orderNum" label="排序" width="200"></el-table-column>
      <el-table-column prop="status" label="狀態" :formatter="statusFormat" width="100"></el-table-column>
      <el-table-column label="建立時間" align="center" prop="createTime" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button 
            size="mini" 
            type="text" 
            icon="el-icon-edit" 
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:dept:edit']"
          >修改</el-button>
          <el-button 
            size="mini" 
            type="text" 
            icon="el-icon-plus" 
            @click="handleAdd(scope.row)"
            v-hasPermi="['system:dept:add']"
          >新增</el-button>
          <el-button
            v-if="scope.row.parentId != 0"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:dept:remove']"
          >刪除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增或修改部門對話方塊 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24" v-if="form.parentId !== 0">
            <el-form-item label="上級部門" prop="parentId">
              <treeselect v-model="form.parentId" :options="deptOptions" :normalizer="normalizer" placeholder="選擇上級部門" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部門名稱" prop="deptName">
              <el-input v-model="form.deptName" placeholder="請輸入部門名稱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="顯示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="負責人" prop="leader">
              <el-input v-model="form.leader" placeholder="請輸入負責人" maxlength="20" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="聯絡電話" prop="phone">
              <el-input v-model="form.phone" placeholder="請輸入聯絡電話" maxlength="11" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="郵箱" prop="email">
              <el-input v-model="form.email" placeholder="請輸入郵箱" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部門狀態">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{dict.dictLabel}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">確 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDept, getDept, delDept, addDept, updateDept } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Dept",
  components: { Treeselect },
  data() {
    return {
      // 遮罩層
      loading: true,
      // 表格樹資料
      deptList: [],
      // 部門樹選項
      deptOptions: [],
      // 彈出層標題
      title: "",
      // 是否顯示彈出層
      open: false,
      // 狀態資料字典
      statusOptions: [],
      // 查詢引數
      queryParams: {
        deptName: undefined,
        status: undefined
      },
      // 表單引數
      form: {},
      // 表單校驗
      rules: {
        parentId: [
          { required: true, message: "上級部門不能為空", trigger: "blur" }
        ],
        deptName: [
          { required: true, message: "部門名稱不能為空", trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: "選單順序不能為空", trigger: "blur" }
        ],
        email: [
          {
            type: "email",
            message: "'請輸入正確的郵箱地址",
            trigger: ["blur", "change"]
          }
        ],
        phone: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "請輸入正確的手機號碼",
            trigger: "blur"
          }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查詢部門列表 */
    getList() {
      this.loading = true;
      listDept(this.queryParams).then(response => {
        this.deptList = this.handleTree(response.data, "deptId");
        this.loading = false;
      });
    },
    /** 轉換部門資料結構 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      };
    },
    /** 查詢部門下拉樹結構 */
    getTreeselect() {
      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
      });
    },
    // 字典狀態字典翻譯
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 取消按鈕
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表單重置
    reset() {
      this.form = {
        deptId: undefined,
        parentId: undefined,
        deptName: undefined,
        orderNum: undefined,
        leader: undefined,
        phone: undefined,
        email: undefined,
        status: "0"
      };
      this.resetForm("form");
    },
    /** 搜尋按鈕操作 */
    handleQuery() {
      this.getList();
    },
    /** 新增按鈕操作 */
    handleAdd(row) {
      this.reset();
      this.getTreeselect();
      if (row != undefined) {
        this.form.parentId = row.deptId;
      }
      this.open = true;
      this.title = "新增部門";
    },
    /** 修改按鈕操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      getDept(row.deptId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改部門";
      });
    },
    /** 提交按鈕 */
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.deptId != undefined) {
            updateDept(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              } else {
                this.msgError(response.msg);
              }
            });
          } else {
            addDept(this.form).then(response => {
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
      this.$confirm('是否確認刪除名稱為"' + row.deptName + '"的資料項?', "警告", {
          confirmButtonText: "確定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDept(row.deptId);
        }).then(() => {
          this.getList();
          this.msgSuccess("刪除成功");
        }).catch(function() {});
    }
  }
};
</script>