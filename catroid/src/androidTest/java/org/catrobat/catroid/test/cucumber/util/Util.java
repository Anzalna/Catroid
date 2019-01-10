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
package org.catrobat.catroid.test.cucumber.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.io.ResourceImporter;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.fail;
import static org.catrobat.catroid.common.Constants.IMAGE_DIRECTORY_NAME;

public final class Util {
    private Util() {
    }

    public static Sprite addNewObjectWithLook(Context context, Project project, String name, int id) throws IOException {
        Sprite sprite = new Sprite(name);
        project.getDefaultScene().addSprite(sprite);
        File file = ResourceImporter.createImageFileFromResourcesInDirectory(
                InstrumentationRegistry.getContext().getResources(), org.catrobat.catroid.test.R.drawable.catroid_banzai,
                new File(project.getDefaultScene().getDirectory(), IMAGE_DIRECTORY_NAME), "catroid_sunglasses.png", 1);
        LookData lookData = newLookData(name, file);
        sprite.getLookDataList().add(lookData);
        return sprite;
    }
    public static Sprite findSprite(Project project, String name) {
        for (Sprite sprite : project.getDefaultScene().getSpriteList()) {
            if (sprite.getName().equals(name)) {
                return sprite;
            }
        }
        fail(String.format("Sprite not found '%s'", name));
        return null;
    }
    public static LookData newLookData(String name, File file) {
        LookData look = new LookData();
        look.setLookName(name);
        look.setLookFilename(file.getName());
        return look;
    }



}