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

import edu.vt.owml.saurav.raininterpolation.GUI.CentersBuiltEvent.CentersBuiltEventListener;
import edu.vt.owml.saurav.raininterpolation.GUI.ProgressUpdateEvent.ProgressUpdateEventListener;
import edu.vt.owml.saurav.raininterpolation.OverAllExecutor;
import edu.vt.owml.saurav.raininterpolation.database.DatabaseWorker;
import edu.vt.owml.saurav.raininterpolation.inputbuilder.GridMaker;
import edu.vt.owml.saurav.raininterpolation.inputbuilder.LastState;
import edu.vt.owml.saurav.raininterpolation.inputbuilder.ParseShapefiles;
import edu.vt.owml.saurav.raininterpolation.inputbuilder.ParseShapefiles.GeometryTypeString;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

/**
 * This is the main class. Lot of work is done by GUI. Will change in later
 * versions.
 *
 * @author saurav
 */
public class MainGUI extends javax.swing.JFrame implements CentersBuiltEventListener, ProgressUpdateEventListener {

    private GUIInputStore is;
    private LastState ls;
    public static Color Okay = Color.green;
    public static Color notOkay = Color.red;
    DatabaseWorker dw;
    private static String rainTable = "RAINTABLE";
    private static String resultsTable = "RESULTSTABLE";
    private static String summaryTable = "SUMMARYTABLE";
    List<Long> dates;
    private Date minDate = new Date();
    private Date maxDate = new Date();

    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Creates new form MainGUI
     *
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public MainGUI() throws ClassNotFoundException, SQLException {
        initComponents();
        ls = new LastState();
        is = new GUIInputStore();
        is.setGridNumber((int) gridElementsNumber.getValue());
        fileChooser.setCurrentDirectory(new File(ls.getLastFileOpenDirectory()));
        messagesPane.setEditable(false);
        messagesPane.setContentType("text/html");
        dw = new DatabaseWorker();
        databaseEngineURL.setText(dw.getURL());
        threadsSpinner.setValue(Runtime.getRuntime().availableProcessors());
        is.setNumberOfThreads(Runtime.getRuntime().availableProcessors());
        IDWPowerSpinner.setValue(2);
        is.setPower(2);
        dates = new ArrayList();
        is.setRainTable(rainTable);
        is.setResultTable(resultsTable);
        is.setStartDate(minDate);
        is.setEndDate(maxDate);
    }

