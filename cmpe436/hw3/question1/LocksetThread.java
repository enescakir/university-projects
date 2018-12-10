// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 3

public class LocksetThread extends Thread {
    static int[] sharedVariable = {0};

    @Override
    public void run() {
        for (int i = 0; i < 3000; i++) {
            sharedVariable[0] += 1;
        }
    }
}