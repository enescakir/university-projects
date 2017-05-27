// Names: Mustafa Enes Çakır & Dilruba Reyyan Kılıç
// Student Numbers: 2013400105 & 2014400075
// Compile Status: Yes
// Complete: Yes

#include "hexator.h"
#include <QtGui>
#include <QLabel>
#include <QGridLayout>
#include <QSpacerItem>

Hexator::Hexator(QWidget *parent) : QWidget(parent)
{
    // Creates calculator display.
    display = new QLineEdit("0");
    customizeDisplay();

    // Creates operation buttons, connects them to their functions.
    // Operation Buttons: +  -  =  Clr
    addButton = createButton(tr("+"), SLOT(changeModeToAdd()), true);
    subButton = createButton(tr("-"), SLOT(changeModeToSub()), true);
    assignButton = createButton(tr("="), SLOT(calculate()), true);
    clearButton = createButton(tr("Clr"), SLOT(clear()), true);

    // Creates first 10 digit buttons and stores them in an array called digitButtons[]
    // Digit Buttons: 0  1  2  3  4  5  6  7  8  9
    for (int i = 0; i < 10; i++)
        digitButtons[i] = createButton(QString::number(i), SLOT( pressDigit() ));

    // Creates last 6 hexadecimal digit buttons and adds them to digitButtons[]
    // Digit  Buttons: A  B  C  D  E  F    char ascii = 0;
    for (int i = 10; i < 16; i++){
        char ascii = i + 55;
        digitButtons[i] = createButton(QString(ascii), SLOT( pressDigit() ), false , true);
    }

    // Customizing // MAIN LAYOUT <Vertical>
    // |__ display
    // |
    // |__ OPERATIONS LAYOUT <Horizontal>
    // |   |__ addButton (+)
    // |   |__ subButton (-)
    // |   |__ assignButton (=)
    // |   |__ clearButton (Clr)
    // |
    // |__ DIGIT ROW 0 <Horizontal>
    // |   |__ digitButtons[0]
    // |   |__ digitButtons[1]
    // |   |__ digitButtons[2]
    // |   |__ digitButtons[3]
    // |
    // |__ DIGIT ROW 1 <Horizontal>
    // |   |__ digitButtons[4]
    // |   |__ digitButtons[5]
    // |   |__ digitButtons[6]
    // |   |__ digitButtons[7]
    // |
    // |__ DIGIT ROW 2 <Horizontal>
    // |   |__ digitButtons[8]
    // |   |__ digitButtons[9]
    // |   |__ digitButtons[10] (A)
    // |   |__ digitButtons[11] (B)
    // |
    // |__ DIGIT ROW 3 <Horizontal>
    //     |__ digitButtons[12] (C)
    //     |__ digitButtons[13] (D)
    //     |__ digitButtons[14] (E)
    //     |__ digitButtons[15] (F)
    //
    //
    // This part creates the layouts for the application and sets the items
    // in the order which is given above.

    QVBoxLayout *mainLayout = new QVBoxLayout;
    QHBoxLayout *operationsLayout = new QHBoxLayout;
    QHBoxLayout *digitsRows[4];
    mainLayout->setSizeConstraint(QLayout::SetFixedSize);


    // Adds the display of the calculator
    mainLayout->addWidget(display);

    // Sets the operations row and adds the operands.
    mainLayout->addLayout(operationsLayout);
    operationsLayout->addWidget(addButton);
    operationsLayout->addWidget(subButton);
    operationsLayout->addWidget(assignButton);
    operationsLayout->addWidget(clearButton);
    mainLayout->setSpacing(1);
    mainLayout->setContentsMargins(0,0,0,0);
    mainLayout->setMargin(0);

    // Sets the digit rows to the layout and then adds the digit buttons in every respectively.
    for (int i = 0; i < 4; i++){
        digitsRows[i] = new QHBoxLayout;
        for (int j = 0; j < 4; j++)
            digitsRows[i]->addWidget(digitButtons[(i*4)+j]);
    }

    for (int i = 0; i < 4; i++)
        mainLayout->addLayout(digitsRows[i]);

    // Final touches done and initializing the calulator with clear()
    setLayout(mainLayout);
    setWindowTitle(tr("Hexator"));
    clear();
}

