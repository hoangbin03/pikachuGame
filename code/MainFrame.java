
package controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;


public class MainFrame extends JFrame implements ActionListener, Runnable {

    private static final long serialVersionUID = 1L;

    private int maxTime = 300;
    public int time = maxTime;
    private int row = 8;
    private int col = 8;
    private int width = 800;
    private int height =600;
    public JLabel lbScore;
    private JProgressBar progressTime;
    private JButton btnNewGame;
    private JButton btnExit;
    private JButton btndiemcao;
    private ButtonEvent graphicsPanel;
    public JPanel mainPanel;
    private boolean pause = false;
    private boolean resume = false;
   
    public MainFrame() {
        
        add(mainPanel = createMainPanel());
        setTitle("Pikachu Game by nhom 9");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setVisible(true);
       
    Sound backgroundMusic = new Sound("/sound/NhacNen.wav");
    backgroundMusic.loop();
    
   
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createGraphicsPanel(), BorderLayout.CENTER);
        panel.add(createControlPanel(), BorderLayout.EAST);
        panel.add(createStatusPanel(), BorderLayout.PAGE_END);
        return panel;
    }

    private JPanel createGraphicsPanel() {
        graphicsPanel = new ButtonEvent(this, row, col);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.BLACK);
        panel.add(graphicsPanel);
        return panel;
    }

    private JPanel createControlPanel() {
        lbScore = new JLabel("0");
        progressTime = new JProgressBar(0, 100);
        progressTime.setValue(100);

        // tạo điểm và thời gian 
        JPanel panelLeft = new JPanel(new GridLayout(0, 1, 5, 5));
        panelLeft.add(new JLabel("Score:"));
        panelLeft.add(new JLabel("Time:"));

        JPanel panelCenter = new JPanel(new GridLayout(0, 1, 5, 5));
        panelCenter.add(lbScore);
        panelCenter.add(progressTime);

        JPanel panelScoreAndTime = new JPanel(new BorderLayout(5, 0));
        panelScoreAndTime.add(panelLeft, BorderLayout.WEST);
        panelScoreAndTime.add(panelCenter, BorderLayout.CENTER);

        //tạo ra một đối tượng JPanel chứa các thông tin về điểm số, thời gian và điểm cao
        JPanel panelControl = new JPanel(new BorderLayout(10, 10));
        panelControl.setBorder(new EmptyBorder(10, 3, 5, 3));
        panelControl.add(panelScoreAndTime, BorderLayout.CENTER);
        
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
        panelButtons.add(btnNewGame = createButton("Ván mới"));
        panelButtons.add(Box.createRigidArea(new Dimension(5, 10)));
        panelButtons.add(btnExit = createButton("Thoát"));
        panelButtons.add(Box.createRigidArea(new Dimension(5, 10)));
        panelButtons.add(btndiemcao = createButton("Điểm cao"));
        
        panelControl.add(panelButtons, BorderLayout.PAGE_END);
        
        Icon icon = new ImageIcon(getClass().getResource("/icon/pokemon.png"));
        
        // sử dụng panel set Layout BorderLayout cho panel control ở trên cùng
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("playing"));
        panel.add(new JLabel(icon), BorderLayout.CENTER);
        panel.add(panelControl, BorderLayout.PAGE_START);

        return panel;
    }

    //tạo ra một đối tượng JPanel chứa chức năng người chơi
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lb = new JLabel("PIKACHU GAME");
        panel.add(lb);
        panel.setBackground(Color.lightGray);

        return panel;
    }

    // tạo button
    private JButton createButton(String buttonName) {
        JButton btn = new JButton(buttonName);
        btn.addActionListener(this);
        return btn;
        
    }
    
    //bắt đầu một ván chơi mới
    public void newGame() {
      
        time = maxTime;
        graphicsPanel.removeAll();
        mainPanel.add(createGraphicsPanel(), BorderLayout.CENTER);
        mainPanel.validate();
        mainPanel.setVisible(true);
        lbScore.setText("0");
    }
    //hiển thị một hộp thoại xác nhận khi người dùng muốn bắt đầu một ván chơi mới
    public boolean showDialogNewGame(String message, String title, int messageType) {
         pause=true;
    resume=false;
	int select = JOptionPane.showOptionDialog(null, message, title,
	JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);
	if (select == 0) {
        pause=false;
		newGame();
	} else {
		   if(time==1){
                System.exit(0);
           }else if (time==0){
               showLostMessage();
           }
                   
                   else{
                resume=true;
           }
                   
	}
        return false;
    }

    public void showWinMessage() {
        JDialog winDialog = new JDialog(this, "Notification");
    winDialog.setSize(300, 200);
    winDialog.setLocationRelativeTo(this);
    JLabel winLabel = new JLabel("<html>&nbsp;&nbsp;&nbsp;&nbsp;Bạn đã thắng rồi!<br>Chơi lại ván mới nhé?</html>");
    winLabel.setForeground(Color.RED);
    winLabel.setFont(new Font("Serif", Font.BOLD, 24));
    winLabel.setHorizontalAlignment(JLabel.CENTER);
    winLabel.setVerticalAlignment(JLabel.CENTER);
    
    JButton newGameButton = new JButton("Ván mới");
    newGameButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            newGame();
            winDialog.dispose();
        }
    });
    winDialog.add(newGameButton, BorderLayout.PAGE_END);
    winDialog.add(winLabel);
    winDialog.setVisible(true);
    }
    
     public void showLostMessage() {
    JDialog lostDialog = new JDialog(this, "Notification", true);
    lostDialog.setSize(300, 200);
    lostDialog.setLocationRelativeTo(this);
    JLabel lostLabel = new JLabel("<html>&nbsp;Game over!<br>bạn đã thua!</html>");
    lostLabel.setForeground(Color.RED);
    lostLabel.setFont(new Font("Serif", Font.BOLD, 24));
    lostLabel.setHorizontalAlignment(JLabel.CENTER);
    lostLabel.setVerticalAlignment(JLabel.CENTER);

    lostDialog.add(lostLabel);
    lostDialog.setVisible(true);
    }
     
    //xử lý sự kiện khi người dùng nhấp vào các nút điều khiển
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == btnNewGame) {
            showDialogNewGame("Bạn có muốn chơi ván mới không ?", "Warning", 0);
        }
        else if (e.getSource() == btnExit) {
        System.exit(0);
        //hiển thị điểm cao
        } else if (e.getSource() == btndiemcao) {
        database db = new database();
        List<Score> highScores = db.getHighScores();

        // Tạo một đối tượng JTable để hiển thị dữ liệu
        String[] columnNames = {"Số thứ tự", "Thời gian", "Điểm số"};
        Object[][] data = new Object[highScores.size()][3];
        for (int i = 0; i < highScores.size(); i++) {
        Score score = highScores.get(i);
        data[i][0] = i + 1;
        data[i][1] = score.getTime();
        data[i][2] = score.getScore();
        }
        JTable table = new JTable(data, columnNames);
        //Tạo kích thước cho cột
        TableColumn column = table.getColumnModel().getColumn(0);
        column.setPreferredWidth(10);
        // Tạo một đối tượng JScrollPane chứa JTable
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Tạo một đối tượng JPanel chứa JScrollPane
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(300,200));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Hiển thị JPanel trong một hộp thoại
        JOptionPane.showMessageDialog(this, panel, "Điểm cao", JOptionPane.INFORMATION_MESSAGE);
    }
    }
    //để cập nhật thanh thời gian
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressTime.setValue((int) ((double) time / maxTime * 100));
            if (time == 0) {
            showLostMessage();
               
            }
        }
    }

    public JProgressBar getProgressTime() {
        return progressTime;
    }

    public void setProgressTime(JProgressBar progressTime) {
        this.progressTime = progressTime;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isResume() {
        return resume;
    }

    public void setResume(boolean resume) {
        this.resume = resume;
    }

        
}
