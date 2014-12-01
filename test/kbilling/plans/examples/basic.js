'use strict';

var Decimal = require('decimal.js');

var contactPrice = 0.3;
var maxPrice = 7000; // new Decimal(600) * contactPrice;
var min = function(x, y) { return x.cmp(y) < 0 ? x : y };

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
        sum: {
          $aggr: function(x, y) { return x.plus(y) },
          $init: function() { return 0 }
        }
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
