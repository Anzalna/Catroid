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

import android.content.Context;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.BroadcastScript;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.AddItemToUserListBrick;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.BroadcastBrick;
import org.catrobat.catroid.content.bricks.BroadcastReceiverBrick;
import org.catrobat.catroid.content.bricks.BroadcastWaitBrick;
import org.catrobat.catroid.content.bricks.ChangeVariableBrick;
import org.catrobat.catroid.content.bricks.ForeverBrick;
import org.catrobat.catroid.content.bricks.LoopBeginBrick;
import org.catrobat.catroid.content.bricks.LoopEndBrick;
import org.catrobat.catroid.content.bricks.RepeatBrick;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.content.bricks.WaitBrick;
import org.catrobat.catroid.content.bricks.WhenBrick;
import org.catrobat.catroid.content.bricks.WhenTouchDownBrick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.FormulaElement;
import org.catrobat.catroid.formulaeditor.Functions;
import org.catrobat.catroid.formulaeditor.Operators;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.io.XstreamSerializer;
import org.catrobat.catroid.test.cucumber.util.CallbackBrick;
import org.catrobat.catroid.test.cucumber.util.PrintBrick;
import org.catrobat.catroid.test.cucumber.util.Util;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.uiespresso.util.actions.CustomActions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertNotNull;
import static org.catrobat.catroid.formulaeditor.FormulaElement.ElementType;
import static org.catrobat.catroid.formulaeditor.FormulaElement.ElementType.NUMBER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;

// CHECKSTYLE DISABLE MethodNameCheck FOR 1000 LINES
@RunWith(AndroidJUnit4.class)
public class ProgramSteps {
	private final Object programStartWaitLock = new Object();
	private boolean programHasStarted = false;
	private int programWaitLockPermits = 1;
	private Semaphore programWaitLock;
	private OutputStream outputStream;
	private static final List<Object> INITIALIZED_LIST_VALUES = new ArrayList<Object>();
	private String projectName = "Zulfiqar";


	@Rule
	public ActivityTestRule<MainMenuActivity> launchActivityRule = new ActivityTestRule<>(MainMenuActivity.class);
	//public ActivityTestRule<ProjectActivity> launchActivityRule = new ActivityTestRule<>(ProjectActivity.class);
	@Test
	public void name(){
		SystemClock.sleep(10000);
		//onView(isRoot()).perform(CustomActions.wait(200));
	}
	@Given("^I have a program$")
	public void I_have_a_program()  {
		Project project = new Project(InstrumentationRegistry.getTargetContext(), projectName);
		ProjectManager.getInstance().setProject(project);
		Cucumber.put(Cucumber.KEY_PROJECT, project);
	}
	@Given("^I have a program with landscape$")
	public void iHaveAProgramWithLandscape(){
		Project project = new Project(InstrumentationRegistry.getTargetContext(), projectName, true);
		ProjectManager.getInstance().setProject(project);
		Cucumber.put(Cucumber.KEY_PROJECT, project);
	}
	@Given("^this program has an object '(\\w+)'$")
	public void program_has_object(String name) throws IOException {
		int lookId = org.catrobat.catroid.R.drawable.default_project_bird_wing_down;
		Project project = new Project(InstrumentationRegistry.getTargetContext(), projectName);
		ProjectManager.getInstance().setProject(project);
		Sprite sprite = Util.addNewObjectWithLook(getContext(), project, name, lookId);
		Cucumber.put(Cucumber.KEY_CURRENT_OBJECT, sprite);
	}
	@And("^this program has an object \"(.*?)\"$")
	public void thisProgramHasAnObject(String name) throws Throwable {
		int lookId = R.drawable.default_project_bird_wing_down;
		Project project = new Project(InstrumentationRegistry.getTargetContext(), projectName);
		ProjectManager.getInstance().setProject(project);
		Sprite sprite = Util.addNewObjectWithLook(getContext(), project, name, lookId);
		Cucumber.put(Cucumber.KEY_CURRENT_OBJECT, sprite);
	}

