'use strict';

var BigNumber = require('bignumber.js');

var price = new BigNumber(5000);

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
    rubBelow0: function(rub) { return new BigNumber(rub).lt(0) }
  }

};
