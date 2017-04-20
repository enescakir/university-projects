/*
Student Name: Mustafa Enes Cakir
Student Number: 2013400105
Project Number: #5
Operating System: Xubuntu VM on macOS Sierra
Compile Status: Done
Program Status: Working

*/

#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
#include <set>

using namespace std;

struct Edge{
    int v1;
    int v2;
    int w;
    Edge(int v1, int v2, int w){
        this->v1 = v1;
        this->v2 = v2;
        this->w = w;
    }
};

struct Vertex{
    vector<Edge> adj;
    int distance = INT32_MAX;
    int heuristic = 0;
};

int main(int argc, char* argv[]) {
    if (argc != 3) {
        cout << "Run the code with the following command: ./project5 [input_file] [output_file]" << endl;
        return 1;
    }

    freopen(argv[1], "r", stdin);
    freopen(argv[2], "w", stdout);

    int nCities, nRoads;
    scanf("%d%d",&nCities,&nRoads);
    vector<Vertex> graph;

    for(int j = 0; j < nCities; j++){
        Vertex v1;
        int heuristic;
        scanf("%d",&heuristic);
        v1.heuristic = heuristic;
        graph.push_back(v1);
    }

    for(int j = 0; j < nRoads; j++){
        int v1,v2,weight;
        scanf("%d%d%d",&v1,&v2,&weight);
        graph[v1].adj.push_back(Edge(v1,v2,weight));
        graph[v2].adj.push_back(Edge(v2,v1,weight));
    }

    int startCity, endCity;
    scanf("%d%d",&startCity,&endCity);

    // A* Algorithm
    graph[startCity].distance = 0;
    set<pair<int,int>> pairs;
    pairs.insert(make_pair(graph[startCity].heuristic,startCity));

    while(pairs.size() > 0){
        int city = pairs.begin()->second;
        int dist = pairs.begin()->first;
        pairs.erase(pairs.begin());

        dist = dist - graph[city].heuristic;

        if(city == endCity) break;
        if(dist > graph[city].distance) continue;

        for(int i = 0; i < graph[city].adj.size();i++){
            Edge temp = graph[city].adj[i];
            if(dist + temp.w < graph[temp.v2].distance ){
                graph[temp.v2].distance = dist + temp.w;
                pairs.insert(make_pair(dist + temp.w + graph[temp.v2].heuristic,temp.v2));
            }
        }
    }
    cout << graph[endCity].distance;
    return 0;
}