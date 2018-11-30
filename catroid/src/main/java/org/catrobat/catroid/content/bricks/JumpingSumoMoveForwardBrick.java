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
package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ScriptSequenceAction;
import org.catrobat.catroid.formulaeditor.Formula;

import java.util.List;

public class JumpingSumoMoveForwardBrick extends FormulaBrick {

	private static final long serialVersionUID = 1L;

	public JumpingSumoMoveForwardBrick() {
		addAllowedBrickField(BrickField.JUMPING_SUMO_TIME_TO_DRIVE_IN_SECONDS,
				R.id.brick_jumping_sumo_move_forward_edit_text_second);
		addAllowedBrickField(BrickField.JUMPING_SUMO_SPEED, R.id.brick_jumping_sumo_move_forward_edit_text_power);
	}

	public JumpingSumoMoveForwardBrick(int durationInMilliseconds, int powerInPercent) {
		this(new Formula(durationInMilliseconds / 1000.0), new Formula(powerInPercent));
	}

	public JumpingSumoMoveForwardBrick(Formula durationInSeconds, Formula powerInPercent) {
		this();
		setFormulaWithBrickField(BrickField.JUMPING_SUMO_TIME_TO_DRIVE_IN_SECONDS, durationInSeconds);
		setFormulaWithBrickField(BrickField.JUMPING_SUMO_SPEED, powerInPercent);
	}

	@Override
	public int getViewResource() {
		return R.layout.brick_jumping_sumo_move_forward;
	}

	@Override
	public int getRequiredResources() {
		return 0;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter adapter) {
		return null;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		return null;
	}

	@Override
	public void addRequiredResources(final ResourcesSet requiredResourcesSet) {
		requiredResourcesSet.add(JUMPING_SUMO);
		super.addRequiredResources(requiredResourcesSet);
	}

	@Override
	public List<ScriptSequenceAction> addActionToSequence(Sprite sprite, ScriptSequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createJumpingSumoMoveForwardAction(sprite,
				getFormulaWithBrickField(BrickField.JUMPING_SUMO_TIME_TO_DRIVE_IN_SECONDS),
				getFormulaWithBrickField(BrickField.JUMPING_SUMO_SPEED)));
		return null;
	}
}
