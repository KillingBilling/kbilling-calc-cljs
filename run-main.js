try {
  require("source-map-support").install();
} catch(err) {
}
require('./target/main/goog/bootstrap/nodejs.js');
require('./target/main.js');
goog.require("kbilling.calc.main");
goog.require("cljs.nodejscli");
