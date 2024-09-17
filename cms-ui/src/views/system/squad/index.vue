<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="小队名称" prop="squadName">
        <el-input
          v-model="queryParams.squadName"
          placeholder="请输入小队名称"
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

      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_squad_status"
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
          v-hasPermi="['system:squad:add']"
          v-hasRole="['admin','student']"
        >报名竞赛</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:squad:edit']"
        >修改小队信息</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:squad:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:squad:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="squadList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
       <el-table-column type="index" label="序号" width="50"/>
      <el-table-column label="小队编号" align="center" prop="squadId"/>
      <el-table-column label="小队名称" align="center" prop="squadName" />
      <el-table-column label="小队组长" align="center" prop="squadLeader" />
      <el-table-column label="小队人数" align="center" prop="memberNum" />
      <el-table-column label="参与竞赛" align="center" key="compName" prop="comp.compName" />
      <el-table-column label="创建人" align="center" prop="createBy" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_squad_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:squad:edit']"
          >修改小队信息</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:squad:remove']"
          >删除</el-button>
          <el-dropdown
            size="mini" @command="(command) =>
            handleCommand(command, scope.row)"
            v-hasPermi="['system:squad:edit']">
              <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item
                    command="handleView"
                    icon="el-icon-view"
                    v-hasPermi="['system:squad:query']"
                  >小队信息</el-dropdown-item>
                  <el-dropdown-item
                   :disabled="scope.row.status !== '3'"
                    command="handleHonor"
                    icon="el-icon-medal"
                    v-hasPermi="['system:squad:edit']"
                  >荣誉信息</el-dropdown-item>
                  <el-dropdown-item
                    command="handleAuthUser"
                    icon="el-icon-user"
                    v-hasPermi="['system:squad:edit']"
                  >管理成员</el-dropdown-item>
                  <el-dropdown-item
                    command="handleFile"
                    icon="el-icon-folder-opened"
                    v-hasPermi="['system:squad:edit']"
                  >过程文件管理</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>

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

  <!-- 小队详细 -->
     <el-dialog :title="title" :visible.sync="dialogFormVisible" width="700px" append-to-body>
       <el-form ref="form" :model="form" label-width="100px" size="mini">
         <el-row>
           <el-descriptions :data="squadList" border column="2">
             <el-descriptions-item label="小队名称：">{{ form.squadName }}</el-descriptions-item>
             <el-descriptions-item label="小队队长：">{{ form.squadLeader }} </el-descriptions-item>
             <el-descriptions-item label="指导教师：">{{ form.squadTeacher }}</el-descriptions-item>
             <el-descriptions-item label="小队人数：">{{ form.memberNum }}</el-descriptions-item>
             <el-descriptions-item label="联系电话：">{{ form.phone }}</el-descriptions-item>
             <el-descriptions-item label="e-mail：">{{ form.email }}</el-descriptions-item>
             <el-descriptions-item label="参与竞赛：" >
               {{form.compName}}
             </el-descriptions-item>
             <el-descriptions-item label="状态：">
               <dict-tag :options="dict.type.sys_squad_status" :value="form.status"/>
             </el-descriptions-item>
             <el-descriptions-item label="创建时间：">{{ form.createTime }}</el-descriptions-item>
             <el-descriptions-item label="更新时间：">{{ form.updateTime }}</el-descriptions-item>
           </el-descriptions>
         </el-row>
         <el-row>
             <el-table v-loading="loading" :data="userList" @selection-change="handleUserSelectionChange">
               <el-table-column label="用户名称" prop="userName" :show-overflow-tooltip="true" />
               <el-table-column label="用户昵称" prop="nickName" :show-overflow-tooltip="true" />
               <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true" />
               <el-table-column label="手机" prop="phonenumber" :show-overflow-tooltip="true" />
             </el-table>
         </el-row>

       </el-form>
       <div slot="footer" class="dialog-footer">
         <el-button @click="dialogFormVisible = false">关 闭</el-button>
       </div>
     </el-dialog>


    <!-- 修改竞赛队伍对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="小队名称" prop="squadName">
          <el-input clearable v-model="form.squadName" placeholder="请输入小队名称" />
        </el-form-item>
        <el-row>
          <el-form-item label="小队成员">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="openSelectUser"
              v-hasPermi="['system:squad:add']"
            >添加用户</el-button>
          </el-form-item>
          <el-form-item>
            <el-table v-loading="loading" :data="userList" @selection-change="handleUserSelectionChange">
              <el-table-column label="用户名称" prop="userName" :show-overflow-tooltip="true" />
              <el-table-column label="用户昵称" prop="nickName" :show-overflow-tooltip="true" />
              <el-table-column label="邮箱" prop="email" :show-overflow-tooltip="true" />
              <el-table-column label="手机" prop="phonenumber" :show-overflow-tooltip="true" />
            </el-table>
          </el-form-item>
          <el-form-item label="小队组长" prop="squadLeader">
            <el-select v-model="form.squadLeader" placeholder="请选择小组组长">
              <el-option
                v-for="item in userList"
                :key="item.userId"
                :label="item.nickName"
                :value="item.nickName"
              ></el-option>

            </el-select>
          </el-form-item>
          <el-form-item label="指导教师" prop="squadTeacher">
            <el-select v-model="form.squadTeacher" placeholder="请选择指导教师">
              <el-option
                v-for="item in userList"
                :key="item.userId"
                :label="item.nickName"
                :value="item.nickName"
              ></el-option>

            </el-select>
          </el-form-item>
        </el-row>
        <el-form-item label="联系电话" prop="phone">
          <el-input clearable v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input clearable v-model="form.email" placeholder="请输入邮箱" >
          </el-input>
        </el-form-item>

        <el-row>
          <el-col :span="24">
           <el-form-item label="竞赛名称">
             <el-select  v-model="form.compId" placeholder="请选择竞赛">
             <el-option
              v-for="item in compOptions"
              :key="item.compId"
              :label="item.compName"
              :value="item.compId"
              :disabled="item.status == 1"/>
             </el-select>
           </el-form-item>
          </el-col>

        <el-form-item v-hasRole="['admin']" label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_squad_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加竞赛队伍对话框 -->
    <el-dialog :title="title" :visible.sync="openCreate" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="小队名称" prop="squadName">
          <el-input clearable v-model="form.squadName" placeholder="请输入小队名称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input clearable v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input clearable v-model="form.email" placeholder="请输入邮箱" >
          </el-input>
        </el-form-item>
        <el-row>
          <el-col :span="24">
            <el-form-item label="竞赛名称">
              <el-select  v-model="form.compId" placeholder="请选择竞赛">
                <el-option
                  v-for="item in compOptions"
                  :key="item.compId"
                  :label="item.compName"
                  :value="item.compId"
                  :disabled="item.status == 1"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
