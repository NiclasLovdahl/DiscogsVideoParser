package com.niclaslovdahl;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	JBrowserDriver driver = new JBrowserDriver(Settings
                .builder().
                timezone(Timezone.EUROPE_ATHENS).build());
        driver.get("https://www.discogs.com/seller/franciesco/profile");
        String loadedPage = driver.getPageSource();

        // JSoup parsing part
        Document document = Jsoup.parse(loadedPage);
        Elements elements = document.getElementsByClass("item_description_title");
        
        for (Element element : elements) {
			System.out.println(element.attr("href"));
			driver.get("https://discogs.com" + element.attr("href"));
			Document doc = Jsoup.parse(driver.getPageSource());
			System.out.println(doc.text());
			Elements ele = doc.getElementsContainingText("View Release Page");
		}

       

        driver.quit();
    }
}
