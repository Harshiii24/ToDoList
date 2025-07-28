import javax.swing.*;
import java.awt.*;

public class TaskCellRenderer extends JPanel implements ListCellRenderer<Task> {

    private JCheckBox checkBox;
    private JLabel descriptionLabel;
    private JLabel detailsLabel;
    private JLabel selectionIndicatorLabel;

    public TaskCellRenderer() {
        setLayout(new BorderLayout(5, 5));
        setOpaque(true);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        descriptionLabel = new JLabel();
        detailsLabel = new JLabel();
        textPanel.add(descriptionLabel);
        textPanel.add(detailsLabel);

        checkBox = new JCheckBox();
        checkBox.setOpaque(false);

        selectionIndicatorLabel = new JLabel();
        selectionIndicatorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        selectionIndicatorLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        add(checkBox, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
        add(selectionIndicatorLabel, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Task> list, Task task,
                                                  int index, boolean isSelected, boolean cellHasFocus) {

        checkBox.setSelected(task.isCompleted());
        detailsLabel.setText("Due: " + task.getDueDate() + " | Priority: " + task.getPriority());

        if (isSelected) {
            selectionIndicatorLabel.setText("<html><b>Selected</b></html>");
        } else {
            selectionIndicatorLabel.setText("");
        }

        if (task.isCompleted()) {
            descriptionLabel.setText("<html><strike>" + task.getDescription() + "</strike></html>");
            setBackground(new Color(225, 225, 225)); 
            descriptionLabel.setForeground(Color.GRAY);
            detailsLabel.setForeground(Color.LIGHT_GRAY);
        } else {
            descriptionLabel.setText(task.getDescription());
            descriptionLabel.setForeground(Color.BLACK); 
            detailsLabel.setForeground(Color.GRAY);      

            switch (task.getPriority()) {
                case HIGH:
                    setBackground(new Color(255, 204, 204));
                    break;
                case MEDIUM:
                    setBackground(new Color(255, 255, 204));
                    break;
                case LOW:
                    setBackground(new Color(204, 255, 204));
                    break;
                default:
                    setBackground(list.getBackground());
                    break;
            }
        }
        
        setEnabled(list.isEnabled());
        setFont(list.getFont());

        return this;
    }
}
