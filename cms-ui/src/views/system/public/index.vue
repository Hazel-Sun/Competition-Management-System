<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="70px">
      <el-form-item label="宣传标题" prop="publicTitle">
        <el-input
          v-model="queryParams.publicTitle"
          placeholder="请输入宣传标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['system:public:add']"
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
          v-hasPermi="['system:public:edit']"
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
          v-hasPermi="['system:public:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:public:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="publicList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="宣传id" align="center" prop="publicId" />
      <el-table-column label="宣传标题" align="center" prop="publicTitle" />
      <el-table-column label="宣传图" align="center" prop="publicPic" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.publicPic" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="宣传内容" align="center" prop="publicContent" />
      <el-table-column label="宣传状态" align="center" prop="publicStatus" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_public_status" :value="scope.row.publicStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="竞赛" align="center" key="compName" prop="comp.compName" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:public:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:public:remove']"
          >删除</el-button>
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

    <!-- 添加或修改赛事宣传对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="宣传标题" prop="publicTitle">
          <el-input v-model="form.publicTitle" placeholder="请输入宣传标题" />
        </el-form-item>
        <el-form-item label="宣传图" prop="publicPic">
          <image-upload v-model="form.publicPic"/>
        </el-form-item>
        <el-form-item label="宣传内容">
          <editor v-model="form.publicContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="宣传状态">
          <el-radio-group v-model="form.publicStatus">
            <el-radio
              v-for="dict in dict.type.sys_public_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-row>
          <el-form-item label="竞赛名称">
            <el-select v-model="form.compId" placeholder="请选择竞赛">
              <el-option
                v-for="item in compOptions"
                :key="item.compId"
                :label="item.compName"
                :value="item.compId" />
            </el-select>
          </el-form-item>
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
import { listPublic, getPublic, delPublic, addPublic, updatePublic,compListSelect } from "@/api/system/public";
import data from "../dict/data";

export default {
  name: "Public",
  dicts: ['sys_public_status'],
  data() {
    return {
      statusoptions: [{
        value: '1',
        label: '设为首页宣传图'
      }, {
        value: '2',
        label: '非首页宣传图'
      }],
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
      // 赛事宣传表格数据
      publicList: [],
      compOptions: undefined,
      compName:undefined,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        publicTitle: null,
        publicPic: null,
        publicContent: null,
        publicStatus: null,
        compId: null

      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        publicTitle: [
          { required: true, message: "宣传标题不能为空", trigger: "blur" }
        ],
        publicContent: [
          { required: true, message: "宣传内容不能为空", trigger: "blur" }
        ],
        compId: [
          { required: true, message: "竞赛Id不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getCompList();
  },
  methods: {
    /** 查询赛事宣传列表 */
    getList() {
      this.loading = true;
      listPublic(this.queryParams).then(response => {
        this.publicList = response.rows;
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
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        publicId: null,
        publicTitle: null,
        publicPic: null,
        publicContent: null,
        publicStatus: null,
        createBy: null,
        createTime: null,
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
      this.ids = selection.map(item => item.publicId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加赛事宣传";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const publicId = row.publicId || this.ids
      getPublic(publicId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改赛事宣传";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.publicId != null) {
            updatePublic(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPublic(this.form).then(response => {
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
      const publicIds = row.publicId || this.ids;
      this.$modal.confirm('是否确认删除赛事宣传编号为"' + publicIds + '"的数据项？').then(function() {
        return delPublic(publicIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/public/export', {
        ...this.queryParams
      }, `public_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
