var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

Page({
  data: {
    balance: 0,
    recharges: [
      {name: '10', value: '10元', checked: 'true'},
      {name: '20', value: '20元'},
      {name: '50', value: '50元'},
      {name: '100', value: '100元'},
      {name: '200', value: '200元'},
      {name: '1000', value: '1000元'},
    ],
    rechargeAmount: 0,
  },
  onLoad: function(options) {
    if (options.balance) {
      this.setData({
        balance: options.balance
      });
    }

    this.data.recharges[0].checked = true;
    this.setData({
      recharges: this.data.recharges,
    });

    this.data.rechargeAmount = 10;
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
  rechargeChange: function (e) {
    this.data.rechargeAmount = e.detail.value;
  },
  // “立即充值”按钮点击
  recharge: function() {
    let that = this;
    util.request(api.RechargeSubmit, {
      amount: that.data.rechargeAmount
    }, 'POST').then(function(res) {
      if (res.errno === 0) {
        const payParam = res.data;
        console.log("支付过程开始");
        wx.requestPayment({
          'timeStamp': payParam.timeStamp,
          'nonceStr': payParam.nonceStr,
          'package': payParam.packageValue,
          'signType': payParam.signType,
          'paySign': payParam.paySign,
          'success': function(res) {
            console.log("支付过程成功");
            util.redirect('/pages/ucenter/order/order');
          },
          'fail': function(res) {
            console.log("支付过程失败");
            util.showErrorToast('支付失败');
          },
          'complete': function(res) {
            console.log("支付过程结束")
          }
        });
      }
    });
  },

})
