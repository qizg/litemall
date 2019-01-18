

Page({
  data: {
    balance: 0
  },
  onLoad: function(options) {
    if (options.balance) {
      this.setData({
        balance: options.balance
      })
    }
  },onReady: function() {
    // 页面渲染完成
  },
  onShow: function() {
    // 页面显示
  },
  onHide: function() {
    // 页面隐藏
  },
  onUnload: function() {
    // 页面关闭
  },
  recharge: function () {
    console.log("=====recharge")
  }
})
