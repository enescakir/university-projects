# CmpE49F -  Introduction to Satellite Space Network
| Term | Instructor |
| --- | --- |
| Fall 2018  | Fatih Alagöz  |

We are asked to develop  cache replacement algorithm for `Information-Centric Network (ICN)`

### Links
- [Project Description](https://github.com/EnesCakir/university-projects/raw/master/cmpe49f/Project_Description.pdf) (given)
- [Project Performance Evaluation](https://github.com/EnesCakir/university-projects/raw/master/cmpe49f/Project_Performance_Evaluation.pdf) (given)

- [First Report](https://github.com/EnesCakir/university-projects/raw/master/cmpe49f/First_Report.pdf)
- [Final Report](https://github.com/EnesCakir/university-projects/raw/master/cmpe49f/Final_Report.pdf)

### Requirements
- python 3
- simpy
- numpy
- matplotlib

### Usage
```bash
    $ python3 main.py
    # You can overwrite time, count, and algorithm via command arguments
    $ python3 main.py --time=2000 --count=1 --algorithm=MY
    # Algorithm parameter has 4 different options: LRU | LFU | RAND | MY
```

You can set general system parameters at `config.py`
