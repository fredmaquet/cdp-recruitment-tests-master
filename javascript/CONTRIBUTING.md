# How I contributed to this project

## New feature
This program has to filter a list of elements containing a pattern.

```sh
$ node app.js --filter=ry
[{"name":"Uzuzozne","people":[{"name":"Lillie Abbott","animals":[{"name":"John Dory"}]}]},{"name":"Satanwi","people":[{"name":"Anthony Bruno","animals":[{"name":"Oryx"}]}]}]

```

The next goal is to count People and Animals by adding the count of children in the name

```sh
$ node app.js --filter=ry --count
[{"name":"Uzuzozne","people":[{"name":"Lillie Abbott","animals":[{"name":"John Dory"}]}]},{"name":"Satanwi","people":[{"name":"Anthony Bruno","animals":[{"name":"Oryx"}]}]}]

[{"name":"Uzuzozne [1]","people":[{"name":"Lillie Abbott [1]","animals":[{"name":"John Dory"}]}]},{"name":"Satanwi [1]","people":[{"name":"Anthony Bruno [1]","animals":[{"name":"Oryx"}]}]}]
```

## Tests

```sh
$ npm test
> javascript@1.0.0 test /home/fred/cdp-recruitment-tests-master/javascript
> mocha

  tests on filtering datas
    ✓ should filter datas with many animals on people
    ✓ should filter and count datas many animals on people
    ✓ should filtered datas with unknown matching filter


  3 passing (5ms)
```

