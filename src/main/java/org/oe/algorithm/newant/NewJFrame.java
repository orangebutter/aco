package org.oe.algorithm.newant;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author ?
 */
public class NewJFrame extends javax.swing.JFrame {
    /**
     * Creates new form NewJFrame
     */
    //private static ACO aco;
    //flagRadio-x: ???????????????????
    public static int
            flagRadio1 = 0, flagRadio2 = 0, flagRadio3 = 0, flagRadio4 = 0, flagRadio5 = 0, flagRadio6 = 0, flagRadio7 = 0;
    //???????:???????????????????????????
    public static int antNum, genNum, QQ;
    public static float alf, bta, h;
    public static float initQ;
    public static String route;
    public static int flag;//????-???????
    public static int updateTimes = 0;


    public NewJFrame() {
        initComponents();
        //????????????,????????,???jButton_actionPerformed(e)???
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButton_actionPerformed(e);
            }
        });
        //????5???????????,????????,
        //?????Radio-x()??,??????????
        jRadioButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Radio1(e);
            }
        });
        jRadioButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Radio2(e);
            }
        });
        jRadioButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Radio3(e);
            }
        });
        jRadioButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Radio4(e);
            }
        });
        jRadioButton5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Radio5(e);
            }
        });
        jRadioButton6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Radio6(e);
            }
        });
        jRadioButton7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Radio7(e);
            }
        });
    }


    /*????5??? Radio-x(e),????????????????
    * ?????,??????????,??parameter?????????????????
    * ???????????,??????????,?????????
    */
    private void Radio1(ActionEvent e) {
        flagRadio1 = ~flagRadio1; //?????,??flagRadio-x??
        if (flagRadio1 == -1) //????,?????
            jTextField1.setText(String.valueOf(Parameter.AntNum));
        else if (flagRadio1 == 0) //???????????
            jTextField1.setText(null);
    }

    private void Radio2(ActionEvent e) {
        flagRadio2 = ~flagRadio2;
        if (flagRadio2 == -1)
            jTextField2.setText(String.valueOf(Parameter.Gen_Num));
        else if (flagRadio2 == 0)
            jTextField2.setText(null);
    }

    private void Radio3(ActionEvent e) {
        flagRadio3 = ~flagRadio3;
        if (flagRadio3 == -1)
            jTextField3.setText(String.valueOf(Parameter.alf));
        else if (flagRadio3 == 0)
            jTextField3.setText(null);
    }

    private void Radio4(ActionEvent e) {
        flagRadio4 = ~flagRadio4;
        if (flagRadio4 == -1)
            jTextField4.setText(String.valueOf(Parameter.bta));
        else if (flagRadio4 == 0)
            jTextField4.setText(null);
    }

    private void Radio5(ActionEvent e) {
        flagRadio5 = ~flagRadio5;
        if (flagRadio5 == -1) {
            jTextField5.setText(String.valueOf(Parameter.Rho));
        } else if (flagRadio5 == 0)
            jTextField5.setText(null);
    }

    private void Radio6(ActionEvent e) {
        flagRadio6 = ~flagRadio6;
        if (flagRadio6 == -1) {
            jTextField6.setText(String.valueOf(Parameter.Q));
        } else if (flagRadio6 == 0)
            jTextField6.setText(null);
    }

    private void Radio7(ActionEvent e) {
        flagRadio7 = ~flagRadio7;
        if (flagRadio7 == -1) {
            jTextField7.setText(String.valueOf(Parameter.initQ));
        } else if (flagRadio7 == 0)
            jTextField7.setText(null);
    }

    /**
     * ??????:??????????????????????????,?????
     * ?????
     * ?????????????->
     * ???ACO-???????->
     * ??????->
     * ???????????????,??:??????????
     * (?????????????????????,??????????)
     *
     * @param e
     */
    private void jButton_actionPerformed(ActionEvent e) {
        flag = jComboBox1.getSelectedIndex();
        String str = "";
        //??????????????,??????????????????,???parameter??????
        antNum = Integer.valueOf(jTextField1.getText()).intValue();
        genNum = Integer.valueOf(jTextField2.getText()).intValue();
        alf = Float.valueOf(jTextField3.getText()).floatValue();
        bta = Float.valueOf(jTextField4.getText()).floatValue();
        h = Float.valueOf(jTextField5.getText()).floatValue();
        QQ = Integer.valueOf(jTextField6.getText()).intValue();
        initQ = Float.valueOf(jTextField7.getText()).floatValue();

        //???ACO-???????,??ACO??solve????????
        System.out.println("~~~~~~~~???????????~~~~~~~~~~~~~~~~~~~~~~~~\n");
        ACO aco = new ACO(Parameter.CityNum, antNum, genNum, alf, bta, h, QQ, initQ);
        aco.init();
        aco.solve();
        //???????x->y->z??????????
        for (int i = 0; i < aco.getCityNum() + 1; i++) {
            str += String.valueOf(aco.getBestTour()[i]) + "->";
            if (i % 12 == 0 && i != 0) {
                str += "\n";
            }
        }
        //??????????????????????
        //JOptionPane.showMessageDialog(getParent(), "? ? ? ? ? ? = "+aco.getBestLength() + "\n" + "????????\n" + str);
        route = str;
        new NewJFramePath().setVisible(true);
    }



    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration 13


    private void initComponents() {
        setupUI();
//        JFrame frame = new JFrame("NewFrame");
        this.setContentPane(jPanel1);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

    }
    private void setupUI() {
        jPanel1 = new JPanel();
        jPanel1.setLayout(new GridBagLayout());
        jComboBox1 = new JComboBox();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jPanel1.add(jComboBox1, gbc);
        jLabel1 = new JLabel();
        jLabel1.setText("????");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jLabel1, gbc);
        jRadioButton1 = new JRadioButton();
        jRadioButton1.setText("?????");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jRadioButton1, gbc);
        jLabel2 = new JLabel();
        jLabel2.setText("????");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jLabel2, gbc);
        jRadioButton2 = new JRadioButton();
        jRadioButton2.setText("?????");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jRadioButton2, gbc);
        jLabel3 = new JLabel();
        jLabel3.setText("??A");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jLabel3, gbc);
        jRadioButton3 = new JRadioButton();
        jRadioButton3.setText("?????");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jRadioButton3, gbc);
        jLabel4 = new JLabel();
        jLabel4.setText("??B");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jLabel4, gbc);
        jRadioButton4 = new JRadioButton();
        jRadioButton4.setText("?????");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jRadioButton4, gbc);
        jLabel5 = new JLabel();
        jLabel5.setText("???????");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jLabel5, gbc);
        jTextField5 = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextField5, gbc);
        jRadioButton5 = new JRadioButton();
        jRadioButton5.setText("?????");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jRadioButton5, gbc);
        jLabel6 = new JLabel();
        jLabel6.setText("?????");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jLabel6, gbc);
        jTextField6 = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextField6, gbc);
        jTextField1 = new JTextField();
        jTextField1.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextField1, gbc);
        jTextField2 = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextField2, gbc);
        jTextField3 = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextField3, gbc);
        jTextField4 = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextField4, gbc);
        jRadioButton6 = new JRadioButton();
        jRadioButton6.setText("?????");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jRadioButton6, gbc);
        jLabel7 = new JLabel();
        jLabel7.setText("????????");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jLabel7, gbc);
        jTextField7 = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        jPanel1.add(jTextField7, gbc);
        jRadioButton7 = new JRadioButton();
        jRadioButton7.setText("?????");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel1.add(jRadioButton7, gbc);
        jButton1 = new JButton();
        jButton1.setText("??");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        jPanel1.add(jButton1, gbc);
    }
    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here: 12
    }

    private void jRadioButton7ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }}
