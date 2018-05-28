/*
    Student Name: Mustafa Enes Cakir
    Student Number: 2013400105
    Project Number: 1
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
#define CONSOLE 1

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
    cout << time << "::HEAD-";
    while (!q.empty()) {
        // If it's is last one don't put `-`
        if (q.size() > 1) cout << q.front().name << "-";
        else cout << q.front().name;
        q.pop();
    }
    cout << "-TAIL" << endl;
}

int main(int argc, char *argv[]) {
    // It keeps arrived procces based on their arrival time. First one is first arrived one.
    priority_queue<Process, vector<Process>, arrivalComparator> processArrivals;

    // The ready queue. Processor takes it's first element in each quantum
    queue<Process> readyQueue;

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
            readyQueue.pop();
            // It keeps used time of the current quantum
            int used_quantum = 0;
            // Execute instructions until process is done or remaining quantum isn't enough
            while (!c_process.isFinished() && used_quantum < quantum) {
                Instruction ins = c_process.instructions[c_process.last_line];
                time += ins.time;
                used_quantum += ins.time;
                c_process.last_line++;
            }
            // If the new event is arrived, transfer it to ready queue
            // Put it before putting unfinished job again
            while (!processArrivals.empty() && processArrivals.top().time <= time) {
                readyQueue.push(processArrivals.top());
                processArrivals.pop();
            }
            // If the process is not finished, put it back to end of the ready queue
            if (!c_process.isFinished()) {
                readyQueue.push(c_process);
            }

        } else {
            // If no process is in ready queue, jump to next quantum
            time += quantum;
        }
    }
    // Print last status of the ready queue
    printReadyQueue(readyQueue, time);
    return 0;
}
