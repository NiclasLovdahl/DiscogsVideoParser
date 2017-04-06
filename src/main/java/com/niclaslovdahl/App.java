package com.niclaslovdahl;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;

/**
 * Main class.
 * 
 * @author Niclas Lövdahl
 * 
 */
public class App {

	private JBrowserDriver driver;
	private String link = "";
	private String finalString = "http://www.youtube.com/watch_videos?video_ids=";
	private JFrame frame;

	public App() {
		this.link = JOptionPane.showInputDialog("Insert discogs link");
		this.frame = new JFrame("Discogs Video Parser");
		ImageIcon loading = new ImageIcon("res/ajax-loader.gif");
		frame.add(new JLabel("loading... ", loading, JLabel.CENTER));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		driver = new JBrowserDriver(Settings.builder().timezone(Timezone.EUROPE_ATHENS).build());
	}

	public void run() throws MalformedURLException {
		Document doc = parsePage(link);
		Elements elements = doc.getElementsByClass("item_description_title");

		for (Element element : elements) {
			Document doc2 = parsePage("https://discogs.com" + element.attr("href"));
			Elements ele = doc2.getElementsContainingText("View Release Page");

			Document doc3 = parsePage("https://discogs.com" + ele.attr("href"));
			Element ele2 = doc3.getElementById("youtube_player_placeholder");
			if (ele2 != null) {
				String links = ele2.attr("data-video-ids");
				String[] links2 = links.split(",");
				for (String string : links2) {
					finalString += string + ",";
				}
			}
		}
		removeLastFromFinal();
		openWebpage(new URL(finalString));
		frame.dispose();
		final JDialog dialog = new JDialog();
		dialog.setAlwaysOnTop(true);
		JOptionPane.showMessageDialog(dialog, "Playlist successfully created. Enjoy!");
		driver.quit();
	}

	public Document parsePage(String link) {
		driver.get(link);
		Document doc = Jsoup.parse(driver.getPageSource());
		return doc;
	}

	public void removeLastFromFinal() {
		if (finalString != null && finalString.length() > 0 && finalString.charAt(finalString.length() - 1) == ',') {
			finalString = finalString.substring(0, finalString.length() - 1);
		}
	}

	public static void openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void openWebpage(URL url) {
		try {
			openWebpage(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		App app = new App();
		app.run();
	}
}
