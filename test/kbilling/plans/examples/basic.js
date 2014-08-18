'use strict';

var BigNumber = require('bignumber.js');

var contactPrice = new BigNumber(0.3);
var maxPrice = new BigNumber(7000); //BigNumber(600) * contactPrice;
var min = function(x, y) { return x.comparedTo(y) < 0 ? x : y };

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
          $aggr: function(x, y) { return new BigNumber(x).plus(y) },
          $init: function() { return 0 }
        }
      },
      rub: {
        $cost: function(coverage_sum) { return min(new BigNumber(coverage_sum).times(contactPrice), maxPrice) }
      }
    }
  },

  $values: {
    rubOrCost: function(rub, monthly_rub_$cost) {
      return new BigNumber(rub).greaterThan(monthly_rub_$cost) ? rub : monthly_rub_$cost
    }
  },

  $notifications: {
    rubBelow0: function(rub) { return new BigNumber(rub).lt(0) }
  }

};
