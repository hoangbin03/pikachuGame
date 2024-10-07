
package controller;


import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;




public class Controller {

    private int row;
    private int col;
    private int[][] matrix;
    private ArrayList<ArrayList<Point>> paths = new ArrayList<ArrayList<Point>>();
    MainFrame frame;

    public Controller(MainFrame frame, int row, int col) {
        this.frame = frame;
        this.row = row;
        this.col = col;
        System.out.println(row + "," + col);
        createMatrix();
        showMatrix();
    }

    // hiển thị ma trận lên màn hình
    public void showMatrix() {
        for (int i = 1; i < row - 1; i++) {
            for (int j = 1; j < col - 1; j++) {
                System.out.printf("%3d", matrix[i][j]);
            }
            System.out.println();
        }
    }

    //kiểm tra xem có đường thẳng nào nối hai điểm trên cùng một hàng hay không
    private boolean checkLineX(int y1, int y2, int x) {
        System.out.println("check line x");
        // tìm điểm có cột max và min
        int min = Math.min(y1, y2);
        int max = Math.max(y1, y2);
        
        for (int y = min + 1; y < max; y++) {
            if (matrix[x][y] != 0) { 
                System.out.println("die: " + x + "" + y);
                return false;
            }
            System.out.println("ok: " + x + "" + y);
        }
       
        return true;
    }

    // kiểm tra xem có đường thẳng nào nối hai điểm trên cùng một cột hay không
    private boolean checkLineY(int x1, int x2, int y) {
        System.out.println("check line y");
        int min = Math.min(x1, x2);
        int max = Math.max(x1, x2);
        for (int x = min + 1; x < max; x++) {
            if (matrix[x][y] != 0) {
                System.out.println("die: " + x + "" + y);
                return false;
            }
            System.out.println("ok: " + x + "" + y);
        }
        return true;
    }

    // kiểm tra xem có đường đi giữa hai điểm theo chiều ngang trong hình chữ nhật hay không
    private boolean checkRectX(Point p1, Point p2) {
        System.out.println("check rect x");
        // tìm điểm có y min và max
        Point pMinY = p1, pMaxY = p2;
        if (p1.y > p2.y) {
            pMinY = p2;
            pMaxY = p1;
        }
        for (int y = pMinY.y; y <= pMaxY.y; y++) {
            if (y > pMinY.y && matrix[pMinY.x][y] != 0) {
                return false;
            }
            // kiểm tra 2 line
            if ((matrix[pMaxY.x][y] == 0)
                    && checkLineY(pMinY.x, pMaxY.x, y)
                    && checkLineX(y, pMaxY.y, pMaxY.x)) {

                System.out.println("Rect x");
                System.out.println("(" + pMinY.x + "," + pMinY.y + ") -> ("
                        + pMinY.x + "," + y + ") -> (" + pMaxY.x + "," + y
                        + ") -> (" + pMaxY.x + "," + pMaxY.y + ")");
                // nếu 3 dòng là true trả về cột y
                return true;
            }
        }
        // có một dòng trong ba dòng không đúng thì trả về -1
        return false;
    }

    //kiểm tra xem có đường đi giữa hai điểm theo chiều dọc trong hình chữ nhật hay không
    private boolean checkRectY(Point p1, Point p2) {
        System.out.println("check rect y");
        // find point have y min
        Point pMinX = p1, pMaxX = p2;
        if (p1.x > p2.x) {
            pMinX = p2;
            pMaxX = p1;
        }
        // find line and y begin
        for (int x = pMinX.x; x <= pMaxX.x; x++) {
            if (x > pMinX.x && matrix[x][pMinX.y] != 0) {
                return false;
            }
            if ((matrix[x][pMaxX.y] == 0)
                    && checkLineX(pMinX.y, pMaxX.y, x)
                    && checkLineY(x, pMaxX.x, pMaxX.y)) {

                System.out.println("Rect y");
                System.out.println("(" + pMinX.x + "," + pMinX.y + ") -> (" + x
                        + "," + pMinX.y + ") -> (" + x + "," + pMaxX.y
                        + ") -> (" + pMaxX.x + "," + pMaxX.y + ")");
                return true;
            }
        }
        return false;
    }

