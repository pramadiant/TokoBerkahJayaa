package config;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class AppStyle {
    
    public static final Color BG_PRIMARY = Color.decode("#f1f5f9"); 
    public static final Color BG_HEADER = Color.decode("#4B2464");   
    public static final Color BG_SIDEBAR = Color.decode("#4B2464");    
    public static final Color TEXT_NORMAL = Color.decode("#0f172a");   
    
    public static final Color BTN_SUCCESS = Color.decode("#10b981");  
    public static final Color BTN_DANGER = Color.decode("#f43f5e");   
    public static final Color BTN_PRIMARY = Color.decode("#4B2464"); 
    public static final Color BTN_WARNING = Color.decode("#f59e0b"); 
    
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    
    public static final EmptyBorder PADDING_FORM = new EmptyBorder(25, 30, 25, 30);
    
    public static void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(38);
        table.setShowHorizontalLines(true);
        table.setGridColor(Color.decode("#f1f5f9"));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Color.decode("#f1f5f9"));
        header.setForeground(Color.decode("#475569"));
    }
    
    public static void styleCard(JPanel panel) {
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#e2e8f0"), 1, true),
            new EmptyBorder(20, 25, 20, 25)
        ));
    }
    
    public static void styleUnifiedCard(JPanel pnlMainCard, JPanel pnlHeader, JPanel pnlKiri, JPanel pnlKanan) {
        pnlMainCard.setBackground(Color.WHITE);
        pnlMainCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.decode("#e2e8f0"), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        pnlHeader.setOpaque(true);
        pnlHeader.setBackground(BG_SIDEBAR);
        pnlHeader.setPreferredSize(new Dimension(0, 95));
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#cbd5e1")),
            BorderFactory.createEmptyBorder(20, 20, 15, 20)
        ));
        for (Component comp : pnlHeader.getComponents()) {
            if (comp instanceof JLabel) {
                comp.setForeground(Color.WHITE);
            }
        }
        
        pnlKiri.setBackground(Color.WHITE);
        pnlKiri.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, Color.decode("#f1f5f9")), 
            new EmptyBorder(15, 10, 15, 20)
        ));
        
        pnlKanan.setBackground(Color.WHITE);
        pnlKanan.setBorder(new EmptyBorder(15, 20, 15, 10));
    }
    
    public static void styleInput(JComponent input) {
        input.setFont(FONT_INPUT);
        if (input instanceof JTextField || input instanceof JComboBox) {
            if (input instanceof JTextField) {
                input.setPreferredSize(new Dimension(200, 38));
            }
        }
    }
    
    public static JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.putClientProperty("JButton.buttonType", "roundRect");
        return btn;
    }
    
    public static void addFormField(JPanel panel, GridBagConstraints gbc, int y, String labelText, JComponent input) {
        gbc.gridy = y;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 5, 0);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_NORMAL);
        panel.add(lbl, gbc);

        gbc.gridy = y + 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        if (input != null) {
            styleInput(input);
            panel.add(input, gbc);
        }
    }
    
    public static void validateNameInput(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && !Character.isWhitespace(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE && c != java.awt.event.KeyEvent.VK_DELETE) {
            evt.consume();
            showInputError(evt.getComponent(), "Hanya huruf yang diperbolehkan");
        }
    }
    
    public static void validateNumberInput(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE && c != java.awt.event.KeyEvent.VK_DELETE) {
            evt.consume();
            showInputError(evt.getComponent(), "Hanya angka yang diperbolehkan");
        }
    }
    
    public static void validateAlphanumericInput(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetterOrDigit(c) && c != java.awt.event.KeyEvent.VK_BACK_SPACE && c != java.awt.event.KeyEvent.VK_DELETE) {
            evt.consume();
            showInputError(evt.getComponent(), "Hanya huruf dan angka");
        }
    }
    
    private static javax.swing.Timer errorTimer;
    private static javax.swing.Popup errorPopup;
    private static java.awt.Component currentErrorComponent;
    private static javax.swing.border.Border originalBorder;

    public static void showInputError(java.awt.Component comp, String message) {
        if (!(comp instanceof javax.swing.JComponent)) return;
        javax.swing.JComponent jc = (javax.swing.JComponent) comp;
        
        if (errorPopup != null) {
            errorPopup.hide();
            errorPopup = null;
        }
        
        if (currentErrorComponent != null && currentErrorComponent != jc && originalBorder != null) {
            ((javax.swing.JComponent)currentErrorComponent).setBorder(originalBorder);
        }
        
        if (errorTimer != null && errorTimer.isRunning()) {
            errorTimer.stop();
        } else {
            originalBorder = jc.getBorder(); 
        }
        
        currentErrorComponent = jc;
        jc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 53, 69), 2));
        
        javax.swing.JLabel lblMsg = new javax.swing.JLabel(" " + message + " ");
        lblMsg.setForeground(java.awt.Color.WHITE);
        lblMsg.setBackground(new java.awt.Color(220, 53, 69));
        lblMsg.setOpaque(true);
        lblMsg.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        lblMsg.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 8, 4, 8));
        
        try {
            java.awt.Point p = comp.getLocationOnScreen();
            errorPopup = javax.swing.PopupFactory.getSharedInstance().getPopup(comp, lblMsg, p.x, p.y - 30);
            errorPopup.show();
        } catch (java.awt.IllegalComponentStateException ex) {
            // Komponen mungkin belum dirender sepenuhnya
        }
        
        errorTimer = new javax.swing.Timer(1500, e -> {
            if (errorPopup != null) {
                errorPopup.hide();
                errorPopup = null;
            }
            if (currentErrorComponent != null && originalBorder != null) {
                ((javax.swing.JComponent)currentErrorComponent).setBorder(originalBorder);
                currentErrorComponent = null;
                originalBorder = null;
            }
        });
        errorTimer.setRepeats(false);
        errorTimer.start();
    }
    
    public static ImageIcon getScaledIcon(String resourcePath, int width, int height) {
        try {
            java.net.URL imgUrl = AppStyle.class.getResource(resourcePath);
            if (imgUrl == null) return null;
            
            ImageIcon originalIcon = new ImageIcon(imgUrl);
            Image originalImg = originalIcon.getImage();
            
            java.awt.image.BufferedImage scaledImg = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImg.createGraphics();
            
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2d.drawImage(originalImg, 0, 0, width, height, null);
            g2d.dispose();
            
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String formatRupiah(double value) {
        java.text.DecimalFormat formatter = (java.text.DecimalFormat) java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("id", "ID"));
        java.text.DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("Rp ");
        formatter.setDecimalFormatSymbols(symbols);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(value);
    }

    public static String formatRupiah(String valueStr) {
        try {
            double val = Double.parseDouble(valueStr);
            return formatRupiah(val);
        } catch (Exception e) {
            return "Rp 0,00";
        }
    }

    public static String cleanRupiah(String text) {
        if (text == null) return "";
        String clean = text;
        if (clean.contains(",")) {
            clean = clean.split(",")[0];
        }
        return clean.replaceAll("[^0-9]", "");
    }
}