<!--        <el-form-item label="状态" prop="status">-->
<!--          <el-radio-group disabled v-model="form.status='1'">-->
<!--            <el-radio-->
<!--              v-for="dict in dict.type.sys_squad_status"-->
<!--              :key="dict.value"-->
<!--              :label="dict.value"-->
<!--            >{{dict.label}}</el-radio>-->
<!--          </el-radio-group>-->
<!--        </el-form-item>-->

        <el-divider content-position="center">添加小队成员</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddSysUserSquad">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSysUserSquad">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="sysUserSquadList" :row-class-name="rowSysUserSquadIndex" @selection-change="handleSysUserSquadSelectionChange" ref="sysUserSquad">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="成员ID" prop="userId" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.userId" placeholder="请输入成员ID" />
            </template>
          </el-table-column>
        </el-table>

        <el-divider content-position="center">上传竞赛报名表</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddSysFileInfo">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSysFileInfo">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="sysFileInfoList" :row-class-name="rowSysFileInfoIndex" @selection-change="handleSysFileInfoSelectionChange" ref="sysFileInfo">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="文件" prop="userId" width="150">
            <template slot-scope="scope">
              <file-upload v-model="scope.row.filePath"/>
            </template>
          </el-table-column>
          <el-table-column label="文件类型" prop="userId" width="150">
            <template slot-scope="scope">
              <el-select disabled v-model="scope.row.fileType='1'">
                <el-option
                  v-for="dict in dict.type.sys_file_info_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="参与竞赛" prop="userId" width="150">
            <template slot-scope="scope">
              <el-select  disabled v-model="scope.row.compId=form.compId" placeholder="无需选择，与报名竞赛相同">
                <el-option
                  v-for="item in compOptions"
                  :key="item.compId"
                  :label="item.compName"
                  :value="item.compId"
                  :disabled="item.status == 1"/>
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

    <!--荣誉信息管理对话框-->
    <el-dialog :title="title" :visible.sync="openHonor" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddSysHonor">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSysHonor">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="sysHonorList" :row-class-name="rowSysHonorIndex" @selection-change="handleSysHonorSelectionChange" ref="sysHonor">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="荣誉名" prop="honorName" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.honorName" placeholder="请输入荣誉名" />
            </template>
          </el-table-column>
          <el-table-column label="荣誉类型" prop="honorType" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.honorType" placeholder="请选择荣誉类型">
                <el-option
                  v-for="dict in dict.type.sys_honor_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
        <el-row>
          <el-table-column label="参与竞赛"  width="250">
            <template slot-scope="scope">
              <el-select v-model="scope.row.compId=form.compId"  placeholder="请选择竞赛">
                <el-option
                  v-for="item in compOptions"
                  :key="item.compId"
                  :label="item.compName"
                  :value="item.compId"
                  :disabled="item.status == 1"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-row>
          <el-table-column label="详细描述（获奖证书及参赛心得等）"  width="600px">
            <template  slot-scope="scope">
              <editor v-model="scope.row.honorContent" :min-height="192" />
            </template>
          </el-table-column>
          <el-table-column label="获奖/成果获得日期" prop="honorDate" width="240">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.honorDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择获奖/成果获得日期" />
            </template>
          </el-table-column>
          <el-table-column label="" prop="status" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.status" placeholder="请选择">
                <el-option
                  v-for="dict in dict.type.sys_honor_status"
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

    <!-- 小队文件管理对话框 -->
    <el-dialog :title="title" :visible.sync="openFile" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddSysFileInfo">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSysFileInfo">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="sysFileInfoList" :row-class-name="rowSysFileInfoIndex" @selection-change="handleSysFileInfoSelectionChange" ref="sysFileInfo">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
