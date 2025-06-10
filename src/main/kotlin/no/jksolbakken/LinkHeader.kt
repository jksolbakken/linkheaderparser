package no.jksolbakken

import java.net.URI
import java.net.URLEncoder
import java.nio.charset.Charset

private val uriRegex = "<(?<uri>.*)>".toRegex()
private val paramRegex = """\w+="?\w+(?!${'$'})?""".toRegex()

data class Link(val uri: URI, val params: Map<String, String> = emptyMap()) {
    val rel: String?
        get() = params["rel"]
}

fun parse(rawHeader: String): List<Link> = valueOf(rawHeader).let { value ->
    value.split(",").map { link ->
        val uri = extractURI(link)
        val params = extractParams(link)
        Link(uri, params)
    }
}

private fun valueOf(rawHeader: String) =
    if (rawHeader.lowercase().startsWith("link:")) {
        rawHeader.substringAfter(':').trim()
    } else {
        rawHeader
    }.trim()

private fun extractURI(link: String) =
    uriRegex.find(link)?.groups?.get("uri")?.value?.let { uriString ->
        val (scheme, rest) = Pair(uriString.substringBefore("//"), uriString.substringAfter("//"))
        URI("$scheme//${urlEncodeEverythingButSlashes(rest)}")
    } ?: throw IllegalArgumentException("'$link' does not contain a valid uri")

private fun extractParams(link: String) = paramRegex.findAll(link)
    .map { it.value }
    .map { it.substringBefore("=") to it.substringAfter("=").trim('"') }
    .toMap()

private fun urlEncodeEverythingButSlashes(input: String) =
    input.split("/")
        .joinToString(separator = "/") { URLEncoder.encode(it, Charset.defaultCharset()) }

