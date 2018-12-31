Mustafa Enes Çakır
2013400105
enes@cakir.web.tr
CmpE436 Assignment 2

Source code of project is "src" folder. All methods have JavaDoc comments.

Question1, Population, CountingSemaphore classes are for solution of Question 1.
Question2, BinarySemaphore, Philosopher classes are for solution of Question 2.
Question3, RaceThread classes are for solution of Question 3.

# Question 1 - Game of Life
    Question1.java class manages flow of the solution. General flow is same with Assignment 1 Question 2.
    It expects three or four arguments: number of rows, number of columns, max generation number, [optional] initial population.
    Each cell has thread. They are sync with barrier synchronization.
    All threads enter critical session 2 times: calculating next value, changing current value with next value.
    After each critical session, they are waiting all threads to complete this step.

# Question 2 - Deadlock
    Question2.java class manages flow of the solution. It representation of classical philosopher problem.
    Each of them took left fork and locked.

# Question 3 - Race Condition
    Question3.java class manages flow of the solution.
    Each thread takes common value to temporary value, change it, wait and write it to common place.
    Threads overwrite each other's result
