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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.view.Display;

import com.robotium.solo.Solo;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.BroadcastScript;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.WhenTouchDownScript;
import org.catrobat.catroid.content.bricks.AddItemToUserListBrick;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.BroadcastBrick;
import org.catrobat.catroid.content.bricks.BroadcastReceiverBrick;
import org.catrobat.catroid.content.bricks.BroadcastWaitBrick;
import org.catrobat.catroid.content.bricks.ChangeVariableBrick;
import org.catrobat.catroid.content.bricks.ForeverBrick;
import org.catrobat.catroid.content.bricks.HideTextBrick;
import org.catrobat.catroid.content.bricks.LoopBeginBrick;
import org.catrobat.catroid.content.bricks.LoopEndBrick;
import org.catrobat.catroid.content.bricks.RepeatBrick;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.content.bricks.ShowTextBrick;
import org.catrobat.catroid.content.bricks.SpeakBrick;
import org.catrobat.catroid.content.bricks.StopScriptBrick;
import org.catrobat.catroid.content.bricks.TurnLeftBrick;
import org.catrobat.catroid.content.bricks.WaitBrick;
import org.catrobat.catroid.content.bricks.WhenBrick;
import org.catrobat.catroid.content.bricks.WhenTouchDownBrick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.FormulaElement;
import org.catrobat.catroid.formulaeditor.Functions;
import org.catrobat.catroid.formulaeditor.Operators;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.physics.content.bricks.TurnLeftSpeedBrick;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.test.cucumber.util.CallbackBrick;
import org.catrobat.catroid.test.cucumber.util.PrintBrick;
import org.catrobat.catroid.test.cucumber.util.Util;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.ProjectActivity;
import org.hamcrest.Matchers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.catrobat.catroid.formulaeditor.FormulaElement.ElementType;
import static org.catrobat.catroid.formulaeditor.FormulaElement.ElementType.NUMBER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.Is.is;

import static java.text.DateFormat.Field.SECOND;

// CHECKSTYLE DISABLE MethodNameCheck FOR 1000 LINES
public class ProgramSteps extends AndroidTestCase {
	private final Object programStartWaitLock = new Object();
	private boolean programHasStarted = false;
	// Decrement once for every new script.
	private int programWaitLockPermits = 1;
	// Release once for each script that ends.
	// Should be == 1 after every script ended.
	private Semaphore programWaitLock;
	private OutputStream outputStream;
	private static final List<Object> INITIALIZED_LIST_VALUES = new ArrayList<Object>();

	@Given("^I have a program$")
	public void I_have_a_program() throws IOException {
		ProjectManager pm = ProjectManager.getInstance();
		pm.initializeDefaultProject(getContext());
		pm.initializeNewProject("Cucumber16", getContext(), true, false, false, false, false);
		Project project = pm.getCurrentProject();
		Cucumber.put(Cucumber.KEY_PROJECT, project);
	}

	@Given("^I have a program with landscape$")
	public void iHaveAProgramWithLandscape() throws Throwable {
		ProjectManager pm = ProjectManager.getInstance();
		pm.initializeDefaultProject(getContext());
		pm.initializeNewProject("Cucumber14", getContext(), true, false, true, false, false);
		Project project = pm.getCurrentProject();
		Cucumber.put(Cucumber.KEY_PROJECT, project);
	}

	/*@Given("^this program has an Object '(\\w+)'$")
	public void program_has_object(String name) {
		int lookId = R.drawable.default_project_bird_wing_down;
		Sprite sprite = Util.addNewObjectWithLook(getContext(), name, lookId);
		Cucumber.put(Cucumber.KEY_CURRENT_OBJECT, sprite);
	}
*/
	@And("^this program has an object \"(.*?)\"$")
	public void thisProgramHasAnObject(String name) throws Throwable {
		int lookId = R.drawable.default_project_bird_wing_down;
		ProjectManager pm = ProjectManager.getInstance();
		Project project = pm.getCurrentProject();
		Sprite sprite = Util.addNewObjectWithLook(getContext(), project, name, lookId);
		Cucumber.put(Cucumber.KEY_CURRENT_OBJECT, sprite);
	}

