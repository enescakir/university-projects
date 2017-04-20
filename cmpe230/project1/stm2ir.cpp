/*
Student Name: Mustafa Enes ÇAKIR
Student Number: 2013400105
Project Number: 1
Compile Status: Done
Program Status: Done
*/
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <set>
#include <algorithm>

using namespace std;

/* ==== GLOBAL VARIABLES ==== */
// It's keep global count of temporary variables
int tempCount = 1;

// For error printing I don't want to pass current line number to each recursive function.
// So I keep it in global variable
int currentLine = 0;

// Input and output file
ifstream in;
ofstream out;

// Allocated variables
set<string> variables;

/* ==== METHOD DECLARATIONS ==== */
// Enter point of recursion.
// If it has '=' it apart it and put in to recursion
void parse(string exp);

/* HANDLING OPERATIONS -> recursivly. They call each other; top to bottom */
string handleAdd(string exp);
string handleSub(string exp);
string handleMul(string exp);
string handleDiv(string exp);
string handlePar(string exp);
string handleVariable(string exp);

/* VARIABLE HELPERS */
string allocateVarible(string var);
bool isValidVariable(string var);
bool isAllocatedVariable(string var);

/* ERROR HELPERS */
bool checkErrors(string line);
void abortError(string desc);

/* GENERAL HELPERS */
string trim(string str);
string left_trim(string str);
string right_trim(string str);
bool isNum(string str);
void checkEmptyStr(string str);


/* ==== METHOD IMPLEMENTATIONS ==== */
/**
 * It's take expression and aparts to parts
 * @param exp test to parse
 */
void parse(string exp){
  exp = trim(exp);
  int equalSign = exp.find('=');
  // If exp has =. It's a assigment. You need to store it.
  // else It's not assignment. So evulate expression and print it.
  if( equalSign != string::npos){
    string left = exp.substr(0, equalSign);
    string right = exp.substr(equalSign + 1, exp.length() - equalSign);
    if(isValidVariable(left)){
      allocateVarible(left);
      string result = handleAdd(right);
      out << "\tstore i32 " << result << ", i32* %" << left << endl;
    }
  } else {
    string var = handleAdd(exp);
    out << "\tcall i32 (i8*, ...)* @printf(i8* getelementptr ([4 x i8]* @print.str, i32 0, i32 0), i32 " << var << " )" << endl;
    tempCount++;
  }
}

/* HANDLING OPERATIONS */
/**
 * Splits string by “+” that not in parenthesis, and call again handleAdd(string) with two parts.
 * If all "+" are splited, It calls handleSub(exp)
 * @param  exp expression to process
 * @return  result of to evaluation
 */
string handleAdd(string exp){
  checkEmptyStr(exp);
  int parCount = 0;
  exp = trim(exp);
  for(int i = exp.length() - 1; i >= 0 ; i--){
    if(exp[i] == '(') parCount++;
    else if(exp[i] == ')') parCount--;
    if(parCount == 0 && exp[i] == '+'){
      string leftVar, rightVar;
      leftVar   = handleAdd(exp.substr(0, i));
      rightVar  = handleAdd(exp.substr(i+1, exp.length() - i - 1));
      string str = "%" + to_string(tempCount);
      out << "\t" << str << " = add i32 " << leftVar << ", " << rightVar << endl;
      tempCount++;
      return str;
    }
  }
  return handleSub(exp);
}

/**
 * Splits string by “-” that not in parenthesis, and call again handleSub(string) with two parts.
 * If all "-" are splited, It calls handleMul(exp)
 * @param  exp expression to process
 * @return  result of to evaluation
 */
string handleSub(string exp){
  checkEmptyStr(exp);
  int parCount = 0;
  exp = trim(exp);
  for(int i = exp.length() - 1; i >= 0 ; i--){
    if(exp[i] == '(') parCount++;
    else if(exp[i] == ')') parCount--;
    if(parCount == 0 && exp[i] == '-'){
      string leftVar, rightVar;
      leftVar   = handleSub(exp.substr(0, i));
      rightVar  = handleSub(exp.substr(i+1, exp.length() - i - 1));
      string str = "%" + to_string(tempCount);
      out << "\t" << str << " = sub i32 " << leftVar << ", " << rightVar << endl;
      tempCount++;
      return str;
    }
  }
  return handleMul(exp);
}


