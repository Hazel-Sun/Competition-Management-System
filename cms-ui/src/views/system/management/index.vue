<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="70px">
      <el-form-item label="竞赛名称" prop="compName">
        <el-input
          v-model="queryParams.compName"
          placeholder="请输入竞赛名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="竞赛类型" prop="compType">
        <el-select v-model="queryParams.compType" placeholder="请选择竞赛类型" clearable>
          <el-option
            v-for="dict in dict.type.sys_comp_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="竞赛">
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
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_comp_status"
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
          v-hasPermi="['system:management:add']"
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
          v-hasPermi="['system:management:edit']"
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
          v-hasPermi="['system:management:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:management:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="managementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="竞赛id" align="center" prop="compId" />
      <el-table-column label="竞赛名称" align="center" prop="compName" />
      <el-table-column label="竞赛类型" align="center" prop="compType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_comp_type" :value="scope.row.compType"/>
        </template>
      </el-table-column>
      <el-table-column label="主办方" align="center" prop="compSponser" />
      <el-table-column label="竞赛链接" align="center" >
       <template slot-scope="scope">
       <el-link target="_blank" type="info" :href="scope.row.compLink">
       {{scope.row.compLink}}
       </el-link>
       </template>
      </el-table-column>

      <el-table-column label="开始时间" align="center" prop="compStartTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.compStartTime, '{y}-{m}-{d} ') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="compEndTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.compEndTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_comp_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasRole="['admin']"
            v-hasPermi="['system:management:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdateSquad(scope.row)"
            v-hasPermi="['system:management:edit']"
            v-hasRole="['admin']"
          >竞赛队伍管理</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleSquadIn(scope.row)"
            :disabled="scope.row.status!=2"
          >报名竞赛</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:management:remove']"
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

    <!-- 添加或修改竞赛信息管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="竞赛名称" prop="compName">
          <el-input clearable v-model="form.compName" placeholder="请输入竞赛名称" />
        </el-form-item>
        <el-form-item label="竞赛类型" prop="compType">
          <el-select v-model="form.compType" placeholder="请选择竞赛类型">
            <el-option
              v-for="dict in dict.type.sys_comp_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="主办方" prop="compSponser">
          <el-input clearable v-model="form.compSponser"  type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="竞赛链接" prop="compLink">
          <el-input clearable v-model="form.compLink" placeholder="请输入竞赛链接" >
          </el-input>
        </el-form-item>
        <el-form-item label="竞赛时间" >
          <el-col :span="11">
          <el-date-picker clearable
                      v-model="form.compStartTime"
                      type="date"
                      value-format="yyyy-MM-dd"
                      placeholder="请选择开始时间">
          </el-date-picker>
          </el-col>
          <el-col class="line" :span="2" align="center">—</el-col>
          <el-col :span="11">
             <el-date-picker clearable
                        v-model="form.compEndTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择结束时间">
                      </el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_comp_status"
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

    <!-- 竞赛队伍管理对话框 -->
    <el-dialog :title="title" :visible.sync="openSquad" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddSysSquad">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSysSquad">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="sysSquadList" :row-class-name="rowSysSquadIndex" @selection-change="handleSysSquadSelectionChange" ref="sysSquad">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="小队名称" prop="squadName" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.squadName" placeholder="请输入小队名称" />
            </template>
          </el-table-column>
          <el-table-column label="小队人数" prop="memberNum" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.memberNum" placeholder="请输入小队人数" />
            </template>
          </el-table-column>
          <el-table-column label="联系电话" prop="phone" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.phone" placeholder="请输入联系电话" />
            </template>
          </el-table-column>
          <el-table-column label="邮箱" prop="email" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.email" placeholder="请输入邮箱" />
            </template>
          </el-table-column>
          <el-table-column label="状态" prop="status" width="150">
            <template slot-scope="scope">
              <el-select disabled v-model="scope.row.status='1'" placeholder="请选择状态">
                <el-option
                  v-for="dict in dict.type.sys_squad_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


    <!-- 报名竞赛对话框 -->
    <el-dialog :title="title" :visible.sync="openSquadIn" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddSysSquad">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSysSquad">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="sysSquadList" :row-class-name="rowSysSquadIndex" @selection-change="handleSysSquadSelectionChange" ref="sysSquad">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="小队名称" prop="squadName" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.squadName" placeholder="请输入小队名称" />
            </template>
          </el-table-column>
          <el-table-column label="小队人数" prop="memberNum" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.memberNum" placeholder="请输入小队人数" />
            </template>
          </el-table-column>
          <el-table-column label="联系电话" prop="phone" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.phone" placeholder="请输入联系电话" />
            </template>
          </el-table-column>
          <el-table-column label="邮箱" prop="email" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.email" placeholder="请输入邮箱" />
            </template>
          </el-table-column>
          <el-table-column label="状态" prop="status" width="150">
            <template slot-scope="scope">
              <el-select disabled v-model="scope.row.status='1'" placeholder="请选择状态">
                <el-option
                  v-for="dict in dict.type.sys_squad_status"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">报 名</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listManagement, getManagement, delManagement, addManagement, updateManagement } from "@/api/system/management";

