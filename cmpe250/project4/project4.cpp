/*
Student Name: Mustafa Enes Cakir
Student Number: 2013400105
Project Number: #4
Operating System: Xubuntu VM on macOS Sierra
Compile Status: Done
Program Status: Working
*/

#include <iostream>
#include <algorithm>
#include <fstream>
#include<queue>

using namespace std;

class DisjointSet{
public:
    long SIZE;
    long* arr;
    DisjointSet(long size){
        SIZE = size;
        arr = new long[SIZE];

        for (long i = 0; i < SIZE; ++i) arr[i] = -1;
    }

    long find(long set){
        if(arr[set] < 0) return set;
        else return find(arr[set]);
    }

    void Union(long set1, long set2, int weight, long& result, bool* selects){
        if(selects[set1] && selects[set2]) result += weight; // If both selected, don't union them. This edge will be cut.
        else {
            if(selects[set1]) arr[set2] = set1; // If set1 is selected make it root
            else if(selects[set2]) arr[set1] = set2; // If set2 is selected make it root
            else arr[set1] = set2; // If none of them selected, select root arbitrarily
        }
    }
};

struct Edge {
    long n1;
    long n2;
    int w;
    Edge(long n1_, long n2_, int w_) {
        n1 = n1_;
        n2 = n2_;
        w = w_;
    }
};

struct edgeHeapComparator{
    bool operator()(Edge e1, Edge e2){
        return e1.w < e2.w;
    }
};

int main(int argc, char* argv[]) {
    if (argc != 3) {
        cout << "Run the code with the following command: ./project4 [input_file] [output_file]" << endl;
        return 1;
    }

    long nCities, nSelects;
    fstream fin(argv[1]);
    fin >> nCities >> nSelects; // Read number of cities and selects

    priority_queue<Edge, vector<Edge>, edgeHeapComparator> edges; // It keeps edges
    bool* selects = new bool[nCities]; // It keeps selected cities. If it's true, it's selected

    // Read edges and push them in PQ
    for(long i=0; i < nCities - 1; i++) {
        long n1, n2; int w;
        fin >> n1 >> n2 >> w;
        edges.push(Edge(n1,n2,w));
    }

    // Make all array false
    for(long i=0; i < nCities; i++) selects[i] = 0;

    // Read selects
    for(long i=0; i < nSelects; i++) {
        long selected;
        fin >> selected;
        selects[selected] = true;
    }
    fin.close();

    long result = 0;
    DisjointSet ds(nCities);
    int c = 0;
    // Pop all edges from PQ, and union it's vertices
    while (c < nCities - 1) {
        Edge e = edges.top(); edges.pop();
        int root1 = ds.find(e.n1);
        int root2 = ds.find(e.n2);
        if (root1 != root2 ){
            ds.Union(root1,root2, e.w, result, selects);
            c++;
        }
    }

    // Write result to file
    ofstream fout(argv[2]);
    fout << result;
    fout.close();
    return 0;
}