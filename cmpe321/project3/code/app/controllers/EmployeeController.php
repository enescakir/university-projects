<?php

namespace App\Controllers;

use Core\Auth;
use App\Model\Employee;

class EmployeeController
{

  public function __construct()
  {
    if (!Auth::check('administrator')) {
      return redirect('login');
    }
  }

  public function index()
  {
    $employees = Employee::all();
    return view('employee/index', compact('employees'));
  }

  public function create()
  {
    return view('employee/create');
  }

  public function store()
  {
    if ($_POST['password'] != $_POST['password_confirmation']) {
      set_flash_message('error', 'Passwords aren\'t matched');
      return redirect_back();
    }
    if (Employee::findByEmail($_POST['email'])) {
      set_flash_message('error', 'This email address is already taken');
      return redirect_back();
    }

    Employee::create([
      'name'       => $_POST['name'],
      'email'      => $_POST['email'],
      'password'   => password_hash($_POST['password'], PASSWORD_BCRYPT),
      'created_at' => date("Y-m-d H:i:s"),
      'created_by' => Auth::user()->id,
    ]);
    set_flash_message('success', "{$_POST['name']} is created succesfully");
    return redirect('employee');
  }

  public function show($id)
  {
    dd(Employee::find($id));
  }

  public function edit($id)
  {
    $employee = Employee::find($id);
    return view('employee/edit', compact('employee'));
  }

  public function update($id)
  {
    if ($_POST['password']) {
      if ($_POST['password'] != $_POST['password_confirmation']) {
        set_flash_message('error', 'Passwords aren\'t matched');
        return redirect_back();
      }
    }
    $employee = Employee::find($id);
    if ($employee->email != $_POST['email'] && Employee::findByEmail($_POST['email'])) {
      set_flash_message('error', 'This email address is already taken');
      return redirect_back();
    }

    $employee->update([
      'name'       => $_POST['name'],
      'email'      => $_POST['email'],
      'password'   => $_POST['password'] ? password_hash($_POST['password'], PASSWORD_BCRYPT) : $employee->password,
      'updated_at' => date("Y-m-d H:i:s"),
      'updated_by' => Auth::user()->id,
    ]);
    set_flash_message('success', "{$employee->name} is updated succesfully");
    return redirect('employee');
  }

  public function delete($id)
  {
    $employee = Employee::find($id);
    $employee->delete();
    return json_encode($employee);
  }

}
