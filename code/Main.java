
package controller;

public class Main {

    MainFrame frame;
   

    public Main() {
        frame = new MainFrame();
        MyTimeCount timeCount = new MyTimeCount();
        timeCount.start();
        new Thread(frame).start();
    }

    public static void main(String[] args) {
        Main m = new Main();
    }

   
    class MyTimeCount extends Thread {

        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (frame.isPause()) {
                    if (frame.isResume()) {
                        frame.time--;
                    }
                } else {
                    frame.time--;
                }
                
                }
            }
        }
    }
