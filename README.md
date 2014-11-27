# kbilling-plans-cljs

KillingBilling calculation server.
Use `nodemon` to autorestart. Usage:

    nodemon -w deploy.txt --exitcrash target/main.js

## Development

### Testing

Type `lein test`: it will launch ClojureScript tests with `node target/test.js` (can also be run directly). 

### REPL

To start Node.js ClojureScript REPL, run `lein repl` and then enter: 

```clojure
(require '[cljs.repl.node :as nr])
(nr/run-node-nrepl)
```

Type `:cljs/quit` to stop the ClojureScript REPL.

## License

Copyright © 2014 KillingBilling Inc.

Distributed under the Eclipse Public License (see the file LICENSE).