// -------------------------createButton(QString, char, bool, bool)-------------------------------------
// This method is to simplify creating all buttons and prevent redundant code.
// Also it customizes the buttons according to their types.
// Shapes, colors, backgrounds, fonts, widhts and heights are set to buttons.
// Returns the QPushButton.
QPushButton *Hexator::createButton(const QString &text, const char *member, bool isOperand, bool isChar)
{
    // Button is created here.
    QPushButton *button = new QPushButton(text);
    connect(button, SIGNAL(clicked()), this, member);

    // Button customization starts here.
    button->setFlat(true);
    button->setAutoFillBackground(true);
    button->setFixedWidth(75);
    button->setFixedHeight(75);

    QPalette palette = button->palette();
    if(isOperand){
        palette.setColor(QPalette::Button, QColor(150, 150, 150));
        palette.setColor(QPalette::ButtonText, QColor(75, 75, 75));
    }
    else if(isChar){
        palette.setColor(QPalette::Button, QColor(70, 33, 70));
        palette.setColor(QPalette::ButtonText, QColor(255, 255, 255));
    }
    else {
        palette.setColor(QPalette::Button, QColor(100, 43, 100));
        palette.setColor(QPalette::ButtonText, QColor(255, 255, 255));
    }

    button->setPalette(palette);
    QFont font = button->font();
    font.setPointSize(32);
    button->setFont(font);
    return button;
}

// -------------------------changeModeToAdd()-----------------------------------------------------------
// This method is called when addbutton (+) is pressed.
// Stores the value written in the display to use it in the calculation later.
// Changes the mode to Addition and lastButtonWasMode to true.
void Hexator::changeModeToAdd(){
    savedNum =  getNumberFromDisplay();
    lastButtonWasMode = true;
    currentMode = Addition;
}


// -------------------------changeModeToSub()-----------------------------------------------------------
// This method is called when subbutton (-) is pressed.
// Stores the value written in the display to use it in the calculation later.
// Changes the mode to Subtraction and lastButtonWasMode to true.
void Hexator::changeModeToSub(){
    savedNum = getNumberFromDisplay();
    lastButtonWasMode = true;
    currentMode = Subtraction;
}

// -------------------------calculate()-----------------------------------------------------------------
// This method is called when a operation button is pressed and a number is written to display.
// Stores the displayed value as second number and makes the calculations according to their operation
// Displayes the result on the screen.
void Hexator::calculate(){
    long secondNum = getNumberFromDisplay();
    long result = 0;
    if(currentMode == Addition && !lastButtonWasMode){
        result = savedNum + secondNum;
        setDisplayNumber(result);
        currentMode = NotSet;
        savedNum = result;
    }
    else if(currentMode == Subtraction && !lastButtonWasMode){
        result = savedNum - secondNum;
        setDisplayNumber(result);
        currentMode = NotSet;
        savedNum = result;
    }
    lastButtonWasMode = true;
}

// -------------------------clear()---------------------------------------------------------------------
// This method clears the display screen and sets all values to initial.
void Hexator::clear(){
    display->setText(tr("0"));
    currentMode = NotSet;
    savedNum = 0;
    lastButtonWasMode = false;
}

// -------------------------pressDigit()----------------------------------------------------------------
// This method is called when a digit button is pressed and it displays the pressed number.
void Hexator::pressDigit(){
    QPushButton *pressedButton = qobject_cast<QPushButton *>(sender());
    QString digitValue = pressedButton->text();
    QString currentValue = display->text();

    if(currentValue == "0" || lastButtonWasMode)
        currentValue = QString("");

    lastButtonWasMode = false;
    display->setText(currentValue + digitValue);
}

// -------------------------customizeDisplay()----------------------------------------------------------
// This method is to customize the display of the calculator.
void Hexator::customizeDisplay(){
    display->setAlignment(Qt::AlignRight);
    display->setReadOnly(true);
    display->setMaxLength(10);
    display->setFixedHeight(70);
    display->setFixedWidth(303);
    QFont font = display->font();
    font.setPointSize(48);
    display->setFont(font);

    QPalette palette = display->palette();
    display->setAutoFillBackground(true);
    palette.setColor(QPalette::Base, QColor(254, 204, 52));
    palette.setColor(QPalette::Text, QColor(255, 255 ,255));

    display->setPalette(palette);
}

// -------------------------setDisplayNumber()----------------------------------------------------------
// This method is to print the given value to the display.
// Negative numbers are also handled here.
void Hexator::setDisplayNumber(long number){
    if(number < 0){
        number = number * -1;
        display->setText("-" + QString::number(number, 16).toUpper());
    } else {
        display->setText(QString::number(number, 16).toUpper());
    }
}

// -------------------------getNumberFromDisplay()----------------------------------------------------------
// This method is to get and evaluate the number pressed in the display.
// Returns the entered final number (long).
long Hexator::getNumberFromDisplay(){
    QString currentValue = display->text();
    long number = 0;
    bool ok;
    if (currentValue[0] == '-'){
        currentValue = currentValue.mid(1);
        number = currentValue.toULong(&ok, 16) * -1;
    } else {
        number = currentValue.toULong(&ok, 16);
    }
    return number;
}
