/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2015 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.test.cucumber;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.robotium.solo.Solo;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.ProjectActivity;
import org.catrobat.catroid.ui.ProjectListActivity;
import org.catrobat.catroid.ui.SpriteActivity;
import org.catrobat.catroid.ui.WebViewActivity;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// CHECKSTYLE DISABLE MethodNameCheck FOR 1000 LINES
public class MainMenuSteps extends AndroidTestCase {
	//private String projectName = DEFAULT_TEST_PROJECT_NAME;
	private final Object programStartWaitLock = new Object();
	private boolean programHasStarted = false;
	//private String testingproject = PROJECTNAME1;

	//@Deprecated
	@Given("^I am in the main menu$")
	public void I_am_in_the_main_menu() {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		assertEquals("I am not in the main menu.", MainMenuActivity.class, solo.getCurrentActivity().getClass());
	}
	//@Deprecated
	@When("^I press the (\\w+) button$")
	public void I_press_the_s_Button(String button) {
		// searchButton(String) apparently returns true even for
		// partial matches, but clickOnButton(String) doesn't work
		// that way. Thus we must always use clickOnText(String) because
		// the features may not contain the full text of the button.
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		solo.clickOnText(button);
		//solo.clickOnImageButton(1);
		//clickOnBottomBar(solo, R.id.button_add);
		//ImageButton imageButton=(ImageButton) solo.getView(R.id.button_add) ;
		//solo.clickOnView(imageButton);
	}
	//@When("^I press the image (\\w+) button$")
	//public void I_press_the_image_Button(String ImageButton) {
		//Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		//clickOnBottomBar(solo, R.id.button_add);
		//clickOnBottomBar(solo, R.id.button_play);
		//ImageButton imageButton=(ImageButton) solo.getView(R.id.button_add) ;
		//ImageButton imageButton1=(ImageButton) solo.getView(R.id.button_play);
		//solo.clickOnView(imageButton);
		//solo.clickOnView(imageButton1);
	@And("^I press the image Add button$")
	public void iPressTheAddButton() throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		clickOnBottomBar(solo, R.id.button_add);
	}

	public static void clickOnBottomBar(Solo solo, int buttonId) {
		solo.waitForView(ImageButton.class);
		ImageButton imageButton = (ImageButton) solo.getView(buttonId);
		solo.clickOnView(imageButton);
	}

	@And("^I press the image Play button$")
	public void iPressThePlayButton() throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		clickOnBottomBar(solo, R.id.button_play);
	}
	//@Deprecated
	@Then("^I should see the following buttons$")
	public void I_should_see_the_following_buttons(List<String> expectedButtons) {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		List<String> actualButtons = new ArrayList<String>();
		for (Button button : solo.getCurrentViews(Button.class)) {
			String text = button.getText().toString();
			if (!text.isEmpty()) {
				// Only use the first paragraph of a button text.
				int trimIndex = text.contains("\n") ? text.indexOf("\n") : text.length();
				actualButtons.add(text.substring(0, trimIndex));
			}
		}
		assertEquals("I do not see the expected buttons.", expectedButtons, actualButtons);
	}

	//@Deprecated
	@Then("^I should switch to the (\\w+) view$")
	public void I_should_switch_to_the_s_view(String view) {
		Class<? extends Activity> activityClass = null;

		if ("New".equals(view)) {
			activityClass = MainMenuActivity.class;

		} else if ("Continue".equals(view)) {
			activityClass = MainMenuActivity.class;

		} else if ("Scene".equals(view)) {
			activityClass = SpriteActivity.class;

		} else if ("Programs".equals(view)) {
			activityClass = ProjectListActivity.class;

		} else if ("Help".equals(view)) {
			activityClass = MainMenuActivity.class;

		} else if ("Upload".equals(view)) {
			activityClass = MainMenuActivity.class;

		} else if ("Explore".equals(view)) {
			activityClass = WebViewActivity.class;

		} else if ("Scripts".equals(view)) {
			activityClass = SpriteActivity.class;

		} else if ("Backgrounds".equals(view)) {
			activityClass = SpriteActivity.class;

		} else if ("Sounds".equals(view)) {
			activityClass = SpriteActivity.class;

		} else {
			fail(String.format("View '%s' does not exist.", view));
		}
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		solo.waitForActivity(activityClass.getSimpleName(), 3000);
		assertEquals("I did not switch to the expected view.", activityClass, solo.getCurrentActivity().getClass());
		solo.sleep(2000); // give activity time to completely load
		solo.getCurrentActivity().finish();
	}
	@And("^I input program name in (.*) button$")
	public void iInputProgramNameInButton(String view) throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		if (view.equals(view)) {
			solo.enterText(0, view);
			String buttonOKText = solo.getString(org.catrobat.catroid.R.string.ok);
			solo.waitForText(buttonOKText);
			solo.clickOnText(buttonOKText);
			assertTrue("dialog not loaded in 5 seconds", solo.waitForText(solo.getString(org.catrobat.catroid.R.string.project_orientation_title), 0, 500));
			solo.clickOnButton(buttonOKText);
			solo.waitForActivity(ProjectActivity.class.getSimpleName());
			solo.sleep(500);
			assertEquals("New Project is not testingproject!", view, ProjectManager.getInstance().getCurrentProject().getName());
		}
	}

	@Then("^I play the program$")
	public void I_play_the_program() throws Throwable {

		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		solo.clickOnView(solo.getView(org.catrobat.catroid.R.id.button_play));
		solo.waitForActivity(StageActivity.class.getSimpleName(), 3000);
		assertEquals("I am in the wrong Activity.", StageActivity.class, solo.getCurrentActivity().getClass());

		synchronized (programStartWaitLock) {
			if (!programHasStarted) {
				programStartWaitLock.wait(1000);
			}
		}

	}
	@And("^I test the brick categories existence$")
	public void I_check_the_brick_category_control() throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);

		clickOnBottomBar(solo, org.catrobat.catroid.R.id.button_add);
		solo.clickOnText(getContext().getString(org.catrobat.catroid.R.string.category_control));
		assertTrue("testing", true);
		solo.goBack();
		solo.clickOnText(getContext().getString(org.catrobat.catroid.R.string.category_motion));
		assertTrue("testing", true);
		solo.goBack();
		solo.clickOnText(getContext().getString(R.string.category_sound));
		assertTrue("testing", true);
		solo.goBack();
		solo.clickOnText(getContext().getString(R.string.category_looks));
		assertTrue("testing", true);
		solo.goBack();
		solo.clickOnText(getContext().getString(R.string.category_data));
		assertTrue("testing", true);
		solo.goBack();
		solo.clickOnText(getContext().getString(R.string.category_user_bricks));
		assertTrue("testing", true);
		solo.goBack();
	}
	@And("^I press the (\\w+)$")
	public void iTestTheCategoryCategories(String Category) throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		switch (Category) {
			case "Categories":
				solo.clickOnText(getContext().getString(R.string.categories));
				assertTrue("testing", true);
				break;
			case "Control":
				solo.clickOnText(getContext().getString(R.string.category_control));
				assertTrue("testing", true);
				break;
			case "Motion":
				solo.clickOnText(getContext().getString(R.string.category_motion));
				assertTrue("testing", true);
				break;
			case "Sound":
				solo.clickOnText(getContext().getString(R.string.category_sound));
				assertTrue("testing", true);
				break;
			case "Looks":
				solo.clickOnText(getContext().getString(R.string.category_looks));
				assertTrue("testing", true);
				break;
			case "Data":
				solo.clickOnText(getContext().getString(R.string.category_data));
				assertTrue("testing", true);
				break;
			case "MyBricks":
				solo.clickOnText(getContext().getString(R.string.category_user_bricks));
				assertTrue("testing", true);
				break;
		}
	}
	@And("^I take the Screenshot$")
	public void iTakeTheScreenshot() throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
