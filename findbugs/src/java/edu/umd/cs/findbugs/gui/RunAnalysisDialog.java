/*
 * RunAnalysisDialog.java
 *
 * Created on April 1, 2003, 3:22 PM
 */

package edu.umd.cs.findbugs.gui;

import java.awt.event.WindowEvent;
import javax.swing.*;
import edu.umd.cs.findbugs.*;

/**
 *
 * @author  daveho
 */
public class RunAnalysisDialog extends javax.swing.JDialog {
    
    private final AnalysisRun analysisRun;
    private Thread analysisThread;
    private boolean completed;
    
    /** Creates new form RunAnalysisDialog */
    public RunAnalysisDialog(java.awt.Frame parent, AnalysisRun analysisRun_) {
        super(parent, true);
        initComponents();
        this.analysisRun = analysisRun_;
        this.completed = false;
        
        // Create a progress callback to give the user feedback
        // about how far along we are.
        final FindBugsProgress progress = new FindBugsProgress() {
            public void reportNumberOfArchives(final int numArchives) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        archivesProgress.setMinimum(0);
                        archivesProgress.setMaximum(numArchives);
                    }
                });
            }     
            
            public void startArchive(final String archiveFile, final int numClasses) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        archiveName.setText(archiveFile);
                        classesProgress.setValue(0);
                        classesProgress.setMinimum(0);
                        classesProgress.setMaximum(numClasses);
                    }
                });
            }
            
            public void finishClass() {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        classesProgress.setValue(classesProgress.getValue() + 1);
                    }
                });
            }
            
            public void finishArchive() {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        archivesProgress.setValue(archivesProgress.getValue() + 1);
                    }
                });
            }
        };
        
        // This is the thread that will actually run the analysis.
        this.analysisThread = new Thread() {
            public void run() {
                try {
                    analysisRun.execute(progress);
                    setCompleted(true);
                } catch (java.io.IOException e) {
                    // TODO: log the exception
                } catch (InterruptedException e) {
                    // TODO: log the fact that the user cancelled the analysis
                    System.out.println("User cancelled the analysis!");
                }
                
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        // Send a message to the dialog that it should close
                        // That way, it goes away without any need for user intervention
                        closeDialog(new WindowEvent(RunAnalysisDialog.this, WindowEvent.WINDOW_CLOSING));
                    }
                });
            }
        };
    }
    
    public synchronized void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    /**
     * The creator of the dialog may call this method to find out whether
     * or not the analysis completed normally.
     */
    public synchronized boolean isCompleted() { return completed; }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        findBugsLabel = new javax.swing.JLabel();
        archivesLabel = new javax.swing.JLabel();
        classesLabel = new javax.swing.JLabel();
        archivesProgress = new javax.swing.JProgressBar();
        classesProgress = new javax.swing.JProgressBar();
        cancelButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        archiveLabel = new javax.swing.JLabel();
        archiveName = new javax.swing.JLabel();
        topVerticalFiller = new javax.swing.JLabel();
        bottomVerticalFiller = new javax.swing.JLabel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        findBugsLabel.setBackground(new java.awt.Color(0, 0, 204));
        findBugsLabel.setFont(new java.awt.Font("Dialog", 1, 24));
        findBugsLabel.setForeground(new java.awt.Color(255, 255, 255));
        findBugsLabel.setText("Find Bugs!");
        findBugsLabel.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(findBugsLabel, gridBagConstraints);

        archivesLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        archivesLabel.setText("Archives:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        getContentPane().add(archivesLabel, gridBagConstraints);

        classesLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        classesLabel.setText("Classes:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        getContentPane().add(classesLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        getContentPane().add(archivesProgress, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        getContentPane().add(classesProgress, gridBagConstraints);

        cancelButton.setFont(new java.awt.Font("Dialog", 0, 12));
        cancelButton.setText("Cancel");
        cancelButton.setToolTipText("null");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        getContentPane().add(cancelButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jSeparator1, gridBagConstraints);

        archiveLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        archiveLabel.setText("Archive:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        getContentPane().add(archiveLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(archiveName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.5;
        getContentPane().add(topVerticalFiller, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 0.5;
        getContentPane().add(bottomVerticalFiller, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        // All we need to do to cancel the analysis is to interrupt
        // the analysis thread.
        analysisThread.interrupt();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // Here is where we actually kick off the analysis thread.
        analysisThread.start();
    }//GEN-LAST:event_formWindowOpened
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar classesProgress;
    private javax.swing.JLabel archiveName;
    private javax.swing.JLabel classesLabel;
    private javax.swing.JProgressBar archivesProgress;
    private javax.swing.JLabel archiveLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel findBugsLabel;
    private javax.swing.JLabel bottomVerticalFiller;
    private javax.swing.JLabel archivesLabel;
    private javax.swing.JLabel topVerticalFiller;
    // End of variables declaration//GEN-END:variables
    
}
