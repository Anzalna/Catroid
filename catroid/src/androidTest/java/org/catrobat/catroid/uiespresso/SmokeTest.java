/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
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
package org.catrobat.catroid.uiespresso;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.R;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.ProjectActivity;
import org.catrobat.catroid.uiespresso.annotations.Flaky;
import org.catrobat.catroid.uiespresso.testsuites.Level;
import org.catrobat.catroid.uiespresso.util.rules.DontGenerateDefaultProjectActivityInstrumentationRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.catrobat.catroid.common.SharedPreferenceKeys.AGREED_TO_PRIVACY_POLICY_PREFERENCE_KEY;
import static org.catrobat.catroid.common.SharedPreferenceKeys.SHOW_COPY_PROJECTS_FROM_EXTERNAL_STORAGE_DIALOG;
import static org.catrobat.catroid.uiespresso.util.UiTestUtils.assertCurrentActivityIsInstanceOf;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class SmokeTest {

	@Rule
	public DontGenerateDefaultProjectActivityInstrumentationRule<MainMenuActivity> baseActivityTestRule = new
			DontGenerateDefaultProjectActivityInstrumentationRule<>(MainMenuActivity.class);

	private boolean bufferedPrivacyPolicyPreferenceSetting;
	private boolean bufferedImportFromExternalStoragePreferenceSetting;

	@Before
	public void setUp() throws Exception {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext());

		bufferedPrivacyPolicyPreferenceSetting = sharedPreferences
				.getBoolean(AGREED_TO_PRIVACY_POLICY_PREFERENCE_KEY, false);

		bufferedImportFromExternalStoragePreferenceSetting = sharedPreferences
				.getBoolean(SHOW_COPY_PROJECTS_FROM_EXTERNAL_STORAGE_DIALOG, false);

		sharedPreferences
				.edit()
				.putBoolean(AGREED_TO_PRIVACY_POLICY_PREFERENCE_KEY, true)
				.putBoolean(SHOW_COPY_PROJECTS_FROM_EXTERNAL_STORAGE_DIALOG, false)
				.commit();
		baseActivityTestRule.launchActivity(null);
	}

	@After
	public void tearDown() {
		PreferenceManager.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext())
				.edit()
				.putBoolean(AGREED_TO_PRIVACY_POLICY_PREFERENCE_KEY, bufferedPrivacyPolicyPreferenceSetting)
				.putBoolean(SHOW_COPY_PROJECTS_FROM_EXTERNAL_STORAGE_DIALOG, bufferedImportFromExternalStoragePreferenceSetting)
				.commit();
	}

	@Test
	@Flaky
	@Category(Level.Smoke.class)
	public void newProject() {
		onView(withText(R.string.main_menu_new))
				.perform(click());

		//check if dialog title is displayed
		onView(withText(R.string.new_project_dialog_title))
				.check(matches(isDisplayed()));

		onView(allOf(withId(android.R.id.button1), withText(R.string.ok)))
				.check(matches(allOf(isDisplayed(), not(isEnabled()))));

		onView(allOf(withId(R.id.input_edit_text), isDisplayed()))
				.perform(replaceText("TestProject"), closeSoftKeyboard());

		onView(allOf(withId(android.R.id.button1), withText(R.string.ok)))
				.perform(click());

		//check if orientation dialog is displayed
		onView(withText(R.string.project_orientation_title))
				.check(matches(isDisplayed()));

		onView(allOf(withId(android.R.id.button1), withText(R.string.ok)))
				.perform(click());

		//check if user ends up in right activity either by checking activity itself:
		assertCurrentActivityIsInstanceOf(ProjectActivity.class);

		//add sprite
		onView(withId(R.id.button_add))
				.perform(click());

		//check if new object dialog is displayed
		onView(withText(R.string.new_sprite_dialog_title))
				.check(matches(isDisplayed()));
		//cancel by back
		pressBack();

		//something you shouldn't do in the first place, but here's how to wait:
		//onView(isRoot()).perform(CustomActions.wait(5000));
	}
}
