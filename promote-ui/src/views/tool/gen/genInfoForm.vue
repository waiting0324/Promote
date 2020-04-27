<template>
  <el-form ref="genInfoForm" :model="info" :rules="rules" label-width="150px">
    <el-row>
      <el-col :span="12">
        <el-form-item prop="tplCategory">
          <span slot="label">生成模板</span>
          <el-select v-model="info.tplCategory">
            <el-option label="單表（增刪改查）" value="crud" />
            <el-option label="樹表（增刪改查）" value="tree"/>
          </el-select>
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="packageName">
          <span slot="label">
            生成包路徑
            <el-tooltip content="生成在哪個java包下，例如 com.ruoyi.system" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.packageName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="moduleName">
          <span slot="label">
            生成模組名
            <el-tooltip content="可理解為子系統名，例如 system" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.moduleName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="businessName">
          <span slot="label">
            生成業務名
            <el-tooltip content="可理解為功能英文名，例如 user" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.businessName" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item prop="functionName">
          <span slot="label">
            生成功能名
            <el-tooltip content="用作類描述，例如 使用者" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-input v-model="info.functionName" />
        </el-form-item>
      </el-col>
    </el-row>

    <el-row v-show="info.tplCategory == 'tree'">
      <h4 class="form-header">其他資訊</h4>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            樹編碼欄位
            <el-tooltip content="樹顯示的編碼欄位名， 如：dept_id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeCode" placeholder="請選擇">
            <el-option
              v-for="column in info.columns"
              :key="column.columnName"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            樹父編碼欄位
            <el-tooltip content="樹顯示的父編碼欄位名， 如：parent_Id" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeParentCode" placeholder="請選擇">
            <el-option
              v-for="column in info.columns"
              :key="column.columnName"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item>
          <span slot="label">
            樹名稱欄位
            <el-tooltip content="樹節點的顯示名稱欄位名， 如：dept_name" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </span>
          <el-select v-model="info.treeName" placeholder="請選擇">
            <el-option
              v-for="column in info.columns"
              :key="column.columnName"
              :label="column.columnName + '：' + column.columnComment"
              :value="column.columnName"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script>
export default {
  name: "BasicInfoForm",
  props: {
    info: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      rules: {
        tplCategory: [
          { required: true, message: "請選擇生成模板", trigger: "blur" }
        ],
        packageName: [
          { required: true, message: "請輸入生成包路徑", trigger: "blur" }
        ],
        moduleName: [
          { required: true, message: "請輸入生成模組名", trigger: "blur" }
        ],
        businessName: [
          { required: true, message: "請輸入生成業務名", trigger: "blur" }
        ],
        functionName: [
          { required: true, message: "請輸入生成功能名", trigger: "blur" }
        ]
      }
    };
  },
  created() {}
};
</script>
