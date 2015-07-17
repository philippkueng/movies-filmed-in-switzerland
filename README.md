# Movies filmed in Switzerland

[![Circle CI](https://circleci.com/gh/philippkueng/movies-filmed-in-switzerland.svg?style=svg)](https://circleci.com/gh/philippkueng/movies-filmed-in-switzerland)

## Development Setup

* `lein figwheel`
* Then connect to the repl on localhost:7888 via Emacs Cider.

```clojure
user> (use 'figwheel-sidecar.repl-api)
user> (cljs-repl)
```

## Data Source

* [IMDb Interfaces](http://www.imdb.com/interfaces) and then download the `locations.list.gz` from the listed FTP shares.
* [Title formats](http://www.imdb.com/updates/guide/title_formats)

