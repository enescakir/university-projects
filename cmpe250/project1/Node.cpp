#include "Node.h"
#include <stack>

Node::Node(int data){
    this->data = data;
    this->next = NULL;
}

Node::Node(const Node& node){
    this->data = node.data;
    if(node.next) {
      this->next = new Node(*(node.next));
    }
}

Node& Node::operator=(const Node &node){
    this->data = node.data;
    if(node.next) {
      delete this->next;
      this->next = new Node(*(node.next));
    }
    return *this;
}

Node Node::operator+(const Node& node){
    Node ret(0);
    ret.data = this->data + node.data;
    return ret;
}
Node Node::operator-(const Node& node){
    Node ret(0);
    ret.data = this->data - node.data;
    return ret;
}

Node::~Node(){
    if (next) {
        delete next;
    }
}