    private void tabulateList(List<?> l, boolean singleColumn) {

        Class obj = l.get(1).getClass();
        String text = "<TABLE>";
        if (!singleColumn) {
            Field[] fields = obj.getDeclaredFields();
            for (Object o : l) {
                text = text + "<TR>";
                for (Field field : fields) {
                    //  System.out.println("field = " + field);
                    try {
                        if (field.getType().isPrimitive() || field.getType().getTypeName().equalsIgnoreCase("java.lang.String")) {
                            text = text + "<TD>";
                            field.setAccessible(true);
                            text = text + field.get(o);

                            text = text + "</TD>";
                        }
                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                text = text + "</TR>";
            }
            text = text + "</TABLE>";
        } else {

            for (Object o : l) {
                text = text + "<TR><TD>";
                text = text + o.toString();
                text = text + "</TD></TR>";
            }

        }
        text = text + "</TABLE>";
        HTMLDocument doc = (HTMLDocument) messagesPane.getDocument();
        HTMLEditorKit editorKit = (HTMLEditorKit) messagesPane.getEditorKit();
        try {
            editorKit.insertHTML(doc, doc.getLength(), text, 0, 0, null);
        } catch (BadLocationException | IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            appendToMessage(ex.toString(), true);
        }
    }

    private void appendToMessage(String s, boolean red) {
        try {
            System.out.println("s = " + s);
            String text = "";
            if (red) {
                text = "<span style=\"color:red\">" + s + "</span>";
            } else {
                text = "<span >" + s + "</span>";
            }
            HTMLDocument doc = (HTMLDocument) messagesPane.getDocument();
            HTMLEditorKit editorKit = (HTMLEditorKit) messagesPane.getEditorKit();

            editorKit.insertHTML(doc, doc.getLength(), text, 0, 0, null);
        } catch (BadLocationException | IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        messagesPane = new javax.swing.JTextPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        stationLabel = new javax.swing.JLabel();
        watershedLabel = new javax.swing.JLabel();
        dataLabel = new javax.swing.JLabel();
        selectStationButton = new javax.swing.JButton();
        selectWatershedButton = new javax.swing.JButton();
        selectDataButton = new javax.swing.JButton();
        stationShapefileLabel = new javax.swing.JLabel();
        watershedShapefileLabel = new javax.swing.JLabel();
        rainValuesLabel = new javax.swing.JLabel();
        attributeCombo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        gridElementsNumber = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        renderGridButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        threadsSpinner = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        startDateField = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        endDateField = new javax.swing.JFormattedTextField();
        generateDailyButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        IDWPowerSpinner = new javax.swing.JSpinner();
        timeRemaningLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        maxDistance = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        saveResultsButton = new javax.swing.JButton();
        databaseEngineURL = new javax.swing.JTextField();
        progressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rain Interpolator");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Messages"));
        jScrollPane1.setViewportView(messagesPane);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Input Files"));

        stationLabel.setBackground(notOkay);
        stationLabel.setLabelFor(selectStationButton);
        stationLabel.setText("Rain Stations Shapefile (Point type)");
        stationLabel.setOpaque(true);

        watershedLabel.setBackground(notOkay);
        watershedLabel.setLabelFor(selectWatershedButton);
        watershedLabel.setText("Wateshed Shapefile");
        watershedLabel.setOpaque(true);

        dataLabel.setBackground(notOkay);
        dataLabel.setLabelFor(selectDataButton);
        dataLabel.setText("Rain values (all in same units)");
        dataLabel.setOpaque(true);

        selectStationButton.setText("Select");
        selectStationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectStationButtonActionPerformed(evt);
            }
        });

        selectWatershedButton.setText("Select");
        selectWatershedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectWatershedButtonActionPerformed(evt);
            }
        });

        selectDataButton.setText("Select");
        selectDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectDataButtonActionPerformed(evt);
            }
        });

        stationShapefileLabel.setText(" ");

        watershedShapefileLabel.setText(" ");

        rainValuesLabel.setText(" ");

        attributeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "attributes" }));
        attributeCombo.setEnabled(false);
        attributeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                attributeComboActionPerformed(evt);
            }
        });

        jLabel4.setText("Station name columns");

        jLabel2.setText("Format should be \"station,date(yyyymmdd),rain\". First line will be ignored");

        jLabel1.setText("Grid Elements");

        gridElementsNumber.setValue(50);
        gridElementsNumber.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                gridElementsNumberStateChanged(evt);
            }
        });

        jLabel3.setText(" Click \"View Grid\" button to visualize grid");

        renderGridButton.setText("View Grid");
        renderGridButton.setEnabled(false);
        renderGridButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renderGridButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Click to generate inputs");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(stationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectStationButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dataLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(watershedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(selectWatershedButton, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(selectDataButton, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(stationShapefileLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                    .addComponent(watershedShapefileLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rainValuesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(renderGridButton, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(attributeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(116, 116, 116))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(gridElementsNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectStationButton)
                    .addComponent(stationShapefileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(attributeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(stationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(watershedShapefileLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(selectWatershedButton)
                            .addComponent(watershedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(gridElementsNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rainValuesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(selectDataButton)
                            .addComponent(jLabel2)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(dataLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(renderGridButton)
                    .addComponent(jLabel5))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Input", jPanel1);

        jLabel6.setText("Threads");

        threadsSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                threadsSpinnerStateChanged(evt);
            }
        });

        jLabel7.setText("Start Date");

        startDateField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy-MM-dd"))));
        startDateField.setText(sdf.format(minDate));
        startDateField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        startDateField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startDateFieldActionPerformed(evt);
            }
        });
        startDateField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                startDateFieldPropertyChange(evt);
            }
        });

        jLabel8.setText("End Date");

        endDateField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy-MM-dd"))));
        endDateField.setText(sdf.format(maxDate));
        endDateField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endDateFieldActionPerformed(evt);
            }
        });
        endDateField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                endDateFieldPropertyChange(evt);
            }
        });

        generateDailyButton.setText("Generate Daily");
        generateDailyButton.setEnabled(false);
        generateDailyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateDailyButtonActionPerformed(evt);
            }
        });

        jButton2.setText("Use template");
        jButton2.setEnabled(false);

        jLabel9.setText("Provide template with date values(yyyymmdd) at which rain values should be computed");
        jLabel9.setEnabled(false);

        jLabel10.setText("IDW Power");

        IDWPowerSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                IDWPowerSpinnerStateChanged(evt);
            }
        });

        timeRemaningLabel.setText(" ");

        jLabel11.setText("Neglect distances greater than (in projection system units)");

        maxDistance.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        maxDistance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxDistanceActionPerformed(evt);
            }
        });
        maxDistance.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                maxDistancePropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(endDateField)
                            .addComponent(startDateField, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                            .addComponent(threadsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(generateDailyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(IDWPowerSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(134, 134, 134)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(maxDistance, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(timeRemaningLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(threadsSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(IDWPowerSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(maxDistance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(endDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generateDailyButton)
                    .addComponent(jButton2)
                    .addComponent(jLabel9)
                    .addComponent(timeRemaningLabel))
                .addGap(24, 24, 24))
        );

        jTabbedPane1.addTab("Execution", jPanel2);

        jLabel12.setText("H2 database engine URL (works only when program is not closed)");

        saveResultsButton.setText("Save daily average for the watershed");
        saveResultsButton.setEnabled(false);
        saveResultsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveResultsButtonActionPerformed(evt);
            }
        });

        databaseEngineURL.setEditable(false);
        databaseEngineURL.setText(" ");
        databaseEngineURL.setToolTipText("");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                    .addComponent(saveResultsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(databaseEngineURL, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(databaseEngineURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(saveResultsButton)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Analysis", jPanel3);

        progressBar.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void resetPoints() {
        stationLabel.setBackground(notOkay);
        attributeCombo.setEnabled(false);
        stationShapefileLabel.setText(" ");
        is.setStationsFile(null);
        checkEnableViewGrid();
        checkEnableGenerateDaily();
    }

    private void printStationNames() {
        try {
            List<String> readAttributes = ParseShapefiles.readAttributes(is.getStationsFile(), is.getAttributeForStationLabel());

            appendToMessage("Attribute values: ", false);
            tabulateList(readAttributes, true);
        } catch (IOException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void selectStationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectStationButtonActionPerformed
        // Show shapefile chooser
        // Checks:1) shapefile has coordinate system defined,  has points, and has a attribute with label that has "name" in it.
        fileChooser.resetChoosableFileFilters();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Shape File", "shp"));
        int showOpenDialog = fileChooser.showOpenDialog(this);
        if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                ParseShapefiles.ShapeFileDesc desc = ParseShapefiles.findDescription(selectedFile);

                if (desc.getType() == GeometryTypeString.POINT) {
                    if (desc.getProjectionSystem() != null) {
                        appendToMessage("Points projection system: " + desc.getProjectionSystem(), false);
                        if (desc.getLabels().size() > 1) {
                            is.setStationsFile(selectedFile);
                            is.setAttributeForStationLabel(desc.getLabels().get(1));
                            attributeCombo.setModel(new DefaultComboBoxModel(desc.getLabels().toArray()));
                            attributeCombo.setEnabled(true);
                            attributeCombo.setSelectedIndex(1);
                            stationLabel.setBackground(Okay);
                            stationShapefileLabel.setText(selectedFile.getName());
                            checkEnableViewGrid();
                            //printStationNames();
                        } else {
                            appendToMessage("No attribute column with label found. Where are station labels? ", true);
                            resetPoints();
                        }
                    } else {
                        appendToMessage("No projection system found for the shapefile. <TODO: Helpful hint> Points projection system is NULL ", true);
                        resetPoints();
                    }
                } else {
                    appendToMessage("Should be a Point type shapefile, found:  " + desc.getType().name(), true);
                    resetPoints();
                }
            } catch (IOException ex) {
                Logger.getLogger(MainGUI.class
                        .getName()).log(Level.SEVERE, null, ex);
                appendToMessage(ex.getLocalizedMessage(), true);
                resetPoints();
            }
        }
    }//GEN-LAST:event_selectStationButtonActionPerformed

    private void attributeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_attributeComboActionPerformed
        // TODO add your handling code here:
        String selectedItem = (String) attributeCombo.getSelectedItem();
        is.setAttributeForStationLabel(selectedItem);
        System.out.println("selectedItem = " + selectedItem);
        printStationNames();

    }//GEN-LAST:event_attributeComboActionPerformed

    private void resetWatershed() {
        watershedLabel.setBackground(notOkay);
        watershedShapefileLabel.setText(" ");
        is.setWatershedFile(null);
        checkEnableViewGrid();
        checkEnableGenerateDaily();
    }

    private void selectWatershedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectWatershedButtonActionPerformed
        // Show shapefile chooser
        // Checks:1) shapefile has coordinate system defined,  and is a polygon
        fileChooser.resetChoosableFileFilters();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Shape File", "shp"));
        int showOpenDialog = fileChooser.showOpenDialog(this);
        if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                ParseShapefiles.ShapeFileDesc desc = ParseShapefiles.findDescription(selectedFile);
                is.setMaxDistance(desc.getDistance());
                maxDistance.setText(Double.toString(desc.getDistance()));
                if (desc.getType() == GeometryTypeString.POLYGON) {
                    if (desc.getProjectionSystem() != null) {
                        appendToMessage("Watershed projection system: " + desc.getProjectionSystem(), false);
                        is.setWatershedFile(selectedFile);
                        watershedLabel.setBackground(Okay);
                        watershedShapefileLabel.setText(selectedFile.getName());
                        checkEnableViewGrid();

                    } else {
                        appendToMessage("No projection system found for the shapefile. <TODO: Helpful hint> Points projection system is NULL ", true);
                        resetWatershed();
                    }
                } else {
                    appendToMessage("Should be a Polygon type shapefile, found:  " + desc.getType().name(), true);
                    resetWatershed();
                }
            } catch (IOException ex) {
                Logger.getLogger(MainGUI.class
                        .getName()).log(Level.SEVERE, null, ex);
                appendToMessage(ex.getLocalizedMessage(), true);
                resetWatershed();
            }
        }
    }//GEN-LAST:event_selectWatershedButtonActionPerformed

    private void checkEnableViewGrid() {
        if (is.showViewGrid()) {
            renderGridButton.setEnabled(true);
            appendToMessage("Click 'View Grid' button to recompute all inputs", true);
        } else {
            renderGridButton.setEnabled(false);
        }
    }

    @Override
    public void CentersBuiltEventOccurred(CentersBuiltEvent evt) {
        is.setIdc(gridMaker.getIdc());
        is.setGrids(gridMaker.getGrids());
        checkEnableGenerateDaily();
    }

    private void checkEnableGenerateDaily() {
        if (is.showGenerateDaily()) {
            generateDailyButton.setEnabled(true);
            //appendToMessage("Click 'View Grid' button to recompute all inputs", true);
        } else {
            generateDailyButton.setEnabled(false);
            saveResultsButton.setEnabled(false);
        }
    }

    private void resetData() {
        dataLabel.setBackground(notOkay);
        rainValuesLabel.setText(" ");
        is.setDataFile(null);
        checkEnableViewGrid();
        checkEnableGenerateDaily();

    }

    private void selectDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectDataButtonActionPerformed
        // TODO code to add CSV file and also check first line has stations
        fileChooser.resetChoosableFileFilters();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
        int showOpenDialog = fileChooser.showOpenDialog(this);
        if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            rainValuesLabel.setText(selectedFile.getName());
            is.setDataFile(selectedFile);
            enableProgressBar(true);
            SwingWorker worker = new SwingWorker<List<DatabaseWorker.RainSummaryData>, Void>() {
                List<DatabaseWorker.RainSummaryData> summarizeRainTable;

                @Override
                protected List<DatabaseWorker.RainSummaryData> doInBackground() {
                    try {
                        dw.loadrainCSVinTable(fileChooser.getSelectedFile().getAbsolutePath(), rainTable);
                        summarizeRainTable = dw.summarizeRainTable(rainTable);

                    } catch (SQLException ex) {
                        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                        appendToMessage(ex.getMessage(), true);
                        resetData();
                        disableProgressBar();
                    }
                    return summarizeRainTable;
                }

                @Override
                protected void done() {
                    super.done(); //To change body of generated methods, choose Tools | Templates.
                    appendToMessage("Data Summary", false);
                    tabulateList(summarizeRainTable, false);

                    dataLabel.setBackground(Okay);
                    minDate = null;
                    maxDate = null;
                    for (DatabaseWorker.RainSummaryData r : summarizeRainTable) {
                        try {
                            if (minDate == null || minDate.after(sdf1.parse(Long.toString(r.getMindate())))) {
                                minDate = sdf1.parse(Long.toString(r.getMindate()));
                            }
                            if (maxDate == null || maxDate.before(sdf1.parse(Long.toString(r.getMaxdate())))) {
                                maxDate = sdf1.parse(Long.toString(r.getMaxdate()));
                            }
                        } catch (ParseException ex) {
                            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    startDateField.setText(sdf.format(minDate));
                    endDateField.setText(sdf.format(maxDate));
                    is.setStartDate(minDate);
                    is.setEndDate(maxDate);
                    checkEnableViewGrid();
                    disableProgressBar();
                }
            };
            worker.execute();
        }
    }//GEN-LAST:event_selectDataButtonActionPerformed
    private GridMaker gridMaker;
    private void renderGridButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renderGridButtonActionPerformed
        // render watershed, points and dummy grid .. change the size of the dummy grid based on the points.
        gridMaker = new GridMaker(is);
        gridMaker.addCentersBuiltEventListener(this);
        System.out.println("is = " + is);
        try {
            gridMaker.displayShapeFile();
        } catch (IOException | FactoryException | MismatchedDimensionException | TransformException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            appendToMessage(ex.getMessage(), true);
        }
    }//GEN-LAST:event_renderGridButtonActionPerformed

    private void gridElementsNumberStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_gridElementsNumberStateChanged
        // TODO add your handling code here:
        is.setGridNumber((int) gridElementsNumber.getValue());
        System.out.println(is.getGridNumber());
        appendToMessage("Grid elements: " + is.getGridNumber(), false);

    }//GEN-LAST:event_gridElementsNumberStateChanged

    private void generateDailyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateDailyButtonActionPerformed
        // TODO add your handling code here:
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        cal.setTime(is.getStartDate());
        cal1.setTime(is.getEndDate());
        System.out.println("cal = " + cal);
        System.out.println("cal1 = " + cal1);
        while (cal.before(cal1)) {
            dates.add(Long.parseLong(sdf1.format(cal.getTime())));
            //  System.out.println("cal = " + cal);
            cal.add(Calendar.DATE, 1);
        }
        System.out.println("dates = " + dates);
        is.setDates(dates);
        System.out.println(is);
        runOnce = true;// makes the progressbar update
        System.gc();
        try {
            ///add code to call SwingWorker!!
            OverAllExecutor oe = new OverAllExecutor(is);
            oe.addProgressUpdateEventListener(this);
            oe.execute();
            generateDailyButton.setEnabled(false);
        } catch (SQLException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
            appendToMessage(ex.getMessage(), true);
        }
    }//GEN-LAST:event_generateDailyButtonActionPerformed

    private void startDateFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startDateFieldActionPerformed
        if (is != null) { //is==null pre init
            try {
                // TODO add your handling code here:
                is.setStartDate(sdf.parse(startDateField.getText()));
                appendToMessage("Start Date:" + sdf.format(is.getStartDate()), false);
            } catch (ParseException ex) {
                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                appendToMessage(ex.getMessage(), true);
            }
        }
    }//GEN-LAST:event_startDateFieldActionPerformed

    private void endDateFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endDateFieldActionPerformed
        if (is != null) { //is==null pre init
            try {
                // TODO add your handling code here:
                is.setEndDate(sdf.parse(endDateField.getText()));
                appendToMessage("End Date:" + sdf.format(is.getEndDate()), false);
            } catch (ParseException ex) {
                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                appendToMessage(ex.getMessage(), true);
            }
        }
    }//GEN-LAST:event_endDateFieldActionPerformed

    private void threadsSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_threadsSpinnerStateChanged
        // TODO add your handling code here:
        is.setNumberOfThreads((int) threadsSpinner.getValue());
        appendToMessage("Threads: " + is.getNumberOfThreads(), false);
    }//GEN-LAST:event_threadsSpinnerStateChanged

    private void IDWPowerSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_IDWPowerSpinnerStateChanged
        // TODO add your handling code here:
        is.setPower((int) IDWPowerSpinner.getValue());
        appendToMessage("IDWPower: " + is.getPower(), false);
    }//GEN-LAST:event_IDWPowerSpinnerStateChanged

    private void enableProgressBar(boolean indeterminate) {
        progressBar.setIndeterminate(indeterminate);
        progressBar.setEnabled(true);

    }

    private void disableProgressBar() {
        progressBar.setIndeterminate(false);
        progressBar.setEnabled(false);
        progressBar.setValue(0);

    }

    private boolean runOnce = true;
    int lastPer;
    private long startTime;

    @Override
    public void ProgressUpdateEventOccurred(ProgressUpdateEvent evt) {
        //   System.out.println(evt.getPercent());

        if (evt.getPercent() < 0) {
            if (!progressBar.isEnabled()) {
                progressBar.setEnabled(true);
                progressBar.setIndeterminate(true);
                return;
            }
        }
        if (runOnce) {
            startTime = System.currentTimeMillis();
            lastPer = evt.getPercent();
            progressBar.setEnabled(true);
            progressBar.setIndeterminate(false);
            progressBar.setMaximum(100);
            progressBar.setMinimum(0);
            progressBar.setValue(evt.getPercent());
            runOnce = false;
        } else {
            progressBar.setValue(evt.getPercent());
            if (evt.getPercent() != lastPer) {
                lastPer = evt.getPercent();
                long timeLeft = (System.currentTimeMillis() - startTime) / lastPer * (100 - lastPer);
                String minLeft = timeLeft / 1000 / 60 < 1 ? "<1 min left" : timeLeft / 1000 / 60 + " min left";
                timeRemaningLabel.setText(minLeft);
            }
            if (evt.getPercent() > 100) {
                System.out.println("Done Computing");
                progressBar.setValue(0);
                disableProgressBar();
                timeRemaningLabel.setText(" ");
                appendToMessage("FINISHED ALL", false);
                generateDailyButton.setEnabled(true);
                runOnce = true;// for future runs
                saveResultsButton.setEnabled(true);
            }
        }

    }

    private void startDateFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_startDateFieldPropertyChange
        // TODO add your handling code here:
        startDateFieldActionPerformed(null);
    }//GEN-LAST:event_startDateFieldPropertyChange

    private void endDateFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_endDateFieldPropertyChange
        // TODO add your handling code here:
        endDateFieldActionPerformed(null);
    }//GEN-LAST:event_endDateFieldPropertyChange

    private void maxDistanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxDistanceActionPerformed
        // TODO add your handling code here:
        if (maxDistance.getText().isEmpty()) {
            is.setMaxDistance(-1);
        } else {
            is.setMaxDistance(Double.parseDouble(maxDistance.getText()));
            appendToMessage("IDWDistance Cutoff: " + is.getMaxDistance(), false);
        }
    }//GEN-LAST:event_maxDistanceActionPerformed

    private void maxDistancePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_maxDistancePropertyChange
        // TODO add your handling code here:
        if (maxDistance.getText().isEmpty()) {
            if (is != null) {
                is.setMaxDistance(-1);
            }
        } else {
            is.setMaxDistance(Double.parseDouble(maxDistance.getText()));
            appendToMessage("IDWDistance Cutoff: " + is.getMaxDistance(), false);
        }
    }//GEN-LAST:event_maxDistancePropertyChange

    private void saveResultsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveResultsButtonActionPerformed
        // TODO add your handling code here:
        fileChooser.resetChoosableFileFilters();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));

        int showSaveDialog = fileChooser.showSaveDialog(this);
        enableProgressBar(true);
        saveResultsButton.setEnabled(false);
        if (showSaveDialog == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fileChooser.getSelectedFile();
            SwingWorker worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    dw.saveResults(selectedFile.getAbsolutePath(), summaryTable, resultsTable);
                    return null;
                }

                @Override
                protected void done() {
                    super.done();
                    saveResultsButton.setEnabled(true);
                    disableProgressBar();
                    appendToMessage("Done saving file " + selectedFile.getAbsolutePath(), false);
                }

            };
            worker.execute();
            appendToMessage("Saving file " + selectedFile.getAbsolutePath(), false);
        }
    }//GEN-LAST:event_saveResultsButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//
