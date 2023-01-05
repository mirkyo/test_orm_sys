<template>

  <el-container>


    <el-main>

      <div style="margin: 10px 0">
        <el-upload action="http://localhost:9090/userfile/upload" :show-file-list="false" :data={hour:this.$route.query.hour,hour1:this.$route.query.hour1,minutes:this.$route.query.minutes,minutes1:this.$route.query.minutes1,testTime:this.$route.query.testTime} :on-success="handleFileUploadSuccess" style="display: inline-block">
          <el-button type="primary" class="ml-5">上传文件<i class="el-icon-top"></i></el-button>
        </el-upload>

      </div>

    </el-main>


  </el-container>




</template>

<script>
export default {
  name: "Test",
  return: {

  },
  created(){

  },
  methods: {
    handleFileUploadSuccess(){
      console.log(this.$route.query.hour)
      this.$message.success("考试结束")
      this.$router.push("/front")
    },
    // getFileDate(){
    //   let hour = this.$route.query.hour
    //   let hour1 = this.$route.query.hour1
    //   let minutes = this.$route.query.minutes
    //   let minutes1 = this.$route.query.minutes1
    //   let testTime = this.$route.query.testTime
    //   this.hour = hour
    //   this.hour1 = hour1
    //   this.minutes = minutes
    //   this.minutes1 = minutes1
    //   this.testTime = testTime
    //   this.fileData.hour = this.hour
    //   this.fileData.hour1 = this.hour1
    //   this.fileData.minutes = this.minutes
    //   this.fileData.minutes1 = this.minutes1
    //   this.fileData.testTime = this.testTime
    //   console.log(hour)
    //   return this.fileData
    // }
  }
}
</script>

<style scoped>

</style>