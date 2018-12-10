// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 3

public class HappensBeforeDetect {
    public static void main(String args[]) {
        HappensBeforeThread thread1 = new HappensBeforeThread(20, true);
        HappensBeforeThread thread2 = new HappensBeforeThread(25, true);
        HappensBeforeThread thread3 = new HappensBeforeThread(30, true);
        HappensBeforeThread thread4 = new HappensBeforeThread(35, true);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
