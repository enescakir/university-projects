% Mustafa Enes ÇAKIR
% 2013400105
% CmpE260 Project 1

:- dynamic student/2, available_slots/1, room_capacity/2.

% This predicate clears all added students, slots and rooms
clear_knowledge_base:-
  all_students(Students), length(Students, StudentsCount), write(StudentsCount), write(' students were removed.'), nl,
  all_slots(Slots), length(Slots, SlotsCount), write(SlotsCount), write(' available slots were removed.'), nl,
  all_rooms(Rooms), length(Rooms, RoomsCount), write(RoomsCount), write(' rooms were removed.'), nl,
  retractall(student(_,_)), retractall(available_slots(_)), retractall(room_capacity(_,_)).

% List all students
all_students(StudentList):- findall(X,student(X,_),StudentList).

% List all rooms
all_rooms(RoomList):- findall(X,room_capacity(X,_),RoomList).

% List all slots
all_slots(SlotList):- findall(X, (available_slots(Slots), member(X, Slots)), SlotList).

% List all courses.
% When we findall courses it return depth:2 list, so we flat it.
% Flat list has duplicates, list_to_set removes duplicates.
all_courses(CourseList):- findall(X, student(_,X), L1), flatten(L1, L2), list_to_set(L2, CourseList).

% Return StudentCount that is number of students who is taking Course.
student_count(Course, StudentCount):- who_taking(Course, Students), length(Students, StudentCount).

% Return list of Students who is taking Course.
who_taking(Course, Students):- setof(X, is_taking(X, Course), Students).

% Checks is student with ID taking Course.
is_taking(ID, Course):- student(ID, CourseList), member(Course, CourseList).

% Returns count of students who take both of Course1 and Course2.
common_students(Course1, Course2, StudentCount):- findall(X, (is_taking(X, Course1), is_taking(X, Course2)), Students), length(Students, StudentCount).

% Returns possible FinalPlan's one by one.
final_plan(FinalPlan):-
  % Takes required lists
  all_courses(Courses), available_slots(Slots), all_rooms(Rooms),
  % Make a FinalPlan with them
  final(Courses, Slots, Rooms, FinalPlan).

% Exit for final/4 recursion call
final([],_,_,[]).

% Recursively calls, and create FinalPlan
final([Course|Courses], Slots, Rooms, FinalPlan):-
  final(Courses, Slots, Rooms, NewFinal),
  choose_class(Course, Rooms, Room),
  choose_slot(Course, Slots, Room, Slot, NewFinal),
  % If it choose Class and Slot successfully, add them to FinalPlan
  append([[Course, Room, Slot]], NewFinal, FinalPlan).

% Finds suitable Room for Course
choose_class(Course, Rooms, Room):-
  member(Room, Rooms),
  % Checks it's capacity is enough
  is_enough(Course, Room).

% Finds suitable Slot for Course
choose_slot(Course, Slots, Room, Slot, NewFinal):-
  member(Slot, Slots),
  % Checks is Room empty at the Slot
  not(member([_,Room,Slot], NewFinal)),
  % Checks is Courses have same slot
  commons(Course, Slot, NewFinal).

% Finds all same slot course:ConflictCourses, and finds common ones
commons(Course, Slot, NewFinal):- findall(Course2, member([Course2, _, Slot], NewFinal), ConflictCourses), check_conflict(Course, ConflictCourses).

% Recursively iterate over ConflictCourses list
check_conflict(_, []).
check_conflict(Course, [Course2|Rest]):- common_courses(Course, Course2, StudentCount), StudentCount == 0, check_conflict(Course, Rest).


% Exit for errors_for_plan/2 recursion call
errors_for_plan([], 0):- !.

% Calculate ErrorCount in given FinalPlan
errors_for_plan([[Course, Room, Slot]|Rest], ErrorCount):-
  % delete([[Course, Room, Slot]|Rest], [Course, Room, Slot], Rest2),
  errors_for_plan(Rest, ErrorCount2),
    % Calculate small class errors
    ( is_small(Course, Room, StudentCount, Capacity) -> ErrorCount3 is StudentCount - Capacity ; ErrorCount3 is 0 ),
    % Calculate common students errors
    ( is_intersect(Course, Slot, Rest, CommonStudents) -> ErrorCount4 is CommonStudents ; ErrorCount4 is 0 ),
  % Add all errors up
  ErrorCount is ErrorCount2 + ErrorCount3 + ErrorCount4.

% It checks is Room capacity small for Course. It returns StudentCount and Capacity.
% StudentCount => number of students who is taking Course
% Capacity => capacity of Room
is_small(Course, Room, StudentCount, Capacity):-
  student_count(Course, StudentCount),
  room_capacity(Room, Capacity),
  StudentCount > Capacity.

% It is inverse of is_small/4. It checks is Room capacity enough for Course.
is_enough(Course, Room):- not(is_small(Course, Room, _, _)).

% Find all common courses
is_intersect(Course, Slot, Rest, CommonStudents):-
  findall( CommonCourse, member([CommonCourse, _, Slot], Rest), Conflicts ),
  common_courses(Course, Conflicts, CommonStudents).

% Iterate ove Conflicts recursively, and find common_students counts
common_courses(_, [], 0):- !.
common_courses(Course1, [Course2|Rest], StudentCount):-
  common_courses(Course1, Rest, CommonStudents1),
  common_students(Course1, Course2, CommonStudents2),
  StudentCount is CommonStudents1 + CommonStudents2.
