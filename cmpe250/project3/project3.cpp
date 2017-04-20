/*
Student Name: Mustafa Enes Cakir
Student Number: 2013400105
Project Number: #3
Operating System: Xubuntu VM on macOS Sierra
Compile Status: Done
Program Status: Done

*/

#include <iostream>
#include <queue>
#include <algorithm>
#include <fstream>
#include <iomanip>

using namespace std;

struct Node {
    // Time needed to complete the process
    double work;

    // Time that is process is completed
    double time;

    Node(){
        work = 0;
        time = 0;
    }

    Node(double work_, double time_){
        work = work_;
        time = time_;
    }
};

int main(int argc, char* argv[]) {
    if (argc != 3) {
        cout << "Run the code with the following command: ./project3 [input_file] [output_file]" << endl;
        return 1;
    }

    // N is number of vertices
    // M is number of edges
    int N, M;

    // Read vertices and edges
    fstream fin(argv[1]);
    fin >> N >> M;

    // Array that keeps indegrees of nodes
    int indegree[N];

    // Array that keeps nodes with their times
    Node nodes[N];

    // Graph that keeps vertices and edges
    vector< vector<int> > graph(N);

    // Make all indegrees to zero
    for(int i = 0; i < N; i++)
        indegree[i]=0;

    // Read processes and add them to nodes array
    for(int i = 0; i < N; i++) {
        double w;
        fin >> w;
        Node node(w, w);
        nodes[i] = node;
    }

    // Read edges and add to graph
    int from, to;
    for(int i = 0; i < M; i++) {
        fin >> from >> to;
        graph[from].push_back(to);
        indegree[to]++;
    }
    fin.close();

    queue<int> zero_list;
    for(int i=0; i < N; i++){
        if(indegree[i]==0){
            zero_list.push(i);
        }
    }

    int counter = 0;
    double max = 0;
    while(!zero_list.empty()){
        int from = zero_list.front();
        zero_list.pop();
        counter++;
        for(int i = 0; i < graph[from].size(); i++){
            // id of related process
            int to = graph[from][i];

            if(nodes[from].time + nodes[to].work > nodes[to].time)
                nodes[to].time = nodes[to].work + nodes[from].time;

            indegree[graph[from][i]]--;
            if(indegree[graph[from][i]] == 0){
                zero_list.push( graph[from][i] );
            }
        }
        // If time of process is bigger than max, it's last process
        if(nodes[from].time > max)
            max = nodes[from].time;
    }

    // Check if it has cycle
    if (counter < N) max = -1;

    freopen(argv[2], "w", stdout);
    cout << fixed << setprecision(6);
    cout << max;

    return 0;
}