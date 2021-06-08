package ca.jrvs.apps.practice;

import junit.framework.TestCase;
import org.junit.*;

public class RegexExcImpTest extends TestCase {

    private RegexExc regexExcTest;

    @Before
    public void setUp() throws Exception {
        regexExcTest = new RegexExcImp();
    }

    @Test
    public void testMatchJpeg() {
        Assert.assertTrue(regexExcTest.matchJpeg("file.jpg"));
        Assert.assertTrue(regexExcTest.matchJpeg("123.jpg"));
        Assert.assertFalse(regexExcTest.matchJpeg(".jpeg"));
        Assert.assertFalse(regexExcTest.matchJpeg(".jpg"));
        Assert.assertFalse(regexExcTest.matchJpeg("file.jpeegg"));
    }

    @Test
    public void testMatchIp() {
        Assert.assertTrue(regexExcTest.matchIp("162.182.0.1"));
        Assert.assertTrue(regexExcTest.matchIp("1.1.1.1"));
        Assert.assertTrue(regexExcTest.matchIp("100.100.100.100"));
        Assert.assertFalse(regexExcTest.matchIp("1621.820"));
    }

    @Test
    public void testIsEmptyLine() {
        Assert.assertTrue(regexExcTest.isEmptyLine(""));
        Assert.assertTrue(regexExcTest.isEmptyLine("  "));
        Assert.assertFalse(regexExcTest.isEmptyLine(" A "));
        Assert.assertFalse(regexExcTest.isEmptyLine("1"));
    }
}