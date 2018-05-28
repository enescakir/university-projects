/*
    Student Name: Mustafa Enes Cakir
    Student Number: 2013400105
    Project Number: 3
    Operating System: macOS High Sierra
    Compile Status: Done
    Program Status: Working
    Note: When compile code, add -std=c++11 arguments
    g++ main.cpp -std=c++11
*/

#include <iostream>
#include <queue>
#include <fstream>
#include <iomanip>

/*
    ############## CONFIGURATION ##############
    You can change configuration of application
    ###########################################
*/

// If it's true, it prints out to console instead of output file
#define CONSOLE 0

// The amount of each quantum
#define QUANTUM 100

// The definition file
#define DEFINITION "definition.txt"

// The output file prefix
#define OUTPUT "output"

// The semaphore count
#define SEMAPHORE_COUNT 10

// The printer count
#define PRINTER_COUNT 2

// The cache count
#define CACHE_COUNT 2

// The log every instruction
#define LOG 0

using namespace std;


/**
 * Instruction struct keeps information of instruction
 */
struct Instruction {
    string name;
    int time;

    Instruction(string name_, int time_) {
        name = name_;
        time = time_;
    }

    /**
     * Checks is instruction wait
     * @return true if it's a wait instruction
     */
    bool isWait() {
        return name.find("waitS") == 0;
    }

    /**
     * Checks is instruction signal
     * @return true if it's a signal instruction
     */
    bool isSignal() {
        return name.find("signS") == 0;
    }

    /**
     * Get semaphore number of instruction
     * @return number of semaphore
     */
    int getSemaphore() {
        return stoi(name.substr(6));
    }

    /**
     * Checks is instruction printer
     * @return true if it's a printer instruction
     */
    bool isPrinter() {
        return name.find("dispM") == 0;
    }

    /**
     * Get printer number of instruction
     * @return number of printer
     */
    int getPrinter() {
        return stoi(name.substr(6));
    }

    /**
     * Checks is instruction hard drive
     * @return true if it's a hard drive instruction
     */
    bool isHDD() {
        return name.find("readM") == 0;
    }

    /**
     * Get block number of instruction
     * @return number of printer
     */
    int getBlock() {
        return stoi(name.substr(6));
    }

};


/**
 * Process struct keeps general information of process
 */
struct Process {
    string name;
    int time;
    string code_file;
    int last_line;
    vector<Instruction> instructions;

    Process(string name_, string code_file_, int time_) {
        name = name_;
        code_file = code_file_;
        time = time_;
        last_line = 0;
        readInstructions();
    }

    /**
     * Reads corresponding code file and push instructions to Process
     */
    void readInstructions() {
        string ins_name;
        int ins_time;
        ifstream in(code_file);
        while (in >> ins_name >> ins_time) {
            Instruction ins(ins_name, ins_time);
            instructions.push_back(ins);
        }
        in.close();
    }

    /**
     * Checks is Process finished
     * @return true if all instructions are executed
     */
    bool isFinished() {
        return last_line == instructions.size();
    }

    /**
     * Returns last executued instruction
     * @return last executued instruction
     */
    Instruction &getLastInstruction() {
        return instructions[last_line];
    }

    /**
     * Returns last executued previous instruction
     * @return last executued previous instruction
     */
    Instruction &getPreviousInstruction() {
        return instructions[last_line - 1];
    }
};

/**
 * Converts queue to string representation
 * @param q the queue to string
 * @param time the current time
 */
string queueToString(queue<Process> q, int time) {
    string result = "";
    result += to_string(time) + "::HEAD-";
    while (!q.empty()) {
        // If it's is last one don't put `-`
        if (q.size() > 1) result += q.front().name + "-";
        else result += q.front().name;
        q.pop();
    }
    result += "-TAIL";
    return result;
}

/**
 * Semaphore struct keeps information of semaphore
 */
struct Semaphore {
    int value;
    int number;
    queue<Process> waitingQueue;

