
package controller;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;



public class ButtonEvent extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private int row;
    private int col;
    private int bound = 2;
    private int size = 50;
    private int score = 0;
    private JButton[][] btn;
    private Point p1 = null;
    private Point p2 = null;
    private Controller algorithm;
    private PointLine line;
    private MainFrame frame;
    private Color backGroundColor = Color.lightGray;
    private int item;
 
    
public ButtonEvent(MainFrame frame, int row, int col) {
    
    this.frame = frame;
    this.row = row + 2;
    this.col = col + 2;
    item = row * col / 2;

    // Thiết lập layout, màu nền, kích thước và đường viền cho panel
    setLayout(new GridLayout(row, col, bound, bound));
    setBackground(backGroundColor);
    setPreferredSize(new Dimension((size + bound) * col, (size + bound)* row));
    setBorder(new EmptyBorder(10, 10, 10, 10));
    setAlignmentY(JPanel.CENTER_ALIGNMENT);

    
    newGame();
}

// Hàm bắt đầu một trò chơi mới
public void newGame() {
    // Khởi tạo một đối tượng thuộc lớp Controller để xử lý logic trò chơi
    algorithm = new Controller(this.frame, this.row, this.col);
    // Thêm một mảng các nút vào panel
    addArrayButton();
}

// Hàm thêm một mảng các nút vào panel
private void addArrayButton() {
    // Khởi tạo mảng các nút
    btn = new JButton[row][col];
    // Duyệt qua từng phần tử trong mảng
    for (int i = 1; i < row - 1; i++) {
        for (int j = 1; j < col - 1; j++) {
            // Tạo một nút mới và thiết lập biểu tượng cho nó
            btn[i][j] = createButton(i + "," + j);
            Icon icon = getIcon(algorithm.getMatrix()[i][j]);
            btn[i][j].setIcon(icon);
            // Thêm nút vào panel
            add(btn[i][j]);
        }
    }
}

// Hàm lấy biểu tượng từ một chỉ số
private Icon getIcon(int index) {
    // Thiết lập kích thước cho biểu tượng
    int width = 48, height = 48;
    // Tải hình ảnh từ tệp và tạo biểu tượng từ hình ảnh đó
    Image image = new ImageIcon(getClass().getResource(
            "/icon/" + index + ".png")).getImage();
    Icon icon = new ImageIcon(image.getScaledInstance(width, height,
            image.SCALE_SMOOTH));
    return icon;
}

// Hàm tạo một nút mới
private JButton createButton(String action) {
    // Khởi tạo một nút mới và thiết lập lệnh hành động cho nó
    JButton btn = new JButton();
    btn.setActionCommand(action);
    btn.setBorder(null);
    // Thêm ActionListener cho nút
    btn.addActionListener(this);
    return btn;
}

// Hàm thực thi khi hai điểm được chọn
public void execute(Point p1, Point p2) {
    System.out.println("delete");
    // Vô hiệu hóa hai nút tương ứng với hai điểm được chọn
    setDisable(btn[p1.x][p1.y]);
    setDisable(btn[p2.x][p2.y]);
}

// Hàm vô hiệu hóa một nút
private void setDisable(JButton btn) {
    // Xóa biểu tượng và thiết lập màu nền cho nút
    btn.setIcon(null);
    btn.setBackground(backGroundColor);
    // Vô hiệu hóa nút
    btn.setEnabled(false);
}

// Hàm xử lý sự kiện khi một nút được nhấp chuột
@Override
public void actionPerformed(ActionEvent e) {
    // Lấy chỉ số của nút được nhấp chuột từ lệnh hành động của nó
    String btnIndex = e.getActionCommand();
    int indexDot = btnIndex.lastIndexOf(",");
    int x = Integer.parseInt(btnIndex.substring(0, indexDot));
    int y = Integer.parseInt(btnIndex.substring(indexDot + 1,
            btnIndex.length()));
    // Nếu đây là lần nhấp chuột đầu tiên
    if (p1 == null) {
        // Lưu lại vị trí của nút được nhấp chuột
        p1 = new Point(x, y);
        // Đổi màu đường viền của nút
        btn[p1.x][p1.y].setBorder(new LineBorder(Color.red));
        
    } else {
        // Nếu đây là lần nhấp chuột thứ hai
        // Lưu lại vị trí của nút được nhấp chuột
        p2 = new Point(x, y);
        System.out.println("(" + p1.x + "," + p1.y + ")" + " --> " + "("
                + p2.x + "," + p2.y + ")");
        // Kiểm tra xem hai điểm có thỏa mãn điều kiện của trò chơi hay không
        line = algorithm.checkTwoPoint(p1, p2);
        if (line != null) {
            System.out.println("line != null");
            // Cập nhật ma trận và hiển thị ma trận
            algorithm.getMatrix()[p1.x][p1.y] = 0;
            algorithm.getMatrix()[p2.x][p2.y] = 0;
            algorithm.showMatrix();
            // Thực thi với hai điểm được chọn
            execute(p1, p2);
            line = null;
            // Cập nhật điểm số và số lượng mục tiêu còn lại
            score += 15;
            item--;
            frame.time++;
            frame.lbScore.setText(score + "");
        }
        // Đặt lại đường viền cho nút được nhấp chuột lần đầu tiên
        btn[p1.x][p1.y].setBorder(null);
        // Đặt lại giá trị cho hai điểm được chọn
        p1 = null;
        p2 = null;
        System.out.println("done");
        
        score -= 5; 
        frame.lbScore.setText(score + "");
        
    }
    
    // Nếu số lượng mục tiêu còn lại bằng 0
    if (item == 0) {
    // Hiển thị thông báo chiến thắng
    frame.showWinMessage();
    database db = new database();
    int finalTime = 300 - frame.time; 
    int finalScore = score; 
    db.saveScore(finalTime, finalScore);
    }
}
}