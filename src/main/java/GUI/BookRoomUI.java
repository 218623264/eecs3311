package GUI;

import model.User;
import room.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookRoomUI extends JPanel {

    private static final String ROOMS_CSV_PATH = System.getProperty("user.dir") + "/eecs3311/src/main/data/rooms.csv";
    //private static final String ROOMS_CSV_PATH = "E:\\York University\\EECS3311\\D2\\eecs3311\\src\\main\\data\\rooms.csv";

    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JButton bookButton;
    private JButton backButton;

    private JFrame parentFrame;
    private JPanel previousPanel;

    public BookRoomUI(JFrame parentFrame, JPanel previousPanel) {
        this.parentFrame = parentFrame;
        this.previousPanel = previousPanel;

        setLayout(new BorderLayout());
        setBackground(new Color(0xF8F9FA));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Available Rooms", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(new Color(0x007BFF));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Load rooms from CSV
        CSVRoomLoader.loadRooms(ROOMS_CSV_PATH);
        List<Room> rooms = room.RoomRepository.getAllRooms();

        // Table setup
        String[] columns = {"RoomID", "Location", "Room Number", "Capacity", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Populate table
        for (Room room : rooms) {
            String status = room.isOccupied() ? "Occupied" : "Available";

            Object[] rowData = {
                    room.getRoomID(),
                    room.getFullLocation(),
                    room.getRoomNumber(),
                    room.getCapacity(),
                    status
            };
            tableModel.addRow(rowData);
        }

        roomTable = new JTable(tableModel);
        roomTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        roomTable.setRowHeight(35);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.setGridColor(new Color(0xE9ECEF));
        roomTable.setShowGrid(true);

        // Header styling
        roomTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        roomTable.getTableHeader().setBackground(new Color(0x007BFF));
        roomTable.getTableHeader().setForeground(Color.WHITE);

        // Custom cell renderer
        // Custom cell renderer
        roomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    // ⭐ Correct location for status check:
                    boolean occupied = table.getValueAt(row, 4).toString().equals("Occupied");

                    c.setBackground(occupied ? new Color(0xFFE5E5) : new Color(0xE8F8E8));
                    c.setForeground(occupied ? new Color(0xC82333) : new Color(0x1B4332));
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(new LineBorder(new Color(0xDEE2E6), 1, true));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(0xF8F9FA));

        bookButton = createStyledButton("Book Selected Room", new Color(0x007BFF));
        bookButton.addActionListener(new BookRoomAction());
        buttonPanel.add(bookButton);

        backButton = createStyledButton("Back", new Color(0x6C757D));
        backButton.addActionListener(e -> switchToPreviousPanel());
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class BookRoomAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = roomTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(BookRoomUI.this, "Please select a room first!", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String status = (String) tableModel.getValueAt(selectedRow, 4);
            if ("Occupied".equals(status)) {
                JOptionPane.showMessageDialog(BookRoomUI.this, "This room is currently occupied!", "Unavailable", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String roomID = (String) tableModel.getValueAt(selectedRow, 0);
            Room selectedRoom = RoomRepository.getRoomByID(roomID);
            User user = ((MainUI) parentFrame).getUser(); // Get from session or facade

            SelectDates selectDates = new SelectDates(parentFrame, BookRoomUI.this, selectedRoom, user);
            parentFrame.setContentPane(selectDates);
            parentFrame.revalidate();
            parentFrame.repaint();
        }
    }

    private void switchToPreviousPanel() {
        parentFrame.setContentPane(previousPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color.brighter());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2d.setColor(color.darker());
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 45));
        return button;
    }
}