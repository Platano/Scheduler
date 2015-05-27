/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import com.opencsv.CSVReader;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Jeff
 */
public class JFrame extends javax.swing.JFrame
{

    /**
     * Creates new form JFrame
     */
    public JFrame()
    {
        initComponents();
        ArrayList<JCheckBox> checkedDays = createBoxArray();

        jTable1.setRowHeight(jTable1.getRowHeight() + 3);
        jTable1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent me)
            {
                JTable table = (JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                if (me.getClickCount() == 2)
                {
                    empPopup.setTitle((String) jTable1.getValueAt(row, 0));
                    empPopup.setVisible(true);
                    ArrayList<Integer> l = new ArrayList();
                    for (JCheckBox checkedDay : checkedDays)
                    {
                        if (checkedDay.isSelected())
                        {
                            l.add(checkedDays.indexOf(checkedDay));
                        }
                        checkedDay.setSelected(false);
                    }

                    WorkDay one = null;
                    if (jCheckBox4.isSelected() && jCheckBox5.isSelected())
                    {
                        one = new WorkDay(jComboBox2.getSelectedItem().toString()
                                + " P.M."
                                + " - " + jComboBox3.getSelectedItem().toString()
                                + " P.M. ",
                                campusDrop.getSelectedItem().toString());
                    }
                    if (jCheckBox4.isSelected() && !jCheckBox5.isSelected())
                    {
                        one = new WorkDay(jComboBox2.getSelectedItem().toString()
                                + " P.M."
                                + " - " + jComboBox3.getSelectedItem().toString()
                                + " A.M. ",
                                campusDrop.getSelectedItem().toString());
                    }
                    if (!jCheckBox4.isSelected() && jCheckBox5.isSelected())
                    {
                        one = new WorkDay(jComboBox2.getSelectedItem().toString()
                                + " A.M."
                                + " - " + jComboBox3.getSelectedItem().toString()
                                + " P.M. ",
                                campusDrop.getSelectedItem().toString());
                    }
                    if (!jCheckBox4.isSelected() && !jCheckBox5.isSelected())
                    {
                        one = new WorkDay(jComboBox2.getSelectedItem().toString()
                                + " A.M."
                                + " - " + jComboBox3.getSelectedItem().toString()
                                + " A.M. ",
                                campusDrop.getSelectedItem().toString());
                    }
                    populateRow(jTable1, row, one, l);

                }
            }
        });
    }

    private ArrayList<JCheckBox> createBoxArray()
    {
        ArrayList<JCheckBox> checkedDays = new ArrayList();
        checkedDays.add(box1);
        checkedDays.add(box2);
        checkedDays.add(box3);
        checkedDays.add(box4);
        checkedDays.add(box5);
        checkedDays.add(box6);
        checkedDays.add(box7);

        return checkedDays;
    }

    public static Calendar DateToCalendar(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    private void populateRow(JTable table, int rowID, WorkDay w, ArrayList<Integer> l)
    {
        for (Integer l1 : l)
        {
            table.setValueAt(w.time + " " + w.campus.substring(0, 4), rowID, l1 + 1);
        }

    }

    public void saveTable() throws Exception
    {

        JFileChooser choose = new JFileChooser();
        choose.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Comma Seperated Value", "csv");
        choose.setFileFilter(filter);

        int status = choose.showSaveDialog(null);

        if (status == JFileChooser.APPROVE_OPTION)
        {

            BufferedWriter bfw = new BufferedWriter(new FileWriter(choose.getSelectedFile().getAbsolutePath() + ".csv"));
            for (int i = 0; i < jTable1.getColumnCount(); i++)
            {
                bfw.write((String) jTable1.getColumnModel().getColumn(i).getHeaderValue());
                bfw.write(",");
            }

            for (int i = 0; i < jTable1.getRowCount(); i++)
            {
                bfw.newLine();
                for (int j = 0; j < jTable1.getColumnCount(); j++)
                {
                    if (jTable1.getValueAt(i, j) == null)
                    {
                        bfw.write(" ");
                        bfw.write(",");
                    } else
                    {
                        bfw.write((String) jTable1.getValueAt(i, j));
                        bfw.write(",");
                    }
                }
            }
            bfw.close();
        }
    }

    public void openTable() throws FileNotFoundException, IOException
    {

        JFileChooser choose = new JFileChooser();
        choose.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Comma Seperated Value", "csv");
        choose.setFileFilter(filter);

        int status = choose.showOpenDialog(null);

        if (status == JFileChooser.APPROVE_OPTION)
        {
            CSVReader rowReader = new CSVReader(new FileReader(choose.getSelectedFile().getAbsolutePath()), ',', '\'', 1);
            List<String[]> myEntries = rowReader.readAll();
            String[][] dataArr = new String[myEntries.size()][];
            dataArr = myEntries.toArray(dataArr);

            CSVReader colReader = new CSVReader(new FileReader(choose.getSelectedFile().getAbsolutePath()));
            String[] col = Arrays.copyOf(colReader.readNext(),
                    colReader.readNext().length - 1);
            
           
            DefaultTableModel dtm = new DefaultTableModel(dataArr, col);

            //Setting new tableModel, frame title, and calendar text
            jTable1.setModel(dtm);
            this.setTitle(col[1].substring(5, 9) + "-\n" + col[col.length-1].substring(5, 9));
            jCalendarButton1.setText(col[1].substring(5, 9) + "-\n" + col[col.length-1].substring(5, 9));
           
            
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new JFrame().setVisible(true);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        empPopup = new javax.swing.JDialog();
        box1 = new javax.swing.JCheckBox();
        box2 = new javax.swing.JCheckBox();
        box3 = new javax.swing.JCheckBox();
        box4 = new javax.swing.JCheckBox();
        box5 = new javax.swing.JCheckBox();
        box6 = new javax.swing.JCheckBox();
        box7 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        campusDrop = new javax.swing.JComboBox();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jProgressBar1 = new javax.swing.JProgressBar();
        jCalendarButton1 = new org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();

        empPopup.setTitle("Work Days");
        empPopup.setMinimumSize(new java.awt.Dimension(410, 175));
        empPopup.setModal(true);

        box1.setText("Sun");

        box2.setText("Mon");

        box3.setText("Tue");

        box4.setText("Wed");

        box5.setText("Thu");

        box6.setText("Fri");

        box7.setText("Sat");

        jLabel1.setText("Start:");

        jLabel2.setText("Finish:");

        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Campus:");

        campusDrop.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Academy", "Prepatory", "Miller", "Montgomery" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00" }));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00" }));

        jCheckBox4.setText("P.M.");

        jCheckBox5.setText("P.M.");

        jLabel4.setText("Other:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "On", "xxx", "Vac" }));

        javax.swing.GroupLayout empPopupLayout = new javax.swing.GroupLayout(empPopup.getContentPane());
        empPopup.getContentPane().setLayout(empPopupLayout);
        empPopupLayout.setHorizontalGroup(
            empPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(empPopupLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(empPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(empPopupLayout.createSequentialGroup()
                        .addComponent(box1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(box2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(box3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(box4))
                    .addGroup(empPopupLayout.createSequentialGroup()
                        .addGroup(empPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(empPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(empPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(empPopupLayout.createSequentialGroup()
                                .addComponent(jCheckBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3))
                            .addGroup(empPopupLayout.createSequentialGroup()
                                .addComponent(jCheckBox5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4))
                            .addGroup(empPopupLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jButton1)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(empPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(empPopupLayout.createSequentialGroup()
                        .addComponent(box5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(box6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(box7))
                    .addComponent(campusDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        empPopupLayout.setVerticalGroup(
            empPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(empPopupLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(empPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(box1)
                    .addComponent(box2)
                    .addComponent(box3)
                    .addComponent(box4)
                    .addComponent(box5)
                    .addComponent(box6)
                    .addComponent(box7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(empPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox4)
                    .addComponent(campusDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(empPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(empPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jCheckBox5)
                        .addComponent(jLabel4)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Jose B.", null, null, null, null, null, null, null},
                {"Anita G.", null, null, null, null, null, null, null},
                {"Camilo C.", null, null, null, null, null, null, null},
                {"Michel M.", null, null, null, null, null, null, null},
                {"David V.", null, null, null, null, null, null, null},
                {"Jesse P.", null, null, null, null, null, null, null},
                {"John B.", null, null, null, null, null, null, null},
                {"Paul D. ", null, null, null, null, null, null, null},
                {"Jorge", null, null, null, null, null, null, null},
                {"Phil A. ", null, null, null, null, null, null, null},
                {"Marcos ", null, null, null, null, null, null, null},
                {"Steve G. ", null, null, null, null, null, null, null},
                {"Jamie", null, null, null, null, null, null, null},
                {"Antonio G. ", null, null, null, null, null, null, null},
                {"Pachev J.", null, null, null, null, null, null, null},
                {"Steven R", null, null, null, null, null, null, null},
                {"Daniel", null, null, null, null, null, null, null}
            },
            new String [] {
                "Employees", "Day 1", "Day 2 ", "Day 3", "Day 4", "Day 5", "Day 6", "Day 7"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jCalendarButton1.setText("Date");
        jCalendarButton1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jCalendarButton1PropertyChange(evt);
            }
        });

        jMenu1.setText("File");

        jMenuItem1.setText("New");
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Save");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Open");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);
        jMenu1.add(jSeparator1);

        jMenuItem4.setText("Print");
        jMenu1.add(jMenuItem4);

        jMenuItem7.setText("Save PDF");
        jMenu1.add(jMenuItem7);
        jMenu1.add(jSeparator2);

        jMenuItem5.setText("Exit");
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem6.setText("New Employee");
        jMenu2.add(jMenuItem6);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1194, Short.MAX_VALUE)))
            .addGroup(layout.createSequentialGroup()
                .addGap(507, 507, 507)
                .addComponent(jCalendarButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jCalendarButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jCalendarButton1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jCalendarButton1PropertyChange
        if (evt.getNewValue() instanceof Date)
        {

            //formating the date
            SimpleDateFormat format = new SimpleDateFormat("EEE MM/dd/yyyy");
            String DateToStr = format.format(evt.getNewValue());
            JTableHeader th = jTable1.getTableHeader();
            TableColumnModel tcm = th.getColumnModel();

            jTable1.getColumnModel().getColumn(1).setHeaderValue((DateToStr));
            Date n = new Date(DateToStr);
            Calendar tr = DateToCalendar(n);
            ArrayList<String> cals = new ArrayList();
            for (int i = 0; i < 6; i++)
            {
                tr.add(Calendar.DATE, 1);  //incrementing days by one
                String newDate = format.format(tr.getTime());
                cals.add(newDate);
            }
            for (int i = 2; i < 8; i++)
            {
                TableColumn tc = tcm.getColumn(i);
                tc.setHeaderValue(cals.get(i - 2));
            }
            th.repaint();
            jCalendarButton1.setText(DateToStr.substring(5, 9) + "-\n" + cals.get(5).substring(5, 9));
            this.setTitle(DateToStr.substring(5, 9) + "-\n" + cals.get(5).substring(5, 9));
        }
    }//GEN-LAST:event_jCalendarButton1PropertyChange

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try
        {

            saveTable();
        } catch (Exception ex)
        {
            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("It worked");
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        // TODO add your handling code here:
        empPopup.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        try
        {
            openTable();
        } catch (IOException ex)
        {
            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("it worked");
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox box1;
    private javax.swing.JCheckBox box2;
    private javax.swing.JCheckBox box3;
    private javax.swing.JCheckBox box4;
    private javax.swing.JCheckBox box5;
    private javax.swing.JCheckBox box6;
    private javax.swing.JCheckBox box7;
    private javax.swing.JComboBox campusDrop;
    private javax.swing.JDialog empPopup;
    private javax.swing.JButton jButton1;
    private org.jbundle.thin.base.screen.jcalendarbutton.JCalendarButton jCalendarButton1;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