/**
 * Splits string by “*” that not in parenthesis, and call again handleMul(string) with two parts.
 * If all "*" are splited, It calls handleDiv(exp)
 * @param  exp expression to process
 * @return  result of to evaluation
 */
string handleMul(string exp){
  checkEmptyStr(exp);
  int parCount = 0;
  exp = trim(exp);
  for(int i = exp.length() - 1; i >= 0 ; i--){
    if(exp[i] == '(') parCount++;
    else if(exp[i] == ')') parCount--;
    if(parCount == 0 && exp[i] == '*'){
      string leftVar, rightVar;
      leftVar   = handleMul(exp.substr(0, i));
      rightVar  = handleMul(exp.substr(i+1, exp.length() - i - 1));
      string str = "%" + to_string(tempCount);
      out << "\t" << str << " = mul i32 " << leftVar << ", " << rightVar << endl;
      tempCount++;
      return str;
    }
  }
  return handleDiv(exp);
}

/**
 * Splits string by “/” that not in parenthesis, and call again handleDiv(string) with two parts.
 * If all "/" are splited, It calls handlePar(exp)
 * @param  exp expression to process
 * @return  result of to evaluation
 */
string handleDiv(string exp){
  checkEmptyStr(exp);
  int parCount = 0;
  exp = trim(exp);
  for(int i = exp.length() - 1; i >= 0 ; i--){
    if(exp[i] == '(') parCount++;
    else if(exp[i] == ')') parCount--;
    if(parCount == 0 && exp[i] == '/'){
      string leftVar, rightVar;
      leftVar   = handleDiv(exp.substr(0, i));
      rightVar  = handleDiv(exp.substr(i+1, exp.length() - i - 1));
      string str = "%" + to_string(tempCount);
      out << "\t" << str << " = sdiv i32 " << leftVar << ", " << rightVar << endl;
      tempCount++;
      return str;
    }
  }
  return handlePar(exp);
}

/**
 * Removes outer parenthesis and calls handleAdd method with inner part
 * @param  exp expression to process
 * @return  result of to evaluation
 */
string handlePar(string exp){
  checkEmptyStr(exp);
  exp = trim(exp);
  if(exp[0] == '(' && exp[exp.length()-1] == ')'){
    return handleAdd(exp.substr(1, exp.length()-2));
  } else {
    return handleVariable(exp);
  }
}

/**
 * Decides is exp variable or just a number.
 * @param  exp expression to decide
 * @return  new varible name
 */
string handleVariable(string exp){
  checkEmptyStr(exp);
  exp = trim(exp);
  if(exp.find('+') != string::npos || exp.find('-') != string::npos || exp.find('*') != string::npos || exp.find('/') != string::npos || exp.find('(') != string::npos || exp.find(')') != string::npos)
    return handleAdd(exp);
  else{
    if(isNum(exp))
      return exp;
    else {
      string str = "%" + to_string(tempCount);
      if(isValidVariable(exp) && isAllocatedVariable(exp)){
        out << "\t" << str << " = load i32* " << "%" << exp << endl;
        tempCount++;
        return str;
      }
      return str;
    }
  }
}

/* VARIBLE HELPERS */
/**
 * Allocates variable, prints and add to variables set
 * @param  var Cariable to allocate
 * @return Variable that allocated
 */
string allocateVarible(string var){
  var = trim(var);
  if(variables.find(var) == variables.end()){
    out << "\t" << "%" << var << " = alloca i32" << endl;
    variables.insert(var);
  }
  return var;
}

/**
 * Checks given text is valid varible name or not
 * @param  variable Variable name to check
 * @return true if it is valid.
 */
bool isValidVariable(string variable){
  bool isValid = true;
  variable = trim(variable);
  if(isalpha(variable[0]) == 0 && variable[0] != '_')
    isValid = false;

  for(int i = 1; i < variable.length(); i++)
    if(isalnum(variable[i]) == 0 || variable[i] == ' ')
      isValid = false;

  if(!isValid)
    abortError("\"" + variable + "\" is not valid varible name.");

  return isValid;
}

