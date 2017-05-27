#include "hexator.h"
#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    Hexator h;

    h.show();
    return app.exec();
}
