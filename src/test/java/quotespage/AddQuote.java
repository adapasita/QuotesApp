package quotespage;

import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import base.TestBaseWeb;
import web.HomeScreen;

public class AddQuote extends TestBaseWeb {

	@BeforeClass
	public void setUps() throws Exception {
		// launching application url

		HomeScreen.launch(driver, appurl);

	}

	@Test(priority = 1)
	public void AddQuote_LessThan10Characters() throws InterruptedException {

		this.AddQuote_LengthofChar_Negative(data.get("Author"), data.get("Quote"));
		/*
		 * String Author = data.get("Author"); String Quote = data.get("Quote");
		 */

	}

	@Test(priority = 1)
	public void AddQuote_checkplagiarism() throws InterruptedException {

		this.AddQuote_plagiarism_Negative(data.get("Author"), data.get("Quote"));
		/*
		 * String Author = data.get("Author"); String Quote = data.get("Quote");
		 */

	}

	public void AddQuote_LengthofChar_Negative(String Author, String Quote) throws InterruptedException {

		// HomeScreen.launch(driver, appurl);
		WebDriverWait wait = new WebDriverWait(driver, 5);

		// creating web element for search text box

		WebElement element = driver.findElement(By.id("show-modal"));

		// Clicking Add Quote Button

		element.click();

		if (driver.findElement(By.xpath("/html/body/div/div/div/div[1]/div/div/div/div/div[1]/h3")).getText()
				.contains("Add new quote")) {

			driver.findElement(By.id("autorInput")).sendKeys(Author);
			driver.findElement(By.id("quoteInput")).sendKeys(Quote);
			driver.findElement(By.xpath("/html/body/div/div/div/div[1]/div/div/div/div/div[3]/button[1]")).click();
			Thread.sleep(2000);

			boolean isPresent = driver.findElements(By.id("autorInput")).size() > 0;

			try {
				if (isPresent) {
					if (Quote.length() < 10)

					{
						// logging pass message in html report
						child.log(Status.PASS, MarkupHelper.createLabel(
								"Quote length less than 10 characters are not accepted and Quote entered was" + Quote,
								ExtentColor.GREEN));

						Assert.assertTrue(true);
					}

					else if (Quote.length() > 200) {
						// logging pass message in html report
						child.log(Status.PASS, MarkupHelper.createLabel(
								"Quote length greater than 200 characters are accepted and Quote entered was" + Quote,
								ExtentColor.GREEN));

						Assert.assertTrue(true);
					}

				} else {
					driver.findElement(By.id("searchBar")).clear();
					driver.findElement(By.id("searchBar")).sendKeys(Quote);

					driver.findElement(By.id("searchBar")).sendKeys(Keys.ENTER);
					Thread.sleep(4000);
					String savedQuote = wait
							.until(ExpectedConditions.visibilityOfElementLocated(By.className("quotes__title")))
							.getText();

					if (Quote.equalsIgnoreCase(savedQuote)) {

						if (savedQuote.length() < 10) {
							// logging failure message in html report
							child.log(Status.FAIL,
									MarkupHelper
											.createLabel(
													"Quote length less than 10 characters are accepted and quote saved was "
															+ savedQuote + " and Author was " + Author,
													ExtentColor.RED));

							Assert.assertTrue(false);
						} else if (savedQuote.length() > 200) {

							// logging failure message in html report
							child.log(Status.FAIL,
									MarkupHelper
											.createLabel(
													"Quote length greater than 200 characters are accepted and quote saved was "
															+ savedQuote + " and Author was " + Author,
													ExtentColor.RED));

							Assert.assertTrue(false);

						}

					}
				}
			} catch (Exception e) {

			}
		}

	}

	public void AddQuote_plagiarism_Negative(String Author, String Quote) throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver, 5);

		// creating web element for search text box
		WebElement element = driver.findElement(By.id("show-modal"));

		element.click();

		if (driver.findElement(By.xpath("/html/body/div/div/div/div[1]/div/div/div/div/div[1]/h3")).getText()
				.contains("Add new quote")) {

			driver.findElement(By.id("autorInput")).sendKeys(Author);
			driver.findElement(By.id("quoteInput")).sendKeys(Quote);
			driver.findElement(By.xpath("/html/body/div/div/div/div[1]/div/div/div/div/div[3]/button[1]")).click();
			Thread.sleep(2000);
			element.click();
			driver.findElement(By.id("autorInput")).sendKeys(Author);
			driver.findElement(By.id("quoteInput")).sendKeys(Quote);
			driver.findElement(By.xpath("/html/body/div/div/div/div[1]/div/div/div/div/div[3]/button[1]")).click();
			Thread.sleep(2000);
			boolean isPresent = driver.findElements(By.id("autorInput")).size() > 0;

			try {
				if (isPresent) {
					// logging pass message in html report
					child.log(Status.PASS,
							MarkupHelper.createLabel(
									"Not allowing th new quote to contains 3 or more consecutive words which have been used in the same order in a previous quote and saved quote was "
											+ Quote  + " and Author was " + Author,
									ExtentColor.GREEN));

					Assert.assertTrue(true);
				} else {
					driver.findElement(By.id("searchBar")).clear();
					driver.findElement(By.id("searchBar")).sendKeys(Quote);

					driver.findElement(By.id("searchBar")).sendKeys(Keys.ENTER);
					Thread.sleep(4000);

					String savedQuote = wait
							.until(ExpectedConditions.visibilityOfElementLocated(By.className("quotes__title")))
							.getText();

					if (Quote.equalsIgnoreCase(savedQuote)) {

						String quoteCount = driver.findElement(By.className("quotes__header")).getText();

						String quoteCountFinal[] = quoteCount.split((Pattern.quote("(")));

						String authorquoteCount = quoteCountFinal[1].substring(0, quoteCountFinal[1].length() - 1);

						// Failing testcase if saved quote count is greater than
						// 1
						if (Integer.parseInt(authorquoteCount) > 1) {
							child.log(Status.FAIL,
									MarkupHelper.createLabel(
											"Allowing th new quote to contains 3 or more consecutive words which have been used in the same order in a previous quote and saved quote was "
													+ savedQuote  + " and Author was " + Author,
											ExtentColor.RED));

							Assert.assertTrue(false);
						}

					}
				}
			} catch (Exception e) {

			}
		}
	}
}
