var util = require('../../utils/util.js');
var api = require('../../config/api.js');

//获取应用实例
const app = getApp();

Page({
  data: {
    categoryList: [],
    currentCategory: {},
    currentSubCategoryList: {},
    scrollLeft: 0,
    scrollTop: 0,
    goodsCount: 0,
    scrollHeight: 0,
    cateScrollTop: 0
  },
  onLoad: function(options) {

  },
  getCatalog: function(id) {
    //CatalogList
    let that = this;
    wx.showLoading({
      title: '加载中...',
    });

    let data = {};
    if (id){
      data = {id: id}
    }
    util.request(api.CatalogList,data)
      .then(function(res) {
        that.setData({
          categoryList: res.data.categoryList,
          currentCategory: res.data.currentCategory
        });
        that.showData(res);
        wx.hideLoading();
    });
    util.request(api.GoodsCount).then(function(res) {
      that.setData({
        goodsCount: res.data.goodsCount
      });
    });

  },
  getCurrentCategory: function(id) {
    let that = this;

    util.request(api.CatalogCurrent, {
      id: id
    }).then(function(res) {
        that.setData({
          currentCategory: res.data.currentCategory,
        });
        that.showData(res);
      });

  },
  onReady: function() {
    // 页面渲染完成
  },
  onShow: function() {
    // 页面显示
    if (app.globalData.indexSkipCatalogId) {
      this.getCatalog(parseInt(app.globalData.indexSkipCatalogId));
    } else {
      this.getCatalog();
    }
  },
  onHide: function() {
    // 页面隐藏
    app.globalData.indexSkipCatalogId = null;
  },
  onUnload: function() {
    // 页面关闭
    for (let i in this._observer) {
      if (this._observer[i]) this._observer[i].disconnect()
    }
  },
  getList: function() {
    var that = this;
    util.request(api.ApiRootUrl + 'api/catalog/' + that.data.currentCategory.catId)
      .then(function(res) {
        that.setData({
          categoryList: res.data,
        });
      });
  },
  switchCate: function(event) {
    var that = this;
    var currentTarget = event.currentTarget;
    if (this.data.currentCategory.id == event.currentTarget.dataset.id) {
      return false;
    }
    this.setData({
        cateScrollTop:0
      }, () => {
        that.getCurrentCategory(event.currentTarget.dataset.id);
      }
    );

  },

  showData: function (res) {
    let that = this;
    that.setData({
      currentSubCategoryList: res.data.currentSubCategory
    }, ()=>{
      that._observer = [];
      for (let i in that.data.currentSubCategoryList) {
        that._observer[i]=wx.createIntersectionObserver(this);
        that._observer[i].relativeTo('.cate')
          .observe('.item-'+i, (data) => {
            if(data.intersectionRatio > 0){
              let categoryList = that.data.currentSubCategoryList;
              let category = categoryList[data.dataset.index];
              if (!category.goods) {
                console.log("请求数据："+data.dataset.id);
                wx.showLoading({
                  title: '加载中...',
                });
                util.request(api.GoodsByCategory,{
                  categoryId: data.dataset.id
                }).then(function(res) {
                  category.reqData = true;
                  category.resultDatas = res.data.resultDataList;
                  categoryList[data.dataset.index] = category;
                  that.setData({
                    currentSubCategoryList: categoryList
                  });

                  wx.hideLoading();
                });
              }
            }
          });
      }
    });
  },

  addToCart: function (event) {
    if (!app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/auth/login/login"
      });
      return false;
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
  }
})