/**
 * Checks var is allocated.
 * @param  var variable to check allocation
 * @return status of var allocation
 */
bool isAllocatedVariable(string var){
  var = trim(var);
  for (string v : variables){
    if(v == var){
      return true;
    }
  }
  abortError("\"" + var + "\" is not allocated before this line.");
  return false;
}


/* ERROR HELPERS */
/**
 * Prints error line and description to console and abort to code.
 * @param  desc Description of error
 */
void abortError(string desc){
  cout << "Error: Line " << currentLine <<": " << desc << endl;
  exit (0);
}

/**
 * Checks basic syntax errors line by line. Such that, double equal signs, unmatched parenthesis
 * @param  line String to check
 * @return if there is an error abort, else retunr true
 */
bool checkErrors(string line){
  int parCount = 0;
  int assignCount = 0;
  for(int i = 0; i < line.length(); i++){
    if(line[i] == '(') parCount++;
    else if(line[i] == ')') parCount--;
    else if(line[i] == '=') assignCount++;
  }
  // Checks unmatched parenthesis
  if(parCount != 0){
    abortError("Parenthises count doesn't match.");
  }

  // Checks equal signs count
  if(assignCount > 1){
    abortError("Double equal sign in one line");
  }

  return true;
}

/* GENERAL HELPERS */
/**
 * Checks given string number or not
 * @param  str String to check numberity
 * @return if it is number returns true
 */
bool isNum(string str){
  for(int i = 0 ; i < str.length(); i++)
    if(str[i] > '9' || str[i] < '0')
      return false;

  return true;
}

/**
 * Checks string is empty
 * @param str If string is empty abort, else return true
 */
void checkEmptyStr(string str){
  str = trim(str);
  if(str.length() == 0){
    abortError("Empty string");
  }
}

/**
 * Removes spaces from begin and end of string
 */
string trim(string str) {
    return right_trim(left_trim(str));
}

string left_trim(string str) {
    int numStartSpaces = 0;
    for (int i = 0; i < str.length(); i++) {
        if (!isspace(str[i])) break;
        numStartSpaces++;
    }
    return str.substr(numStartSpaces);
}

string right_trim(string str) {
    int numEndSpaces = 0;
    for (int i = str.length() - 1; i >= 0; i--) {
        if (!isspace(str[i])) break;
        numEndSpaces++;
    }
    return str.substr(0, str.length() - numEndSpaces);
}


/* ==== MAIN METHOD ==== */
int main(int argc, char* argv[]) {
  // 2 arguments are needed for running script
  if (argc != 2) {
    cout << "Run the code with the following command: ./stm2ir [input_file]" << endl;
    return 1;
  }

  // Open input file
  string input_name = argv[1];
  in.open(input_name);

  // Create output filename
  int dot = input_name.length();
  for(int i = 0; i < input_name.length(); i++)
    if(input_name[i] == '.')
      dot = i;
  string output_name = input_name.substr(0, dot) + ".ll";
  out.open(output_name);

  // It keeps all lines
  vector<string> lines;

  // Current line to read
  string line;

  // Read all lines and push to lines vector
  while(getline(in, line))
    lines.push_back(line);

  // Check basic errors for each line
  for(int i=0; i < lines.size(); i++){
    currentLine++;
    checkErrors(lines[i]);
  }

  // Print beginning part of IR file
  out << "; ModuleID = 'stm2ir'" << endl;
  out << "declare i32 @printf(i8*, ...)" << endl;
  out << "@print.str = constant [4 x i8] c\"%d\\0A\\00\"" << endl << endl;
  out << "define i32 @main() {" << endl;

  // Parse line by line
  currentLine = 0;
  for(int i=0; i < lines.size(); i++){
    currentLine++;
    parse(lines[i]);
  }

  // Print ending part of IR file
  out << "\tret i32 0" << endl;
  out << "}";

  return 0;
}
