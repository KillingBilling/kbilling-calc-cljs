try {
  require("source-map-support").install();
} catch(err) {
}
require('./target/test/goog/bootstrap/nodejs.js');
require('./target/test.js');
goog.require("kbilling.calc.test");
goog.require("cljs.nodejscli");