    //kiểm tra xem có đường đi giữa hai điểm theo chiều ngang với nhiều hơn một đường thẳng hay không
    private boolean checkMoreLineX(Point p1, Point p2, int type) {
        System.out.println("check chec more x");
       
        Point pMinY = p1, pMaxY = p2;
        if (p1.y > p2.y) {
            pMinY = p2;
            pMaxY = p1;
        }
        
        int y = pMaxY.y + type;
        int row = pMinY.x;
        int colFinish = pMaxY.y;
        if (type == -1) {
            colFinish = pMinY.y;
            y = pMinY.y + type;
            row = pMaxY.x;
            System.out.println("colFinish = " + colFinish);
        }

        
        if ((matrix[row][colFinish] == 0 || pMinY.y == pMaxY.y)
                && checkLineX(pMinY.y, pMaxY.y, row)) {
            while (matrix[pMinY.x][y] == 0
                    && matrix[pMaxY.x][y] == 0) {
                if (checkLineY(pMinY.x, pMaxY.x, y)) {

                    System.out.println("TH X " + type);
                    System.out.println("(" + pMinY.x + "," + pMinY.y + ") -> ("
                            + pMinY.x + "," + y + ") -> (" + pMaxY.x + "," + y
                            + ") -> (" + pMaxY.x + "," + pMaxY.y + ")");
                    return true;
                }
                y += type;
            }
        }
        return false;
    }

    // kiểm tra xem có đường đi giữa hai điểm theo chiều dọc với nhiều hơn một đường thẳng hay không
    private boolean checkMoreLineY(Point p1, Point p2, int type) {
        Point pMinX = p1, pMaxX = p2;
        if (p1.x > p2.x) {
            pMinX = p2;
            pMaxX = p1;
        }
        int x = pMaxX.x + type;
        int col = pMinX.y;
        int rowFinish = pMaxX.x;
        if (type == -1) {
            rowFinish = pMinX.x;
            x = pMinX.x + type;
            col = pMaxX.y;
        }
        if ((matrix[rowFinish][col] == 0 || pMinX.x == pMaxX.x)
                && checkLineY(pMinX.x, pMaxX.x, col)) {
            while (matrix[x][pMinX.y] == 0
                    && matrix[x][pMaxX.y] == 0) {
                if (checkLineX(pMinX.y, pMaxX.y, x)) {
                    System.out.println("TH Y " + type);
                    System.out.println("(" + pMinX.x + "," + pMinX.y + ") -> ("
                            + x + "," + pMinX.y + ") -> (" + x + "," + pMaxX.y
                            + ") -> (" + pMaxX.x + "," + pMaxX.y + ")");
                    return true;
                }
                x += type;
            }
        }
        return false;
    }

    //kiểm tra xem có đường đi giữa hai điểm hay không

    public PointLine checkTwoPoint(Point p1, Point p2) {

        if (!p1.equals(p2) && matrix[p1.x][p1.y] == matrix[p2.x][p2.y]) {
            // check line with x
            if (p1.x == p2.x) {
                if (checkLineX(p1.y, p2.y, p1.x)) {
                    return new PointLine(p1, p2);
                }
            }
            // check line with y
            if (p1.y == p2.y) {
                System.out.println("line y");
                if (checkLineY(p1.x, p2.x, p1.y)) {
                    return new PointLine(p1, p2);
                }
            }


            // check in rectangle with x
            if (checkRectX(p1, p2)) {
                return new PointLine(p1, p2);
            }

            // check in rectangle with y
            if (checkRectY(p1, p2)) {
                return new PointLine(p1, p2);
            }
            // check more right
            if (checkMoreLineX(p1, p2, 1)) {
                return new PointLine(p1, p2);
            }
            // check more left
            if (checkMoreLineX(p1, p2, -1)) {
                return new PointLine(p1, p2);
            }
            // check more down
            if (checkMoreLineY(p1, p2, 1)) {
                return new PointLine(p1, p2);
            }
            // check more up
            if (checkMoreLineY(p1, p2, -1)) {
                return new PointLine(p1, p2);
            }
        }
        return null;
    }
    
    //tạo ra một ma trận ngẫu nhiên
    private void createMatrix() {
        matrix = new int[row][col];
        for (int i = 0; i < col; i++) {
            matrix[0][i] = matrix[row - 1][i] = 0;
        }
        for (int i = 0; i < row; i++) {
            matrix[i][0] = matrix[i][col - 1] = 0;
        }

        //tạo ra các số ngẫu nhiên
        Random rand = new Random();
        int imgCount = 21;
        int max = imgCount / 2;
        int[] arr = new int[imgCount + 1];
        ArrayList<Point> listPoint = new ArrayList<Point>();
        for (int i = 1; i < row - 1; i++) {
            for (int j = 1; j < col - 1; j++) {
                listPoint.add(new Point(i, j));
            }
        }
        int i = 0;
        do {
            int index = rand.nextInt(imgCount) + 1;
            if (arr[index] < max) {

                arr[index] += 2;
                for (int j = 0; j < 2; j++) {
                    try {
                        int size = listPoint.size();
                        int pointIndex = rand.nextInt(size);
                        matrix[listPoint.get(pointIndex).x][listPoint
                                .get(pointIndex).y] = index;
                        listPoint.remove(pointIndex);
                    } catch (Exception e) {
                    }
                }
                i++;
            }
        } while (i < row * col / 2);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }
}