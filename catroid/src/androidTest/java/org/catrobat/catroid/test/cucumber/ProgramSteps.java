
package org.catrobat.catroid.test.cucumber;

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
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.BroadcastBrick;
import org.catrobat.catroid.content.bricks.BroadcastWaitBrick;
import org.catrobat.catroid.content.bricks.ChangeVariableBrick;
import org.catrobat.catroid.content.bricks.ForeverBrick;
import org.catrobat.catroid.content.bricks.IfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfLogicElseBrick;
import org.catrobat.catroid.content.bricks.IfLogicEndBrick;
import org.catrobat.catroid.content.bricks.IfThenLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfThenLogicEndBrick;
import org.catrobat.catroid.content.bricks.LoopEndBrick;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.content.bricks.StopScriptBrick;
import org.catrobat.catroid.content.bricks.WaitBrick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.FormulaElement;
import org.catrobat.catroid.formulaeditor.Operators;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.test.cucumber.util.CallbackBrick;
import org.catrobat.catroid.test.cucumber.util.Util;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.catrobat.catroid.formulaeditor.FormulaElement.ElementType;
import static org.catrobat.catroid.formulaeditor.FormulaElement.ElementType.NUMBER;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


// CHECKSTYLE DISABLE MethodNameCheck FOR 1000 LINES
@RunWith(AndroidJUnit4.class)
public class ProgramSteps {
	private final Object programStartWaitLock = new Object();
	private boolean programHasStarted = false;
	private int programWaitLockPermits = 1;
	private Semaphore programWaitLock;
	private String projectName = "Cucumber";

	@Rule
	public ActivityTestRule<MainMenuActivity> launchActivityRule = new ActivityTestRule<>(MainMenuActivity.class);

	@Test
	public void name() {
		SystemClock.sleep(10000);
		//onView(isRoot()).perform(CustomActions.wait(200));
	}

	@Given("^I have a program$")
	public void I_have_a_program() {
		Project project = new Project(InstrumentationRegistry.getTargetContext(), projectName);
		//ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentProject(project);
		Cucumber.put(Cucumber.KEY_PROJECT, project);
	}

	@Given("^this program has an object '(\\w+)'$")
	public void program_has_object(String name) throws IOException {
		int lookId = org.catrobat.catroid.R.drawable.default_project_bird_wing_down;
		Project project = new Project(InstrumentationRegistry.getTargetContext(), projectName);
		//ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentProject(project);
		Sprite sprite = Util.addNewObjectWithLook(getContext(), project, name, lookId);
		Cucumber.put(Cucumber.KEY_CURRENT_OBJECT, sprite);
	}

