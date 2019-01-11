const util = require('../../utils/util.js');
const api = require('../../config/api.js');
const user = require('../../utils/user.js');

//获取应用实例
const app = getApp();

Page({
  data: {
    newGoods: [],
    hotGoods: [],
    topics: [],
    brands: [],
    groupons: [],
    floorGoods: [],
    banner: [],
    channel: [],
    coupon: [],
    flashSales: [],
    timers: [],
    isHideLoadMore: true,  //"上拉加载"的变量，true，隐藏
    page: 1, //第一页
    noMore: false,
    floorstatus: false,
    periodText:'',
    localAddressText:''
  },

  onShareAppMessage: function () {
    return {
      title: '几个水果',
      desc: '几个水果小程序商城',
      path: '/pages/index/index'
    }
  },

  onPullDownRefresh() {
    wx.showNavigationBarLoading() //在标题栏中显示加载
    this.getIndexData();
    wx.hideNavigationBarLoading() //完成停止加载
    wx.stopPullDownRefresh() //停止下拉刷新
  },

  getIndexData: function () {
    let that = this;
    that.setData({
      page: 1,
      noMore: false
    });
    util.request(api.FlashSalesIndexUrl, {
      page: this.data.page
    }).then(function (res) {
      if (res.errno === 0) {

        that.setData({
          banner: res.data.banner,
          channel: res.data.channel,
          flashSales: res.data.flashSaleList
        });

        for (let i = 0; i < that.data.banner.length; i++) {
          let item = that.data.banner[i];
          if (item.link.startsWith("/pages")) {
            item.type = 1;
          } else if (item.link.startsWith("http")) {
            item.type = 2;
          } else {
            item.type = 0;
          }
        }
        that.setData({
          banner: that.data.banner
        });

        that.processFlashSales(that, that.data.flashSales);
      }
    });

  },
  onLoad: function (options) {

    // 页面初始化 options为页面跳转所带来的参数
    if (options.scene) {
      //这个scene的值存在则证明首页的开启来源于朋友圈分享的图,同时可以通过获取到的goodId的值跳转导航到对应的详情页
      var scene = decodeURIComponent(options.scene);
      console.log("scene:" + scene);

      let info_arr = [];
      info_arr = scene.split(',');
      let _type = info_arr[0];
      let id = info_arr[1];

      if (_type == 'goods') {
        wx.navigateTo({
          url: '../goods/goods?id=' + id
        });
      } else if (_type == 'groupon') {
        wx.navigateTo({
          url: '../goods/goods?grouponId=' + id
        });
      } else {
        wx.navigateTo({
          url: '../index/index'
        });
      }
    }

    // 页面初始化 options为页面跳转所带来的参数
    if (options.grouponId) {
      //这个pageId的值存在则证明首页的开启来源于用户点击来首页,同时可以通过获取到的pageId的值跳转导航到对应的详情页
      wx.navigateTo({
        url: '../goods/goods?grouponId=' + options.grouponId
      });
    }

    // 页面初始化 options为页面跳转所带来的参数
    if (options.goodId) {
      //这个goodId的值存在则证明首页的开启来源于分享,同时可以通过获取到的goodId的值跳转导航到对应的详情页
      wx.navigateTo({
        url: '../goods/goods?id=' + options.goodId
      });
    }

    // 页面初始化 options为页面跳转所带来的参数
    if (options.orderId) {
      //这个orderId的值存在则证明首页的开启来源于订单模版通知,同时可以通过获取到的pageId的值跳转导航到对应的详情页
      wx.navigateTo({
        url: '../ucenter/orderDetail/orderDetail?id=' + options.orderId
      });
    }

    let defaultPeriodText = app.globalData.defaultPeriodText;
    let defaultCounty = app.globalData.defaultCounty;
    this.setData({
      periodText: defaultPeriodText,
      localAddressText: defaultCounty
    });

    this.getIndexData();
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  getCoupon(e) {
    if (!app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/auth/login/login"
      });
    }

    let couponId = e.currentTarget.dataset.index
    util.request(api.CouponReceive, {
      couponId: couponId
    }, 'POST').then(res => {
      if (res.errno === 0) {
        wx.showToast({
          title: "领取成功"
        })
      } else {
        util.showErrorToast(res.errmsg);
      }
    })
  },
  //添加到购物车
  addToCart: function (event) {

    if (!app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/auth/login/login"
      });
    }

    let goodsId = event.target.dataset.goods.goods.id;
    let productId = event.target.dataset.goods.goods_product_id;

    let number = event.target.dataset.goods.last_number;
    //验证库存
    if (number <= 0) {
      wx.showToast({
        image: '/static/images/icon_error.png',
        title: '没有库存'
      });
      return false;
    }

    //添加到购物车
    util.request(api.CartAdd, {
      goodsId: goodsId,
      number: 1,
      productId: productId
    }, "POST")
      .then(function (res) {
        let _res = res;
        if (_res.errno == 0) {
          wx.showToast({
            title: '添加成功'
          });
        } else {
          wx.showToast({
            image: '/static/images/icon_error.png',
            title: _res.errmsg,
            mask: true
          });
        }
      });
  },

  showWebView: function (event) {
    let type = event.target.dataset.banner.type;

    let link = event.target.dataset.banner.link;
  },

  nowTime: function () {//时间函数
    let that = this;

    let len = that.data.flashSales.length;//时间数据长度
    // console.log(a)
    for (let i = 0; i < len; i++) {
      let intDiff = that.data.flashSales[i].dat;//获取数据中的时间戳
      // console.log(intDiff)
      let day = 0, hour = 0, minute = 0, second = 0;
      if (intDiff > 0) {//转换时间
        day = Math.floor(intDiff / (60 * 60 * 24));
        hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
        minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
        second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
        if (hour <= 9) hour = '0' + hour;
        if (minute <= 9) minute = '0' + minute;
        if (second <= 9) second = '0' + second;
        that.data.flashSales[i].dat--;
        var str;
        if (day > 0) {
          str = day + "天" + hour + ':' + minute + ':' + second
        } else {
          str = hour + ':' + minute + ':' + second
        }
        // console.log(str)
      } else {
        var str = "已结束！";
        if (that.timers != null && that.timers.length > 0) {
          clearInterval(that.timers[i]);
          that.timers.splice(i, 1);

        }
      }
      // console.log(str);
      that.data.flashSales[i].difftime = str;//在数据中添加difftime参数名，把时间放进去
    }
    that.setData({
      flashSales: that.data.flashSales
    })
    // console.log(that)
  },

  processFlashSales: function (that, flashSales) {
    if (that.timers != null) {
      for (let i = 0; i < that.timers.length; i++) {
        clearInterval(that.timers[i]);
        that.timers.splice(i, 1);
      }
    }

    for (let i = 0; i < flashSales.length; i++) {
      let item = flashSales[i];
      item.dat = (item.expire_time - new Date().getTime()) / 1000;
    }

    that.nowTime();
    let timer = setInterval(that.nowTime, 1000);
    if (that.timers == null) {
      that.timers = new Array();
    }
    that.timers.push(timer);
  },

  onReachBottom: function () {
    if (this.data.noMore) {
      return;
    }

    let that = this;
    this.data.page++;

    that.setData({
      isHideLoadMore: false
    });

    let flashSalesBefore = this.data.flashSales;

    util.request(api.FlashSalesIndexUrl, {
      page: this.data.page
    }).then(function (res) {
      if (res.errno === 0) {

        if (res.data.flashSaleList.length < 10) {
          that.setData({
            noMore: true
          });
        }

        that.setData({
          isHideLoadMore: true,
          flashSales: flashSalesBefore.concat(res.data.flashSaleList)
        });
        that.processFlashSales(that, res.data.flashSaleList);
      }
    });
  },

  skipContent: function () {
    const query = wx.createSelectorQuery();
    query.select('#channel').boundingClientRect();
    query.exec(function (res) {
      wx.pageScrollTo({
        scrollTop: res[0].top + 30,
        duration: 300
      })
    })
  },
  // 获取滚动条当前位置
  onPageScroll: function (e) {
    if (e.scrollTop > 100) {
      this.setData({
        floorstatus: true
      });
    } else {
      this.setData({
        floorstatus: false
      });
    }
  },

  goTop: function () {
    wx.pageScrollTo({
      scrollTop: 0,
      duration: 300
    });
    this.setData({
      floorstatus: false
    })
  },

  toChooseAddress: function () {
    console.log("toChooseAddress")
  }
});