export default {
  name: "Management",
  dicts: ['sys_comp_status', 'sys_comp_type','sys_squad_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSysSquad: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 竞赛信息管理表格数据
      managementList: [],
      // 竞赛队伍表格数据
      sysSquadList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openSquad:false,
      openSquadIn:false,
      openCreate:false,
      dateRange:"",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        compName: null,
        compType: null,
        compSponser: null,
        compLink: null,
        compStartTime: null,
        compEndTime: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        compName: [
          { required: true, message: "竞赛名称不能为空", trigger: "blur" }
        ],
        compType: [
          { required: true, message: "竞赛类型不能为空", trigger: "blur" }
        ],


      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询竞赛信息管理列表 */
    getList() {
      this.loading = true;
      listManagement(this.addDateRange(this.queryParams,this.dateRange)).then(response => {
        this.managementList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openSquad=false;
      this.openSquadIn=false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        compId: null,
        compName: null,
        compType: null,
        compSponser: null,
        compLink: null,
        compStartTime: null,
        compEndTime: null,
        status: null
      };
      this.sysSquadList = [];
      this.resetForm("form");
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange=[];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.compId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加竞赛信息管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const compId = row.compId || this.ids
      getManagement(compId).then(response => {
        this.form = response.data;
        this.sysSquadList = response.data.sysSquadList;
        this.open = true;
        this.title = "修改竞赛信息管理";
      });
    },
    handleUpdateSquad(row) {
      this.reset();
      const compId = row.compId || this.ids
      getManagement(compId).then(response => {
        this.form = response.data;
        this.sysSquadList = response.data.sysSquadList;
        this.openSquad = true;
        this.title = "竞赛队伍管理";
      });
    },
    handleSquadIn(row) {
      // this.reset();
      // const compId = row.compId || this.ids
      // getManagement(compId).then(response => {
      //   this.form = response.data;
      //   this.openSquadIn = true;
      //   this.title = "报名竞赛";
      // });
      this.$router.push("/competition/squad");
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.sysSquadList = this.sysSquadList;
          if (this.form.compId != null) {
            updateManagement(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.openSquad = false;
              this.openSquadIn=false;
              this.getList();
            });
          } else {
            addManagement(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.openSquad=false;
              this.openSquadIn=false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const compIds = row.compId || this.ids;
      this.$modal.confirm('是否确认删除竞赛信息管理编号为"' + compIds + '"的数据项？').then(function() {
        return delManagement(compIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 竞赛队伍序号 */
    rowSysSquadIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 竞赛队伍添加按钮操作 */
    handleAddSysSquad() {
      let obj = {};
      obj.squadName = "";
      obj.squadLeader = "";
      obj.memberNum = "";
      obj.squadMember = "";
      obj.squadTeacher = "";
      obj.phone = "";
      obj.email = "";
      obj.status = "";
      this.sysSquadList.push(obj);
    },
    /** 竞赛队伍删除按钮操作 */
    handleDeleteSysSquad() {
      if (this.checkedSysSquad.length == 0) {
        this.$modal.msgError("请先选择要删除的竞赛队伍数据");
      } else {
        const sysSquadList = this.sysSquadList;
        const checkedSysSquad = this.checkedSysSquad;
        this.sysSquadList = sysSquadList.filter(function(item) {
          return checkedSysSquad.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleSysSquadSelectionChange(selection) {
      this.checkedSysSquad = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/management/export', {
        ...this.queryParams
      }, `management_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
