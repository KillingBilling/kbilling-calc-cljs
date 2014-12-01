'use strict';

var Decimal = require('decimal.js');

var price = 5000;

module.exports = {

  $cycles: {
    $subscription: {
      $begin: ['monthly']
    },
    monthly: {
      $duration: "1 month",
      rub: {
        $cost: function() { return price }
      }
    }
  },

  $notifications: {
    rubBelow0: function(rub) { return rub.lt(0) }
  }

};