	@Given("^'(\\w+)' has a start script$")
	public void object_has_start_script(String object) {
		programWaitLockPermits -= 1;
		Project project = ProjectManager.getInstance().getCurrentProject();
		ProjectManager.getInstance().setProject(project);
		Sprite sprite = Util.findSprite(project, object);
		StartScript script = new StartScript(sprite);
		script.addBrick(new CallbackBrick(sprite, new CallbackBrick.BrickCallback() {

			@Override
			public void onCallback() {
				synchronized (programStartWaitLock) {
					if (!programHasStarted) {
						programHasStarted = true;
						programStartWaitLock.notify();
					}
				}
			}
		}));
		sprite.addScript(script);
		Cucumber.put(Cucumber.KEY_CURRENT_SCRIPT, script);
	}
	@And("^when program starts$")
	public void whenProgramStarts()  {
		programWaitLockPermits -= 1;
		Project project = ProjectManager.getInstance().getCurrentProject();
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Sprite sprite = Util.findSprite(project, String.valueOf(object));
		StartScript script = new StartScript(sprite);

		script.addBrick(new CallbackBrick(sprite, new CallbackBrick.BrickCallback() {
			@Override
			public void onCallback() {
				synchronized (programStartWaitLock) {
					if (!programHasStarted) {
						programHasStarted = true;
						programStartWaitLock.notify();
					}
				}
			}
		}));
		sprite.addScript(script);
		Cucumber.put(Cucumber.KEY_CURRENT_SCRIPT, script);
	}

	@Given("^'(\\w+)' has a When '(\\w+)' script$")
	public void object_has_a_when_script(String object, String message) {
		programWaitLockPermits -= 1;
		Project project = ProjectManager.getInstance().getCurrentProject();
		Sprite sprite = Util.findSprite(project, object);
		BroadcastScript script = new BroadcastScript( message);

		sprite.addScript(script);
		Cucumber.put(Cucumber.KEY_CURRENT_SCRIPT, script);
	}

