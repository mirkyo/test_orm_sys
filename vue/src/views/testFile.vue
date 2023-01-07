<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-upload action="http://localhost:9090/file/upload" :show-file-list="false" :on-success="handleFileUploadSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">上传文件<i class="el-icon-top"></i></el-button>
      </el-upload>
      <el-popconfirm
          class="ml-5"
          confirmButtonText='确定'
          cancelButtonText='我再想想'
          icon="el-icon-info"
          iconColor="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>

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
          <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column label="启用">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirmButtonText='确定'
              cancelButtonText='我再想想'
              icon="el-icon-info"
              iconColor="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
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


    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="开始时间">
          <el-input v-model="form.startTime" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="考试时间">
          <el-input v-model="form.testTime" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="班级">
          <el-input v-model="form.klass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="主讲教师">
          <el-input v-model="form.lecturer" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="监考教师">
          <el-input v-model="form.invigilator" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="人数">
          <el-input v-model="form.nnt" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学期">
          <el-input v-model="form.semester" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="科目">
          <el-input v-model="form.subject" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="机房名称">
          <el-input v-model="form.mrn" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="考试环境">
          <el-input v-model="form.examEnv" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="考试学生">
          <el-input v-model="form.student" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="补考学生">
          <el-input v-model="form.bukaoStudent" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>



  </div>
</template>

<script>
export default {
  name: "testFile",
  data(){
    return {
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total:0,
      form: "",
      dialogFormVisible: false
    }
  },
  created(){
    this.load()
  },
  methods: {
    del(id){
      this.request.delete("/file/" + id).then(res => {
        if(res.code === '200'){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    save(){
      this.request.post("/file/update", this.form).then(res =>{
        if(res.data){
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error("保存失败")
        }
      })
    },
    handleEdit(row){
      this.form = row
      this.dialogFormVisible = true
    },
    handleSelectionChange(val){
      this.multipleSelection = val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id)
      this.request.post("/file/del/batch", ids).then(res => {
        if(res.code === '200'){
          this.$message.success("批量删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },

    reset(){
      this.name = ""
      this.load()
    },
    load(){
      this.request.get("/file/page",{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          klass: this.klass
        }
      }).then(res => {
        console.log(res)
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
    download(url){
      window.open(url)
    },
    changeEnable(row){
      this.request.post("/file/update", row).then(res =>{
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