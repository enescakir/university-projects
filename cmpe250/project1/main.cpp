#include <iostream>
#include <sstream>
#include <algorithm>
#include <fstream>
#include <string>
#include "BigInteger.h"

using namespace std;

// you may need to modify function if you store more than one digit in Node.data
ostream& operator<<(ostream& out, const Node& node) {
    Node n2 = node;
    Node* p = &n2;
    out << p->data << " ";
    while(p->next) {
        p = p->next;
        out << p->data << " ";
    }
    return out;
}
// you may need to modify function if you store more than one digit in Node.data
ostream &operator<<(ostream &out, const BigInteger &bigInteger)  {
    string str = "";
    Node *head = bigInteger.num->head;
    while (head) {
        str += to_string(head->data);
        head = head->next;
    }
    reverse(str.begin(), str.end());
    if (str == "")
        str = "0";
    out << str;
    return out;
}


int main (int argc, char* argv[]) {
    // below reads the input file
    // in your next projects, you will implement that part as well

    if (argc != 3) {
        cout << "Run the code with the following command: ./project1 [input_file] [output_file]" << endl;
        return 1;
    }

    cout << "input file: " << argv[1] << endl;
    cout << "output file: " << argv[2] << endl;

    ifstream infile(argv[1]);
    string line;
    vector<string> input;
    while (getline(infile, line)) {
        istringstream iss(line);
        string str;
        iss >> str;
        input.push_back(str);
    }

    cout << "input file has been read" << endl;
    cout << "\toperand: " << input[0] << endl;
    cout << "\tnumber1: " << input[1] << endl;
    cout << "\tnumber2: " << input[2] << endl;


    // your code must do the following
    BigInteger int1(input[1]);
    BigInteger int2(input[2]);

    BigInteger result;
    if (input[0] == "+") {
      result = int1 + int2;
    }
    else {
       result = int1 * int2;
     }

    cout << "your result: " << result << endl;
    ofstream outfile(argv[2]);
    outfile << result;
    outfile.close();
    // here, perform the output operation. in other words,
    // print your results into the file named <argv[2]>

    return 0;
}