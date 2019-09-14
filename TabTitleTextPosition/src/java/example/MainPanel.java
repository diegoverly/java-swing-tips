// -*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
// @homepage@

package example;

import java.awt.*;
import java.util.Objects;
import java.util.Optional;
import javax.swing.*;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthConstants;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;

public final class MainPanel extends JPanel {
  private static void addTab(JTabbedPane tabbedPane, String title, Icon icon, Component c) {
    tabbedPane.addTab(title, c);
    JLabel label = new JLabel(title, icon, SwingConstants.CENTER);
    label.setVerticalTextPosition(SwingConstants.BOTTOM);
    label.setHorizontalTextPosition(SwingConstants.CENTER);
    // label.setVerticalAlignment(SwingConstants.CENTER);
    // label.setHorizontalAlignment(SwingConstants.CENTER);
    tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, label);
  }

  private MainPanel() {
    super(new BorderLayout());
    JTabbedPane t = new ClippedTitleTabbedPane();
    t.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    // [XP Style Icons - Download](https://xp-style-icons.en.softonic.com/)
    addTab(t, "JTree", new ImageIcon(getClass().getResource("wi0009-32.png")), new JScrollPane(new JTree()));
    addTab(t, "JTextArea", new ImageIcon(getClass().getResource("wi0054-32.png")), new JScrollPane(new JTextArea()));
    addTab(t, "Preference", new ImageIcon(getClass().getResource("wi0062-32.png")), new JScrollPane(new JTree()));
    addTab(t, "Help", new ImageIcon(getClass().getResource("wi0063-32.png")), new JScrollPane(new JTextArea()));

    // t.addTab(makeTitle("Title", "wi0009-32.png"), new JLabel("a"));
    // t.addTab(makeTitle("Help", "wi0054-32.png"), new JLabel("b"));

    add(t);
    setPreferredSize(new Dimension(320, 240));
  }
  // private String makeTitle(String t, String p) {
  //   return "<html><center><img src='" + getClass().getResource(p) + "'/><br/>" + t;
  // }

  public static void main(String[] args) {
    EventQueue.invokeLater(MainPanel::createAndShowGui);
  }

  private static void createAndShowGui() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
      ex.printStackTrace();
      Toolkit.getDefaultToolkit().beep();
    }
    JFrame frame = new JFrame("@title@");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.getContentPane().add(new MainPanel());
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}

class ClippedTitleTabbedPane extends JTabbedPane {
  protected ClippedTitleTabbedPane() {
    super();
  }

  protected ClippedTitleTabbedPane(int tabPlacement) {
    super(tabPlacement);
  }

  private Insets getSynthInsets(Region region) {
    SynthStyle style = SynthLookAndFeel.getStyle(this, region);
    SynthContext context = new SynthContext(this, region, style, SynthConstants.ENABLED);
    return style.getInsets(context, null);
  }

  protected Insets getTabInsets() {
    return Optional.ofNullable(UIManager.getInsets("TabbedPane.tabInsets"))
        .orElseGet(() -> getSynthInsets(Region.TABBED_PANE_TAB));
  }

  protected Insets getTabAreaInsets() {
    return Optional.ofNullable(UIManager.getInsets("TabbedPane.tabAreaInsets"))
        .orElseGet(() -> getSynthInsets(Region.TABBED_PANE_TAB_AREA));
  }

  @Override public void doLayout() {
    int tabCount = getTabCount();
    if (tabCount == 0 || !isVisible()) {
      super.doLayout();
      return;
    }
    Insets tabInsets = getTabInsets();
    Insets tabAreaInsets = getTabAreaInsets();
    Insets insets = getInsets();
    int tabPlacement = getTabPlacement();
    int areaWidth = getWidth() - tabAreaInsets.left - tabAreaInsets.right - insets.left - insets.right;
    int tabWidth; // = tabInsets.left + tabInsets.right + 3;
    int gap;

    if (tabPlacement == LEFT || tabPlacement == RIGHT) {
      tabWidth = areaWidth / 4;
      gap = 0;
    } else { // TOP || BOTTOM
      tabWidth = areaWidth / tabCount;
      gap = areaWidth - tabWidth * tabCount;
    }

    // "3" is magic number @see BasicTabbedPaneUI#calculateTabWidth
    tabWidth -= tabInsets.left + tabInsets.right + 3;
    updateAllTabWidth(tabWidth, gap);

    super.doLayout();
  }

  // @Override public void insertTab(String title, Icon icon, Component component, String tip, int index) {
  //   super.insertTab(title, icon, component, Objects.toString(tip, title), index);
  //   setTabComponentAt(index, new JLabel(title, icon, SwingConstants.CENTER));
  // }

  protected void updateAllTabWidth(int tabWidth, int gap) {
    Dimension dim = new Dimension();
    int rest = gap;
    for (int i = 0; i < getTabCount(); i++) {
      JComponent tab = (JComponent) getTabComponentAt(i);
      if (Objects.nonNull(tab)) {
        int a = (i == getTabCount() - 1) ? rest : 1;
        int w = rest > 0 ? tabWidth + a : tabWidth;
        dim.setSize(w, tab.getPreferredSize().height);
        tab.setPreferredSize(dim);
        rest -= a;
      }
    }
  }
}