	@Given("^this program has an object '(\\w+)'$")
	public void program_has_object(String name) {
		int lookId = org.catrobat.catroid.R.drawable.default_project_bird_wing_down;
		ProjectManager pm = ProjectManager.getInstance();
		Project project = pm.getCurrentProject();
		Sprite sprite = Util.addNewObjectWithLook(getContext(), project, name, lookId);

		Cucumber.put(Cucumber.KEY_CURRENT_OBJECT, sprite);
	}

	@Given("^'(\\w+)' has a start script$")
	public void object_has_start_script(String object) {
		programWaitLockPermits -= 1;
		Project project = ProjectManager.getInstance().getCurrentProject();
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
	public void whenProgramStarts() throws Throwable {
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

	@Given("^'(\\w+)' has a when screen is touched script$")
	public void objectHasAWhenScreenIsTouchedScript(String object) throws Throwable {
		programWaitLockPermits -= 1;
		Project project = ProjectManager.getInstance().getCurrentProject();
		Sprite sprite = Util.findSprite(project, object);
		WhenTouchDownScript script = new WhenTouchDownScript();
		sprite.addScript(script);
		Cucumber.put(Cucumber.KEY_CURRENT_SCRIPT, script);
	}

	@And("^tap the screen (\\d+) times$")
	public void clickOnTheScreen(int x) throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		//solo.clickOnScreen( 100, 200, 1);
		Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		solo.sleep(100);
		solo.clickOnScreen(width / 2, height / 2, x);
	}

	@And("^When it turn left (\\d+)$")
	public void turnleft(String a) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		FormulaElement elemB1 = new FormulaElement(ElementType.NUMBER, a, null);
		Brick brick = new TurnLeftBrick(new Formula(elemB1));
		script.addBrick(brick);
	}

	@And("^When it rotate left (\\d+)$")
	public void rotateleft(String a) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		FormulaElement elemB1 = new FormulaElement(ElementType.NUMBER, a, null);
		Brick brick = new TurnLeftSpeedBrick(new Formula(elemB1));
		script.addBrick(brick);
	}

	@And("^When screen is touched$")
	public void whenScreenIsTouched() throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Brick brick = new WhenTouchDownBrick();
		script.addBrick(brick);
	}

	@And("^When tapped$")
	public void whenTapped() throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Brick brick1 = new WhenBrick();
		script.addBrick(brick1);
	}

	@And("^forever$")
	public void Foreeverever() throws Throwable {
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

	@And("^hide variable '(\\w+)'$")
	public void whenvariablehide(String a) throws Throwable {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Brick brick = new HideTextBrick();
		script.addBrick(brick);
	}

	@And("^set '(\\w+)' have set \"(.*?)\" \\+ set '(\\w+)'$")
	public void thisScriptHasASetPlusSetI(String a, String b, String c) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}
		FormulaElement elemB = new FormulaElement(ElementType.OPERATOR, Operators.PLUS.name(), null,
				new FormulaElement(ElementType.USER_VARIABLE, b, null), new FormulaElement(ElementType.USER_VARIABLE,
				c, null));

		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@And("^set '(\\w+)' to \"(.*?)\"$")
	public void thisScriptHasASetIToBrick1(String a, String b) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}
		FormulaElement elemB = new FormulaElement(ElementType.STRING, b, null);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@And("^Show Variable '(\\w+)' at X (\\d+) Y (\\d+)$")
	public void showVariableXAtXY(String a, String c, String d) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}
		FormulaElement elemB1 = new FormulaElement(ElementType.NUMBER, c, null);
		FormulaElement elemB2 = new FormulaElement(ElementType.NUMBER, d, null);
		Brick brick = new ShowTextBrick(new Formula(elemB1), new Formula(elemB2), varA);
		script.addBrick(brick);
	}

	@And("^set '(\\w+)' have set '(\\w+)' \\- set '(\\w+)'$")
	public void thisScriptHasASetKMinusSetI(String a, String b, String c) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}
		FormulaElement elemB = new FormulaElement(ElementType.OPERATOR, Operators.MINUS.name(), null,
				new FormulaElement(ElementType.USER_VARIABLE, b, null), new FormulaElement(ElementType.USER_VARIABLE,
				c, null));

		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@And("^set '(\\w+)' to (\\d+.?\\d*)$")
	public void script_has_set_var_to_val_brick(String a, String b) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		//Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		Project project = ProjectManager.getInstance().getCurrentProject();

		//String c = String.valueOf(200);
		//String d = String.valueOf(800);
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}
		FormulaElement elemB = new FormulaElement(ElementType.NUMBER, b, null);
		//varA.equals(20);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