//		StageListener stageListener =new StageListener();
//		stageListener.makeManualScreenshot();
	//solo.takeScreenshot();
	}
/*
	@And("^I press the \"([^\"]*)\"$")
	public void iPressThe(String category) throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		solo.clickOnText(category);
		solo.sleep(500);
		dragFloatingBrickDownwards(solo);
		switch (category) {
			case "Set variable":
				category = solo.getString(R.string.brick_set_variable);
				assertTrue("testing", true);
				break;
			case "Show variable":
				//category = solo.getString(R.string.brick_show_text_var);
				assertTrue("testing", true);
				break;
			case "Change variable":
				category = solo.getString(R.string.brick_change_variable);
				assertTrue("testing", true);
				break;
		}
	} */
	@And("^I assign setvariable \"([^\"]*)\"$")
	public void iAssignSetvariable(String text) throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		solo.clickOnText("New");
		Spinner setVariableSpinner = solo.getCurrentViews(Spinner.class).get(0);
		solo.clickOnView(setVariableSpinner);
		solo.enterText(0, "Varname");
		String buttonOKText = solo.getString(org.catrobat.catroid.R.string.ok);
		solo.waitForText(buttonOKText);
		solo.clickOnText(buttonOKText);
		solo.clickOnText("1");
		solo.clickOnView(solo.getView(R.id.formula_editor_keyboard_string));
		solo.enterText(0, text);
		solo.clickOnButton(solo.getString(R.string.ok));
		solo.clickOnView(solo.getView(R.id.formula_editor_keyboard_ok));
		//solo.waitForView(solo.getView(R.id.button_play));
		//clickOnBottomBar(solo, R.id.button_play);
		//solo.sleep(500);
	}

	@Then("^I compare two screenshot$")
	public void CompareTwoScreenshot() throws Throwable {
		File sdCard = Environment.getExternalStorageDirectory();
		File directory = new File(sdCard.getAbsolutePath() + "/Pocket-Code/%2E%2E");
		File screenShotFileA = new File(directory, "ScreenshotA.png");
		File screenShotFileB = new File(directory, "ScreenshotB.png");
		FileInputStream streamInA = new FileInputStream(screenShotFileA);
		FileInputStream streamInB = new FileInputStream(screenShotFileB);
		Bitmap ScreenshotA = BitmapFactory.decodeStream(streamInA);
		Bitmap ScreenshotB = BitmapFactory.decodeStream(streamInB);
		streamInA.close();streamInB.close();
		boolean actual = compareAreEquals(ScreenshotA, ScreenshotB);
		boolean expected = false;
		assertEquals("ScreenshotA & ScreenshotB are equals", expected, actual);
	}
	private static boolean compareAreEquals(Bitmap ScreenshotA, Bitmap ScreenshotB) {
		if (ScreenshotA.getWidth() == ScreenshotB.getWidth() && ScreenshotA.getHeight() == ScreenshotB.getHeight()) {
			int[] pixels1 = new int[ScreenshotA.getWidth() * ScreenshotA.getHeight()];
			int[] pixels2 = new int[ScreenshotB.getWidth() * ScreenshotB.getHeight()];
			ScreenshotA.getPixels(pixels1, 0, ScreenshotA.getWidth(), 0, 0, ScreenshotA.getWidth(), ScreenshotA.getHeight());
			ScreenshotB.getPixels(pixels2, 0, ScreenshotB.getWidth(), 0, 0, ScreenshotB.getWidth(), ScreenshotB.getHeight());
			if (Arrays.equals(pixels1, pixels2)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}