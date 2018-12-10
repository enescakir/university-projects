// Mustafa Enes Çakır
// 2013400105
// enes@cakir.web.tr
// CmpE436 Assignment 2

/**
 * Implementation of binary semaphore based on counting semaphore
 */
public class BinarySemaphore extends CountingSemaphore {
    public BinarySemaphore() {
        super(1);
    }
}
