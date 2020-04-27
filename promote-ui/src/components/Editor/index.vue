<template>
  <div>
    <!-- 圖片上傳元件輔助 -->
    <el-upload
      class="avatar-uploader quill-img"
      :action="uploadImgUrl"
      name="file"
      :headers="headers"
      :show-file-list="false"
      :on-success="quillImgSuccess"
      :on-error="uploadError"
      :before-upload="quillImgBefore"
      accept='.jpg,.jpeg,.png,.gif'
    ></el-upload>

    <!-- 富文字元件 -->
    <quill-editor
      class="editor"
      v-model="content"
      ref="quillEditor"
      :options="editorOption"
      @blur="onEditorBlur($event)"
      @focus="onEditorFocus($event)"
      @change="onEditorChange($event)"
    ></quill-editor>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'

// 工具欄配置
const toolbarOptions = [
  ["bold", "italic", "underline", "strike"],       // 加粗 斜體 下劃線 刪除線
  ["blockquote", "code-block"],                    // 引用  程式碼塊
  [{ list: "ordered" }, { list: "bullet" }],       // 有序、無序列表
  [{ indent: "-1" }, { indent: "+1" }],            // 縮排
  [{ size: ["small", false, "large", "huge"] }],   // 字型大小
  [{ header: [1, 2, 3, 4, 5, 6, false] }],         // 標題
  [{ color: [] }, { background: [] }],             // 字型顏色、字型背景顏色
  [{ align: [] }],                                 // 對齊方式
  ["clean"],                                       // 清除文字格式
  ["link", "image", "video"]                       // 連結、圖片、視訊
];

import { quillEditor } from "vue-quill-editor";
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";

export default {
  props: {
    /* 編輯器的內容 */
    value: {
      type: String
    },
    /* 圖片大小 */
    maxSize: {
      type: Number,
      default: 4000 //kb
    }
  },
  components: { quillEditor },
  data() {
    return {
      content: this.value,
      uploadImgUrl: "",
      editorOption: {
        placeholder: "",
        theme: "snow", // or 'bubble'
        placeholder: "請輸入內容",
        modules: {
          toolbar: {
            container: toolbarOptions,
            handlers: {
              image: function(value) {
                if (value) {
                  // 觸發input框選擇圖片檔案
                  document.querySelector(".quill-img input").click();
                } else {
                  this.quill.format("image", false);
                }
              }
            }
          }
        }
      },
      uploadImgUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上傳的圖片伺服器地址
      headers: {
        Authorization: 'Bearer ' + getToken()
      }
    };
  },
  watch: {
    value: function() {
      this.content = this.value;
    }
  },
  methods: {
    onEditorBlur() {
      //失去焦點事件
    },
    onEditorFocus() {
      //獲得焦點事件
    },
    onEditorChange() {
      //內容改變事件
      this.$emit("input", this.content);
    },

    // 富文字圖片上傳前
    quillImgBefore(file) {
      let fileType = file.type;
			if(fileType === 'image/jpeg' || fileType === 'image/png'){
				return true;
			}else {
				this.$message.error('請插入圖片型別檔案(jpg/jpeg/png)');
				return false;
			}
    },

    quillImgSuccess(res, file) {
      // res為圖片伺服器返回的資料
      // 獲取富文字元件例項
      let quill = this.$refs.quillEditor.quill;
      // 如果上傳成功
      if (res.code == 200) {
        // 獲取游標所在位置
        let length = quill.getSelection().index;
        // 插入圖片  res.url為伺服器返回的圖片地址
        quill.insertEmbed(length, "image", res.url);
        // 調整游標到最後
        quill.setSelection(length + 1);
      } else {
        this.$message.error("圖片插入失敗");
      }
    },
    // 富文字圖片上傳失敗
    uploadError() {
      // loading動畫消失
      this.$message.error("圖片插入失敗");
    }
  }
};
</script> 

<style>
.editor {
  line-height: normal !important;
  height: 192px;
}
.quill-img {
  display: none;
}
.ql-snow .ql-tooltip[data-mode="link"]::before {
  content: "請輸入連結地址:";
}
.ql-snow .ql-tooltip.ql-editing a.ql-action::after {
  border-right: 0px;
  content: "儲存";
  padding-right: 0px;
}

.ql-snow .ql-tooltip[data-mode="video"]::before {
  content: "請輸入視訊地址:";
}

.ql-snow .ql-picker.ql-size .ql-picker-label::before,
.ql-snow .ql-picker.ql-size .ql-picker-item::before {
  content: "14px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
  content: "10px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
  content: "18px";
}
.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
  content: "32px";
}

.ql-snow .ql-picker.ql-header .ql-picker-label::before,
.ql-snow .ql-picker.ql-header .ql-picker-item::before {
  content: "文字";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
  content: "標題1";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
  content: "標題2";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
  content: "標題3";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
  content: "標題4";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
  content: "標題5";
}
.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
  content: "標題6";
}

.ql-snow .ql-picker.ql-font .ql-picker-label::before,
.ql-snow .ql-picker.ql-font .ql-picker-item::before {
  content: "標準字型";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
  content: "襯線字型";
}
.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
  content: "等寬字型";
}
</style>