    Semaphore(int value_, int number_) {
        value = value_;
        number = number_;
    }

    /**
     * Tries to lock this semaphore. If isn't available push to waiting queue
     * @param p process that wants to lock resources
     * @return true if resource isn't available, if it's available returns false
     */
    bool wait(Process &p) {
        value--;
        if (value < 0) {
            waitingQueue.push(p);
            return true;
        }
        return false;
    }

    /**
     * Releases this semaphore
     * @return true if there is process in waiting queue
     */
    bool signal() {
        value++;
        if (value <= 0) {
            return true;
        }
        return false;
    }

    /**
     * Get process from top of the waiting queue
     * @return
     */
    Process getFromWait() {
        Process p = waitingQueue.front();
        waitingQueue.pop();
        return p;
    }

    /**
     * Prints out the waiting queue of this semaphore
     * @param sem_no number of corresponding semaphore
     * @param time current time
     */
    void printOut(int time) {
        string filename = OUTPUT;
        filename += "_" + to_string(number) + ".txt";
        ofstream sem_out(filename, ios_base::app);
        sem_out << queueToString(waitingQueue, time) << endl;
        sem_out.close();
    }

};


/**
 * Printer struct keeps information of printer
 */
struct Printer {
    int number;
    int last_time; // Last time printer used
    queue<Process> waitingQueue;

    Printer(int number_) {
        number = number_;
        last_time = 0;
    }

    /**
     * Adds new process to printer's waiting queue
     * @param p Process that added to waiting queue
     * @param time current time
     */
    void wait(Process &p, int time) {
        if (waitingQueue.empty()) last_time = time;
        waitingQueue.push(p);
    }

    /**
     * Checks waiting queue for completed instructions
     * @param time current time
     * @return true if there is completed instruction in printer
     */
    bool hasReady(int time) {
        if (!waitingQueue.empty()) {
            Instruction ins = waitingQueue.front().getPreviousInstruction();
            if (last_time + ins.time <= time) {
                if (LOG)
                    cout << "[PRINT_READY] TIME: " << time << " - " << waitingQueue.front().name << " " << ins.name
                         << ":" << ins.time << " start:" << last_time << endl;
                return true;
            }
        } else {
            last_time = time;
        }
        return false;
    }

    /**
     * Returns completed process
     * @param time current time
     * @return completed process
     */
    Process getReady(int time) {
        Process p = waitingQueue.front();
        waitingQueue.pop();
        last_time = time;
        return p;
    }

    /**
     * Prints out to printer waiting queue to file
     * @param time current time
     */
    void printOut(int time) {
        string filename = OUTPUT;
        filename += "_1" + to_string(number) + ".txt";
        ofstream print_out(filename, ios_base::app);
        print_out << queueToString(waitingQueue, time) << endl;
        print_out.close();
    }
};

/**
 * Cache struct keeps information of cache
 */
struct Cache {
    int block_no; // Cached block no
    int last_use; // Keeps last used time for LRU

    Cache() {
        block_no = -1;
        last_use = -1;
    }

    Cache(int block_no_, int last_use_) {
        block_no = block_no_;
        last_use = last_use_;
    }
};

/**
 * Hard drive struct keeps information of hard drive
 */
struct HardDrive {
    int last_time; // Last time printer used
    queue<Process> waitingQueue;
    vector<Cache> caches; // Caches of hard drive

    HardDrive() {
        last_time = 0;
        // Add empty caches to hard drive
        for (int i = 0; i < CACHE_COUNT; i++) caches.push_back(Cache());
    }

