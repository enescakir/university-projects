#ifndef NODE_H
#define NODE_H

#include <cstddef>
#include <iostream>
class Node{
public:
  int data;
  Node* next = 0;

  Node(int data);
  Node(const Node& node);
  Node& operator=(const Node &node);
  Node operator+(const Node& node);
  Node operator-(const Node& node);
  ~Node(); 
};

#endif
