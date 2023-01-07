<template>

    <el-container>
      <el-header style="border-bottom: 1px solid #ccc;">
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item index="1">我的考试</el-menu-item>
          <el-submenu index="2">
            <template slot="title">我的工作台</template>
            <el-menu-item index="2-1">选项1</el-menu-item>
            <el-menu-item index="2-2">选项2</el-menu-item>
            <el-menu-item index="2-3">选项3</el-menu-item>
            <el-submenu index="2-4">
              <template slot="title">选项4</template>
              <el-menu-item index="2-4-1">选项1</el-menu-item>
              <el-menu-item index="2-4-2">选项2</el-menu-item>
              <el-menu-item index="2-4-3">选项3</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-menu-item index="3" disabled>消息中心</el-menu-item>
        </el-menu>
      </el-header>

      <el-main>

        <div>
          <div style="margin: 10px 0">
            <el-upload action="http://localhost:9090/userfile/upload" :show-file-list="false" :on-success="handleFileUploadSuccess" style="display: inline-block">
              <el-button type="primary" class="ml-5">上传文件<i class="el-icon-top"></i></el-button>
            </el-upload>

          </div>

          <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="id" label="ID" width="80"></el-table-column>
            <el-table-column prop="name" label="文件名称" ></el-table-column>
            <el-table-column prop="type" label="文件类型" ></el-table-column>
            <el-table-column prop="size" label="文件大小(kb)"></el-table-column>
            <el-table-column prop="startTime" label="开始时间" ></el-table-column>
            <el-table-column prop="testTime" label="考试时间" ></el-table-column>

            <el-table-column label="下载">
              <template slot-scope="scope">
                <el-button type="primary" @click="download(scope.row)">下载</el-button>
              </template>
            </el-table-column>
            <el-table-column label="启用">
              <template slot-scope="scope">
                <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
              </template>
            </el-table-column>
          </el-table>

          <!--      分页组件-->
          <div style="padding: 10px 0">
            <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="pageNum"
                :page-sizes="[2, 5, 10, 20]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total">
            </el-pagination>
          </div>

        </div>

      </el-main>
    </el-container>


</template>

<script>
export default {
  name: "FrontHome",

  data(){
    return {
      tableData: [],
      klass: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).klass : '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total:0,
      hour: '',
      hour1: '',
      minutes: '',
      minutes1: '',
      testTime: '',
      student: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : '',
    }
  },
  created(){
    this.load()
  },
  methods: {
    handleSelectionChange(val){
      this.multipleSelection = val
    },
    reset(){
      this.name = ""
      this.load()
    },
    load(){
      this.request.get("/file/page1",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          klass: this.klass, // 按照班级查询试卷
          student: this.student
        }
      }).then(res => {
        console.log("sdfsdfsdfsdf")
        console.log(this.name)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    handleSizeChange(pageSize){
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    handleFileUploadSuccess(res){
      console.log(res)
      this.load()
    },
    download(row){
      this.request.get("/file/page",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          id: row.id // 按照班级查询试卷
        }
      }).then(res => {
        console.log(res.data.records[0].startTime)// startTime
        console.log(res.data.records[0].testTime)// testTime
        let time = new Date()
        let time1 = res.data.records[0].startTime
        let testTime = res.data.records[0].testTime
        let month = time.getMonth()+1
        let day = time.getDate()
        let hour = time.getHours()
        let minutes = time.getMinutes()
        let month1 = time1.split("-")[0]
        let day1 = time1.split("-")[1]
        let hour1 = time1.split("-")[2]
        let minutes1 = time1.split("-")[3]
        let t = month1 + "-" + day1 + "-" + hour1 + "-" + minutes1
        this.hour = hour
        this.hour1 = hour1
        this.minutes = minutes
        this.minutes1 = minutes1
        this.testTime = testTime
        console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>")
        console.log(month + "-" + day + "-" + hour + "-" + minutes)
        console.log(month1 + "-" + day1 + "-" + hour1 + "-" + minutes1)
        console.log(">>>>>>>>>>>>>>>>>>>>>>>>>>")
        if((month == month1) && (day == day1)){
          if((hour>hour1)){
            console.log(t)
            this.$router.push({path: "/test", query: {hour: this.hour, hour1: this.hour1, minutes: this.minutes, minutes1: this.minutes1, testTime: this.testTime}})
            window.open(row.url)
          }
          if((hour == hour1) && (minutes1 >= minutes1)){
            this.$router.push({path: "/test", query: {hour: this.hour, hour1: this.hour1, minutes: this.minutes, minutes1: this.minutes1, testTime: this.testTime}})
            window.open(row.url)
          }
        }else{
          this.$message.error("考试暂未开始")
        }

      })
    },
    changeEnable(row){
      this.request.post("/userfile/update", row).then(res =>{
        if(res.code === '200'){
          this.$message.success("操作成功")
        }
      })
    }
  }
}
</script>

<style scoped>

</style>