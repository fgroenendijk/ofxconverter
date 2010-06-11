/*
 * OFXConverterView.java
 */

package ofxconverter;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.table.TableColumn;
import ofxconverter.gui.FileTableModel;
import ofxconverter.gui.renderer.ComboBoxCellEditor;
import ofxconverter.gui.renderer.ComboBoxCellRenderer;
import ofxconverter.module.input.Bank;
import ofxconverter.module.input.IngPostbank;
import ofxconverter.module.input.Rabobank;
import ofxconverter.module.output.Ofx;
import ofxconverter.structure.BankStatement;
import ofxconverter.util.CheckFile;

/**
 * The application's main frame.
 */
public class OFXConverterView extends FrameView {

    private ResourceMap resourceMap = getResourceMap();
    ImageIcon icon = null;

    public OFXConverterView(SingleFrameApplication app) {

        super(app);

        java.net.URL imageURL = OFXConverterView.class.getResource("resources/icon.png");
        if (imageURL != null) {
            icon = new ImageIcon(imageURL);
            this.getFrame().setIconImage(icon.getImage());
        }

        initComponents();

    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = OFXConverterApp.getApplication().getMainFrame();
            aboutBox = new OFXConverterAboutBox(mainFrame);
            aboutBox.setIconImage(icon.getImage());
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        OFXConverterApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        fileTable = new javax.swing.JTable();
        processButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem openMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ofxconverter.OFXConverterApp.class).getContext().getResourceMap(OFXConverterView.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tableModel = new FileTableModel( resourceMap );
        fileTable.setModel( tableModel );
        // These are the combobox values

        ArrayList<String> list = new ArrayList<String>();
        list.add("");

        for(FileType fileType: FileType.values() ){
            if( !fileType.isGeneral() ){
                list.add( fileType.getName() );
            }
        }

        String[] values = new String[]{};

        values = list.toArray( values );

        // Set the combobox editor on the 1st visible column
        int vColIndex = 2;

        TableColumn col = fileTable.getColumnModel().getColumn(vColIndex);

        col.setCellEditor(new ComboBoxCellEditor(values));
        // If the cell should appear like a combobox in its
        // non-editing state, also set the combobox renderer
        col.setCellRenderer(new ComboBoxCellRenderer(values));
        fileTable.setFillsViewportHeight(true);
        fileTable.setName("fileTable"); // NOI18N
        jScrollPane1.setViewportView(fileTable);

        processButton.setText(resourceMap.getString("processButton.text")); // NOI18N
        processButton.setName("processButton"); // NOI18N
        processButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                processButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(352, Short.MAX_VALUE)
                .addComponent(processButton)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(processButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setText(resourceMap.getString("openMenuItem.text")); // NOI18N
        openMenuItem.setName("openMenuItem"); // NOI18N
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ofxconverter.OFXConverterApp.class).getContext().getActionMap(OFXConverterView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        JFileChooser open = new JFileChooser();
        open.setControlButtonsAreShown(true);
        open.setDialogType(JFileChooser.OPEN_DIALOG);

        open.setDoubleBuffered(true);

        open.setMultiSelectionEnabled(true);
        open.setFileSelectionMode(JFileChooser.FILES_ONLY);
        open.setCurrentDirectory(null);

        int result = open.showOpenDialog(null);
        if( result == JFileChooser.APPROVE_OPTION ){
            File [] files = open.getSelectedFiles();
            StringBuilder fileList = new StringBuilder();
            CheckFile checkFile = new CheckFile();

            // TODO: have error icon when ioException was thrown
            // TODO: have warning icon when the file has not been properly identified
            // TODO: move this bank selection code to another class

            for ( File file: files ){
                if( checkFile.isValid(file) ){

                    FileHandler fileHandler = new FileHandler( file, checkFile.getFileType(), checkFile.hasHeader() );

                    tableModel.addRow( new Object[] { false, file.getAbsolutePath(), fileHandler } );

                }else{
                    fileList.append("INVALID").append( file.getName() ).append("");
                }
            }

        }
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void processButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_processButtonMouseClicked
        for( int i = 0; i < tableModel.getRowCount(); i++ ){
            Object object = tableModel.getValueAt(i, 0);
            if( object instanceof Boolean ){
                Boolean checked = (Boolean) object;
                if( !checked ){
                    continue;
                }
            }
            object = tableModel.getObjectAt( i );
            if( object instanceof FileHandler ){
                FileHandler fileHandler = (FileHandler) object;

                Bank bank = null;

                switch( fileHandler.getType() ){
                    case CSV_RABOBANK: bank = new Rabobank();
                                       break;
                    case CSV_ING: bank = new IngPostbank();
                                  break;
                }

                if( bank != null ){
                    BankStatement bankStatement = bank.readFile( fileHandler.getFile() );

                    Ofx ofxWriter = new Ofx();
                    ofxWriter.createXmlFile( fileHandler.getFile(), bankStatement );
                }

            }
        }
    }//GEN-LAST:event_processButtonMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable fileTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton processButton;
    // End of variables declaration//GEN-END:variables

    private FileTableModel tableModel = null;

    private JDialog aboutBox;
}
