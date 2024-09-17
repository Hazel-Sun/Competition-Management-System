<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <div class="block">
          <el-carousel :interval="4000" indicator-position="outside" height="400px">
              <el-carousel-item v-if="item.publicStatus==1" v-for="item in publicList" :key="item.publicId">
                <img
                  style="object-fit:scale-down;
                  width:100%;
                  height:100%"
                  :src="item.publicPic"
                  class="image">
              </el-carousel-item>
            </el-carousel>
        </div>
    </el-row>
    <el-divider />
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="6">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>赛事宣传</span>
          </div>
          <el-collapse accordion>
            <el-collapse-item
              v-for="item in publicList"
              :key="item.publicId"
              :title="item.publicTitle"
              :value="item.publicId">
              <div v-html="item.publicContent" style="margin-left:0px;margin-right:76px" class="ql-editor"></div>
            </el-collapse-item>
          </el-collapse>
          <div class="body">

          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <el-link>通知公告</el-link>
          </div>
          <el-collapse accordion>
           <el-collapse-item
           v-for="item in noticeList"
           :key="item.noticeId"
           :title="item.noticeTitle+'\n'+item.createTime"
           :value="item.noticeId">
               <div v-html="item.noticeContent" style="margin-left:0px;margin-right:76px" class="ql-editor"></div>
           </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="6">
        <el-card class="update-log">
          <div slot="header" class="clearfix">
            <span>荣誉墙</span>
          </div>
           <el-collapse accordion>
           <el-collapse-item
           v-for="item in honorList"
           :key="item.honorId"
           :title="item.honorName+'\n'+item.honorDate"
           :value="item.honorId"
           v-if="item.status==3">

           </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>


<script>
import { listNotice, getNotice } from "@/api/system/notice";
import { listHonor, getHonor} from "@/api/system/honor";
import { listPublic, getPublic } from "@/api/system/public";

export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "3.6.2",
      imgList:[],
      noticeList:[],
      honorList:[],
      publicList:[],
      loading:true,
      url:"./notice/index"



    };
  },
  created() {
      this.getNoticeList();
      this.getHonorList();
      this.getPublicList();
    },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
 /** 查询公告列表 */
        getNoticeList() {
          this.loading = true;
          listNotice().then(response => {
            this.noticeList = response.rows;
            this.loading = false;
          });
        },
    /** 查询荣誉信息管理列表 */
    getHonorList() {
      this.loading = true;
      listHonor().then(response => {
        this.honorList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询赛事宣传管理列表 */
    getPublicList() {
      this.loading = true;
      listPublic().then(response => {
        this.publicList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },



  },

};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

