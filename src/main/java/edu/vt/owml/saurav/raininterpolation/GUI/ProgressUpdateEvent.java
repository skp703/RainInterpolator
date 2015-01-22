/*
 * Copyright (C) 2015 saurav
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.vt.owml.saurav.raininterpolation.GUI;

import java.util.EventListener;
import java.util.EventObject;

/**
 *
 * @author saurav
 */
public class ProgressUpdateEvent extends EventObject {

    int percent;

    public ProgressUpdateEvent(Object source, int percent) {
        super(source);
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public interface ProgressUpdateEventListener extends EventListener {

        public void ProgressUpdateEventOccurred(ProgressUpdateEvent evt);
    }

}