<!--          <el-table-column label="文件名" prop="fileName" width="150">-->
<!--            <template slot-scope="scope">-->
<!--              <el-input v-model="scope.row.fileName" placeholder="请输入文件名" />-->
<!--            </template>-->
<!--          </el-table-column>-->
          <el-table-column label="选择文件" prop="filePath" width="250">
            <template slot-scope="scope">
              <file-upload v-model="scope.row.filePath"/>
            </template>
          </el-table-column>
          <el-table-column label="文件类型" prop="fileType" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.fileType" placeholder="请选择文件类型">
                <el-option
                  v-for="dict in dict.type.sys_file_info_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="参与竞赛" prop="compId" width="150">
            <template slot-scope="scope">
              <el-select  v-model="scope.row.compId" placeholder="请选择竞赛">
                <el-option
                  v-for="item in compOptions"
                  :key="item.compId"
                  :label="item.compName"
                  :value="item.compId"
                  :disabled="item.status == 1||item.status==3"/>
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


    <select-user ref="select" :squadId="queryParams.squadId" @ok="handleQuery" />
  </div>
</template>

<script>
import { listSquad, getSquad, delSquad, addSquad, updateSquad,compListSelect,allocatedSquadUserList, unallocatedSquadUserList, authUserSelectAll } from "@/api/system/squad";
import selectUser from "./selectUser";