	@Given("^'(\\w+)' has a start script$")
	public void object_has_start_script(String object) {
		programWaitLockPermits -= 1;
		Project project = ProjectManager.getInstance().getCurrentProject();
		//ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentProject(project);
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
	public void whenProgramStarts() {
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
		BroadcastScript script = new BroadcastScript(message);
		sprite.addScript(script);
		Cucumber.put(Cucumber.KEY_CURRENT_SCRIPT, script);
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
		Brick brick = new SetVariableBrick(new Formula(elemB), variable);
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

	@And("^broadcastWait '(\\w+)'$")
	public void script_has_broadcast_wait_brick1(String message) {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		BroadcastWaitBrick brick = new BroadcastWaitBrick(message);
		script.addBrick(brick);
	}

	@And("^broadcast '(\\w+)'$")
	public void script_has_broadcast_brick1(String message) {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		BroadcastBrick brick = new BroadcastBrick(message);
		script.addBrick(brick);
	}

	@And("^broadcast \"(.*?)\"$")
	public void script_has_broadcast_brick(String message) {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);

		BroadcastBrick brick = new BroadcastBrick(message);
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
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		WaitBrick brick = new WaitBrick(seconds * 1000);
		script.addBrick(brick);
	}
	@And("^if '(\\w+)' = (\\d+.?\\d*) is true then$")
	public void ifVarIsTrueThen(String a, String b) {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Formula validFormula = new Formula(1);
		validFormula.setRoot(new FormulaElement(ElementType.OPERATOR, Operators.EQUAL.name(), null,
				new FormulaElement(ElementType.USER_VARIABLE, a, null), new FormulaElement(ElementType.NUMBER, b, null)));
		Brick brick = new IfThenLogicBeginBrick(validFormula);
		script.addBrick(brick);
	}
	@And("^stop this script$")
	public void thisScriptHasAStopThisScriptBrick() throws Throwable {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Brick brick = new StopScriptBrick();
		script.addBrick(brick);
	}
	@And("^if '(\\w+)' < (\\d+.?\\d*) is true then$")
	public void ifVarIsTrueThen1(String a, String b) {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Formula validFormula = new Formula(1);
		validFormula.setRoot(new FormulaElement(ElementType.OPERATOR, Operators.SMALLER_THAN.name(), null,
				new FormulaElement(ElementType.USER_VARIABLE, a, null), new FormulaElement(ElementType.NUMBER, b, null)));
		Brick brick = new IfThenLogicBeginBrick(validFormula);
		script.addBrick(brick);
	}
	@And("^if (\\d+) < (\\d+) is true then$")
	public void ifVarIsTrueThennumber(String a, String b) {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Formula validFormula = new Formula(1);
		validFormula.setRoot(new FormulaElement(ElementType.OPERATOR, Operators.SMALLER_THAN.name(), null,
				new FormulaElement(ElementType.NUMBER, a, null), new FormulaElement(ElementType.NUMBER, b, null)));
		Brick brick = new IfThenLogicBeginBrick(validFormula);
		script.addBrick(brick);
	}
	@And("^forever$")
	public void Forever1() {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Brick brick = new ForeverBrick();
		script.addBrick(brick);
	}
	@And("^forever end$")
	public void thisScriptHasAForeverEndBrick() {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		LoopEndBrick loopEndBrick = (LoopEndBrick) Cucumber.get(Cucumber.KEY_LOOP_END_BRICK);
		Brick brick = new LoopEndBrick(object, loopEndBrick);
		script.addBrick(brick);
	}
	@And("^if '(\\w+)' < (\\d+.?\\d*) is true then..Else$")
	public void ifVarIsTrueThen11(String a, String b) {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Formula validFormula = new Formula(1);
		validFormula.setRoot(new FormulaElement(ElementType.OPERATOR, Operators.SMALLER_THAN.name(), null,
				new FormulaElement(ElementType.USER_VARIABLE, a, null), new FormulaElement(ElementType.NUMBER, b, null)));
		IfLogicBeginBrick ifLogicBeginBrick = new IfLogicBeginBrick(validFormula);
		script.addBrick(ifLogicBeginBrick);
	}
	@And("^if (\\d+) < (\\d+) is true then..Else$")
	public void ifVarIsTrueThen1number(String a, String b) {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Formula validFormula = new Formula(1);
		validFormula.setRoot(new FormulaElement(ElementType.OPERATOR, Operators.SMALLER_THAN.name(), null,
				new FormulaElement(ElementType.NUMBER, a, null), new FormulaElement(ElementType.NUMBER, b, null)));

		IfLogicBeginBrick ifLogicBeginBrick = new IfLogicBeginBrick(validFormula);
		script.addBrick(ifLogicBeginBrick);
	}
	@And("^Else$")
	public void else1() {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		LoopEndBrick loopEndBrick = (LoopEndBrick) Cucumber.get(Cucumber.KEY_LOOP_END_BRICK);
		IfLogicElseBrick ifLogicElseBrick = new IfLogicElseBrick(object, loopEndBrick);
		script.addBrick(ifLogicElseBrick);
	}
	@And("^end if else$")
	public void endIf1() {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		LoopEndBrick loopEndBrick = (LoopEndBrick) Cucumber.get(Cucumber.KEY_LOOP_END_BRICK);
		IfLogicEndBrick ifLogicEndBrick = new IfLogicEndBrick(object, loopEndBrick);
		script.addBrick(ifLogicEndBrick);
	}
	@And("^end if$")
	public void endIf() {
		Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		LoopEndBrick loopEndBrick = (LoopEndBrick) Cucumber.get(Cucumber.KEY_LOOP_END_BRICK);
		IfThenLogicEndBrick ifThenLogicEndBrick = new IfThenLogicEndBrick(object, loopEndBrick);
		script.addBrick(ifThenLogicEndBrick);
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

	@Then("^the '(\\w+)' should be equal to (\\d+.?\\d*)$")
    public void theLengthOfTheStringShouldBeEqualTo(String a, double expected) throws Throwable {
        Sprite object = (Sprite) Cucumber.get(Cucumber.KEY_CURRENT_OBJECT);
        Project project = ProjectManager.getInstance().getCurrentProject();
        UserVariable variable = project.getDefaultScene().getDataContainer().getUserVariable(object, a);
        double actual = (double) variable.getValue();
        assertThat("The variable is != the value.", actual, equalTo(expected));
    }

	@And("^if \"([^\"]*)\" < (\\d+.?\\d*) is true then..Else$")
	public void ifIsTrueThen(String a, String b) throws Throwable {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Formula validFormula = new Formula(1);
		validFormula.setRoot(new FormulaElement(ElementType.OPERATOR, Operators.SMALLER_THAN.name(), null,
				new FormulaElement(ElementType.STRING, a, null), new FormulaElement(ElementType.NUMBER, b, null)));
		IfLogicBeginBrick ifLogicBeginBrick = new IfLogicBeginBrick(validFormula);
		script.addBrick(ifLogicBeginBrick);
	}
	@And("^if \"([^\"]*)\" is true then..Else$")
	public void ifIsTrueThenww(String TRUE ) throws Throwable {
		Script script = (Script) Cucumber.get(Cucumber.KEY_CURRENT_SCRIPT);
		Formula validFormula = new Formula(String.valueOf(TRUE));
		IfLogicBeginBrick ifLogicBeginBrick = new IfLogicBeginBrick(validFormula);
		script.addBrick(ifLogicBeginBrick);}
}