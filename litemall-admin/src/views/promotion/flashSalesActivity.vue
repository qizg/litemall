<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.goodsId" clearable class="filter-item" style="width: 200px;" placeholder="请输入商品编号"/>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button
        :loading="downloadLoading"
        class="filter-item"
        type="primary"
        icon="el-icon-download"
        @click="handleDownload">导出
      </el-button>
    </div>

    <!-- 查询结果 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      size="small"
      element-loading-text="正在查询中。。。"
      border
      fit
      highlight-current-row>

      <el-table-column align="center" label="订单ID" prop="flashSales.orderId"/>

      <el-table-column align="center" label="用户ID" prop="flashSales.userId"/>

      <el-table-column align="center" label="抢购优惠（元）" prop="rules.discount"/>

      <el-table-column align="center" label="开始时间" prop="rules.addTime"/>

      <el-table-column align="center" label="结束时间" prop="rules.expireTime"/>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-tooltip placement="top" content="返回顶部">
      <back-to-top :visibility-height="100"/>
    </el-tooltip>

  </div>
</template>

<style>
  .table-expand {
    font-size: 0;
  }

  .table-expand label {
    width: 100px;
    color: #99a9bf;
  }

  .table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
  }

  .gallery {
    width: 80px;
    margin-right: 10px;
  }
</style>

<script>
import { listRecord } from '@/api/flashsales'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  name: 'FlashSalesActivity',
  components: { BackToTop, Pagination },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        goodsId: undefined,
        sort: 'add_time',
        order: 'desc'
      },
      goodsDetail: '',
      detailDialogVisible: false,
      downloadLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      listRecord(this.listQuery).then(response => {
        this.list = response.data.data.items
        this.total = response.data.data.total
        this.listLoading = false
      }).catch(() => {
        this.list = []
        this.total = 0
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleDownload() {
      this.downloadLoading = true
        import('@/vendor/Export2Excel').then(excel => {
          const tHeader = ['商品ID', '名称', '首页主图', '折扣（元）', '活动开始时间', '活动结束时间']
          const filterVal = ['goods.id', 'goods.name', 'goods.pic_url', 'rules.discount', 'rules.addTime', 'rules.expireTime']
          excel.export_json_to_excel2(tHeader, this.list, filterVal, '抢购商品信息')
          this.downloadLoading = false
        })
    }
  }
}
</script>
