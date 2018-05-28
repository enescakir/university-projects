/*
    Student Name: Mustafa Enes Cakir
    Student Number: 2013400105
    Project Number: 2
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

// The output file
#define OUTPUT "output.txt"

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
    queue<Process> waitingQueue;

    Semaphore(int value_) {
        value = value_;
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
    void printOut(int sem_no, int time) {
        ofstream sem_out ("output_" + to_string(sem_no) + ".txt", ios_base::app);
        sem_out << queueToString(waitingQueue, time) << endl;
        sem_out.close();
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


int main(int argc, char *argv[]) {
    // It keeps arrived procces based on their arrival time. First one is first arrived one.
    priority_queue<Process, vector<Process>, arrivalComparator> processArrivals;

    // The ready queue. Processor takes it's first element in each quantum
    queue<Process> readyQueue;

    // The semaphore list. Index gives corresponding semaphore
    vector<Semaphore> semaphores;

    // Initializing all semaphore with 1 resource
    for (int i = 0; i < 10; i++) semaphores.push_back(Semaphore(1));

    // The amount of each quantum
    int quantum = QUANTUM;

    // Current time
    int time = 0;

    // Prints out to console or file
    if (!CONSOLE) freopen(OUTPUT, "w", stdout);

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
                time += ins.time;
                used_quantum += ins.time;
                c_process.last_line++;

                // Check is it wait instruction
                if (ins.isWait()) {
                    int sem_no = ins.getSemaphore();
                    if(semaphores[sem_no].wait(c_process)) {
                        // Semaphore is already locked, pushed to waiting queue
                        is_waiting = true;
                        semaphores[sem_no].printOut(sem_no, time);
                    }
                }
                // Check is it signal instruction
                else if (ins.isSignal()){
                    int sem_no = ins.getSemaphore();
                    if(semaphores[sem_no].signal()) {
                        // There is process in waiting queue of signaled semaphore, transfer it to ready queue
                        readyQueue.push(semaphores[sem_no].getFromWait());
                        printReadyQueue(readyQueue, time);
                        semaphores[sem_no].printOut(sem_no, time);
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
