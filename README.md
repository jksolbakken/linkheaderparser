# linkheaderparser

![workflow](https://github.com/jksolbakken/linkheaderparser/actions/workflows/main.yaml/badge.svg)

Parses [HTTP Link Headers](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Link). 
The motivation behind this library is the ability to use pagination in the [GitHub API](https://docs.github.com/en/rest/guides/using-pagination-in-the-rest-api?apiVersion=2022-11-28). 

## ⌨️ Usage

```kotlin
val linkHeader = parse("""Link: <https://example.com>; rel="preconnect" """)
val uri = linkHeader.uri
val rel = linkHeader.rel
val otherParam = linkHeader.params["other"]
```

## ⚖️ License
[MIT](LICENSE).

## 👥 Contact

This project is maintained by [@jksolbakken](https://github.com/jksolbakken).

Questions and/or feature requests? Please create an [issue](https://github.com/jksolbakken/linkheaderparser/issues).

