'use strict';

var Decimal = require('decimal.js');

var contactPrice = new Decimal(0.3);
var maxPrice = new Decimal(7000);
var min = function(x, y) { return x.cmp(y) < 0 ? x : y };
var max = function(x, y) { return x.cmp(y) > 0 ? x : y };
var identity = function(x) { return x };

var aggr = {
  sum: [function(x, y) { return x.plus(y) }, identity, function() { return 0 }],
  min: [min, identity],
  max: [max, identity]
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
    },

    daily: {
      coverage: {
        min: aggr.min,
        max: aggr.max
      },
      rub: {
        $cost: function(coverage_max) { return coverage_max.times(maxPrice) }
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
