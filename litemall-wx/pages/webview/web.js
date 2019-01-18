

Page({
  data: {
    article: ''
  },
  onLoad: function(options) {
    if (options.article) {
      this.setData({
        article: options.article
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

})
