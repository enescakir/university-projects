/*
Student Name: Mustafa Enes Cakir
Student Number: 2013400105
Project Number: 2
Operating System: Xubuntu VM on macOS Sierra
Compile Status: Done
Program Status: Working
Notes:
*/

#include<iostream>
#include <algorithm>
#include<queue>
#include<thread>
#include <fstream>
#include <iomanip>

/*
   In output file ID's starts with 1. But in log files there are start with 0.
   For checking log file make it 0.
*/
#define ID_SHIFT 1

/*
    If you make LOGGING 1, it prints out logs file likes example testcases logs.
*/
#define LOGGING 0

using namespace std;

// Event struct keeps general information of events.
struct Event{
    int id;
    string type;
    double time;
    double cpu_work;
    double io_work;
    double arr_time;
    double dep_time;
    // The time that event was pushed to queue
    double queue_time = 0;
    double total_waiting_time = 0;
    int device_id = -1;

    Event(int id_, string type_, double time_, double cpu_work_, double io_work_){
        id = id_;
        type = type_;
        time = time_;
        arr_time = time_;
        cpu_work = cpu_work_;
        io_work = io_work_;
    }
};

// Process struct keeps statistics of events
struct Process{
    int id;
    double arr_time = 0;
    double dep_time = 0;
    double total_waiting_time = 0;
    Process(int id_, double arr_){
        id = id_;
        arr_time = arr_;
    }

};

struct CPU{
    int id;
    int speed;
    bool isIdle = true;
    double active_time = 0;

    CPU(int id_, int speed_){
        id = id_;
        speed = speed_;
    }
};

struct IO{
    int id;
    int quantum;
    bool isIdle = true;
    double active_time = 0;

    IO(int id_, int quantum_){
        id = id_;
        quantum = quantum_;
    }
};

struct eventHeapComparator{
    bool operator()(Event e1, Event e2){
        return e1.time > e2.time;
    }
};

struct cpuHeapComparator{
    bool operator()(Event e1, Event e2){
        return e1.cpu_work > e2.cpu_work;
    }
};

