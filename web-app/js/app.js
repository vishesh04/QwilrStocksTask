var app = angular.module('qwilr', ['ngRoute', 'ui.bootstrap']);

var showError = function (error, response) {
  if (response && response.data.error) error += '. ' + response.data.error;
  flatNotify().error(error);
}

app.controller('HomeCtrl', function ($scope, $http) {
  $scope.init = function () {
    $scope.editingCash = false;
    $scope.editingBuy = false;
    $scope.getAssets();
  }

  $scope.editCash = function () {
    $scope.editingCash = true;
    $scope.newCash = $scope.cash;
  }


  $scope.saveCash = function () {
    $http({
      method: 'POST',
      url: '/api/assets/cash',
      data: {cash: $scope.newCash}
    }).then(function successCallback(response) {
      $scope.cash = response.data.cash
    }, function errorCallback(response) {
      showError('Error in updating cash', response);
    }).finally(function() {
      $scope.editingCash = false;
    })
  }
  
  $scope.lookup = function (val) {
    return $http.get('/api/lookup', {
      params: {
        query: val
      }
    }).then(function(response){
      angular.forEach(response.data, function (val) {
        val.label = val.Name + ' (' + val.Symbol + ')'
      });
      return response.data
    });
  }

  $scope.getQuote = function ($item, $model) {
    $http({
      method: 'GET',
      url: '/api/quote/?symbol=' + $model.Symbol
    }).then(function successCallback(response) {
      $scope.quote = response.data
    }, function errorCallback(response) {
      showError('Error in fetching quote', response);
    })
  }

  $scope.editBuy = function () {
    $scope.editingBuy = true;
  }

  $scope.confirmBuy = function () {
    if ($scope.quote.quantity < 1) {
      showError('Please request atleast 1 unit to buy');
      return;
    }
    $http({
      method: 'POST',
      url: '/api/assets/buy',
      data: $scope.quote
    }).then(function successCallback(response) {
      $scope.getAssets();
    }, function errorCallback(response) {
      showError('Error in buying stocks', response);
    }).finally(function() {
      $scope.editingBuy = false;
    })
  }

  $scope.cancelBuy = function () {
    $scope.quote.units = undefined;
    $scope.editingBuy = false;
  }

  $scope.editSell = function (stock) {
    $scope.loadingSymbols = true;
    $scope.updateCurrentValue(stock, function () {
      stock.editingSell = true;
    }, function() {
      $scope.loadingSymbols = false;
    })
  }

  $scope.confirmSell = function (stock) {
    if (stock.quantityToSell < 1) {
      showError('Please request atleast 1 unit to sell');
      return;
    }
    $http({
      method: 'POST',
      url: '/api/assets/sell',
      data: stock
    }).then(function successCallback(response) {
      $scope.getAssets();
    }, function errorCallback(response) {
      showError('Error in selling stocks', response);
      stock.editingSell = false;
    })
  }

  $scope.cancelSell = function (stock) {
    stock.quantityToSell = undefined;
    stock.editingSell = false;
  }


  $scope.getAssets = function() {
    $http({
      method: 'GET',
      url: '/api/assets/'
    }).then(function successCallback(response) {
      $scope.cash = response.data.cash;
      $scope.stocks = response.data.stocks;
      $scope.updateCurrentValueOfAllStocks();
    }, function errorCallback(response) {
      showError('Error in fetching assets', response);
    })
  }

  $scope.updateCurrentValue = function(stock, sCallback, alwaysCallback) {
    $http({
      method: 'GET',
      url: '/api/quote/?symbol=' + stock.symbol
    }).then(function successCallback(response) {
      stock.LastPrice = response.data.LastPrice;
      if (sCallback) sCallback();
    }).finally(function() {
      if (alwaysCallback) alwaysCallback();
    })
  }

  $scope.calculateGain = function(stock) {
    if (stock.LastPrice) {
      var invested = 0, earned = 0;
      angular.forEach(stock.transactions, function (value) {
        if (value.type == 'BUY') {
          invested += (value.unitPrice*value.quantity)
        } else {
          earned += (value.unitPrice*value.quantity)
        }
      })
      var gain = ((stock.LastPrice * stock.quantity) + earned - invested)*100/invested;
      return Math.round(gain * 100) / 100;
    }
  }

  $scope.updateCurrentValueOfAllStocks = function() {
    angular.forEach($scope.stocks, function (stock) {
      $scope.updateCurrentValue(stock);
    })
  }

  $scope.calculatePrice = function (stock) {
    if (stock.LastPrice) {
      var price = stock.quantity * stock.LastPrice;
      return Math.round(price * 100) / 100;
    }
  }

  $scope.calculateTotalAsset = function() {
    if ($scope.stocks) {
      var total = 0;
      for (var i=0; i< $scope.stocks.length; i++) {
        var price = $scope.calculatePrice($scope.stocks[i]);
        if (price == 0) continue;
        if (!price) return;
        total += price;
      }
      return total;
    }
   }

  $scope.init()
});

app.config(function ($routeProvider) {
  $routeProvider
    .when('/', {
      templateUrl: '/templates/home.html',
      controller: 'HomeCtrl'
    });
});