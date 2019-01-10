/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2015 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.test.cucumber;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import com.robotium.solo.Solo;
import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.ui.MainMenuActivity;
import java.io.IOException;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class BeforeAfterSteps extends ActivityInstrumentationTestCase2<MainMenuActivity> {
	private Solo solo;
	public static final String TAG = "cucumber-android";
	public BeforeAfterSteps() {
		super(MainMenuActivity.class);
	}
	@Before
	public void before() {
		Log.d(BeforeAfterSteps.TAG, "before step");
		solo = new Solo(getInstrumentation(), getActivity());
	}
	@After
	public void after() throws IOException {
		Log.d(BeforeAfterSteps.TAG, "after step");
		solo.finishOpenedActivities();
		ProjectManager.getInstance().deleteCurrentProject(null);
	}
}
