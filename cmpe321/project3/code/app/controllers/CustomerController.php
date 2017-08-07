<?php

namespace App\Controllers;

use Core\Auth;
use App\Model\Customer;

class CustomerController
{

  public function __construct()
  {
    if (!Auth::check('employee')) {
      return redirect('login');
    }
  }

  public function index()
  {
    $customers = Customer::allWithRelations();
    return view('customer/index', compact('customers'));
  }

  public function create()
  {
    return view('customer/create');
  }

  public function store()
  {
    Pilot::create([
      'name'      => $_POST['name'],
      'created_at' => date("Y-m-d H:i:s"),
      'created_by' => Auth::user()->id,
    ]);
    set_flash_message('success', "{$_POST['name']} is created succesfully");
    return redirect('customer');
  }

  public function show($id)
  {
    dd(Customer::find($id));
  }

  public function edit($id)
  {
    $customer = Customer::find($id);
    return view('customer/edit', compact('customer'));
  }

  public function update($id)
  {
    $customer = Customer::find($id);
    $customer->update([
      'name'      => $_POST['name'],
      'updated_at' => date("Y-m-d H:i:s"),
      'updated_by' => Auth::user()->id,
    ]);
    set_flash_message('success', "{$customer->name} is updated succesfully");
    return redirect('customer');
  }

  public function delete($id)
  {
    $customer = Customer::find($id);
    $customer->delete();
    return json_encode($customer);
  }

}
