/*
Student Name: Mustafa Enes ÇAKIR
Student Number: 2013400105
Project Number: 1
Operating System: Virtual Machine on macOS Sierra
Compile Status: Done
Program Status: Done
Notes: Anything you want to say about your code that will be helpful in the grading process.

*/

#include "BigInteger.h"
#include <algorithm>


BigInteger::BigInteger(int number) {
    this->num = new LinkedList();
    // Push digits of the number one by one.
    while (number > 0) {
        this->num->pushTail(number % 10);
        number /= 10;
    }
}

BigInteger::BigInteger(const string &bigInteger) {
    this->num = new LinkedList();
    // Iterate characters of string the begin to end, one by one; and push them to head of LinkedList
    for (int i = 0; i < bigInteger.length(); i++)
        this->num->pushHead(bigInteger[i] - '0');
}

BigInteger::BigInteger(const BigInteger &other) {
    if (this->num) delete this->num;
    this->num = 0;
    // Allocate new memory space for new list
    this->num = new LinkedList(*other.num);
}

BigInteger BigInteger::operator+(const BigInteger &list) {
    LinkedList *resultList = new LinkedList();
    Node *ptr1 = this->num->head;
    Node *ptr2 = list.num->head;
    int remainder = 0;
    while (ptr1 != NULL && ptr2 != NULL) {
        int sum = ptr1->data + ptr2->data + remainder;
        resultList->pushTail(sum % 10);
        // if sum of 2 digit is bigger than 10, remainder for next digit is 1
        if (sum >= 10) remainder = 1;
        else remainder = 0;
        ptr1 = ptr1->next;
        ptr2 = ptr2->next;
    }

    Node *bigger = 0;
    if (ptr1 != NULL) bigger = ptr1;
    else if (ptr2 != NULL) bigger = ptr2;
    while (bigger != NULL) {
        if ((bigger->data + remainder) < 10) {
            resultList->pushTail(bigger->data + remainder);
            remainder = 0;
        } else {
            resultList->pushTail((bigger->data + remainder) % 10);
            remainder = 1;
        }
        bigger = bigger->next;
    }
    // if we have still reminder, push new digit.
    if (remainder != 0) resultList->pushTail(1);
    BigInteger result;
    result.num = resultList;
    return result;
}

BigInteger BigInteger::operator*(const BigInteger &list) {
    BigInteger totalResult;
    int depth = 0;
    Node *ptr = list.num->head;
    while (ptr != NULL) {
        if (ptr->data != 0) {
            //Multiply depth'th digit of second BigInteger with this BigInteger.
            BigInteger sum(*this * ptr->data);
            //Push zeros as many number of digits from right to list
            for (int j = 0; j < depth; j++)
                sum.num->pushHead(0);
            //Add result of each multiplication to total result.
            totalResult = totalResult + sum;
        }
        ptr = ptr->next;
        depth++;
    }
    return totalResult;
}

BigInteger BigInteger::operator*(int i) {
    BigInteger totalResult;
    int depth = 0;
    Node *ptr1 = this->num->head;
    while (ptr1 != NULL) {
        if (ptr1->data != 0 && i != 0) {
            //Multiply depth'th digit of this BigInteger with i
            BigInteger mult(ptr1->data * i);
            //Push zeros as many number of digits from right to list
            for (int j = 0; j < depth; j++)
                mult.num->pushHead(0);
            //Add result of each multiplication to total result.
            totalResult = totalResult + mult;
        }
        ptr1 = ptr1->next;
        depth++;
    }
    return totalResult;
}

BigInteger &BigInteger::operator=(const BigInteger &list) {
    if (this->num) delete this->num;
    this->num = 0;
    //Allocate new memory space for new list.
    this->num = new LinkedList(*list.num);
    return *this;
}

BigInteger::~BigInteger() {
    if (num) delete num;
}