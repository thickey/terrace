# Terrace - an edn data viewer

## Usage

```bash
  $ npm install

  $ lein cljsbuild once

  # Start app
  $ node js/main.js

  # Open http://localhost:1337/

  # Test
  $ curl -v -X POST -H "Content-Type: application/x-www-form-urlencoded" -d '{:a 1 :b [2 3] :c #{4 5}}' http://127.0.0.1:1337/edn
```
