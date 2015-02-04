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
 * Fired when all centers have been been built by GridMaker
 *
 * @author saurav
 */
public class CentersBuiltEvent extends EventObject {

    public CentersBuiltEvent(Object source) {
        super(source);
    }

    public interface CentersBuiltEventListener extends EventListener {

        public void CentersBuiltEventOccurred(CentersBuiltEvent evt);
    }

}