//                }
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(MainGUI.class
//                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainGUI().setVisible(true);
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner IDWPowerSpinner;
    private javax.swing.JComboBox attributeCombo;
    private javax.swing.JLabel dataLabel;
    private javax.swing.JTextField databaseEngineURL;
    private javax.swing.JFormattedTextField endDateField;
    private javax.swing.JFileChooser fileChooser;
    private javax.swing.JButton generateDailyButton;
    private javax.swing.JSpinner gridElementsNumber;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JFormattedTextField maxDistance;
    private javax.swing.JTextPane messagesPane;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel rainValuesLabel;
    private javax.swing.JButton renderGridButton;
    private javax.swing.JButton saveResultsButton;
    private javax.swing.JButton selectDataButton;
    private javax.swing.JButton selectStationButton;
    private javax.swing.JButton selectWatershedButton;
    private javax.swing.JFormattedTextField startDateField;
    private javax.swing.JLabel stationLabel;
    private javax.swing.JLabel stationShapefileLabel;
    private javax.swing.JSpinner threadsSpinner;
    private javax.swing.JLabel timeRemaningLabel;
    private javax.swing.JLabel watershedLabel;
    private javax.swing.JLabel watershedShapefileLabel;
    // End of variables declaration//GEN-END:variables

}
