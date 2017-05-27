#ifndef HEXATOR_H
#define HEXATOR_H

#include <QWidget>
#include <QPushButton>
#include <QLineEdit>

class QLineEdit;
class QPushButton;

enum Mode { NotSet, Addition, Subtraction };

class Hexator : public QWidget
{
    Q_OBJECT

public:
    Hexator(QWidget *parent = 0);

private slots:
   void changeModeToAdd();
   void changeModeToSub();
   void pressDigit();
   void clear();
   void calculate();

private:
   QLineEdit *display;
   QPushButton *addButton;
   QPushButton *subButton;
   QPushButton *assignButton;
   QPushButton *clearButton;
   QPushButton *digitButtons[16];

   QPushButton *createButton(const QString &text, const char *member, bool isOperand = false, bool isChar = false);

   void customizeDisplay();
   void setDisplayNumber(long number);
   long getNumberFromDisplay();

   Mode currentMode;
   long savedNum;
   bool lastButtonWasMode;

};

#endif // HEXATOR_H
