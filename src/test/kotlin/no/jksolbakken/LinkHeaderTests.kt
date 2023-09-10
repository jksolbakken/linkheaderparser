package no.jksolbakken

import java.net.URI
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LinkHeaderTests {

    @ParameterizedTest
    @ValueSource(strings = ["Link: <https://example.com>",
        """<https://example.com>; rel="preconnect" """])
    fun `headers with and without key can be parsed`(link: String) {
        assertEquals(1, parse(link).size)
    }

    @Test
    fun `uris are extracted correctly`() {
        val expected = URI("https://example.com")
        val actual = parse("Link: <https://example.com>")[0].uri
        assertEquals(expected, actual)
    }

    @Test
    fun `param rel is extracted correctly`() {
        val expected = "preconnect"
        val actual = parse("""Link: <https://example.com>; rel="preconnect" """)[0].rel
        assertEquals(expected, actual)
    }

    @Test
    fun `all params are extracted`() {
        val expected = mapOf("rel" to "preconnect", "something" to "other", "whatever" to "thing")
        val actual = parse("""Link: <https://example.com>; rel="preconnect"; something=other; whatever="thing" """)[0].params
        assertEquals(expected, actual)
    }

    @Test
    fun `invalid header should fail`() {
        assertThrows<IllegalArgumentException> { parse("Link: bla bla bla") }
    }

    @Test
    fun `funky chars should be urlencoded`() {
        val expected = listOf(Link(URI("https://example.com/%F0%9F%98%80/%F0%9F%94%89"), mapOf("a" to "b")))
        val actual = parse("Link: <https://example.com/😀/🔉>; a=b")
        assertEquals(expected, actual)
    }

}