export default {
  name: "Squad",
  dicts: ['sys_honor_type', 'sys_squad_status', 'sys_honor_status','sys_file_info_type','sys_normal_disable'],
  components: { selectUser },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 选中用户组
      userIds: [],
      uuuserList:[],
      // 子表选中数据
      checkedSysHonor: [],
      // 子表选中数据
      checkedSysFileInfo: [],
      // 子表选中数据
      checkedSysUserSquad: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 竞赛队伍表格数据
      squadList: [],
      // 荣誉信息管理表格数据
      sysHonorList: [],
      // 过程文件管理表格数据
      sysFileInfoList: [],
      userList: [],
      // ${subTable.functionName}表格数据
      sysUserSquadList: [],
      // 岗位选项
      compOptions: undefined,
      compName:undefined,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      dialogFormVisible:false,
      openHonor:false,
      openFile:false,
      openCreate:false,
      visible:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        squadName: null,
        squadLeader: null,
        memberNum: null,
        squadMember: null,
        squadTeacher: null,
        phone: null,
        email: null,
        status: null,
        compId:null,
        squadId: undefined,
        userName: undefined,
        nickName:undefined,
        phonenumber: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        compId:[
          {required: true, message:"报名竞赛不能为空", trigger:"blur"}
        ],
        squadName: [
          { required: true, message: "小队名称不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getCompList();
  },
  methods: {
    /** 查询竞赛队伍列表 */
    getList() {
      this.loading = true;
      listSquad(this.queryParams).then(response => {
        this.squadList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getAllocatedSquadUserList() {
      this.loading = true;
      allocatedSquadUserList(this.queryParams).then(response => {
          this.userList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
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
      this.openCreate=false;
      this.openHonor = false;
      this.openFile = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        squadId: null,
        squadName: null,
        squadLeader: null,
        memberNum: null,
        squadMember: null,
        squadTeacher: null,
        phone: null,
        email: null,
        status: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        compId: null
      };
      this.sysUserSquadList = [];
      this.sysHonorList = [];
      this.sysFileInfoList = [];
      this.userList=[];
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleView":
          this.handleView(row);
          break;
        case "handleHonor":
          this.handleHonor(row);
          break;
        case "handleAuthUser":
          this.handleAuthUser(row);
          break;
        case "handleFile":
          this.handleFile(row);
          break;
          default:
          break;
      }
    },
    /** 详细按钮操作 */
    handleView(row) {
          const squadId = row.squadId || this.ids
          this.queryParams.squadId=squadId;
          this.getAllocatedSquadUserList();
          getSquad(squadId).then(response => {
            this.form = response.data;
            this.compOptions = response.comps;
            this.$set(this.form, "compIds", response.compIds);
            this.dialogFormVisible=true;
            this.title = "详细内容";
          });

    },
    /** 荣誉信息按钮操作 */
    handleHonor(row) {
      this.reset();
      const squadId = row.squadId || this.ids
      getSquad(squadId).then(response => {
        this.form = response.data;
        this.sysHonorList = response.data.sysHonorList;
        this.openHonor = true;
        this.title = "荣誉信息";
      });
    },
    /**过程文件管理**/
    handleFile(row) {
      this.reset();
      const squadId = row.squadId || this.ids
      getSquad(squadId).then(response => {
        this.form = response.data;
        this.sysFileInfoList = response.data.sysFileInfoList;
        this.openFile = true;
        this.title = "过程文件管理";
      });
    },
    /** 分配用户操作 */
    handleAuthUser: function(row) {
      const squadId = row.squadId;
      this.$router.push("/system/squad-auth/user/" + squadId);
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.squadId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.openCreate = true;
      this.title = "添加竞赛队伍";
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const squadId = row.squadId || this.ids
      getSquad(squadId).then(response => {
        this.queryParams.squadId=squadId;
        this.form = response.data;
        this.sysHonorList = response.data.sysHonorList;
        this.sysFileInfoList=response.data.sysFileInfoList;
        this.sysUserSquadList = response.data.sysUserSquadList;
        this.open = true;
        this.title = "修改竞赛队伍";
        this.getAllocatedSquadUserList();
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if(this.form)
          this.form.sysHonorList = this.sysHonorList;
          this.form.sysFileInfoList=this.sysFileInfoList;
          this.form.sysUserSquadList = this.sysUserSquadList;
          if (this.form.squadId != null) {
            updateSquad(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.openCreate=false;
              this.openHonor = false;
              this.openFile = false;
              this.getList();
            });
          } else {
            addSquad(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.openCreate=false;
              this.openHonor = false;
              this.openFile = false;
              this.getList();
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const squadIds = row.squadId || this.ids;
      this.$modal.confirm('是否确认删除竞赛队伍编号为"' + squadIds + '"的数据项？').then(function() {
        return delSquad(squadIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 荣誉信息管理序号 */
    rowSysHonorIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 荣誉信息管理添加按钮操作 */
    handleAddSysHonor() {
      let obj = {};
      obj.honorName = "";
      obj.honorType = "";
      obj.compId = "";
      obj.honorContent = "";
      obj.honorDate = "";
      obj.status = "";
      this.sysHonorList.push(obj);
    },
    /** 荣誉信息管理删除按钮操作 */
    handleDeleteSysHonor() {
      if (this.checkedSysHonor.length == 0) {
        this.$modal.msgError("请先选择要删除的荣誉信息管理数据");
      } else {
        const sysHonorList = this.sysHonorList;
        const checkedSysHonor = this.checkedSysHonor;
        this.sysHonorList = sysHonorList.filter(function(item) {
          return checkedSysHonor.indexOf(item.index) == -1
        });
      }
    },
     /** 复选框选中数据 */
    handleSysHonorSelectionChange(selection) {
      this.checkedSysHonor = selection.map(item => item.index)
    },
    /** 过程文件管理序号 */
    rowSysFileInfoIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** 过程文件管理添加按钮操作 */
    handleAddSysFileInfo() {
      let obj = {};
      obj.fileName = "";
      obj.filePath = "";
      obj.fileType = "";
      obj.compId = "";
      this.sysFileInfoList.push(obj);
    },
    /** 过程文件管理删除按钮操作 */
    handleDeleteSysFileInfo() {
      if (this.checkedSysFileInfo.length == 0) {
        this.$modal.msgError("请先选择要删除的过程文件管理数据");
      } else {
        const sysFileInfoList = this.sysFileInfoList;
        const checkedSysFileInfo = this.checkedSysFileInfo;
        this.sysFileInfoList = sysFileInfoList.filter(function(item) {
          return checkedSysFileInfo.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleSysFileInfoSelectionChange(selection) {
      this.checkedSysFileInfo = selection.map(item => item.index)
    },
    openSelectUser() {
      this.$refs.select.show();
    },

    /** ${subTable.functionName}序号 */
    rowSysUserSquadIndex({ row, rowIndex }) {
      row.index = rowIndex + 1;
    },
    /** ${subTable.functionName}添加按钮操作 */
    handleAddSysUserSquad() {
      let obj = {};
      obj.userId = "";
      this.sysUserSquadList.push(obj);
    },
    /** ${subTable.functionName}删除按钮操作 */
    handleDeleteSysUserSquad() {
      if (this.checkedSysUserSquad.length == 0) {
        this.$modal.msgError("请先选择要删除的${subTable.functionName}数据");
      } else {
        const sysUserSquadList = this.sysUserSquadList;
        const checkedSysUserSquad = this.checkedSysUserSquad;
        this.sysUserSquadList = sysUserSquadList.filter(function(item) {
          return checkedSysUserSquad.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleSysUserSquadSelectionChange(selection) {
      this.checkedSysUserSquad = selection.map(item => item.index)
    },
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
      this.download('system/squad/export', {
        ...this.queryParams
      }, `squad_${new Date().getTime()}.xlsx`)
    }

  }
};
</script>
