var util = require('../../utils/util.js');
var api = require('../../config/api.js');

var app = getApp();

Page({
  data: {
    checkedGoodsList: [],
    checkedAddress: {},
    availableCouponLength: 0, // 可用的优惠券数量
    goodsTotalPrice: 0.00, //商品总价
    freightPrice: 0.00, //快递费
    couponPrice: 0.00, //优惠券的价格
    grouponPrice: 0.00, //团购优惠价格
    orderTotalPrice: 0.00, //订单总价
    actualPrice: 0.00, //实际需要支付的总价
    cartId: 0,
    addressId: 0,
    couponId: 0,
    userCouponId: 0,
    message: '',
    grouponLinkId: 0, //参与的团购
    grouponRulesId: 0, //团购规则ID
    deliveryMethod: '1', //配送方式:1:快递,2:自提
    showPayType: false, //显示支付类型底部弹窗
    payType: '1', //支付类型:1:微信支付,2:余额支付
    orderId:0
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
  },

  //获取checkou信息
  getCheckoutInfo: function() {
    let that = this;
    util.request(api.CartCheckout, {
      cartId: that.data.cartId,
      addressId: that.data.addressId,
      couponId: that.data.couponId,
      userCouponId: that.data.userCouponId,
      grouponRulesId: that.data.grouponRulesId
    }).then(function(res) {
      if (res.errno === 0) {
        that.setData({
          checkedGoodsList: res.data.checkedGoodsList,
          checkedAddress: res.data.checkedAddress,
          availableCouponLength: res.data.availableCouponLength,
          actualPrice: res.data.actualPrice,
          couponPrice: res.data.couponPrice,
          grouponPrice: res.data.grouponPrice,
          freightPrice: res.data.freightPrice,
          goodsTotalPrice: res.data.goodsTotalPrice,
          orderTotalPrice: res.data.orderTotalPrice,
          addressId: res.data.addressId,
          couponId: res.data.couponId,
          userCouponId: res.data.userCouponId,
          grouponRulesId: res.data.grouponRulesId,
        });
      }
      wx.hideLoading();
    });
  },
  selectAddress() {
    wx.navigateTo({
      url: '/pages/ucenter/address/address',
    })
  },
  selectCoupon() {
    wx.navigateTo({
      url: '/pages/ucenter/couponSelect/couponSelect',
    })
  },
  bindMessageInput: function(e) {
    this.setData({
      message: e.detail.value
    });
  },
  onReady: function() {
    // 页面渲染完成

  },
  onShow: function() {
    // 页面显示
    wx.showLoading({
      title: '加载中...',
    });
    try {
      var cartId = wx.getStorageSync('cartId');
      if (cartId === "") {
        cartId = 0;
      }
      var addressId = wx.getStorageSync('addressId');
      if (addressId === "") {
        addressId = 0;
      }
      var couponId = wx.getStorageSync('couponId');
      if (couponId === "") {
        couponId = 0;
      }
      var userCouponId = wx.getStorageSync('userCouponId');
      if (userCouponId === "") {
        userCouponId = 0;
      }
      var grouponRulesId = wx.getStorageSync('grouponRulesId');
      if (grouponRulesId === "") {
        grouponRulesId = 0;
      }
      var grouponLinkId = wx.getStorageSync('grouponLinkId');
      if (grouponLinkId === "") {
        grouponLinkId = 0;
      }

      this.setData({
        cartId: cartId,
        addressId: addressId,
        couponId: couponId,
        userCouponId: userCouponId,
        grouponRulesId: grouponRulesId,
        grouponLinkId: grouponLinkId
      });

    } catch (e) {
      // Do something when catch error
      console.log(e);
    }

    this.getCheckoutInfo();
  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭

  },
  submitOrder: function() {
    if (this.data.addressId <= 0) {
      util.showErrorToast('请选择收货地址');
      return false;
    }
    util.request(api.OrderSubmit, {
      cartId: this.data.cartId,
      addressId: this.data.addressId,
      couponId: this.data.couponId,
      userCouponId: this.data.userCouponId,
      message: this.data.message,
      grouponRulesId: this.data.grouponRulesId,
      grouponLinkId: this.data.grouponLinkId,
      deliveryMethod: this.data.deliveryMethod
    }, 'POST').then(res => {
      if (res.errno === 0) {

        // 下单成功，重置couponId
        try {
          wx.setStorageSync('couponId', 0);
        } catch (error) {

        }

        this.setData({
          orderId : res.data.orderId,
          grouponLinkId : res.data.grouponLinkId,
          showPayType: true
        });


      } else {
        util.showErrorToast(res.errmsg);
      }
    });
  },

  onRadioChange(event) {
    let that = this;
    let actualPrice = that.data.actualPrice;
    let freightPrice = that.data.freightPrice;
    if(event.detail=='2'){
      actualPrice = actualPrice - freightPrice;
    } else {
      actualPrice = actualPrice + freightPrice;
    }

    this.setData({
      deliveryMethod: event.detail,
      actualPrice: actualPrice
    });
  },

  onRadioClick(event) {
    const { name } = event.currentTarget.dataset;
    this.setData({
      deliveryMethod: name
    });
  },

  onActionSheetClose(event) {
    this.setData({
      showPayType: false
    });
  },

  onPayTypeRadioChange(event) {
    this.setData({
      payType: event.detail
    });
  },

  onPayTypeRadioClick(event) {
    const { name } = event.currentTarget.dataset;
    this.setData({
      payType: name
    });
  },

  onPayBtnClick(event){

    let that = this;

    if(that.data.payType === '2'){

      util.request(api.OrderBalancePay, {
        orderId: that.data.orderId
      }, 'POST').then(function(res) {
        if (res.errno === 0) {
          wx.redirectTo({
            url: '/pages/payResult/payResult?status=1&orderId=' + that.data.orderId
          });
        } else{
          wx.redirectTo({
            url: '/pages/payResult/payResult?status=0&orderId=' + that.data.orderId+"&msg="+res.errmsg
          });
        }
      });
      return;
    }

    util.request(api.OrderPrepay, {
      orderId: that.data.orderId
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
            if (that.data.grouponLinkId) {
              setTimeout(() => {
                wx.redirectTo({
                  url: '/pages/groupon/grouponDetail/grouponDetail?id=' + that.data.grouponLinkId
                })
              }, 1000);
            } else {
              wx.redirectTo({
                url: '/pages/payResult/payResult?status=1&orderId=' + that.data.orderId
              });
            }
          },
          'fail': function(res) {
            console.log("支付过程失败");
            wx.redirectTo({
              url: '/pages/payResult/payResult?status=0&orderId=' + that.data.orderId
            });
          },
          'complete': function(res) {
            console.log("支付过程结束")
          }
        });
      } else {
        wx.redirectTo({
          url: '/pages/payResult/payResult?status=0&orderId=' + that.data.orderId
        });
      }
    });
  }
});