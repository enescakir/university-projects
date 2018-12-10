// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 3

public class Lockset {
    public static void main(String args[]) {
        LocksetThread thread1 = new LocksetThread();
        LocksetThread thread2 = new LocksetThread();

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(LocksetThread.sharedVariable[0]);
    }
}
