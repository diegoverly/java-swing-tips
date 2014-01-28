package example;
//-*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class MainPanel extends JPanel {
    private static final Highlighter.HighlightPainter highlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
    private final JTextArea textArea = new JTextArea();
    private final JComboBox<String> combo = new JComboBox<>();
    private static final String initTxt =
      "Trail: Creating a GUI with JFC/Swing\n" +
      "Lesson: Learning Swing by Example\n" +
      "This lesson explains the concepts you need to use Swing components in building a user interface." +
      " First we examine the simplest Swing application you can write." +
      " Then we present several progressively complicated examples of creating user interfaces using components in the javax.swing package." +
      " We cover several Swing components, such as buttons, labels, and text areas." +
      " The handling of events is also discussed, as are layout management and accessibility." +
      " This lesson ends with a set of questions and exercises so you can test yourself on what you?ve learned.\n" +
      "http://docs.oracle.com/javase/tutorial/uiswing/learn/index.html\n";
    public MainPanel(final JFrame frame) {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        textArea.setText(initTxt);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("swing");
        combo.setModel(model);

        JButton searchButton = new JButton(new AbstractAction("Search") {
            @Override public void actionPerformed(ActionEvent e) {
                searchActionPerformed();
            }
        });
        frame.getRootPane().setDefaultButton(searchButton);

        combo.setEditable(true);
//         combo.addItemListener(new ItemListener() {
//             private boolean adj = false;
//             @Override public void itemStateChanged(ItemEvent e) {
//             //    if(combo.isPopupVisible()) { return; }
//                 if(!adj && e.getStateChange()==ItemEvent.SELECTED) {
//                     adj = true;
//                     searchActionPerformed();
//                     adj = false;
//                 }
//             }
//         });

        JPanel p = new JPanel(new BorderLayout(5,5));
        p.setBorder(BorderFactory.createEmptyBorder(0,5,5,0));
        p.add(new JLabel("Search History:"), BorderLayout.WEST);
        p.add(combo);
        p.add(searchButton, BorderLayout.EAST);

        add(p, BorderLayout.NORTH);
        add(new JScrollPane(textArea));
        setPreferredSize(new Dimension(320, 240));
    }
    public static boolean addItem(JComboBox<String> combo, String str, int max) {
        //if(str==null || str.trim().length()==0) { return false; }
        if(str==null || str.length()==0) {
            return false;
        }
        combo.setVisible(false);
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>)combo.getModel();
        model.removeElement(str);
        model.insertElementAt(str, 0);
        if(model.getSize()>max) {
            model.removeElementAt(max);
        }
        combo.setSelectedIndex(0);
        combo.setVisible(true);
        return true;
    }
    private void searchActionPerformed() {
        String pattern = (String)combo.getEditor().getItem();
        if(addItem(combo, pattern, 4)) {
            //pattern = pattern.trim();
            //combo.getEditor().setItem(pattern);
            setHighlight(textArea, pattern);
        }else{
            //combo.getEditor().setItem("");
            textArea.getHighlighter().removeAllHighlights();
        }
    }
    public void setHighlight(JTextComponent jtc, String pattern) {
        Highlighter highlighter = jtc.getHighlighter();
        highlighter.removeAllHighlights();
        try{
            Document doc = jtc.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;
            while((pos = text.indexOf(pattern, pos)) >= 0) {
                highlighter.addHighlight(pos, pos+pattern.length(), highlightPainter);
                pos += pattern.length();
            }
        }catch(BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGUI();
            }
        });
    }
    public static void createAndShowGUI() {
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(ClassNotFoundException | InstantiationException |
               IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("@title@");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel(frame));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
