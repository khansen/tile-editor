package tm.tests;

import tm.utils.XMLParser;

final class XMLParserTests {

    private XMLParserTests() {}

    static void runAll() {
        testEscapeXML();
    }

    private static void testEscapeXML() {
        String value = "a&b<c>d\"e'f";
        String expected = "a&amp;b&lt;c&gt;d&quot;e&apos;f";
        TestAssertions.assertEquals(expected, XMLParser.escapeXML(value), "escapeXML should escape XML entities");
    }
}
