#ifndef LinkedList_H
#define LinkedList_H

#include <iostream>
#include "Node.h"

class LinkedList {
public:
    int length;
    Node *head = 0;
    Node *tail = 0;

    LinkedList();

    LinkedList(const LinkedList &list);

    LinkedList &operator=(const LinkedList &list);

    void pushTail(int data);

    void pushHead(int data);

    LinkedList operator+(const LinkedList &list);

    ~LinkedList();

public:
    friend std::ostream &operator<<(std::ostream &out, const LinkedList &list);
};

#endif
