'use strict';

var Decimal = require('decimal.js');

var contactPrice = new Decimal(0.3);
var maxPrice = new Decimal(7000);
var min = function(x, y) { return x.cmp(y) < 0 ? x : y };

var aggr = {
  sum: [
    function(x, y) { return x.plus(y) },
    function(x) { return x },
    function() { return 0 }
  ]
};

module.exports = {

  $cycles: {
    $subscription: {
      $begin: ['monthly', 'monthly'],
      rub: {
        $cost: function() { return 2800 }
      }
    },

    monthly: {
      $duration: "1 month",
      coverage: {
        sum: aggr.sum
      },
      rub: {
        $cost: function(coverage_sum) { return min(coverage_sum.times(contactPrice), maxPrice) }
      }
    }
  },

  $values: {
    rubOrCost: function(rub, monthly_rub_$cost) { return rub.gt(monthly_rub_$cost) ? rub : monthly_rub_$cost }
  },

  $notifications: {
    rubBelow0: function(rub) { return rub.lt(0) }
  }

};
