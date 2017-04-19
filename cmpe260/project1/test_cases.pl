errors_for_plan([['ec102', 'ef106', 'm-1'], ['cmpe160', 'nh105', 'w-2'], ['math102',
'ef106', 'f-2'], ['math202', 'nh105', 'w-1'], ['cmpe240', 'nh101', 'm-3'], ['cmpe230',
'nh101', 'f-3'], ['phys201', 'nh105', 'f-1'], ['ee212', 'nh101', 'w-2']], ErrorCount).

errors_for_plan([['ec102', 'ef106', 'm-1'], ['cmpe160', 'nh105', 'w-2'], ['math102',
'ef106', 'w-2'], ['math202', 'nh105', 'w-1'], ['cmpe240', 'nh101', 'm-3'], ['cmpe230',
'nh101', 'f-3'], ['phys201', 'nh105', 'f-1'], ['ee212', 'nh101', 'f-2']], ErrorCount).

errors_for_plan([['ec102', 'nh101', 'm-1'], ['cmpe160', 'nh105', 'w-2'], ['math102',
'ef106', 'w-2'], ['math202', 'nh105', 'w-1'], ['cmpe240', 'nh101', 'm-3'], ['cmpe230',
'nh101', 'f-3'], ['phys201', 'nh105', 'f-1'], ['ee212', 'nh101', 'f-2']], ErrorCount).

common_students(ec102, ['cmpe160', 'cmpe240'], StudentCount).

is_intersect(ec102, 'w-1', [['cmpe160', 'nh101', 'w-1'], ['cmpe240', 'nh102', 'w-1']], CommonStudents).
