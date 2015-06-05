package pl.siimplon.desktop;

import com.google.common.io.Files;
import org.w3c.dom.Document;
import pl.siimplon.reporter.ReportContext;
import pl.siimplon.reporter.scheme.transfer.Transfer;
import pl.siimplon.reporter.scheme.transfer.TransferPair;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class TransferListEditor extends JDialog {

    private JPanel contentPane;

    private JButton buttonOK;
    private JButton buttonCancel;

    private JList<String> listTransfers;

    private JComboBox<String> comboBoxSelect;

    private JButton buttonAdd;
    private JButton buttonDelete;
    private JTextField textFieldName;
    private JButton buttonsaveToXml;
    private JButton buttonLoadFromXml;
    private JButton buttonUp;
    private JButton buttonDown;

    private final List<TransferPair> transfer;

    private int status;

    private DefaultListModel<String> listModel;

    private ReportContext reportContext;

    public TransferListEditor(ReportContext reportContext) {
        this(reportContext, new ArrayList<TransferPair>(), "new transfer");
    }

    public TransferListEditor(ReportContext reportContext, List<TransferPair> transfer) {
        this(reportContext, transfer, "new transfer");
    }

    public TransferListEditor(final ReportContext reportContext, final List<TransferPair> transfer, String transferName) {
        this.reportContext = reportContext;
        this.transfer = new ArrayList<TransferPair>();
        this.transfer.addAll(transfer);

        textFieldName.setText(transferName);

        setContentPane(contentPane);
        setModal(true);
//        getRootPane().setDefaultButton(buttonOK);
        setName(ResourceBundle.getBundle("names").getString("form.main.dialog.transferListEditor"));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                onCancel();
            }
        });

        for (Transfer t : EnumSet.allOf(Transfer.class)) {
            comboBoxSelect.addItem(t.name());
        }
        listModel = new DefaultListModel<String>();
        listTransfers.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                buttonDelete.setEnabled(listSelectionEvent.getFirstIndex() != -1 &&
                        listSelectionEvent.getLastIndex() != -1);
            }
        });
        listTransfers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TransferPair pair = getSelectedTransferPair();
                    if (pair.getSource().getAttrSize() > 0) {
                        TransferEditor editor = getTransferEditor(pair.getSource(), pair.getAttributes());
                        editor.setVisible(true);
                        int status = editor.getStatus();
                        if (status == JOptionPane.OK_OPTION) {
                            getTransferPairList().set(listTransfers.getSelectedIndex(), editor.getResultTransfer());
                            updateTransfersList();
                        }
                    }
                }
            }
        });
        listTransfers.setModel(listModel);

        updateTransfersList();

        listTransfers.setSelectedIndex(-1);
        buttonDelete.setEnabled(false);
        buttonDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                getTransferPairList().remove(listTransfers.getSelectedIndex());
                updateTransfersList();
            }
        });
        buttonAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Transfer t = getSelectedTransfer();
                if (t.getAttrSize() == 0) {
                    getTransferPairList().add(new TransferPair(t, ""));
                    updateTransfersList();
                } else {
                    TransferEditor transferEditor = getTransferEditor(t, new Object[t.getAttrSize()]);
                    transferEditor.setVisible(true);
                    int status = transferEditor.getStatus();
                    if (status == JOptionPane.OK_OPTION) {
                        getTransferPairList().add(transferEditor.getResultTransfer());
                        updateTransfersList();
                    }
                }
            }
        });

        buttonUp.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                int index = listTransfers.getSelectedIndex();
                if (index != 0 && index != -1) {
                    List<TransferPair> list = getTransferPairList();
                    TransferPair a = list.get(index);
                    TransferPair b = list.get(index - 1);
                    list.set(index - 1, a);
                    list.set(index, b);
                    updateTransfersList();

                    listTransfers.setSelectedIndex(index - 1);
                }
            }
        });

        buttonDown.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                int index = listTransfers.getSelectedIndex();
                List<TransferPair> tpl = getTransferPairList();
                if (index != tpl.size() - 1 && index != -1) {
                    TransferPair a = tpl.get(index);
                    TransferPair b = tpl.get(index + 1);

                    tpl.set(index + 1, a);
                    tpl.set(index, b);

                    updateTransfersList();
                    listTransfers.setSelectedIndex(index + 1);
                }
            }
        });

        buttonsaveToXml.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.home"));
                int result = jFileChooser.showSaveDialog(TransferListEditor.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        Document transferXML = reportContext.getTransferXML(getTransferPairList(), textFieldName.getText());
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                        DOMSource source = new DOMSource(transferXML);
                        StreamResult xmlResult = new StreamResult(jFileChooser.getSelectedFile());

                        // Output to console for testing
                        // StreamResult result = new StreamResult(System.out);

                        transformer.transform(source, xmlResult);
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (TransformerConfigurationException e) {
                        e.printStackTrace();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        buttonLoadFromXml.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser jFileChooser = new JFileChooser(System.getProperty("user.home"));
                int result = jFileChooser.showOpenDialog(TransferListEditor.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        setTransferPairList(reportContext.parseXMLList(new FileInputStream(jFileChooser.getSelectedFile())));
                        String nameWithoutExtension = Files.getNameWithoutExtension(jFileChooser.getSelectedFile().getName());
                        textFieldName.setText(nameWithoutExtension);
                        updateTransfersList();
                    } catch (XMLStreamException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        this.status = JOptionPane.CANCEL_OPTION;
        pack();
    }

    private Transfer getSelectedTransfer() {
        return Transfer.valueOf(((String) comboBoxSelect.getSelectedItem()));
    }

    private TransferPair getSelectedTransferPair() {
        return getTransferPairList().get(listTransfers.getSelectedIndex());
    }

    //TODO: if modification then delete, if new than add
    private TransferEditor getTransferEditor(Transfer t, Object[] attributes) {
        return new TransferEditor(new TransferPair(t, attributes));
//        transferEditor.setVisible(true);
//        int status = transferEditor.getStatus();
//        if (status == JOptionPane.OK_OPTION) {
//            TransferPair resultTransfer = transferEditor.getResultTransfer();
//            if (resultTransfer != null) {
//            }
//        }
    }

    private void updateTransfersList() {
        listModel.removeAllElements();
        for (TransferPair pair : transfer) {
            listModel.addElement(pair.toString());
        }
    }

    private void onCancel() {
        this.status = JOptionPane.CANCEL_OPTION;
        dispose();
    }

    private void onOK() {
        this.status = JOptionPane.OK_OPTION;
        dispose();
    }

    public List<TransferPair> getTransferPairList() {
        return transfer;
    }

    private void setTransferPairList(List<TransferPair> transferPairs) {
        this.transfer.clear();
        for (TransferPair transferPair : transferPairs) {
            this.transfer.add(transferPair);
        }
    }

    public int getStatus() {
        return status;
    }

    public String getTransferName() {
        return textFieldName.getText();
    }
}