/*
		Brick bb = new WaitBrick(1000);
		script.addBrick(bb);

		//FormulaElement elemB0 = new FormulaElement(ElementType.NUMBER, b, null);
		FormulaElement elemB1 = new FormulaElement(NUMBER,c, null);
		FormulaElement elemB2 = new FormulaElement(ElementType.NUMBER,d, null);
		Brick brick1 = new ShowTextBrick(new Formula(elemB1),new Formula(elemB2));
		script.addBrick(brick1);
		solo.sleep(9000);
		*/
	}

	@And("^this script has a set '(\\w+)' to (\\d+.?\\d*) brick$")
	public void script_has_set_var_to_val_brick1(String a, String b) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}

		FormulaElement elemB = new FormulaElement(FormulaElement.ElementType.NUMBER, b, null);

		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@And("^set '(\\w+)' to '(\\w+)'$")
	public void script_has_set_var_to_var_brick(String a, String b) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}
		FormulaElement elemB = new FormulaElement(ElementType.USER_VARIABLE, b, null);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@And("^this script has a set '(\\w+)' to '(\\w+)' brick$")
	public void script_has_set_var_to_var_brick1(String a, String b) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}
		FormulaElement elemB = new FormulaElement(FormulaElement.ElementType.USER_VARIABLE, b, null);

		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@And("^this script has a set '(\\w+)' to \"(.*?)\" brick$")
	public void thisScriptHasASetIToBrick(String a, String b) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}
		FormulaElement elemB = new FormulaElement(ElementType.STRING, b, null);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@And("^change '(\\w+)' by (\\d+.?\\d*)$")
	public void script_has_change_var_by_val_brick(String name, String value) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, name);
		if (variable == null) {
			variable = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, name);
		}
		FormulaElement elemValue = new FormulaElement(NUMBER, value, null);
		Brick brick = new ChangeVariableBrick(new Formula(elemValue), variable);
		script.addBrick(brick);
	}

	@And("^this script has a change '(\\w+)' by (\\d+.?\\d*) brick$")
	public void script_has_change_var_by_val_brick1(String name, String value) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, name);
		if (variable == null) {
			variable = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, name);
		}

		FormulaElement elemValue = new FormulaElement(FormulaElement.ElementType.NUMBER, value, null);

		Brick brick = new ChangeVariableBrick(new Formula(elemValue), variable);
		script.addBrick(brick);
	}

	@And("^this script has a Repeat (\\d+) times brick$")
	public void script_has_repeat_times_brick(int iterations) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		Brick brick = new RepeatBrick(new Formula(iterations));
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

	@And("^broadcast '(\\w+)'$")
	public void script_has_broadcast_brick(String message) {
		//Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		BroadcastBrick brick = new BroadcastBrick( message);
		script.addBrick(brick);
	}

	@And("^this script has a Broadcast '(\\w+)' brick$")
	public void script_has_broadcast_brick1(String message) {
		//Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		BroadcastBrick brick = new BroadcastBrick(message);
		script.addBrick(brick);
	}

	@And("^broadcastWait '(\\w+)'$")
	public void script_has_broadcast_wait_brick(String message) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		BroadcastWaitBrick brick = new BroadcastWaitBrick( message);
		script.addBrick(brick);
	}

	@And("^this script has a BroadcastWait '(\\w+)' brick$")
	public void script_has_broadcast_wait_brick1(String message) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		BroadcastWaitBrick brick = new BroadcastWaitBrick( message);
		script.addBrick(brick);
	}

	/*@And("^when receive '(\\w+)'$")
	public void whenIReceivedHello(String message) throws Throwable {
		programWaitLockPermits -= 1;
		String object = "Object";
		//Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		Sprite sprite = Util.findSprite(project, object);
		BroadcastScript script = new BroadcastScript( message);
		sprite.addScript(script);
		Cucumber.put(Cucumber.KEY_CURRENT_SCRIPT, script);
	}*/

	@And("^when receive '(\\w+)'$")
	public void whenIReceivedHellöo(String message) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		BroadcastReceiverBrick brick = new BroadcastReceiverBrick( message);
		script.addBrick(brick);
	}
	@And("^add (\\d+) to list \"([^\"]*)\"$")
	public void thisScriptAddToListNameDdBrickUr(String a, String b) throws Throwable {

		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserList varA = project.getDefaultScene().getDataContainer().addSpriteUserListToSprite(object, b);
		varA.setList(INITIALIZED_LIST_VALUES);
		FormulaElement elemB = new FormulaElement(ElementType.NUMBER, a, null);
		Brick brick = new AddItemToUserListBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@And("^set '(\\w+)' with the no of items in the list \"([^\"]*)\"$")
	public void thisScriptHasASetTestWithTheNoOfItemsInTheListTimeUr(String a, String b) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}
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

		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}
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

		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}
		FormulaElement aaa = new FormulaElement(ElementType.NUMBER, c, null);
		FormulaElement indexFormulaElement = new FormulaElement(ElementType.USER_LIST, b, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.CONTAINS.name(), null, indexFormulaElement, aaa);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@And("^this script has a Wait (\\d+) milliseconds brick$")
	public void script_has_wait_ms_brick(int millis) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		WaitBrick brick = new WaitBrick(millis);
		script.addBrick(brick);
	}

	@And("^wait (\\d+.?\\d*) seconds$")
	public void script_has_wait_s_brick(int seconds) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		WaitBrick brick = new WaitBrick(seconds * 1000);
		script.addBrick(brick);
	}
	@And("^I wait for (\\d+.?\\d*) seconds$")
	public void script_has_wait_s_brick2(int seconds) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		WaitBrick brick = new WaitBrick(seconds * 1000);
		script.addBrick(brick);
	}

	@And("^this script has a Wait (\\d+.?\\d*) seconds? brick$")
	public void script_has_wait_s_brick1(int seconds) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		WaitBrick brick = new WaitBrick(seconds * 1000);
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
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);

		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		assertEquals("I am in the wrong Activity.", MainMenuActivity.class, solo.getCurrentActivity().getClass());
		solo.clickOnView(solo.getView(org.catrobat.catroid.R.id.main_menu_button_continue));
		solo.waitForActivity(ProjectActivity.class.getSimpleName(), 3000);
		assertEquals("I am in the wrong Activity.", ProjectActivity.class, solo.getCurrentActivity().getClass());
		solo.clickOnView(solo.getView(org.catrobat.catroid.R.id.button_play));
		solo.waitForActivity(StageActivity.class.getSimpleName(), 3000);
		assertEquals("I am in the wrong Activity.", StageActivity.class, solo.getCurrentActivity().getClass());

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

	@And("^Take a screenshot of the stage$")
	public void I_start_the_program22() throws InterruptedException {
		programWaitLock = new Semaphore(programWaitLockPermits);
		//addScriptEndCallbacks();
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		assertEquals("I am in the wrong Activity.", MainMenuActivity.class, solo.getCurrentActivity().getClass());
		solo.clickOnView(solo.getView(org.catrobat.catroid.R.id.main_menu_button_continue));
		solo.waitForActivity(ProjectActivity.class.getSimpleName(), 3000);
		assertEquals("I am in the wrong Activity.", ProjectActivity.class, solo.getCurrentActivity().getClass());
		solo.clickOnView(solo.getView(org.catrobat.catroid.R.id.button_play));
		solo.waitForActivity(StageActivity.class.getSimpleName(), 3000);
		assertEquals("I am in the wrong Activity.", StageActivity.class, solo.getCurrentActivity().getClass());
		solo.sleep(10000);
		solo.goBack();
		solo.sleep(1000);
		solo.clickOnView(solo.getView(R.id.stage_dialog_button_screenshot));
		//solo.takeScreenshot();
	}

	@And("^I wait until the program has stopped$")
	public void wait_until_program_has_stopped() throws InterruptedException {
		// While there are still scripts running, the available permits should be < 1.
		programWaitLock.tryAcquire(1, 60, TimeUnit.MILLISECONDS);
	}

	@And("^I wait until the program has stopped in (\\d+) seconds$")
	public void iWaitUntilTheProgramHasStoppedInSecond(int SECONDS) throws Throwable {
		// While there are still scripts running, the available permits should be < 1.
		programWaitLock.tryAcquire(SECONDS, TimeUnit.SECONDS);

	}

	@And("^stop this Script$")
	public void thisScriptHasAStopThisScriptBrick() throws Throwable {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Brick brick = new StopScriptBrick();
		script.addBrick(brick);
	}

	@Then("^the variable '(\\w+)' should be greater than or equal (\\d+.?\\d*)$")
	public void var_should_greater_than_equal_float(String name, double expected) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, name);
		assertNotNull("The variable does not exist.", variable);

		double actual = (double) variable.getValue();
		assertThat("The variable is < than the value.", actual, greaterThanOrEqualTo(expected));
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

	@Then("^the variable '(\\w+)' \\- '(\\w+)' should be less than or equal (\\d+.?\\d*)$")
	public void theVariableXMinusTShouldBeLessThanOrEqual(String a, String b, double expected) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		assertNotNull("The variable does not exist.", variable);
		UserVariable variable2 = project.getDefaultScene().getDataContainer().getUserVariable(object, b);
		assertNotNull("The variable does not exist.", variable2);
		double actual = (double) variable.getValue() - (double) variable2.getValue();
		assertThat("The variable is > than the value.", actual, lessThanOrEqualTo(expected));
	}

	@Then("^the variable '(\\w+)' should be equal (\\d+.?\\d*)$")
	public void var_should_equal_float(String name, double expected) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, name);
		assertNotNull("The variable does not exist.", variable);
		double actual = (double) variable.getValue();
		assertThat("The variable is != the value.", actual, equalTo(expected));
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

	@Then("^the '(\\w+)' should be equal to (\\d+.?\\d*)$")
	public void theLengthOfTheStringShouldBeEqualTo(String a, double expected) throws Throwable {

		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, a);

		double actual = (double) variable.getValue();
		assertThat("The variable is != the value.", actual, equalTo(expected));
	}

	@Then("^the variable '(\\w+)' should be equal '(\\w+)'$")
	public void var_should_equal_float1(String name, String text) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, name);
		assertNotNull("The variable does not exist.", variable);
		String expected = text;
		double actual = (double) variable.getValue();
		assertThat("The variable is != the value.", String.valueOf(actual), equalTo(expected));
	}

	@Then("^the variable '(\\w+)' should not be equal (\\d+.?\\d*)$")
	public void var_should_equal_float3(String name, double expected) {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, name);
		assertNotNull("The variable does not exist.", variable);
		double actual = (double) variable.getValue();
		//assertThat("The variable is != the value.", actual, equalTo(expected));
		assertThat("The variable is equal to the value.", actual, is(Matchers.not(equalTo(expected))));
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

	@Then("^the stage/screenshot is not empty$")
	public void theStageScreenshotIsEmptyOrNot() throws Throwable {
		File desktop = Environment.getExternalStorageDirectory();
		File directory = new File(desktop.getAbsolutePath() + "/Pocket Code/Cucumber14/Scene 1");
		File screenShotFileA = new File(directory, "automatic_screenshot.png");
		FileInputStream streamInA = new FileInputStream(screenShotFileA);
		Bitmap ScreenshotA = BitmapFactory.decodeStream(streamInA);
		streamInA.close();
		assertFalse("manual_screenshot is White and Transparent", screenshotIsEmpty(ScreenshotA));
	}

	public static boolean screenshotIsEmpty(Bitmap ScreenshotA) {
		int[] pixels = new int[ScreenshotA.getWidth() * ScreenshotA.getHeight()];
		ScreenshotA.getPixels(pixels, 0, ScreenshotA.getWidth(), 0, 0, ScreenshotA.getWidth(), ScreenshotA.getHeight());
		for (int i : pixels) {
			if (!(Color.alpha(i) == 0 || (Color.blue(i) == 255 && Color.red(i) == 255 && Color.green(i) == 255))) {
				return false;
			}
		}
		return true;
	}

	@Then("^the name of the variable should be equal \"(.*?)\"$")
	public void theNameOfTheVariableShouldBeEqualG(String name) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, name);
		assertNotNull("The variable does not exist.", variable);
		String expected = name;
		String actual = variable.getName();
		assertThat("The variable is != the value.", String.valueOf(actual), equalTo(expected));
	}

	@And("^set \"(.*?)\" to (\\d+.?\\d*)$")
	public void thisScriptHasASetToBrick(String a, String b) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}
		FormulaElement elemB = new FormulaElement(ElementType.NUMBER, b, null);
		//varA.equals(20);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@Then("^the object should be equal to \"(.*?)\"$")
	public void theNameOfTheObjectShouldBeEqualToObject(String name) throws Throwable {
		List<Sprite> spriteList = ProjectManager.getInstance().getCurrentProject().getDefaultScene().getSpriteList();
		assertEquals("Name of object is not as expected", spriteList.get(1).getName(), name);
	}

	@Then("^the name of the variable should be equal to \"([^\"]*)\"$")
	public void theNameOfTheVariableShouldBeEqualTo(String name) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, name);
		assertNotNull("The variable does not exist.", variable);
		String expected = name;
		String actual = variable.getName();
		assertThat("The variable is != the value.", String.valueOf(actual), equalTo(expected));
	}

	@And("^this script add '(\\d+)' to list name '(\\w+)' brick$")
	public void thisScriptAddToListNameDdBrick(int b, String a) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserList varA = project.getDefaultScene().getDataContainer().getUserList(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserListToSprite(object, a);
		}
		FormulaElement elemB = new FormulaElement(ElementType.USER_LIST, b, null);
		Brick brick = new AddItemToUserListBrick(elemB, varA);
		script.addBrick(brick);
	}

	@And("^this script add variable \"(.*?)\" to list name '(\\w+)' brick$")
	public void thisScriptAddVariableGToListNameItemBrick(String a, String b) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserList varA = project.getDefaultScene().getDataContainer().getUserList(object, b);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserListToSprite(object, b);
			//FormulaElement elemA = new FormulaElement(ElementType.USER_LIST, a, null);
			//FormulaElement elemB = new FormulaElement(ElementType.USER_VARIABLE, a, null);
			//Brick brick = new AddItemToUserListBrick(elemB, varA);
			//script.addBrick(brick);
			Brick brick = new AddItemToUserListBrick(new Formula(a), varA);
			script.addBrick(brick);
		}
	}

	@And("^this script add (\\d+) to list  '(\\w+)'$")
	public void thisScriptAddToListTime(int b, String a) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserList varA = project.getDefaultScene().getDataContainer().getUserList(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserListToSprite(object, a);
			//FormulaElement elemA = new FormulaElement(ElementType.USER_LIST, a, null);
			//FormulaElement elemB = new FormulaElement(ElementType.USER_VARIABLE, a, null);
			//Brick brick = new AddItemToUserListBrick(elemB, varA);
			//script.addBrick(brick);
			Brick brick = new AddItemToUserListBrick(new Formula(b), varA);
			script.addBrick(brick);
		}
	}

	@Then("^Current list '(\\w+)' should be equal to (\\d+)$")
	public void currentListTimeShouldBeEqualTo(String a, int expected) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserList userlist = project.getDefaultScene().getDataContainer().getUserList(object, a);
		assertNotNull("The userlist does not exist.", userlist);
		String actual = String.valueOf(userlist.getList().get(0));
		assertThat("The value of userlist is not equal to the value.", String.valueOf(actual), Matchers.<String>equalTo(String.valueOf(expected)));
	}

	@Then("^Current list '(\\w+)' should be equal to \"([^\"]*)\"$")
	public void currentListTimeShouldBeEqualTo22(String a, String expected) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Project project = ProjectManager.getInstance().getCurrentProject();
		UserList userlist = project.getDefaultScene().getDataContainer().getUserList(object, a);
		assertNotNull("The userlist does not exist.", userlist);
		String actual = String.valueOf(userlist.getList().get(0));
		assertThat("The value of userlist is not equal to the value.", String.valueOf(actual), Matchers.<String>equalTo(String.valueOf(expected)));
	}

	@And("^set '(\\w+)' to letter no '(\\d+)' of \"([^\"]*)\"$")
	public void thisScriptHasASetTestLetterNoTo(String a, String b, String c) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}

		FormulaElement aaa = new FormulaElement(ElementType.STRING, c, null);
		FormulaElement indexFormulaElement = new FormulaElement(ElementType.NUMBER, b, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.LETTER.name(), null, indexFormulaElement, aaa);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@Then("^the '(\\d+)' no of the letter \"([^\"]*)\" should be equal to \"([^\"]*)\"$")
	public void theNoOfTheLetterShouldBeEqualToA(String b, String c, String a) throws Throwable {

		FormulaElement aaa = new FormulaElement(ElementType.STRING, c, null);
		FormulaElement indexFormulaElement = new FormulaElement(ElementType.NUMBER, b, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.LETTER.name(), null, indexFormulaElement, aaa);
		Formula letterFormular = new Formula(elemB);
		String computeDialogResult = letterFormular.getResultForComputeDialog(null);
		assertEquals("ComputeDialogResult for letter function not working", a, computeDialogResult);
	}

	@And("^set '(\\w+)' to length \"([^\"]*)\"$")
	public void thisScriptHasASetTestToLengthBrick(String a, String b) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}

		FormulaElement indexFormulaElement = new FormulaElement(ElementType.STRING, b, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.LENGTH.name(), null, indexFormulaElement, null);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@Then("^the length of the string \"([^\"]*)\" should be equal to (\\d+.?\\d*)$")
	public void theLengthOfTheStringShouldBeEqualTo1(String b, double expected) throws Throwable {

		FormulaElement indexFormulaElement = new FormulaElement(ElementType.STRING, b, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.LENGTH.name(), null, indexFormulaElement);
		Formula letterFormular = new Formula(elemB);
		String computeDialogResult1 = letterFormular.getResultForComputeDialog(null);
		assertEquals("ComputeDialogResult for length function not working", expected, computeDialogResult1);
	}

	@And("^this script has a set '(\\w+)' with string \"([^\"]*)\" and \"([^\"]*)\" brick$")
	public void thisScriptHasASetTestWithStringAndBrick(String a, String b, String c) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}

		FormulaElement aaa = new FormulaElement(ElementType.STRING, b, null);
		FormulaElement bbb = new FormulaElement(ElementType.STRING, c, null);
		//FormulaElement indexFormulaElement = new FormulaElement(ElementType.NUMBER, b, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.JOIN.name(), null, bbb, aaa);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@Then("^the join string \"([^\"]*)\" and \"([^\"]*)\" should be equal to \"([^\"]*)\"$")
	public void theJoinStringAndShouldBeEqualTo(String b, String c, String d) throws Throwable {

		FormulaElement aaa = new FormulaElement(ElementType.STRING, b, null);
		FormulaElement bbb = new FormulaElement(ElementType.STRING, c, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.JOIN.name(), null, aaa, bbb);
		Formula letterFormular = new Formula(elemB);
		String computeDialogResult = letterFormular.getResultForComputeDialog(null);
		assertEquals("ComputeDialogResult for letter function not working", d, computeDialogResult);
	}

	@And("^set '(\\w+)' with join \"([^\"]*)\" and \"([^\"]*)\"$")
	public void thisScriptHasASetTestWithStringAndBrick1(String a, String b, String c) throws Throwable {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Project project = ProjectManager.getInstance().getCurrentProject();

		UserVariable varA = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
		if (varA == null) {
			varA = project.getDefaultScene().getDataContainer().addSpriteUserVariableToSprite(object, a);
		}

		FormulaElement aaa = new FormulaElement(ElementType.STRING, b, null);
		FormulaElement bbb = new FormulaElement(ElementType.STRING, c, null);
		//FormulaElement indexFormulaElement = new FormulaElement(ElementType.NUMBER, b, null);
		FormulaElement elemB = new FormulaElement(ElementType.FUNCTION,
				Functions.JOIN.name(), null, bbb, aaa);
		Brick brick = new SetVariableBrick(new Formula(elemB), varA);
		script.addBrick(brick);
	}

	@And("^this script has a speak '(\\w+)'$")
	public void thisScriptHasASpeakHello12(String text) throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Brick brick = new SpeakBrick(new Formula(text));
		script.addBrick(brick);
		assertNotNull("Text is not updated after SpeakBrick executed", solo.searchText(text));
	}

	@Then("^the name of the speak should be equal to '(\\w+)'$")
	public void theNameOfTheSpeakShouldBeEqualToSound(String text) throws Throwable {
		Solo solo = (Solo) Cucumber.get(Cucumber.KEY_SOLO);
		SpeakBrick speakBrick = new SpeakBrick(text);
		assertNotNull("Text is not updated after SpeakBrick executed", solo.searchText(text));
		assertEquals("Text is not updated after SpeakBrick executed", text, speakBrick
				.getFormulaWithBrickField(Brick.BrickField.SPEAK));
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


}
