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

package tm.modaldialog;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Vector;
import tm.tilecodecs.TileCodec;

/**
*
* The dialog that's shown when user wants to create a new ("blank") file.
*
**/

public class TMNewFileDialog extends TMModalDialog {

    private JLabel sizeLabel;
    private JTextField sizeField;

    private JLabel columnsLabel;
    private SpinnerNumberModel columnsModel;
    private JSpinner columnsSpinner;
    private JLabel rowsLabel;
    private SpinnerNumberModel rowsModel;
    private JSpinner rowsSpinner;
    private JLabel codecLabel;
    private DefaultComboBoxModel<TileCodec> codecModel = null;
    private JComboBox<TileCodec> codecCombo;

    private TileCodec defaultCodec = null;
    private Vector<TileCodec> availableCodecs;
    private boolean codecsChanged = false;

    public static final int DIMENSIONS_MODE = 1;
    public static final int FILESIZE_MODE = 2;

/**
*
* Creates the New File dialog.
*
**/

    public TMNewFileDialog(Frame owner, tm.utils.Xlator xl) {
        super(owner, "New_File_Dialog_Title", xl);
    }

/**
*
* Gets the filesize given by the user.
*
**/

    public int getFileSize() {
        return Integer.parseInt(sizeField.getText());
    }

    public void setDefaultColumnAndRowCount(int columnCount, int rowCount)
    {
        columnsModel.setValue(columnCount);
        rowsModel.setValue(rowCount);
    }

    public void setDefaultCodec(TileCodec codec)
    {
        this.defaultCodec = codec;
    }

    public void setAvailableCodecs(Vector<TileCodec> codecs)
    {
        this.availableCodecs = codecs;
        codecsChanged = true;
    }

    public TileCodec getSelectedCodec()
    {
        return (TileCodec)codecModel.getSelectedItem();
    }

    public int getRowCount()
    {
        return rowsModel.getNumber().intValue();
    }

    public int getColumnCount()
    {
        return columnsModel.getNumber().intValue();
    }

    public int getMode()
    {
	return DIMENSIONS_MODE;
    }

/**
*
*
*
**/

    protected JPanel getDialogPane() {
        columnsLabel = new JLabel(xlate("Columns_Prompt"));
        columnsModel = new SpinnerNumberModel(16, 1, 256, 1);
        columnsSpinner = new JSpinner(columnsModel);
        rowsLabel = new JLabel(xlate("Rows_Prompt"));
        rowsModel = new SpinnerNumberModel(16, 1, 256, 1);
        rowsSpinner = new JSpinner(rowsModel);
        codecLabel = new JLabel(xlate("Codec"));
        codecCombo = new JComboBox<>();

        JPanel p = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        p.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        buildConstraints(gbc, 0, 0, 1, 1, 50, 100);
        gbl.setConstraints(columnsLabel, gbc);
        p.add(columnsLabel);
        buildConstraints(gbc, 1, 0, 1, 1, 50, 100);
        gbl.setConstraints(columnsSpinner, gbc);
        p.add(columnsSpinner);
        buildConstraints(gbc, 0, 1, 1, 1, 50, 100);
        gbl.setConstraints(rowsLabel, gbc);
        p.add(rowsLabel);
        buildConstraints(gbc, 1, 1, 1, 1, 50, 100);
        gbl.setConstraints(rowsSpinner, gbc);
        p.add(rowsSpinner);
        buildConstraints(gbc, 0, 2, 1, 1, 50, 100);
        gbl.setConstraints(codecLabel, gbc);
        p.add(codecLabel);
        buildConstraints(gbc, 1, 2, 1, 1, 50, 100);
        gbl.setConstraints(codecCombo, gbc);
        p.add(codecCombo);
        p.setPreferredSize(new Dimension(400, 100));
        /*
        sizeLabel = new JLabel(xlate("Size_Prompt"));
        buildConstraints(gbc, 0, 0, 1, 1, 50, 100);
        gbl.setConstraints(sizeLabel, gbc);
        p.add(sizeLabel);
        sizeField = new JTextField();
        buildConstraints(gbc, 1, 0, 1, 1, 50, 100);
        gbl.setConstraints(sizeField, gbc);
        p.add(sizeField);
        p.setPreferredSize(new Dimension(200, 50));

        sizeField.setColumns(7);
        sizeField.addKeyListener(new DecimalNumberVerifier());
        sizeField.getDocument().addDocumentListener(new TMDocumentListener());
	*/
        return p;
    }

    public int showDialog() {
        if ((codecModel == null) || codecsChanged) {
	    codecModel = new DefaultComboBoxModel<>(availableCodecs);
	    codecCombo.setModel(codecModel);
            codecsChanged = false;
	}
        if (defaultCodec != null)
	    codecModel.setSelectedItem(defaultCodec);
/*        sizeField.setText("");
        maybeEnableOKButton();
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                sizeField.requestFocus();
            }
        });*/
        return super.showDialog();
    }

    public boolean inputOK() {
        return !(sizeField.getText().equals("") || (getFileSize() == 0));
    }

}
