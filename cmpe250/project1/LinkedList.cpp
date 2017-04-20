#include "LinkedList.h"

LinkedList::LinkedList() {
    this->length = 0;
}

void LinkedList::pushTail(int data) {
    this->length++;
    if (!head) {
        head = new Node(data);
        tail = head;
        return;
    }
    this->tail->next = new Node(data);
    this->tail = tail->next;
}

void LinkedList::pushHead(int data) {
    this->length++;
    Node *newHead = new Node(data);
    newHead->next = this->head;
    if (head == NULL) { tail = newHead; }
    this->head = newHead;
}

LinkedList::LinkedList(const LinkedList &list) {
    Node *temp = list.head;
    while (temp != NULL) {
        pushTail(temp->data);
        temp = temp->next;
    }
}


LinkedList LinkedList::operator+(const LinkedList& list){
    LinkedList ret;
    Node* ptr1 = this->head;
    Node* ptr2 = list.head;
    while (ptr1!=NULL && ptr2!=NULL) {
        ret.pushTail(ptr1->data + ptr2->data);
        ptr1 = ptr1->next;
        ptr2 = ptr2->next;
    }
    Node* bigger = 0;
    if (ptr1!=NULL) bigger = ptr1;
    else if (ptr2!=NULL) bigger = ptr2;
    while (bigger!=NULL) {
        ret.pushTail(bigger->data);
        bigger = bigger->next;
    }
    return ret;
}


LinkedList &LinkedList::operator=(const LinkedList &list) {
    if(this->head) delete this->head;
    this->head = 0;
    this->tail = 0;
    this->length = 0;
    Node *temp = list.head;
    while (temp != NULL) {
        pushTail(temp->data);
        temp = temp->next;
    }
    return *this;
}

std::ostream &operator<<(std::ostream &out, const LinkedList &list) {
    Node *head = list.head;
    while (head) {
        out << head->data << " ";
        head = head->next;
    }
    out << std::endl;
    return out;
}

LinkedList::~LinkedList() {
    if(head) delete head;
}
