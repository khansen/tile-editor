/*
*
*    Copyright (C) 2003 Kent Hansen.
*
*    This file is part of Tile Manipulator.
*
*    Tile Manipulator is free software; you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation; either version 2 of the License, or
*    (at your option) any later version.
*
*    Tile Manipulator is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*/

import tm.ui.TMUI;

/**
*
* Tile Manipulator main class.
* A quite pointless class really. The application is very UI-centric,
* so the TMUI class evolved into the real application backbone.
* This class just gets the show started.
*
**/

public class TileManipulator {

/**
*
* Constructor.
*
**/

    public TileManipulator() {
        new TMUI();
    }

/**
*
* Starts up the program.
*
**/

    public static void main(String[] args) {
        if (System.getProperty("os.name").equals("Mac OS X")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        new TileManipulator();
    }

}