    /**
     * Checks given block is cached
     * @param block Block no to check cache
     * @param time current tume
     * @return true if block is already cached
     */
    bool isCached(int block, int time) {
        int least_index = 0, least_time = time;
        if (LOG) cout << "[CACHE] TIME: " << time << " - Checking cache Block:" << block << endl;
        for (int i = 0; i < caches.size(); ++i) {
            // Checks cache
            if (caches[i].block_no == block) {
                if (LOG) cout << "\tCache " << i << " is same" << endl;
                caches[i].last_use = time;
                return true;
            }
            // Checks LRU
            if (caches[i].last_use < least_time) {
                if (LOG) cout << "\tCache " << i << " is least recently used one" << endl;
                least_time = caches[i].last_use;
                least_index = i;
            }
        }
        // Change new one with LRU
        caches[least_index] = Cache(block, time);
        return false;
    }

    /**
     * Adds new process to hard drive's waiting queue
     * @param p Process that added to waiting queue
     * @param time current time
     */
    void wait(Process &p, int time) {
        if (waitingQueue.empty()) last_time = time;
        waitingQueue.push(p);
    }

    /**
     * Checks waiting queue for completed instructions
     * @param time current time
     * @return true if there is completed instruction in hard drive
     */
    bool hasReady(int time) {
        if (!waitingQueue.empty()) {
            Instruction ins = waitingQueue.front().getPreviousInstruction();
            if (last_time + ins.time <= time) {
                last_time += ins.time;
                return true;
            }
        } else {
            last_time = time;
        }
        return false;
    }

    /**
     * Returns completed process
     * @param time current time
     * @return completed process
     */
    Process getReady(int time) {
        Process p = waitingQueue.front();
        waitingQueue.pop();
        last_time = time;
        return p;
    }

    /**
     * Prints out to hard drive waiting queue to file
     * @param time current time
     */
    void printOut(int time) {
        string filename = OUTPUT;
        filename += "_12.txt";
        ofstream print_out(filename, ios_base::app);
        print_out << queueToString(waitingQueue, time) << endl;
        print_out.close();
    }
};

/**
 * Compares arrival times for arrival priority queue
 */
struct arrivalComparator {
    bool operator()(Process p1, Process p2) {
        return p1.time > p2.time;
    }
};


/**
 * Prints ready queue
 * @param q the queue to print
 * @param time the current time
 */
void printReadyQueue(queue<Process> q, int time) {
    cout << queueToString(q, time) << endl;
}

/**
 * Removes old output files
 */
void removeOldOutputs() {
    // Removes semaphore files
    for (int i = 0; i < SEMAPHORE_COUNT; i++) {
        string filename = OUTPUT;
        filename += "_" + to_string(i) + ".txt";
        remove(filename.c_str());
    };

    // Removes printer files
    for (int i = 0; i < PRINTER_COUNT; i++) {
        string filename = OUTPUT;
        filename += "_1" + to_string(i) + ".txt";
        remove(filename.c_str());
    };

    // Remove hard drive file
    string filename = OUTPUT;
    filename += "_12.txt";
    remove(filename.c_str());

}

