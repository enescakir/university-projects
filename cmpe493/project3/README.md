Entry point of my application is app.py

You should use python 3.6.5

Run my app with "python3 app.py COMMAND DATA_DIRECTORY [FILE]" command.

File name is optional.
If any file name is specified, it run it for all files in data directory

## Available Commands ##
    lex      -   Return lex rank scores of sentences
    summary  -   Return generated summary of new
    gold     -   Return gold summary of new
    rouge    -   Return average rouge scores of new

## Example ##

python3 app.py lex Dataset 1.txt
    Print lex rank of sentences in 1.txt

-----

python3 app.py lex Dataset
    Print lex rank of sentences of all files line by line

-----

python3 app.py rouge Dataset
    Print average rouge scores of all files