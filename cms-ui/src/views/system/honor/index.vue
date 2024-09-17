<template>

  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="70px">
      <el-form-item label="荣誉名" prop="honorName">
        <el-input
          v-model="queryParams.honorName"
          placeholder="请输入荣誉名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="荣誉类型" prop="honorType">
        <el-select v-model="queryParams.honorType" placeholder="请选择荣誉类型" clearable>
          <el-option
            v-for="dict in dict.type.sys_honor_type"
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
      <el-form-item label="获奖日期">
        <el-date-picker clearable
          v-model="dateRange"
          type="daterange"
          value-format="yyyy-MM-dd"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="荣誉状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择荣誉状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_honor_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
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
          v-hasPermi="['system:honor:add']"
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
          v-hasPermi="['system:honor:edit']"
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
          v-hasPermi="['system:honor:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:honor:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="honorList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="荣誉id" align="center" prop="honorId" />
      <el-table-column label="荣誉名" align="center" prop="honorName" />
      <el-table-column label="荣誉类型" align="center" prop="honorType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_honor_type" :value="scope.row.honorType"/>
        </template>
      </el-table-column>
      <el-table-column label="参赛队伍" align="center" key="squadName" prop="squad.squadName" />
      <el-table-column label="参与竞赛" align="center" key="compName" prop="comp.compName" />
       <el-table-column label="备注" align="center" prop="honorContent" />
      <el-table-column label="获奖日期" align="center" prop="honorDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.honorDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="荣誉状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_honor_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:honor:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:honor:remove']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="openDetailDialog(scope.row.honorId)"
          >查看</el-button>
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

    <!-- 添加或修改荣誉信息管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="荣誉名" prop="honorName" >
          <el-input v-model="form.honorName" placeholder="请输入荣誉名" />
        </el-form-item>
        <el-form-item label="荣誉类型" prop="honorType">
          <el-select v-model="form.honorType" placeholder="请选择荣誉类型">
            <el-option
              v-for="dict in dict.type.sys_honor_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
       <el-row>
       <el-col :span="12">
        <el-form-item label="获奖小队" prop="squadId">
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
        <el-form-item label="备注">
          <editor v-model="form.honorContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="获奖/成果获得日期" prop="honorDate">
          <el-date-picker clearable
            v-model="form.honorDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择获奖/成果获得日期">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="荣誉状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_honor_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!--通知公告详情 -->
        <el-dialog :title="form.honorName" :visible.sync="openDetail" width="800px" append-to-body>
          <div style="margin-top:-20px;margin-bottom:10px;">
           <el-tag size="mini" effect="dark" type="warning" v-if="form.honorType==2">成果信息</el-tag>
            <el-tag size="mini" effect="dark" v-else>获奖信息</el-tag>
            <span style="margin-left:20px;">{{form.honorDate}}</span>
          </div>
          <div v-loading="loadingDetail" class="content">
            <div v-html="form.honorContent" style="margin-left:0px;margin-right:76px" class="ql-editor"></div>
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="cancel"> 关 闭 </el-button>
          </div>
        </el-dialog>
  </div>
</template>

<script>
import { listHonor, getHonor, delHonor, addHonor, updateHonor ,compListSelect,squadListSelect} from "@/api/system/honor";

export default {
  name: "Honor",
  dicts: ['sys_honor_type', 'sys_honor_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 详情加载
            loadingDetail: false,
            // 打开详情
            openDetail: false,
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
      // 荣誉信息管理表格数据
      honorList: [],
      //日期范围
      dateRange:"",
      // 岗位选项
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
        honorName: null,
        honorType: null,
        squadId: undefined,
        compId: undefined,
        honorContent: null,
        honorDate: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        squadId: [
          { required: true, message: "参赛队伍不能为空", trigger: "blur" }
        ],
        compId: [
          { required: true, message: "参赛队伍不能为空", trigger: "blur" }
        ],

        honorName: [
          { required: true, message: "荣誉名不能为空", trigger: "blur" }
        ],
        honorType: [
          { required: true, message: "荣誉类型不能为空", trigger: "blur" }
        ],
        honorDate: [
          { required: true, message: "荣誉获得日期不能为空", trigger: "blur" }
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
    /** 查询荣誉信息管理列表 */
    getList() {
      this.loading = true;
      listHonor(this.addDateRange(this.queryParams,this.dateRange)).then(response =>{
        this.honorList = response.rows;
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
      this.openDetail = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        honorId: null,
        honorName: null,
        honorType: null,
        squadId: null,
        compId: undefined,
        honorContent: null,
        honorDate: null,
        status: null,
        createTime: null,
        createBy: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
      this.getCompList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange=[];
      this.resetForm("queryForm");
      this.queryParams.compId = undefined;
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.honorId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加荣誉信息管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const honorId = row.honorId || this.ids
      getHonor(honorId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改荣誉信息管理";
      });
    },
    // 打开信息详情
        openDetailDialog(honorId) {
          this.openDetail = true;
          this.loadingDetail = true;
          getHonor(honorId).then(response => {
            this.form = response.data;
            this.openDetail = true;
            this.loadingDetail = false;
          });
        },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.honorId != null) {
            updateHonor(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addHonor(this.form).then(response => {
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
      const honorIds = row.honorId || this.ids;
      this.$modal.confirm('是否确认删除荣誉信息管理编号为"' + honorIds + '"的数据项？').then(function() {
        return delHonor(honorIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/honor/export', {
        ...this.queryParams
      }, `honor_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
