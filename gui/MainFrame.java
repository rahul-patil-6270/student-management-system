package gui;

import dao.StudentDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Student;
import util.GeminiChatService;

public class MainFrame extends JFrame {
    private static final Color BG = new Color(8, 15, 28);
    private static final Color SURFACE = new Color(242, 238, 228);
    private static final Color SOFT = new Color(232, 241, 238);
    private static final Color PANEL = new Color(248, 248, 244);
    private static final Color DEEP = new Color(19, 43, 63);
    private static final Color ACCENT = new Color(240, 116, 69);
    private static final Color ACCENT_SOFT = new Color(255, 228, 214);
    private static final Color TEXT = new Color(25, 35, 45);
    private static final Color MUTE = new Color(93, 104, 116);
    private static final Color BORDER = new Color(214, 221, 227);
    private static final Pattern EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);
    private static final DecimalFormat DF = new DecimalFormat("0.00");

    private final StudentDAO dao = new StudentDAO();
    private final GeminiChatService chatService = new GeminiChatService();
    private final ArrayList<Student> students = new ArrayList<Student>();

    private JTextField codeField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField cityField;
    private JTextField searchField;
    private JSpinner ageSpinner;
    private JSpinner cgpaSpinner;
    private JComboBox<String> deptBox;
    private JComboBox<String> courseBox;
    private JComboBox<String> yearBox;
    private JComboBox<String> statusBox;
    private JComboBox<String> statusFilter;
    private JComboBox<String> deptFilter;
    private JTable table;
    private DefaultTableModel model;
    private JLabel totalLabel;
    private JLabel activeLabel;
    private JLabel avgLabel;
    private JLabel deptLabel;
    private JLabel spotlightLabel;
    private JLabel innovationLabel;
    private JLabel messageLabel;
    private JLabel cockpitTitle;
    private JLabel readinessLabel;
    private JLabel recommendationLabel;
    private JLabel riskLabel;
    private JLabel innovationScoreLabel;
    private JLabel momentumLabel;
    private JLabel supportLaneLabel;
    private JLabel chip1;
    private JLabel chip2;
    private JLabel chip3;
    private JTextArea narrative;
    private JTextArea topTalentArea;
    private JTextArea attentionArea;
    private JTextArea cityPulseArea;
    private JTextArea intelligenceArea;
    private JTextArea chatQuestionArea;
    private JTextArea chatAnswerArea;
    private JLabel chatStatusLabel;
    private JButton askButton;
    private int selectedId = -1;

    public MainFrame() {
        setTitle("EduPulse Nexus | Student Intelligence Console");
        setSize(1540, 920);
        setMinimumSize(new Dimension(1300, 780));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(ui());
        bindPreview();
        loadStudents();
        resetForm();
        setVisible(true);
    }

    private JComponent ui() {
        JPanel root = new JPanel(new BorderLayout(20, 20));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        root.add(hero(), BorderLayout.NORTH);
        root.add(workspaceTabs(), BorderLayout.CENTER);
        return root;
    }

    private JComponent hero() {
        JPanel shell = card(DEEP);
        shell.setLayout(new BorderLayout(18, 0));

        JPanel left = clear();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(txt("EduPulse Nexus", 34, true, Color.WHITE));
        left.add(Box.createVerticalStrut(6));
        left.add(subWhite("A student management app reimagined as a talent radar, support engine, and showcase dashboard."));

        JPanel right = clear();
        right.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.add(tag("Talent Radar"));
        right.add(tag("Risk Signals"));
        right.add(tag("City Pulse"));
        right.add(tag("Competition Mode"));

        shell.add(left, BorderLayout.WEST);
        shell.add(right, BorderLayout.EAST);
        return shell;
    }

    private JComponent workspaceTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.addTab("Dashboard", dashboardPage());
        tabs.addTab("Manage Students", managePage());
        tabs.addTab("Chatbot", chatbotPage());
        return tabs;
    }

    private JComponent dashboardPage() {
        JPanel panel = clear();
        panel.setLayout(new BorderLayout(16, 16));
        panel.add(stats(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dashboardNarrative(), radarWall());
        split.setBorder(null);
        split.setResizeWeight(0.38);
        split.setDividerSize(8);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private JComponent managePage() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, studio(), operations());
        split.setBorder(null);
        split.setResizeWeight(0.34);
        split.setDividerSize(10);
        return split;
    }

    private JComponent chatbotPage() {
        JPanel panel = clear();
        panel.setLayout(new BorderLayout(16, 16));

        JPanel intro = card(DEEP);
        intro.setLayout(new BorderLayout(0, 16));

        JPanel introTop = clear();
        introTop.setLayout(new BoxLayout(introTop, BoxLayout.Y_AXIS));
        introTop.add(txt("Campus Copilot", 30, true, Color.WHITE));
        introTop.add(Box.createVerticalStrut(8));
        introTop.add(subWhite("Your AI side panel for student insights, dashboard summaries, and project-aware answers."));
        introTop.add(Box.createVerticalStrut(14));

        JPanel tags = clear();
        tags.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
        tags.add(tag("Gemini 2.5 Flash"));
        tags.add(tag("Project Aware"));
        tags.add(tag("Clean Replies"));
        introTop.add(tags);

        JPanel promptPanel = clear();
        promptPanel.setLayout(new GridLayout(3, 1, 0, 10));
        promptPanel.add(quickPromptButton("Summarize this dashboard for me"));
        promptPanel.add(quickPromptButton("How can low-performing students improve?"));
        promptPanel.add(quickPromptButton("Give feedback for the selected student"));

        intro.add(introTop, BorderLayout.NORTH);
        intro.add(insightCard("Quick Prompts", "Tap one to fill the chat box instantly.", wrapPanel(promptPanel), ACCENT_SOFT, TEXT), BorderLayout.CENTER);

        JPanel chatPanel = card(PANEL);
        chatPanel.setLayout(new BorderLayout(0, 14));

        JPanel heading = clear();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(txt("Chat With Gemini", 24, true, TEXT));
        heading.add(Box.createVerticalStrut(4));
        heading.add(sub("Ask anything about students, risk signals, dashboard insights, or academic suggestions."));
        chatPanel.add(heading, BorderLayout.NORTH);

        JPanel center = clear();
        center.setLayout(new GridLayout(2, 1, 0, 12));

        chatQuestionArea = promptArea();
        chatQuestionArea.setText("What would you like to know?");
        center.add(insightCard("Message", "Write your question here.", chatQuestionArea, Color.WHITE, TEXT));

        chatAnswerArea = promptArea();
        chatAnswerArea.setEditable(false);
        chatAnswerArea.setText("Gemini answers will appear here.");
        center.add(insightCard("Reply", "The response will use your project context from EduPulse Nexus.", chatAnswerArea, SURFACE, TEXT));

        chatPanel.add(center, BorderLayout.CENTER);

        JPanel actions = clear();
        actions.setLayout(new BorderLayout(12, 0));
        askButton = btn("Ask Gemini", ACCENT, Color.WHITE, e -> askChatbot());
        askButton.setMargin(new Insets(10, 18, 10, 18));
        actions.add(askButton, BorderLayout.WEST);
        chatStatusLabel = sub(keyPresent() ? "Gemini key detected. Ready to chat." : "Set GEMINI_API_KEY before using the chatbot.");
        actions.add(chatStatusLabel, BorderLayout.CENTER);
        chatPanel.add(actions, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, intro, chatPanel);
        split.setBorder(null);
        split.setResizeWeight(0.34);
        split.setDividerSize(10);

        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private JComponent studio() {
        JPanel shell = card(PANEL);
        shell.setPreferredSize(new Dimension(400, 0));
        shell.setLayout(new BorderLayout(0, 10));

        JPanel form = clear();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.add(txt("Student Studio", 26, true, TEXT));
        form.add(Box.createVerticalStrut(6));
        form.add(sub("Capture the record once. The dashboard turns it into insight, momentum, and intervention cues."));
        form.add(Box.createVerticalStrut(16));

        codeField = field();
        nameField = field();
        emailField = field();
        phoneField = field();
        cityField = field();
        ageSpinner = new JSpinner(new SpinnerNumberModel(20, 16, 60, 1));
        cgpaSpinner = new JSpinner(new SpinnerNumberModel(Double.valueOf(8.2), Double.valueOf(0), Double.valueOf(10), Double.valueOf(0.05)));
        deptBox = combo(new String[]{"Computer Science", "Business Analytics", "Mechanical Engineering", "Civil Engineering", "Design", "Commerce", "Law", "Biotechnology"});
        courseBox = combo(new String[]{"B.Tech", "BCA", "BBA", "MBA", "MCA", "B.Com", "LLB", "B.Des"});
        yearBox = combo(new String[]{"1st Year", "2nd Year", "3rd Year", "4th Year", "Postgraduate"});
        statusBox = combo(new String[]{"Active", "Scholarship", "Probation", "Internship", "Graduated"});

        form.add(row("Student Code", codeField));
        form.add(gap());
        form.add(row("Full Name", nameField));
        form.add(gap());
        form.add(row("Age", ageSpinner));
        form.add(gap());
        form.add(row("Email", emailField));
        form.add(gap());
        form.add(row("Phone", phoneField));
        form.add(gap());
        form.add(row("Department", deptBox));
        form.add(gap());
        form.add(row("Course", courseBox));
        form.add(gap());
        form.add(row("Academic Year", yearBox));
        form.add(gap());
        form.add(row("CGPA", cgpaSpinner));
        form.add(gap());
        form.add(row("Status", statusBox));
        form.add(gap());
        form.add(row("City", cityField));
        form.add(Box.createVerticalStrut(16));

        JPanel actions = clear();
        actions.setLayout(new GridLayout(2, 2, 10, 10));
        actions.add(btn("Save Student", ACCENT, Color.WHITE, e -> saveStudent()));
        actions.add(btn("Update Selected", DEEP, Color.WHITE, e -> updateStudent()));
        actions.add(btn("Reset Form", new Color(228, 235, 241), TEXT, e -> resetForm()));
        actions.add(btn("Delete Record", new Color(255, 232, 232), new Color(135, 50, 50), e -> deleteStudent()));
        form.add(actions);
        form.add(Box.createVerticalStrut(16));
        form.add(infoPill("Live preview transforms each record into a talent and support story."));
        form.add(Box.createVerticalStrut(6));
        form.add(infoPill("Select any row to compare its profile against the wider cohort."));

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(PANEL);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        messageLabel = sub("System ready. Build a record and watch the console respond.");
        shell.add(scroll, BorderLayout.CENTER);
        shell.add(messageLabel, BorderLayout.SOUTH);
        return shell;
    }

    private JComponent operations() {
        JPanel panel = clear();
        panel.setLayout(new BorderLayout(16, 16));
        panel.add(deck(), BorderLayout.CENTER);
        return panel;
    }

    private JComponent stats() {
        JPanel panel = clear();
        panel.setLayout(new GridLayout(1, 6, 12, 0));

        totalLabel = value();
        activeLabel = value();
        avgLabel = value();
        deptLabel = value();
        innovationLabel = value();
        spotlightLabel = white("No records yet");

        panel.add(stat("Students", "Total records loaded", totalLabel));
        panel.add(stat("Active Flow", "Active, internship, scholarship", activeLabel));
        panel.add(stat("Avg CGPA", "Current academic pulse", avgLabel));
        panel.add(stat("Departments", "Spread of disciplines", deptLabel));
        panel.add(stat("Innovation", "Average cohort potential", innovationLabel));

        JPanel spotlight = card(DEEP);
        spotlight.setLayout(new BoxLayout(spotlight, BoxLayout.Y_AXIS));
        spotlight.add(white("STUDENT SPOTLIGHT"));
        spotlight.add(Box.createVerticalStrut(10));
        spotlight.add(spotlightLabel);
        panel.add(spotlight);
        return panel;
    }

    private JComponent deck() {
        JPanel panel = card(SOFT);
        panel.setLayout(new BorderLayout(0, 16));

        JPanel head = clear();
        head.setLayout(new BorderLayout(16, 0));

        JPanel left = clear();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(txt("Mission Control", 28, true, TEXT));
        left.add(Box.createVerticalStrut(4));
        left.add(sub("Not just a database. This view spots leaders, flags intervention cases, and reads the cohort story."));

        head.add(left, BorderLayout.WEST);
        head.add(filters(), BorderLayout.EAST);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePane(), intelligenceTabs());
        split.setBorder(null);
        split.setResizeWeight(0.68);
        split.setDividerSize(8);

        panel.add(head, BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private JComponent filters() {
        JPanel panel = clear();
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        panel.setPreferredSize(new Dimension(460, 76));

        searchField = field();
        searchField.setToolTipText("Search by code, name, department, course or city");
        statusFilter = combo(new String[]{"All Status", "Active", "Scholarship", "Probation", "Internship", "Graduated"});
        deptFilter = combo(new String[]{"All Departments"});

        DocumentListener d = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilters();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilters();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilters();
            }
        };

        searchField.getDocument().addDocumentListener(d);
        statusFilter.addActionListener(e -> applyFilters());
        deptFilter.addActionListener(e -> applyFilters());

        panel.add(searchField);
        panel.add(statusFilter);
        panel.add(deptFilter);
        panel.add(btn("Refresh Data", DEEP, Color.WHITE, e -> loadStudents()));
        return panel;
    }

    private JComponent tablePane() {
        model = new DefaultTableModel(new Object[]{"ID", "Code", "Name", "Department", "Course", "Year", "Status", "CGPA", "City", "Created"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setShowVerticalLines(false);
        table.setGridColor(BORDER);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.setDefaultRenderer(Object.class, new TableRenderer());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelection();
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    private JComponent intelligenceTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.BOLD, 12));
        tabs.addTab("Profile", scrollCard(cockpit(), DEEP));
        tabs.addTab("Insights", scrollCard(radarWall(), SOFT));
        return tabs;
    }

    private JComponent cockpit() {
        JPanel panel = card(DEEP);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        cockpitTitle = txt("Live Profile Cockpit", 24, true, Color.WHITE);
        readinessLabel = white("Readiness: Draft");
        recommendationLabel = subWhite("Recommendation: Start filling the studio form.");
        riskLabel = subWhite("Risk signal: Not enough data yet.");
        innovationScoreLabel = txt("Innovation Score: 0/100", 20, true, new Color(255, 232, 184));
        momentumLabel = subWhite("Momentum lane: Warming up");
        supportLaneLabel = subWhite("Support lane: No priority set");
        chip1 = chip();
        chip2 = chip();
        chip3 = chip();
        narrative = insightArea(new Color(225, 236, 243), DEEP);

        panel.add(cockpitTitle);
        panel.add(Box.createVerticalStrut(12));
        panel.add(innovationScoreLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(readinessLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(momentumLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(supportLaneLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(recommendationLabel);
        panel.add(Box.createVerticalStrut(8));
        panel.add(riskLabel);
        panel.add(Box.createVerticalStrut(14));
        panel.add(chip1);
        panel.add(Box.createVerticalStrut(8));
        panel.add(chip2);
        panel.add(Box.createVerticalStrut(8));
        panel.add(chip3);
        panel.add(Box.createVerticalStrut(14));
        panel.add(white("Narrative Snapshot"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(narrative);
        return panel;
    }

    private JComponent radarWall() {
        JPanel panel = clear();
        panel.setLayout(new GridLayout(2, 2, 12, 12));

        topTalentArea = insightArea(TEXT, SURFACE);
        attentionArea = insightArea(TEXT, SURFACE);
        cityPulseArea = insightArea(TEXT, SURFACE);
        intelligenceArea = insightArea(TEXT, SURFACE);

        panel.add(insightCard("Top Talent Radar", "Who is ready for spotlight opportunities right now.", topTalentArea, SURFACE, TEXT));
        panel.add(insightCard("Attention Queue", "Students who may need mentoring, academic support, or intervention.", attentionArea, SURFACE, TEXT));
        panel.add(insightCard("City Pulse", "Where momentum is coming from across locations.", cityPulseArea, SURFACE, TEXT));
        panel.add(insightCard("Cohort Intelligence", "A quick summary of this filtered cohort's story.", intelligenceArea, SURFACE, TEXT));
        return panel;
    }

    private JComponent dashboardNarrative() {
        JPanel panel = clear();
        panel.setLayout(new BorderLayout(0, 12));
        JTextArea story = insightArea(TEXT, PANEL);
        story.setText("EduPulse Nexus now works like a cleaner control room. Dashboard is for the big picture. Manage Students is where saving, editing, filtering, and row selection happen. This keeps the table readable and gives the insight cards room to feel deliberate instead of cramped.");

        JTextArea highlights = insightArea(TEXT, SURFACE);
        highlights.setText("What makes it feel more special now:\n1. Separate pages for overview and operations.\n2. A profile tab that stays focused on one student.\n3. An insights tab that reads the filtered cohort.\n4. A calmer visual hierarchy so the table is no longer buried.");

        panel.add(insightCard("Console Story", "A lighter summary panel that keeps the dashboard calm and readable.", story, PANEL, TEXT), BorderLayout.NORTH);
        panel.add(insightCard("Design Intent", "The interface now separates record actions from portfolio-style analytics.", highlights, SURFACE, TEXT), BorderLayout.CENTER);
        return panel;
    }

    private void bindPreview() {
        DocumentListener d = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                preview();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                preview();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                preview();
            }
        };

        codeField.getDocument().addDocumentListener(d);
        nameField.getDocument().addDocumentListener(d);
        emailField.getDocument().addDocumentListener(d);
        phoneField.getDocument().addDocumentListener(d);
        cityField.getDocument().addDocumentListener(d);
        deptBox.addActionListener(e -> preview());
        courseBox.addActionListener(e -> preview());
        yearBox.addActionListener(e -> preview());
        statusBox.addActionListener(e -> preview());
        ageSpinner.addChangeListener(e -> preview());
        cgpaSpinner.addChangeListener(e -> preview());
    }

    private void askChatbot() {
        final String question = chatQuestionArea.getText().trim();
        if (question.isEmpty() || "What would you like to know?".equals(question)) {
            JOptionPane.showMessageDialog(this, "Type a question for the chatbot first.");
            return;
        }
        if (!keyPresent()) {
            chatStatusLabel.setText("GEMINI_API_KEY is missing. Add it to your environment and restart the app.");
            chatAnswerArea.setText("Gemini key not found.\n\nSet an environment variable named GEMINI_API_KEY before running the app.");
            return;
        }

        askButton.setEnabled(false);
        chatStatusLabel.setText("Thinking with Gemini 2.5 Flash...");
        chatAnswerArea.setText("Generating answer...");

        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return chatService.askQuestion(question, projectContext());
            }

            @Override
            protected void done() {
                askButton.setEnabled(true);
                try {
                    chatAnswerArea.setText(get());
                    chatStatusLabel.setText("Answer ready.");
                } catch (Exception ex) {
                    chatAnswerArea.setText("Unable to get a response from Gemini.\n\n" + ex.getMessage());
                    chatStatusLabel.setText("Chat request failed.");
                }
            }
        };
        worker.execute();
    }

    private void saveStudent() {
        Student s = fromForm();
        if (s == null) {
            return;
        }
        try {
            dao.addStudent(s);
            loadStudents();
            resetForm();
            msg("Student record saved and routed into the live dashboard.");
        } catch (SQLException ex) {
            dbError(ex);
        }
    }

    private void updateStudent() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Select a student record first.");
            return;
        }
        Student s = fromForm();
        if (s == null) {
            return;
        }
        s.setId(selectedId);
        try {
            dao.updateStudent(s);
            loadStudents();
            msg("Student record updated and intelligence cards refreshed.");
        } catch (SQLException ex) {
            dbError(ex);
        }
    }

    private void deleteStudent() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Choose a row to delete.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete selected record?", "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.deleteStudent(selectedId);
            loadStudents();
            resetForm();
            msg("Student record deleted from the console.");
        } catch (SQLException ex) {
            dbError(ex);
        }
    }

    private Student fromForm() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String city = cityField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, email and city are required.");
            return null;
        }
        if (!EMAIL.matcher(email).matches()) {
            JOptionPane.showMessageDialog(this, "Enter a valid email.");
            return null;
        }
        if (code.isEmpty()) {
            code = code(name, val(deptBox));
        }

        Student s = new Student();
        s.setStudentCode(code);
        s.setName(name);
        s.setAge(((Integer) ageSpinner.getValue()).intValue());
        s.setEmail(email);
        s.setPhone(phoneField.getText().trim());
        s.setDepartment(val(deptBox));
        s.setCourse(val(courseBox));
        s.setAcademicYear(val(yearBox));
        s.setCgpa(((Double) cgpaSpinner.getValue()).doubleValue());
        s.setStatus(val(statusBox));
        s.setCity(city);
        return s;
    }

    private void loadStudents() {
        try {
            students.clear();
            students.addAll(dao.getAllStudents());
            refreshDeptFilter();
            applyFilters();
        } catch (SQLException ex) {
            dbError(ex);
        }
    }

    private void applyFilters() {
        if (model == null) {
            return;
        }

        String q = txt(searchField);
        String statusValue = String.valueOf(statusFilter.getSelectedItem());
        String deptValue = String.valueOf(deptFilter.getSelectedItem());
        ArrayList<Student> filtered = new ArrayList<Student>();
        model.setRowCount(0);

        for (Student s : students) {
            boolean searchMatch = q.isEmpty()
                    || safe(s.getStudentCode()).contains(q)
                    || safe(s.getName()).contains(q)
                    || safe(s.getDepartment()).contains(q)
                    || safe(s.getCourse()).contains(q)
                    || safe(s.getCity()).contains(q);
            boolean statusMatch = "All Status".equals(statusValue) || statusValue.equalsIgnoreCase(s.getStatus());
            boolean deptMatch = "All Departments".equals(deptValue) || deptValue.equalsIgnoreCase(s.getDepartment());

            if (searchMatch && statusMatch && deptMatch) {
                filtered.add(s);
                model.addRow(new Object[]{
                    Integer.valueOf(s.getId()),
                    s.getStudentCode(),
                    s.getName(),
                    s.getDepartment(),
                    s.getCourse(),
                    s.getAcademicYear(),
                    s.getStatus(),
                    DF.format(s.getCgpa()),
                    s.getCity(),
                    s.getCreatedAt()
                });
            }
        }

        updateStats(filtered);
        updateRadarWall(filtered);
    }

    private void refreshDeptFilter() {
        Object current = deptFilter.getSelectedItem();
        deptFilter.removeAllItems();
        deptFilter.addItem("All Departments");

        Set<String> values = new LinkedHashSet<String>();
        for (Student s : students) {
            if (s.getDepartment() != null && !s.getDepartment().trim().isEmpty()) {
                values.add(s.getDepartment());
            }
        }

        for (String value : values) {
            deptFilter.addItem(value);
        }

        if (current != null) {
            deptFilter.setSelectedItem(current);
        }
        if (deptFilter.getSelectedIndex() == -1) {
            deptFilter.setSelectedIndex(0);
        }
    }

    private void loadSelection() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return;
        }

        Student s = find(((Integer) model.getValueAt(table.convertRowIndexToModel(row), 0)).intValue());
        if (s == null) {
            return;
        }

        selectedId = s.getId();
        codeField.setText(s.getStudentCode());
        nameField.setText(s.getName());
        ageSpinner.setValue(Integer.valueOf(s.getAge()));
        emailField.setText(s.getEmail());
        phoneField.setText(s.getPhone());
        deptBox.setSelectedItem(s.getDepartment());
        courseBox.setSelectedItem(s.getCourse());
        yearBox.setSelectedItem(s.getAcademicYear());
        cgpaSpinner.setValue(Double.valueOf(s.getCgpa()));
        statusBox.setSelectedItem(s.getStatus());
        cityField.setText(s.getCity());
        updateCockpit(s, false);
        msg("Edit mode active for " + s.getName() + ".");
    }

    private void resetForm() {
        selectedId = -1;
        codeField.setText("");
        nameField.setText("");
        ageSpinner.setValue(Integer.valueOf(20));
        emailField.setText("");
        phoneField.setText("");
        deptBox.setSelectedIndex(0);
        courseBox.setSelectedIndex(0);
        yearBox.setSelectedIndex(0);
        cgpaSpinner.setValue(Double.valueOf(8.2));
        statusBox.setSelectedIndex(0);
        cityField.setText("");
        if (table != null) {
            table.clearSelection();
        }
        preview();
        msg("Form reset and ready for the next profile.");
    }

    private void updateStats(List<Student> list) {
        int active = 0;
        double cgpaTotal = 0;
        double innovationTotal = 0;
        Set<String> departments = new LinkedHashSet<String>();
        Student top = null;

        for (Student s : list) {
            if (isActiveFlow(s)) {
                active++;
            }
            cgpaTotal += s.getCgpa();
            innovationTotal += innovationScore(s);
            if (filled(s.getDepartment())) {
                departments.add(s.getDepartment());
            }
            if (top == null || innovationScore(s) > innovationScore(top)) {
                top = s;
            }
        }

        totalLabel.setText(String.valueOf(list.size()));
        activeLabel.setText(String.valueOf(active));
        avgLabel.setText(list.isEmpty() ? "0.00" : DF.format(cgpaTotal / list.size()));
        deptLabel.setText(String.valueOf(departments.size()));
        innovationLabel.setText(list.isEmpty() ? "0" : String.valueOf((int) Math.round(innovationTotal / list.size())));
        spotlightLabel.setText(top == null ? "No records yet" : top.getName() + " | " + scoreBand(innovationScore(top)) + " | " + top.getDepartment());
    }

    private void updateRadarWall(List<Student> list) {
        topTalentArea.setText(buildTopTalentSummary(list));
        attentionArea.setText(buildAttentionSummary(list));
        cityPulseArea.setText(buildCityPulseSummary(list));
        intelligenceArea.setText(buildCohortSummary(list));
    }

    private void preview() {
        updateCockpit(draftStudent(), true);
    }

    private void updateCockpit(Student s, boolean draftMode) {
        int score = innovationScore(s);
        cockpitTitle.setText(showNameAndCode(s, draftMode));
        innovationScoreLabel.setText("Innovation Score: " + score + "/100");
        readinessLabel.setText("Readiness: " + readinessText(score, s));
        momentumLabel.setText("Momentum lane: " + momentumText(s, score));
        supportLaneLabel.setText("Support lane: " + supportText(s, score));
        recommendationLabel.setText(asHtml("Recommendation: " + recommendationText(s, score)));
        riskLabel.setText(asHtml("Risk signal: " + riskText(s, score)));
        chip1.setText("Identity: " + show(s.getDepartment()) + " | " + show(s.getCourse()) + " | " + show(s.getAcademicYear()));
        chip2.setText("Profile mix: " + show(s.getStatus()) + " | " + show(s.getCity()) + " | Age " + s.getAge());
        chip3.setText("Contact strength: " + contactStrength(s) + " | CGPA " + DF.format(s.getCgpa()) + " | Band " + scoreBand(score));
        narrative.setText(studentNarrative(s, score, draftMode));
    }

    private Student draftStudent() {
        Student s = new Student();
        String code = codeField.getText().trim();
        s.setStudentCode(code.isEmpty() ? code(nameField.getText().trim(), val(deptBox)) : code);
        s.setName(nameField.getText().trim());
        s.setAge(((Integer) ageSpinner.getValue()).intValue());
        s.setEmail(emailField.getText().trim());
        s.setPhone(phoneField.getText().trim());
        s.setDepartment(val(deptBox));
        s.setCourse(val(courseBox));
        s.setAcademicYear(val(yearBox));
        s.setCgpa(((Double) cgpaSpinner.getValue()).doubleValue());
        s.setStatus(val(statusBox));
        s.setCity(cityField.getText().trim());
        return s;
    }

    private boolean isActiveFlow(Student s) {
        return "Active".equalsIgnoreCase(s.getStatus())
                || "Scholarship".equalsIgnoreCase(s.getStatus())
                || "Internship".equalsIgnoreCase(s.getStatus());
    }

    private int innovationScore(Student s) {
        int score = 20;
        score += (int) Math.round(s.getCgpa() * 5.2);
        score += statusBonus(s.getStatus());
        score += yearBonus(s.getAcademicYear());
        score += filled(s.getEmail()) ? 6 : 0;
        score += filled(s.getPhone()) ? 6 : 0;
        score += filled(s.getCity()) ? 4 : 0;
        score += filled(s.getDepartment()) ? 4 : 0;
        return Math.max(0, Math.min(100, score));
    }

    private int statusBonus(String status) {
        if ("Scholarship".equalsIgnoreCase(status)) {
            return 14;
        }
        if ("Internship".equalsIgnoreCase(status)) {
            return 11;
        }
        if ("Active".equalsIgnoreCase(status)) {
            return 7;
        }
        if ("Graduated".equalsIgnoreCase(status)) {
            return 6;
        }
        if ("Probation".equalsIgnoreCase(status)) {
            return -10;
        }
        return 0;
    }

    private int yearBonus(String year) {
        if (year == null) {
            return 0;
        }
        String lower = year.toLowerCase();
        if (lower.contains("4th") || lower.contains("post")) {
            return 8;
        }
        if (lower.contains("3rd")) {
            return 6;
        }
        if (lower.contains("2nd")) {
            return 4;
        }
        return 2;
    }

    private String readinessText(int score, Student s) {
        if (score >= 86 && s.getCgpa() >= 8.7) {
            return "Showcase Candidate";
        }
        if (score >= 72) {
            return "Momentum Builder";
        }
        if (score >= 58) {
            return "Stable Profile";
        }
        return "Needs Lift";
    }

    private String momentumText(Student s, int score) {
        if (score >= 86) {
            return "High velocity for competitions, internships, and public recognition";
        }
        if (score >= 72) {
            return "Strong upward path if exposure and mentoring continue";
        }
        if (score >= 58) {
            return "Usable baseline with room to sharpen profile strength";
        }
        return "Momentum is fragile and needs structured support";
    }

    private String supportText(Student s, int score) {
        if ("Probation".equalsIgnoreCase(s.getStatus()) || s.getCgpa() < 6.0) {
            return "Priority mentoring";
        }
        if (score < 58 || !filled(s.getPhone())) {
            return "Guided follow-up";
        }
        if (score < 80) {
            return "Light-touch coaching";
        }
        return "Opportunity acceleration";
    }

    private String recommendationText(Student s, int score) {
        if ("Probation".equalsIgnoreCase(s.getStatus()) || s.getCgpa() < 6.0) {
            return "Launch a mentor check-in, academic recovery plan, and weekly follow-up.";
        }
        if (score >= 86) {
            return "Push toward scholarship showcases, hackathons, and visible campus leadership.";
        }
        if (score >= 72) {
            return "Pair with internship pathways and portfolio-building opportunities.";
        }
        return "Improve contact completeness and create a clearer growth path before showcase placement.";
    }

    private String riskText(Student s, int score) {
        if (filled(s.getEmail()) && !EMAIL.matcher(s.getEmail()).matches()) {
            return "Email quality needs correction before outreach.";
        }
        if ("Probation".equalsIgnoreCase(s.getStatus())) {
            return "Probation status keeps this profile in the intervention lane.";
        }
        if (s.getCgpa() < 6.0) {
            return "Academic concern detected from current CGPA.";
        }
        if (!filled(s.getPhone()) || !filled(s.getCity())) {
            return "Profile is missing contact depth for strong coordination.";
        }
        if (score < 58) {
            return "Low current signal; more support than showcase value right now.";
        }
        return "No immediate red flag. Focus can stay on momentum and positioning.";
    }

    private String studentNarrative(Student s, int score, boolean draftMode) {
        String name = filled(s.getName()) ? s.getName() : "This student";
        String mode = draftMode ? "draft signal" : "live record";
        return name + " is presented as a " + mode + " inside EduPulse Nexus, where the profile is translated into a "
                + scoreBand(score)
                + " trajectory. Instead of stopping at storage, the console reads academic standing, engagement status, contact strength, and cohort context to decide whether this record belongs in spotlight opportunities, steady mentoring, or active intervention.";
    }

    private String buildTopTalentSummary(List<Student> list) {
        if (list.isEmpty()) {
            return "No students in the current filter yet.\n\nAdd records to unlock the talent radar.";
        }
        ArrayList<Student> ranked = new ArrayList<Student>(list);
        Collections.sort(ranked, Comparator.comparingInt(this::innovationScore).reversed().thenComparing(Comparator.comparingDouble(Student::getCgpa).reversed()));
        return rankedList(ranked, 3, true);
    }

    private String buildAttentionSummary(List<Student> list) {
        if (list.isEmpty()) {
            return "The attention queue will appear when records are available.";
        }
        ArrayList<Student> ranked = new ArrayList<Student>(list);
        Collections.sort(ranked, Comparator.comparingInt(this::attentionPriority).reversed());
        return rankedList(ranked, 3, false);
    }

    private int attentionPriority(Student s) {
        int score = 0;
        if ("Probation".equalsIgnoreCase(s.getStatus())) {
            score += 50;
        }
        if (s.getCgpa() < 6.0) {
            score += 35;
        } else if (s.getCgpa() < 7.0) {
            score += 18;
        }
        if (!filled(s.getPhone())) {
            score += 10;
        }
        if (!filled(s.getCity())) {
            score += 5;
        }
        return score;
    }

    private String buildCityPulseSummary(List<Student> list) {
        if (list.isEmpty()) {
            return "City pulse updates from the filtered cohort.";
        }

        ArrayList<String> rows = new ArrayList<String>();
        ArrayList<String> seen = new ArrayList<String>();
        for (Student s : list) {
            String city = filled(s.getCity()) ? s.getCity() : "Unknown";
            if (seen.contains(city.toLowerCase())) {
                continue;
            }
            seen.add(city.toLowerCase());
            int count = 0;
            double cgpaTotal = 0;
            for (Student inner : list) {
                String innerCity = filled(inner.getCity()) ? inner.getCity() : "Unknown";
                if (city.equalsIgnoreCase(innerCity)) {
                    count++;
                    cgpaTotal += inner.getCgpa();
                }
            }
            rows.add(city + ": " + count + " students | Avg CGPA " + DF.format(cgpaTotal / count));
        }
        Collections.sort(rows);
        return joinLimited(rows, 4, "No city data available.");
    }

    private String buildCohortSummary(List<Student> list) {
        if (list.isEmpty()) {
            return "This cohort is empty. Add or refresh data to generate the story.";
        }

        int highPotential = 0;
        int intervention = 0;
        int internshipReady = 0;
        for (Student s : list) {
            int score = innovationScore(s);
            if (score >= 80) {
                highPotential++;
            }
            if (attentionPriority(s) >= 35) {
                intervention++;
            }
            if (s.getCgpa() >= 8.0 && !"Probation".equalsIgnoreCase(s.getStatus())) {
                internshipReady++;
            }
        }

        return "Filtered cohort size: " + list.size()
                + "\nHigh-potential profiles: " + highPotential
                + "\nImmediate support cases: " + intervention
                + "\nInternship-ready lane: " + internshipReady
                + "\nNarrative: " + cohortNarrative(list, highPotential, intervention);
    }

    private String cohortNarrative(List<Student> list, int highPotential, int intervention) {
        if (highPotential > intervention && highPotential >= Math.max(2, list.size() / 3)) {
            return "The cohort currently leans opportunity-heavy, with a strong bench for showcases and placements.";
        }
        if (intervention > highPotential) {
            return "This view is support-heavy right now; mentoring and stabilization should lead the next actions.";
        }
        return "The cohort is balanced, with room to convert stable profiles into standout ones.";
    }

    private String rankedList(List<Student> list, int limit, boolean positive) {
        ArrayList<String> lines = new ArrayList<String>();
        int count = 0;
        for (Student s : list) {
            if (!positive && attentionPriority(s) <= 0) {
                continue;
            }
            lines.add((count + 1) + ". " + s.getName()
                    + " | " + show(s.getDepartment())
                    + " | Score " + innovationScore(s)
                    + " | CGPA " + DF.format(s.getCgpa()));
            count++;
            if (count == limit) {
                break;
            }
        }
        return lines.isEmpty() ? (positive ? "Top lane is warming up." : "No active intervention cases right now.") : joinLimited(lines, limit, "");
    }

    private String joinLimited(List<String> rows, int limit, String fallback) {
        if (rows.isEmpty()) {
            return fallback;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows.size() && i < limit; i++) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(rows.get(i));
        }
        return sb.toString();
    }

    private String showNameAndCode(Student s, boolean draftMode) {
        String code = filled(s.getStudentCode()) ? s.getStudentCode() : "Draft";
        String name = filled(s.getName()) ? s.getName() : "Live Profile Cockpit";
        return name + " / " + (draftMode ? code + " Preview" : code);
    }

    private String contactStrength(Student s) {
        int score = 0;
        if (filled(s.getEmail())) {
            score++;
        }
        if (filled(s.getPhone())) {
            score++;
        }
        if (filled(s.getCity())) {
            score++;
        }
        if (score == 3) {
            return "strong";
        }
        if (score == 2) {
            return "usable";
        }
        return "thin";
    }

    private String scoreBand(int score) {
        if (score >= 86) {
            return "Launch Tier";
        }
        if (score >= 72) {
            return "Growth Tier";
        }
        if (score >= 58) {
            return "Steady Tier";
        }
        return "Recovery Tier";
    }

    private String asHtml(String text) {
        return "<html><body style='width:250px'>" + text + "</body></html>";
    }

    private String projectContext() {
        StringBuilder sb = new StringBuilder();
        sb.append("Application: EduPulse Nexus.\n");
        sb.append("Purpose: Student management dashboard with analytics, risk signals, and opportunity insights.\n");
        sb.append("Total students loaded: ").append(students.size()).append(".\n");
        sb.append("Current spotlight: ").append(spotlightLabel == null ? "Not available" : spotlightLabel.getText()).append(".\n");
        sb.append("Dashboard notes: Top Talent Radar, Attention Queue, City Pulse, and Cohort Intelligence are available.\n");

        if (selectedId != -1) {
            Student selected = find(selectedId);
            if (selected != null) {
                sb.append("Selected student context: ")
                        .append(selected.getName()).append(", ")
                        .append(show(selected.getDepartment())).append(", ")
                        .append(show(selected.getCourse())).append(", ")
                        .append(show(selected.getAcademicYear())).append(", CGPA ")
                        .append(DF.format(selected.getCgpa())).append(", status ")
                        .append(show(selected.getStatus())).append(", city ")
                        .append(show(selected.getCity())).append(".\n");
            }
        }

        sb.append("Current cohort summary:\n").append(intelligenceArea == null ? "Not available." : intelligenceArea.getText()).append("\n");
        return sb.toString();
    }

    private boolean keyPresent() {
        String apiKey = System.getenv("GEMINI_API_KEY");
        return apiKey != null && !apiKey.trim().isEmpty();
    }

    private Student find(int id) {
        for (Student s : students) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    private String code(String name, String dept) {
        String d = dept.replaceAll("[^A-Za-z]", "").toUpperCase();
        String n = name.replaceAll("[^A-Za-z]", "").toUpperCase();
        return (d.length() >= 3 ? d.substring(0, 3) : "STU") + "-" + (n.length() >= 2 ? n.substring(0, 2) : "SN") + "-" + (students.size() + 101);
    }

    private void dbError(SQLException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        msg("Database action failed. Check MySQL connection details.");
    }

    private void msg(String text) {
        messageLabel.setText(text);
    }

    private boolean filled(String text) {
        return text != null && !text.trim().isEmpty();
    }

    private JPanel card(Color bg) {
        JPanel panel = new JPanel();
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(20, 20, 20, 20)));
        return panel;
    }

    private JPanel clear() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }

    private JLabel txt(String text, int size, boolean bold, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(bold ? "Serif" : "SansSerif", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(color);
        return label;
    }

    private JLabel sub(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(MUTE);
        return label;
    }

    private JLabel white(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JLabel subWhite(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(new Color(223, 236, 241));
        return label;
    }

    private JPanel tag(String text) {
        JPanel panel = clear();
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 45)), new EmptyBorder(8, 10, 8, 10)));
        panel.add(white(text));
        return panel;
    }

    private JTextField field() {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(10, 12, 10, 12)));
        return field;
    }

    private JComboBox<String> combo(String[] items) {
        JComboBox<String> combo = new JComboBox<String>(items);
        combo.setEditable(false);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
        return combo;
    }

    private JPanel row(String title, JComponent input) {
        JPanel row = clear();
        row.setLayout(new BorderLayout(0, 6));
        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(TEXT);
        row.add(label, BorderLayout.NORTH);
        row.add(input, BorderLayout.CENTER);
        return row;
    }

    private Component gap() {
        return Box.createVerticalStrut(12);
    }

    private JButton btn(String text, Color bg, Color fg, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(fg);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }

    private JPanel infoPill(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(8, 10, 8, 10)));
        panel.add(sub(text), BorderLayout.CENTER);
        return panel;
    }

    private JLabel value() {
        return txt("0", 28, true, TEXT);
    }

    private JPanel stat(String title, String subtitle, JLabel valueLabel) {
        JPanel panel = card(PANEL);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(sub(title));
        panel.add(Box.createVerticalStrut(8));
        panel.add(valueLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(sub(subtitle));
        return panel;
    }

    private JLabel chip() {
        JLabel label = subWhite("");
        label.setOpaque(true);
        label.setBackground(new Color(255, 255, 255, 18));
        label.setBorder(new EmptyBorder(8, 10, 8, 10));
        return label;
    }

    private JPanel insightCard(String title, String subtitle, JTextArea area, Color bg, Color fg) {
        JPanel panel = card(bg);
        panel.setLayout(new BorderLayout(0, 10));
        JPanel head = clear();
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
        head.add(txt(title, 18, true, fg));
        head.add(Box.createVerticalStrut(4));
        head.add(subtitleLabel(subtitle));
        panel.add(head, BorderLayout.NORTH);
        panel.add(area, BorderLayout.CENTER);
        return panel;
    }

    private JPanel insightCard(String title, String subtitle, JComponent body, Color bg, Color fg) {
        JPanel panel = card(bg);
        panel.setLayout(new BorderLayout(0, 10));
        JPanel head = clear();
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));
        head.add(txt(title, 18, true, fg));
        head.add(Box.createVerticalStrut(4));
        head.add(subtitleLabel(subtitle));
        panel.add(head, BorderLayout.NORTH);
        panel.add(body, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane scrollCard(JComponent component, Color background) {
        JScrollPane scroll = new JScrollPane(component);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(background);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JLabel subtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(MUTE);
        return label;
    }

    private JTextArea insightArea(Color fg, Color bg) {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(true);
        area.setBackground(bg);
        area.setForeground(fg);
        area.setFont(new Font("SansSerif", Font.PLAIN, 13));
        area.setBorder(null);
        return area;
    }

    private JTextArea promptArea() {
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        area.setBackground(Color.WHITE);
        area.setForeground(TEXT);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
        return area;
    }

    private JButton quickPromptButton(String prompt) {
        JButton button = btn(prompt, Color.WHITE, TEXT, e -> {
            if (chatQuestionArea != null) {
                chatQuestionArea.setText(prompt);
            }
        });
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(10, 12, 10, 12)));
        return button;
    }

    private JPanel wrapPanel(JComponent component) {
        JPanel panel = clear();
        panel.setLayout(new BorderLayout());
        panel.add(component, BorderLayout.NORTH);
        return panel;
    }

    private String val(JComboBox<String> box) {
        Object value = box.isEditable() ? box.getEditor().getItem() : box.getSelectedItem();
        return value == null ? "" : value.toString().trim();
    }

    private String safe(String text) {
        return text == null ? "" : text.toLowerCase();
    }

    private String txt(JTextField field) {
        return field == null ? "" : field.getText().trim().toLowerCase();
    }

    private String show(String text) {
        return text == null || text.trim().isEmpty() ? "not specified" : text;
    }

    private static class TableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            setHorizontalAlignment(column == 0 || column == 7 ? SwingConstants.CENTER : SwingConstants.LEFT);
            if (!selected) {
                component.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
            }
            setBorder(new EmptyBorder(0, 8, 0, 8));
            return component;
        }
    }
}
