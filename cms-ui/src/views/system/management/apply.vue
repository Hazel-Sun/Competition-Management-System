<template>
  <div class="app-container">
    <el-steps :active="active" finish-status="success">
      <el-step title="新建竞赛小队"></el-step>
      <el-step title="填写小队信息"></el-step>
      <el-step title="上传报名表"></el-step>
      <el-step title="完成报名"></el-step>
    </el-steps>
    <el-form ref="form" :model="heighAddress" label-width="90px" :rules="rule" class="highForm">
      <div class="form1" v-if="active==0">

      </div>

    </el-form>
    <el-button style="margin-top: 12px;" @click="next">下一步</el-button>

  </div>
</template>

<script>
import { addSquad } from "@/api/system/squad";
import { addProcessinfo } from "@/api/system/processinfo";

export default {
  name: "apply",
  components:{},
  data() {
    return {
      active: 0
    };
  },

  methods: {
    next() {
      if (this.active++ > 3) this.active = 0;
    }
  }
}
</script>

<style scoped>

</style>
