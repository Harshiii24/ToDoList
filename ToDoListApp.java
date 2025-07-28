import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ToDoListApp extends JFrame {

    private DefaultListModel<Task> listModel;
    private JList<Task> taskList;
    private List<Task> allTasks; // The master list of all tasks
    private final String FILENAME = "tasks.dat";
    private JTextField taskDescriptionField;
    private JComboBox<Priority> priorityComboBox;
    private JSpinner dateSpinner;
    private JButton addButton;
    private JLabel statusLabel;
    private enum FilterStatus { ALL, ACTIVE, COMPLETED }
    private enum SortCriterion { DATE, PRIORITY }
    private FilterStatus currentFilter = FilterStatus.ALL;
    private SortCriterion currentSort = SortCriterion.DATE;
    private String searchQuery = "";
    private JTextField searchField;
    
    public ToDoListApp() {
        setTitle("To-Do List Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        allTasks = SerializationManager.loadTasks(FILENAME);
        listModel = new DefaultListModel<>();
        setupUI();
        setupActionListeners();
        updateListModel();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SerializationManager.saveTasks(allTasks, FILENAME);
            }
        });
    }
    


    private void showTaskActionDialog(Task task) {
        boolean isTaskCompleted = task.isCompleted();
        String markUnmarkActionText = isTaskCompleted ? "Unmark as Complete" : "Mark as Complete";

        String[] options = {"Edit", markUnmarkActionText, "Delete", "Cancel"};

        int choice = JOptionPane.showOptionDialog(
            this,
            "What would you like to do with this task?\n\"" + task.getDescription() + "\"",
            "Task Actions",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );

        switch (choice) {
            case 0:
                showEditTaskDialog(task);
                break;

            case 1:
                task.setCompleted(!isTaskCompleted);
                taskList.repaint();
                break;

            case 2:
                int response = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to permanently delete this task?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                if (response == JOptionPane.YES_OPTION) {
                    allTasks.remove(task);
                    updateListModel();
                }
                break;

            default:
                break;
        }
    }
    
    private void showEditTaskDialog(Task task) {
   
        JPanel editPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField descriptionField = new JTextField(task.getDescription());
        JComboBox<Priority> priorityCombo = new JComboBox<>(Priority.values());
        priorityCombo.setSelectedItem(task.getPriority());

        SpinnerDateModel spinnerModel = new SpinnerDateModel();
  
        spinnerModel.setValue(java.sql.Date.valueOf(task.getDueDate()));

        JSpinner dateSpinner = new JSpinner(spinnerModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);


        editPanel.add(new JLabel("Description:"));
        editPanel.add(descriptionField);
        editPanel.add(new JLabel("Priority:"));
        editPanel.add(priorityCombo);
        editPanel.add(new JLabel("Due Date:"));
        editPanel.add(dateSpinner); 

    
        int result = JOptionPane.showConfirmDialog(this, editPanel, "Edit Task",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

  
        if (result == JOptionPane.OK_OPTION) {
     
            String newDescription = descriptionField.getText();
            if (newDescription.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Description cannot be empty.", "Edit Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            task.setDescription(newDescription);
            task.setPriority((Priority) priorityCombo.getSelectedItem());

            java.util.Date newDate = (java.util.Date) dateSpinner.getValue();
            task.setDueDate(newDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());

            updateListModel(); 
            taskList.repaint(); 
        }
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu filterMenu = new JMenu("Filter");
        ButtonGroup filterGroup = new ButtonGroup();

        JRadioButtonMenuItem filterAll = new JRadioButtonMenuItem("Show All", true);
        filterAll.addActionListener(e -> {
            currentFilter = FilterStatus.ALL;
            updateListModel();
        });

        JRadioButtonMenuItem filterActive = new JRadioButtonMenuItem("Show Active");
        filterActive.addActionListener(e -> {
            currentFilter = FilterStatus.ACTIVE;
            updateListModel();
        });

        JRadioButtonMenuItem filterCompleted = new JRadioButtonMenuItem("Show Completed");
        filterCompleted.addActionListener(e -> {
            currentFilter = FilterStatus.COMPLETED;
            updateListModel();
        });

        filterGroup.add(filterAll);
        filterGroup.add(filterActive);
        filterGroup.add(filterCompleted);
        filterMenu.add(filterAll);
        filterMenu.add(filterActive);
        filterMenu.add(filterCompleted);

        JMenu sortMenu = new JMenu("Sort");
        ButtonGroup sortGroup = new ButtonGroup();

        JRadioButtonMenuItem sortByDate = new JRadioButtonMenuItem("By Due Date", true);
        sortByDate.addActionListener(e -> {
            currentSort = SortCriterion.DATE;
            updateListModel();
        });

        JRadioButtonMenuItem sortByPriority = new JRadioButtonMenuItem("By Priority");
        sortByPriority.addActionListener(e -> {
            currentSort = SortCriterion.PRIORITY;
            updateListModel();
        });

        sortGroup.add(sortByDate);
        sortGroup.add(sortByPriority);
        sortMenu.add(sortByDate);
        sortMenu.add(sortByPriority);

        menuBar.add(filterMenu);
        menuBar.add(sortMenu);
        setJMenuBar(menuBar); 
    }

    
    private void setupUI() {
        createMenuBar();

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        inputPanel.add(new JLabel("Task:"));
        taskDescriptionField = new JTextField(30);
        inputPanel.add(taskDescriptionField);

        inputPanel.add(new JLabel("Priority:"));
        priorityComboBox = new JComboBox<>(Priority.values());
        inputPanel.add(priorityComboBox);

        inputPanel.add(new JLabel("Due Date:"));
        SpinnerDateModel spinnerModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(spinnerModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setValue(new java.util.Date());
        inputPanel.add(dateSpinner);

        addButton = new JButton("Add Task");
        inputPanel.add(addButton);

        searchField = new JTextField(20);
        searchField.setText("Search task");
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search task")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("Search task");
                }
            }
        });
        inputPanel.add(searchField);

        add(inputPanel, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new BorderLayout());

        statusLabel = new JLabel(" ");
        
        // Set the border to add 5 pixels of padding to the top and bottom
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));

        // Get the label's current font to use as a base
        Font labelFont = statusLabel.getFont();
        // Set a new font that is BOLD and size 14
        statusLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 14));

        actionPanel.add(statusLabel, BorderLayout.CENTER);

        add(actionPanel, BorderLayout.SOUTH);

        taskList = new JList<>(listModel);
        taskList.setCellRenderer(new TaskCellRenderer());
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(taskList), BorderLayout.CENTER);
    }
   
    
    private void setupActionListeners() {
        addButton.addActionListener(e -> {
            try {
                String description = taskDescriptionField.getText();
                if (description.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Task description cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Priority priority = (Priority) priorityComboBox.getSelectedItem();
                java.util.Date spinnerDate = (java.util.Date) dateSpinner.getValue();
                LocalDate dueDate = spinnerDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                Task newTask = new Task(description, dueDate, priority);
                allTasks.add(newTask);
                updateListModel();
                taskDescriptionField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Could not add task. Please check that all inputs are correct.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateSearch(); }
            private void updateSearch() {
                String currentText = searchField.getText();
                if (currentText.equals("Search task")) {
                    searchQuery = "";
                } else {
                    searchQuery = currentText;
                }
                updateListModel();
            }
        });

        taskList.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Task selectedTask = taskList.getSelectedValue();
                if (selectedTask != null) {
                    showTaskActionDialog(selectedTask);
                    taskList.clearSelection();
                }
            }
        });
    }

    private void updateListModel() {
        listModel.clear();

        List<Task> processedTasks = allTasks.stream()
        	.filter(task -> {
                if (searchQuery.trim().isEmpty()) {
                    return true; 
                }
              
                return task.getDescription().toLowerCase().contains(searchQuery.toLowerCase());
            })
   
            .filter(task -> {
                switch (currentFilter) {
                    case ACTIVE:    return !task.isCompleted();
                    case COMPLETED: return task.isCompleted();
                    case ALL:
                    default:        return true;
                }
            })
        
            .sorted((task1, task2) -> {
                if (currentSort == SortCriterion.DATE) {
                    return task1.getDueDate().compareTo(task2.getDueDate());
                } else {
                    return task1.getPriority().compareTo(task2.getPriority());
                }
            })
            .collect(Collectors.toList());

        for (Task task : processedTasks) {
            listModel.addElement(task);
        }
        statusLabel.setText("Showing " + listModel.getSize() + " of " + allTasks.size() + " total tasks");
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ToDoListApp().setVisible(true);
        });
    }
}
