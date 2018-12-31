Mustafa Enes Çakır
2013400105
enes@cakir.web.tr
CmpE436 Assignment 1

Source code of project is "src" folder. All methods have JavaDoc comments.

Question1 and Matrix classes are for solution of Question 1.
Question2 and Population classes are for solution of Question 2.

# Question 1 - Matrix Multiplication
    Question1.java class manages flow of the solution. It takes file names from arguments and constructs matrix from them. Then it multiplies matrices.
    It expects three arguments: [in] file name of first matrix, [in] file name of second matrix, [out] file name of result matrix.
    Matrix files should be in the given structure that is in the assignment PDF.
    Matrix.java class handles matrix operations.

    ## Checked Errors
    - Enough application arguments
    - Positive integer dimensions
    - All integer elements
    - Valid input files
    - Valid matrix dimensions for multiplication


# Question 2 - Game of Life
    Question2.java class manages flow of the solution. It takes population sizes, max generation number, optionally initial data file name and constructs population from them.
    If initial population is not given, it generates random population. Then it runs population until reaches max generation. It print each population steps.
    It expects three or four arguments: number of rows, number of columns, max generation number, [optional] initial population.
    Initial population files should be in the given structure expect first dimension line is unnecessary.
    Population.java class handles population operations. It extends from Matrix.class.
    It's data array has 2 more rows and columns, because of neighbor count checking for border cells. Outer element has fixed zero values.

    ## Checked Errors
    - Same errors from Question 1
    - All elements have to be 1 or 0
    - Positive max generation number
