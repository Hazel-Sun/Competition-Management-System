<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="70px">
      <el-form-item label="文件名" prop="fileName">
        <el-input
          v-model="queryParams.fileName"
          placeholder="请输入文件名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文件类型" prop="fileType">
        <el-select v-model="queryParams.fileType" placeholder="请选择文件类型" clearable>
          <el-option
            v-for="dict in dict.type.sys_file_info_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="参赛队伍" prop="squadId">
        <el-select
          v-model="queryParams.squadId"
          placeholder="请选择参赛队伍"
          clearable
        >
          <el-option
            v-for="item in squadOptions"
            :key="item.squadId"
            :label="item.squadName"
            :value="item.squadId" />
        </el-select>

      </el-form-item>
      <el-form-item label="参与竞赛" prop="comp.compName">
        <el-select v-model="queryParams.compId"
                   placeholder="请选择竞赛"
                   clearable>
          <el-option
            v-for="item in compOptions"
            :key="item.compId"
            :label="item.compName"
            :value="item.compId" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:processinfo:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:processinfo:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:processinfo:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:processinfo:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="processinfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="文件id" align="center" prop="fileId" />
      <el-table-column label="文件名" align="center" prop="fileName" />
      <el-table-column label="文件路径" align="center" prop="filePath" />
      <el-table-column label="文件类型" align="center" prop="fileType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_file_info_type" :value="scope.row.fileType"/>
        </template>
      </el-table-column>
      <el-table-column label="参赛队伍" align="center" key="squadName" prop="squad.squadName" />
      <el-table-column label="参与竞赛" align="center" key="compName" prop="comp.compName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:processinfo:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:processinfo:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleDownload(scope.row)"
          >下载</el-button>
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

    <!-- 添加或修改过程文件管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文件上传" prop="filePath">
          <file-upload v-model="form.filePath"/>
        </el-form-item>
        <el-form-item label="文件类型" prop="fileType">
          <el-select v-model="form.fileType" placeholder="请选择文件类型">
            <el-option
              v-for="dict in dict.type.sys_file_info_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="参赛队伍" prop="squadId">
              <el-select v-model="form.squadId" placeholder="请选择小队" >
                <el-option
                  v-for="item in squadOptions"
                  :key="item.squadId"
                  :label="item.squadName"
                  :value="item.squadId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="竞赛名称">
              <el-select v-model="form.compId" placeholder="请选择竞赛">
                <el-option
                  v-for="item in compOptions"
                  :key="item.compId"
                  :label="item.compName"
                  :value="item.compId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProcessinfo, getProcessinfo, delProcessinfo, addProcessinfo, updateProcessinfo,compListSelect,squadListSelect } from "@/api/system/processinfo";

export default {
  name: "Processinfo",
  dicts: ['sys_file_info_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 过程文件管理表格数据
      processinfoList: [],
      compOptions: undefined,
      compName:undefined,
      compId:undefined,
      // 小队选项
      squadId:undefined,
      squadOptions:undefined,
      squadName:undefined,
      squadLeader:undefined,
      squadTeacher:undefined,
      squadMember:undefined,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fileName: null,
        filePath: null,
        fileType: null,
        squadId: null,
        compId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
       filePath: [
          { required: true, message: "文件不能为空", trigger: "blur" }
        ],
        squadId: [
          { required: true, message: "参赛队伍不能为空", trigger: "blur" }
        ],
        compId: [
          { required: true, message: "参与竞赛不能为空", trigger: "blur" }
        ],
        fileType: [
          { required: true, message: "文件类型不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getCompList();
    this.getSquadList();
  },
  methods: {
    /** 查询过程文件管理列表 */
    getList() {
      this.loading = true;
      listProcessinfo(this.queryParams).then(response => {
        this.processinfoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getCompList()
    {
      compListSelect().then(response => {
        this.compOptions = response.data;
      });
    },
    getSquadList()
    {
      squadListSelect().then(response => {
        this.squadOptions = response.data;

      } );
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        fileId: null,
        fileName: null,
        filePath: null,
        fileType: null,
        createBy: null,
        createTime: null,
        squadId: null,
        compId: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.fileId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加过程文件管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const fileId = row.fileId || this.ids
      getProcessinfo(fileId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改过程文件管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.fileId != null) {
            updateProcessinfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProcessinfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const fileIds = row.fileId || this.ids;
      this.$modal.confirm('是否确认删除过程文件管理编号为"' + fileIds + '"的数据项？').then(function() {
        return delProcessinfo(fileIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    // 文件下载处理
    handleDownload(row) {
      var name = row.fileName;
      var url = row.filePath;
      var suffix = url.substring(url.lastIndexOf("."), url.length);
      const a = document.createElement('a')
      a.setAttribute('download', name + suffix)
      a.setAttribute('target', '_blank')
      a.setAttribute('href', url)
      a.click()
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/processinfo/export', {
        ...this.queryParams
      }, `processinfo_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