int main(int argc, char* argv[]){

    if (argc != 3) {
        cout << "Run the code with the following command: ./project2 [input_file] [output_file]" << endl;
        return 1;
    }

    if (LOGGING){
        cout << "input file: " << argv[1] << endl;
        cout << "output file: " << argv[2] << endl;
        cout << "== LOGGING MODE ENABLED. CHECK OUTPUT FILE. == " << endl;
    }

    freopen(argv[1], "r", stdin);
    freopen(argv[2], "w", stdout);

    int numsOfCPU, numsOfIO, numsOfEvent;

    vector<CPU> CPUs;
    vector<IO> IOs;
    vector<Process> processes;

    // Read all CPUs
    cin >> numsOfCPU;
    for (int i = 0; i < numsOfCPU; i++) {
        int speed;
        cin >> speed;
        CPU cpu(i + ID_SHIFT, speed);
        CPUs.push_back(cpu);
    }

    // Read all IO Units
    cin >> numsOfIO;
    for (int i = 0; i < numsOfIO; i++) {
        int quantum;
        cin >> quantum;
        IO io(i + ID_SHIFT, quantum);
        IOs.push_back(io);
    }

    cin >> numsOfEvent;
    priority_queue<Event, vector<Event>, eventHeapComparator> events;
    priority_queue<Event, vector<Event>, cpuHeapComparator> cpuWaits;
    queue<Event> ioWaits;

    // Read all Events
    for (int i = 0; i < numsOfEvent; i++) {
        double arr, cpu, io;
        cin >> arr >> cpu >> io;
        Event e(i + ID_SHIFT, "cpu_arrival", arr, cpu, io);
        events.push(e);
        Process p(i + ID_SHIFT, arr);
        processes.push_back(p);
    }

    double now = 0;
    int maxFirstPQ = 0, maxSecondPQ = 0;

    while (!events.empty()) {
        Event current = events.top();
        events.pop();
        now = current.time;
        if (LOGGING) {
            cout << "T:" << now << endl;
        }
        if (current.type == "cpu_arrival") {
            for (int i = 0; i < numsOfCPU; i++) {
                if (CPUs[i].isIdle) {
                    current.type = "cpu_departure";
                    current.time = now + (current.cpu_work / CPUs[i].speed);
                    current.device_id = CPUs[i].id;
                    current.total_waiting_time = now - current.arr_time;
                    processes[current.id - ID_SHIFT].total_waiting_time = current.total_waiting_time;
                    CPUs[i].isIdle = false;
                    CPUs[i].active_time += (current.cpu_work / CPUs[i].speed);
                    events.push(current);
                    if (LOGGING){
                        cout << "\tprocess#" << current.id << " goes to CPU#" << CPUs[i].id << " after waiting " << current.total_waiting_time << " seconds" << endl;
                        cout << "\tCPU departure event set at T=" << current.time << "" << endl;
                    }
                    break;
                }
            }
            if (current.device_id == -1){
                if (LOGGING){
                    cout << "\tprocess#" << current.id << " goes to first level queue" << endl;
                }
                cpuWaits.push(current);
            }
        }
        else if (current.type == "cpu_departure") {
            if (LOGGING){
                cout << "\tprocess#" << current.id << " is done at CPU#" << current.device_id << endl;
            }
            current.type = "io_arrival";
            CPUs[current.device_id - ID_SHIFT].isIdle = true;
            current.device_id = -1;
            current.queue_time = now;
            events.push(current);
            if( !cpuWaits.empty()){
                current = cpuWaits.top();
                cpuWaits.pop();
                current.time = now;
                events.push(current);
            }
        }
        else if (current.type == "io_arrival") {
            for (int i = 0; i < numsOfIO; i++) {
                if (IOs[i].isIdle) {
                    current.type = "io_departure";
                    current.time = now + (min( double(IOs[i].quantum), current.io_work ));
                    current.device_id = IOs[i].id;
                    IOs[i].isIdle = false;
                    IOs[i].active_time += (min( double(IOs[i].quantum), current.io_work ));
                    current.io_work -= IOs[i].quantum;
                    if (current.queue_time > 0){
                        current.total_waiting_time += ( now - current.queue_time);
                        processes[current.id - ID_SHIFT].total_waiting_time = current.total_waiting_time;
                    }
                    if (LOGGING){
                        cout << "\tprocess#" << current.id << " goes to IO_Unit#" << IOs[i].id << " after waiting " << now - current.queue_time << " seconds in the queue and " << current.total_waiting_time << " in total"<< endl;
                        cout << "\tIO Unit departure event set at T=" << current.time << "" << endl;
                    }
                    current.queue_time = 0;
                    events.push(current);
                    break;
                }
            }
            if (current.device_id == -1){
                if (LOGGING){
                    cout << "\tprocess#" << current.id << " goes to second level queue" << endl;
                }
                current.queue_time = now;
                ioWaits.push(current);
            }
        }
        else if (current.type == "io_departure") {
            if( !ioWaits.empty()){
                Event c = ioWaits.front();
                ioWaits.pop();
                c.time = now;
                events.push(c);
            }

            if (LOGGING){
                cout << "\tprocess#" << current.id << " is done at IO_Unit#" << current.device_id << endl;
            }
            IOs[current.device_id - ID_SHIFT].isIdle = true;
            current.device_id = -1;
            current.dep_time = now;
            processes[current.id - ID_SHIFT].dep_time = now;
            if( current.io_work > 0){
                current.type = "io_arrival";
                events.push(current);
            }
        }
        if (LOGGING) {
            cout << endl;
        }

        // Check sizes of the queues. If they are bigger than maxs change them.
        if( cpuWaits.size() > maxFirstPQ )
            maxFirstPQ = cpuWaits.size();

        if( ioWaits.size() > maxSecondPQ )
            maxSecondPQ = ioWaits.size();
    }

    double sumOfWaits = 0, maxWait = 0, sumOfTotal = 0, maxCPUtime = 0, maxIOtime = 0;
    int maxCPUid = 0, maxIOid = 0;

    // Find CPU that work the most
    for (int i = 0; i < numsOfCPU; i++) {
        if( CPUs[i].active_time > maxCPUtime){
            maxCPUtime = CPUs[i].active_time;
            maxCPUid = i + ID_SHIFT;
        }
    }

    // Find IO Unit that work the most
    for (int i = 0; i < numsOfIO; i++) {
        if( IOs[i].active_time > maxIOtime){
            maxIOtime = IOs[i].active_time;
            maxIOid = i + ID_SHIFT;
        }
    }


    for (int i = 0; i < processes.size(); i++) {
        // Sum all of the waiting times.
        sumOfWaits += processes[i].total_waiting_time;

        // Sum all of times that proccess in the system
        sumOfTotal += (processes[i].dep_time - processes[i].arr_time);

        // Find max waiting time
        if ( processes[i].total_waiting_time > maxWait)
            maxWait = processes[i].total_waiting_time;
    }

    cout << fixed << setprecision(6);
    cout << maxFirstPQ << endl;
    cout << maxSecondPQ << endl;
    cout << maxCPUid << endl;
    cout << maxIOid << endl;
    cout << sumOfWaits / numsOfEvent << endl;
    cout << maxWait << endl;
    cout << sumOfTotal / numsOfEvent << endl;

    return 0;
}