	@And("^this script has a set '(\\w+)' to (\\d+.?\\d*) brick$")
	public void script_has_set_var_to_val_brick(String a, String b) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable userVariable = new UserVariable(a);
		project.getDefaultScene().getDataContainer().addUserVariable(userVariable);
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);

		FormulaElement elemB = new FormulaElement(ElementType.NUMBER, b, null);

		Brick brick = new SetVariableBrick( new Formula(elemB), varA);
		script.addBrick(brick);
	}
	@And("^set '(\\w+)' with the no of items in the list \"([^\"]*)\"$")
	public void thisScriptHasASetTestWithTheNoOfItemsInTheListTimeUr(String a, String b) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable userVariable = new UserVariable(a);
		project.getDefaultScene().getDataContainer().addUserVariable(userVariable);
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);

		FormulaElement indexFormulaElement = new FormulaElement(ElementType.USER_LIST, b, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.NUMBER_OF_ITEMS.name(), null, indexFormulaElement);

		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}
	@And("^set '(\\w+)' with the element (\\d+) of \"([^\"]*)\"$")
	public void setTestWithTheElementOf(String a, String b, String c) throws Throwable {

		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable userVariable = new UserVariable(a);
		project.getDefaultScene().getDataContainer().addUserVariable(userVariable);
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);

		FormulaElement aaa = new FormulaElement(ElementType.NUMBER, b, null);
		FormulaElement indexFormulaElement = new FormulaElement(ElementType.USER_LIST, c, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.LIST_ITEM.name(), null, aaa, indexFormulaElement);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}
	@And("^set '(\\w+)' with the \"([^\"]*)\" contain (\\d+)$")
	public void thisScriptHasASetTestWithTheNamelistTimeValue(String a, String b, String c) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable userVariable = new UserVariable(a);
		project.getDefaultScene().getDataContainer().addUserVariable(userVariable);
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);

		FormulaElement aaa = new FormulaElement(ElementType.NUMBER, c, null);
		FormulaElement indexFormulaElement = new FormulaElement(ElementType.USER_LIST, b, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.CONTAINS.name(), null, indexFormulaElement, aaa);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@And("^set '(\\w+)' to letter no '(\\d+)' of \"([^\"]*)\"$")
	public void thisScriptHasASetTestLetterNoTo(String a, String b, String c) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable userVariable = new UserVariable(a);
		project.getDefaultScene().getDataContainer().addUserVariable(userVariable);
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);

		FormulaElement aaa = new FormulaElement(ElementType.STRING, c, null);
		FormulaElement indexFormulaElement = new FormulaElement(ElementType.NUMBER, b, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.LETTER.name(), null, indexFormulaElement, aaa);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}
	@And("^set '(\\w+)' with join \"([^\"]*)\" and \"([^\"]*)\"$")
	public void thisScriptHasASetTestWithStringAndBrick1(String a, String b, String c)  {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable userVariable = new UserVariable(a);
		project.getDefaultScene().getDataContainer().addUserVariable(userVariable);
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);

		FormulaElement aaa = new FormulaElement(ElementType.STRING, b, null);
		FormulaElement bbb = new FormulaElement(ElementType.STRING, c, null);
		//FormulaElement indexFormulaElement = new FormulaElement(ElementType.NUMBER, b, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.JOIN.name(), null, bbb, aaa);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}
	@And("^set '(\\w+)' to length \"([^\"]*)\"$")
	public void thisScriptHasASetTestToLengthBrick(String a, String b)  {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable userVariable = new UserVariable(a);
		project.getDefaultScene().getDataContainer().addUserVariable(userVariable);
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);

		FormulaElement indexFormulaElement = new FormulaElement(ElementType.STRING, b, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.LENGTH.name(), null, indexFormulaElement, null);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@And("^set \"(.*?)\" to (\\d+.?\\d*)$")
	public void thisScriptHasASetToBrick(String a, String b) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable userVariable = new UserVariable(a);
		project.getDefaultScene().getDataContainer().addUserVariable(userVariable);
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);

		FormulaElement elemB = new FormulaElement(ElementType.NUMBER, b, null);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}
	@And("^set '(\\w+)' have set \"(.*?)\" \\+ set '(\\w+)'$")
	public void thisScriptHasASetPlusSetI(String a, String b, String c) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable userVariable = new UserVariable(a);
		project.getDefaultScene().getDataContainer().addUserVariable(userVariable);
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);

		FormulaElement elemB = new FormulaElement(ElementType.OPERATOR, Operators.PLUS.name(), null,
				new FormulaElement(ElementType.USER_VARIABLE, b, null), new FormulaElement(ElementType.USER_VARIABLE,
				c, null));

		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);

	}
	@And("^this script add '(\\d+)' to list name '(\\w+)' brick$")
	public void thisScriptAddToListNameDdBrick(int a, String b) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserList userList = new UserList(b);
		project.getDefaultScene().getDataContainer().addUserList(userList);
		UserList userList1 = project.getDefaultScene().getDataContainer().getUserList(object, b);

		//userList1.setList(INITIALIZED_LIST_VALUES);
		FormulaElement elemB = new FormulaElement(ElementType.NUMBER, a, null);
		Brick brick = new AddItemToUserListBrick(new Formula(elemB), userList1);
		script.addBrick(brick);
	}


	@And("^add (\\d+) to list \"([^\"]*)\"$")
	public void thisScriptAddToListNameDdBrickUr(String a, String b) throws Throwable {

		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

        UserList userList = new UserList(b);
        project.getDefaultScene().getDataContainer().addUserList(userList);

        UserList userList1 = project.getDefaultScene().getDataContainer().getUserList(object, b);
        userList1.setList(INITIALIZED_LIST_VALUES);

		FormulaElement elemB = new FormulaElement(ElementType.NUMBER, a, null);
		Brick brick = new AddItemToUserListBrick(new Formula(elemB), userList1);
		script.addBrick(brick);
	}

	@And("^when receive '(\\w+)'$")
	public void whenIReceivedHellöo(String message) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		BroadcastReceiverBrick brick = new BroadcastReceiverBrick(message);
		script.addBrick(brick);
	}
	@And("^When screen is touched$")
	public void whenScreenIsTouched()  {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Brick brick = new WhenTouchDownBrick();
		script.addBrick(brick);
	}

	@And("^When tapped$")
	public void whenTapped()  {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Brick brick1 = new WhenBrick();
		script.addBrick(brick1);
	}

	@And("^forever$")
	public void Foreeverever()  {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Brick brick = new ForeverBrick();
		script.addBrick(brick);
	}

	@And("^forever end$")
	public void thisScriptHasAForeverEndBrick() throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		LoopBeginBrick loopBeginBrick = (LoopBeginBrick) Cucumber.get(Cucumber.KEY_LOOP_BEGIN_BRICK);
		Brick brick = new LoopEndBrick(object, loopBeginBrick);
		script.addBrick(brick);
	}

	@And("^this script has a set '(\\w+)' to '(\\w+)' brick$")
	public void script_has_set_var_to_var_brick(String a, String b) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable userVariable = new UserVariable(a);
		project.getDefaultScene().getDataContainer().addUserVariable(userVariable);
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, a);

		FormulaElement elemB = new FormulaElement(ElementType.USER_VARIABLE, b, null);

		Brick brick = new SetVariableBrick( new Formula(elemB), variable);
		script.addBrick(brick);
	}

	@And("^this script has a change '(\\w+)' by (\\d+.?\\d*) brick$")
	public void script_has_change_var_by_val_brick(String name, String value) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable userVariable = new UserVariable(name);
		project.getDefaultScene().getDataContainer().addUserVariable(userVariable);
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, name);

		FormulaElement elemValue = new FormulaElement(ElementType.NUMBER, value, null);
		Brick brick = new ChangeVariableBrick( new Formula(elemValue), variable);
		script.addBrick(brick);
	}
	@And("^change '(\\w+)' by (\\d+.?\\d*)$")
	public void script_has_change_var_by_val_brick1(String name, String value) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable userVariable = new UserVariable(name);
		project.getDefaultScene().getDataContainer().addUserVariable(userVariable);
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, name);

		FormulaElement elemValue = new FormulaElement(NUMBER, value, null);
		Brick brick = new ChangeVariableBrick(new Formula(elemValue), variable);
		script.addBrick(brick);
	}

	@And("^this script has a Repeat (\\d+) times brick$")
	public void script_has_repeat_times_brick(int iterations) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		Brick brick = new RepeatBrick( new Formula(iterations));
		Cucumber.put(Cucumber.KEY_LOOP_BEGIN_BRICK, brick);
		script.addBrick(brick);
	}

    @And("^this script has a Repeat end brick$")
    public void script_has_repeat_end_brick() {
        Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
        Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

        LoopBeginBrick loopBeginBrick = (LoopBeginBrick) Cucumber.get(Cucumber.KEY_LOOP_BEGIN_BRICK);
        Brick brick = new LoopEndBrick(object, loopBeginBrick);
        script.addBrick(brick);
    }
	@And("^this script has a Broadcast '(\\w+)' brick$")
	public void script_has_broadcast_brick(String message) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		BroadcastBrick brick = new BroadcastBrick(message);
		script.addBrick(brick);
	}

	@And("^this script has a BroadcastWait '(\\w+)' brick$")
	public void script_has_broadcast_wait_brick(String message) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		BroadcastWaitBrick brick = new BroadcastWaitBrick(message);
		script.addBrick(brick);
	}
	@And("^broadcastWait '(\\w+)'$")
	public void script_has_broadcast_wait_brick1(String message) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		BroadcastWaitBrick brick = new BroadcastWaitBrick( message);
		script.addBrick(brick);
	}

	@And("^broadcast '(\\w+)'$")
	public void script_has_broadcast_brick1(String message) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		BroadcastBrick brick = new BroadcastBrick( message );
		script.addBrick(brick);
	}

	@And("^this script has a Wait (\\d+) milliseconds brick$")
	public void script_has_wait_ms_brick(int millis) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		WaitBrick brick = new WaitBrick(millis);
		script.addBrick(brick);
	}

	@And("^this script has a Wait (\\d+.?\\d*) seconds? brick$")
	public void script_has_wait_s_brick(int seconds) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		WaitBrick brick = new WaitBrick( seconds * 1000);
		script.addBrick(brick);
	}

	@And("^this script has a Print brick with '(.*)'$")
	public void script_has_a_print_brick_s(String text) {
		script_has_a_print_brick(text);
	}

	@And("^this script has a Print brick with$")
	public void script_has_a_print_brick(String text) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		if (outputStream == null) {
			outputStream = new ByteArrayOutputStream();
		}
		PrintBrick brick = new PrintBrick(object, text);
		brick.setOutputStream(outputStream);
		script.addBrick(brick);
	}


	@When("^I start the program$")
	public void I_start_the_program() throws InterruptedException {
		programWaitLock = new Semaphore(programWaitLockPermits);
		addScriptEndCallbacks();
		launchActivityRule.launchActivity(null);
		onView(withText(R.string.main_menu_continue)).perform(click());
		onView(withId(R.id.button_play)).perform(click());

        synchronized (programStartWaitLock) {
			if (!programHasStarted) {
				programStartWaitLock.wait(10000);
			}
		}
	}
	private void addScriptEndCallbacks() {
		Project project = ProjectManager.getInstance().getCurrentProject();
		for (Sprite sprite : project.getDefaultScene().getSpriteList()) {
			for (int i = 0; i < sprite.getNumberOfScripts(); i++) {
				sprite.getScript(i).addBrick(new CallbackBrick(sprite, new CallbackBrick.BrickCallback() {
					@Override
					public void onCallback() {
						programWaitLock.release();
					}
				}));
			}
		}
	}
	@And("^I wait until the program has stopped$")
	public void wait_until_program_has_stopped() throws InterruptedException {
		// While there are still scripts running, the available permits should be < 1.
		programWaitLock.tryAcquire(1, 60, TimeUnit.MILLISECONDS);
	}

	@And("^set '(\\w+)' to (\\d+.?\\d*)$")
	public void script_has_set_var_to_val_brick1(String a, String b) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable userVariable = new UserVariable(a);
		project.getDefaultScene().getDataContainer().addUserVariable(userVariable);
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, a);

		FormulaElement elemB = new FormulaElement(ElementType.NUMBER, b, null);
		Brick brick = new SetVariableBrick(new Formula(elemB), variable);
		script.addBrick(brick);
	}

	@And("^wait (\\d+.?\\d*) seconds$")
	public void script_has_wait_s_brick1(int seconds) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		WaitBrick brick = new WaitBrick(seconds * 1000);
		script.addBrick(brick);
	}
	@Then("^I should see the printed output '(.*)'$")
	public void I_should_see_printed_output_s(String text) throws IOException {
		I_should_see_printed_output(text);
	}

	@Then("^I should see the printed output$")
	public void I_should_see_printed_output(String text) throws IOException {
		String actual = outputStream.toString().replace(System.getProperty("line.separator"), "");
		String expected = text.replace(System.getProperty("line.separator"), "");

		assertEquals("The printed output is wrong.", expected, actual);
		outputStream.close();
	}
	@Then("^the variable '(\\w+)' should be equal (\\d+.?\\d*)$")
	public void var_should_equal_float(String name, double expected) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, name);
        Assert.assertNotNull("The variable does not exist.", variable);
		double actual = (double) variable.getValue();
		assertThat("The variable is != the value.", actual, equalTo(expected));
	}
	@Then("^the variable '(\\w+)' should be less than or equal (\\d+.?\\d*)$")
	public void var_should_less_than_equal_float(String name, double expected) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, name);
		assertNotNull("The variable does not exist.", variable);

		double actual = (double) variable.getValue();
		assertThat("The variable is > than the value.", actual, lessThanOrEqualTo(expected));
	}
	@Then("^the '(\\w+)' should be equal to (\\d+.?\\d*)$")
	public void theLengthOfTheStringShouldBeEqualTo(String a, double expected) throws Throwable {

		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, a);

		double actual = (double) variable.getValue();
		assertThat("The variable is != the value.", actual, equalTo(expected));
	}
	@Then("^the object should be equal to \"(.*?)\"$")
	public void theNameOfTheObjectShouldBeEqualToObject(String name) throws Throwable {
		List<Sprite> spriteList = ProjectManager.getInstance().getCurrentProject().getDefaultScene().getSpriteList();
		assertEquals("Name of object is not as expected", spriteList.get(1).getName(), name);
	}
	@Then("^the '(\\w+)' should be true$")
	public void ifTheListContainElementTheVarShouldTrueOtherwiseFalse(String a) throws Throwable {
		Project project = ProjectManager.getInstance().getCurrentProject();
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		assertNotNull("The userlist does not exist.", variable);
		String expected = "1.0";
		double actual = (double) variable.getValue();
		assertThat("The userlist does not contain the element.", String.valueOf(actual), equalTo(expected));
	}
	@Then("^the '(\\w+)' should be equal to \"([^\"]*)\"$")
	public void theTestShouldBeEqualToTo(String a, String text) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		String expected = text;
		String actual = (String) variable.getValue();
		assertThat("The variable is != the value.", String.valueOf(actual), equalTo(expected));
	}

}