int main(int argc, char *argv[]) {
    // Remove old files
    removeOldOutputs();

    // It keeps arrived procces based on their arrival time. First one is first arrived one.
    priority_queue<Process, vector<Process>, arrivalComparator> processArrivals;

    // The ready queue. Processor takes it's first element in each quantum
    queue<Process> readyQueue;

    // The semaphore list. Index gives corresponding semaphore
    vector<Semaphore> semaphores;

    // Initializing all semaphore with 1 resource and remove old output files
    for (int i = 0; i < SEMAPHORE_COUNT; i++) {
        semaphores.push_back(Semaphore(1, i));
    };

    // The printer list. Index gives corresponding printer
    vector<Printer> printers;

    // Initializing all printers and remove old output files
    for (int i = 0; i < PRINTER_COUNT; i++) {
        printers.push_back(Printer(i));
    };

    // The hard drive
    HardDrive hardDrive;

    // The amount of each quantum
    int quantum = QUANTUM;

    // Current time
    int time = 0;

    // Prints out to console or file
    string out_name = OUTPUT;
    out_name += ".txt";
    if (!CONSOLE) freopen(out_name.c_str(), "w", stdout);

    // Read processes and related code files
    ifstream definition_file(DEFINITION);
    string p_name, p_file;
    int p_time;
    while (definition_file >> p_name >> p_file >> p_time) {
        Process p(p_name, p_file, p_time);
        processArrivals.push(p);
    }
    definition_file.close();

    // Continue to execution until ready queue is empty and no new event is arriving
    while (!processArrivals.empty() || !readyQueue.empty()) {
        // If the new event is arrived, transfer it to ready queue
        while (!processArrivals.empty() && processArrivals.top().time <= time) {
            readyQueue.push(processArrivals.top());
            processArrivals.pop();
        }
        // Print current status of the ready queue
        printReadyQueue(readyQueue, time);

        // Transfer first element of ready queue to CPU
        if (!readyQueue.empty()) {
            Process c_process = readyQueue.front();
            bool is_waiting = false;
            // It keeps used time of the current quantum
            int used_quantum = 0;
            // Execute instructions until process is done or remaining quantum isn't enough or it's not waiting unavailable semaphore
            while (!c_process.isFinished() && used_quantum < quantum && !is_waiting) {
                Instruction ins = c_process.instructions[c_process.last_line];
                if (LOG) cout << "[INS] TIME:" << time << " - " << c_process.name << " - " << ins.name << " " << ins.time << "" << endl;
                c_process.last_line++;

                if (ins.isWait()) {
                    // Check is it wait instruction

                    int sem_no = ins.getSemaphore();
                    if (semaphores[sem_no].wait(c_process)) {
                        // Semaphore is already locked, pushed to waiting queue
                        is_waiting = true;
                        semaphores[sem_no].printOut(time);
                    }
                }
                else if (ins.isSignal()) {
                    // Check is it signal instruction
                    int sem_no = ins.getSemaphore();
                    if (semaphores[sem_no].signal()) {
                        // There is process in waiting queue of signaled semaphore, transfer it to ready queue
                        readyQueue.push(semaphores[sem_no].getFromWait());
                        printReadyQueue(readyQueue, time);
                        semaphores[sem_no].printOut(time);
                    }
                } else if (ins.isPrinter()) {
                    // Check is it printer instruction
                    int p_no = ins.getPrinter();
                    printers[p_no].wait(c_process, time);
                    is_waiting = true;
                    printers[p_no].printOut(time);
                } else if (ins.isHDD()) {
                    // Check is it hard drive instruction
                    int b_no = ins.getBlock();
                    // If it's cached not wait
                    if (!hardDrive.isCached(b_no, time)) {
                        hardDrive.wait(c_process, time);
                        is_waiting = true;
                        hardDrive.printOut(time);
                    }
                } else {
                    // Move time to next point
                    time += ins.time;
                    used_quantum += ins.time;

                    // Check printers are they ready
                    for (int i = 0; i < PRINTER_COUNT; ++i) {
                        while (printers[i].hasReady(time)) {
                            readyQueue.push(printers[i].getReady(time));
                            printers[i].printOut(time);
                        }
                    }

                    // Check hard drive are they ready
                    while (hardDrive.hasReady(time)) {
                        readyQueue.push(hardDrive.getReady(time));
                        hardDrive.printOut(time);
                    }
                }
            }

            // If the new event is arrived, transfer it to ready queue
            // Put it before putting unfinished job again
            while (!processArrivals.empty() && processArrivals.top().time <= time) {
                readyQueue.push(processArrivals.top());
                processArrivals.pop();
            }
            // If the process is not finished and not waiting, put it back to end of the ready queue
            if (!c_process.isFinished() && !is_waiting) {
                readyQueue.push(c_process);
            }

            // Remove current process from ready queue
            readyQueue.pop();

        } else {
            // If no process is in ready queue, jump to next quantum
            time += quantum;
        }
    }
    // Print last status of the ready queue
    printReadyQueue(readyQueue, time);
    return 0;
}
