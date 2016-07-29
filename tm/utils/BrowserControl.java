package tm.utils;
import java.awt.Desktop;
import java.net.URI;

/**
* A simple, static class to display a URL in the system browser.
*
* Examples:
*
BrowserControl.displayURL("http://www.javaworld.com")
*
BrowserControl.displayURL("file://c:\\docs\\index.html")
*
BrowserContorl.displayURL("file:///user/joe/index.html");
*
* Note - you must include the url type -- either "http://" or
* "file://".
*/
public class BrowserControl
{
    /**
     * Display a file in the system browser.  If you want to display a
     * file, you must include the absolute path name.
     *
     * @param url the file's url (the url must start with either "http://"
or
     * "file://").
     */
    public static void displayURL(String url)
    {
        try
        {
            Desktop.getDesktop().browse(new URI(url));
        }
        catch (Exception x)
        {
            System.err.println("Error bringing up browser: " + x);
        }
    }
}