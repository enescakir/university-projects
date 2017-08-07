<?php

namespace App\Controllers;

use App\Model\Administrator;
use Core\Auth;

class AdministratorController
{

  public function __construct()
  {
    if (!Auth::check('administrator')) {
      return redirect('login');
    }
  }
  /**
  * Show all users.
  */
  public function index()
  {
    $administrators = Administrator::all();
    return view('administrator/index', compact('administrators'));
  }

  public function activate($id)
  {
    $administrator = Administrator::find($id);
    $administrator->update([
      'activated_at' => date("Y-m-d H:i:s")
    ]);
    return json_encode($administrator);
  }
}
