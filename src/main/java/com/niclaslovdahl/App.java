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
import com.sun.media.sound.SoftSynthesizer;

/**
 * Main class.
 * 
 * @author Niclas Lövdahl
 * 
 */
public class App {
	public static void main(String[] args) throws IOException {
		JBrowserDriver driver = new JBrowserDriver(Settings.builder().timezone(Timezone.EUROPE_ATHENS).build());
		driver.get("https://www.discogs.com/seller/franciesco/profile");
		String loadedPage = driver.getPageSource();

		// Item page link parsed
		Document document = Jsoup.parse(loadedPage);
		Elements elements = document.getElementsByClass("item_description_title");

		for (Element element : elements) {
			// Item page load
			System.out.println(element.attr("href"));
			driver.get("https://discogs.com" + element.attr("href"));
			Document doc = Jsoup.parse(driver.getPageSource());
			// Release page link parsed
			Elements ele = doc.getElementsContainingText("View Release Page");
			System.out.println(ele.attr("href"));
			
			driver.get("https://discogs.com" + ele.attr("href"));
			Document doc2 = Jsoup.parse(driver.getPageSource());	
			
			Element element2 = doc2.getElementById("youtube_player_placeholder");
			
			System.out.println(element2.attr("data-video-ids"));
			String links = element2.attr("data-video-ids");
			String[] links2 = links.split(",");
			for (String string : links2) {
				System.out.println(string);
			}
			break;
		}

		driver.quit();
	}
}
