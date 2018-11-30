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
import org.catrobat.catroid.formulaeditor.UserList;

import java.util.List;

public class DeleteItemOfUserListBrick extends UserListBrick {

	private static final long serialVersionUID = 1L;

	public DeleteItemOfUserListBrick() {
		addAllowedBrickField(BrickField.LIST_DELETE_ITEM, R.id.brick_delete_item_of_userlist_edit_text);
	}

	public DeleteItemOfUserListBrick(Integer item) {
		this(new Formula(item));
	}

	public DeleteItemOfUserListBrick(Formula formula, UserList userList) {
		this(formula);
		this.userList = userList;
	}

	public DeleteItemOfUserListBrick(Formula formula) {
		this();
		setFormulaWithBrickField(BrickField.LIST_DELETE_ITEM, formula);
	}

	@Override
	public int getViewResource() {
		return R.layout.brick_delete_item_of_userlist;
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
	protected int getSpinnerId() {
		return R.id.delete_item_of_userlist_spinner;
	}

	@Override
	public List<ScriptSequenceAction> addActionToSequence(Sprite sprite, ScriptSequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory()
				.createDeleteItemOfUserListAction(sprite, getFormulaWithBrickField(BrickField.LIST_DELETE_ITEM), userList));
		return null;
	}
}
