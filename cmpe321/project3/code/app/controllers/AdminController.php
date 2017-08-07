<?php

namespace App\Controllers;

use App\Model\Administrator;
use Core\Auth;

class AdminController
{
  public function dashboard()
  {
    if (Auth::check()) {
      return view('dashboard');
    } else {
      return redirect('login');
    }
  }

  public function showLogin()
  {
    return view('login');
  }

  public function login()
  {
    if (Auth::login($_POST['email'], $_POST['password'], $_POST['type'])) {
      return redirect('');
    }
    return redirect_back();
  }

  public function showRegister()
  {
    return view('register');
  }

  public function register()
  {
    if ($_POST['password'] != $_POST['password_confirmation']) {
      set_flash_message('error', 'Passwords aren\'t matched');
      return redirect_back();
    }

    if (Administrator::findByEmail($_POST['email'])) {
      set_flash_message('error', 'This email address is already taken');
      return redirect_back();
    }

    Administrator::create([
      'name'         => $_POST['name'],
      'email'        => $_POST['email'],
      'password'     => password_hash($_POST['password'], PASSWORD_BCRYPT),
      'activated_at' => Administrator::all() ? NULL : date("Y-m-d H:i:s"),
    ]);
    set_flash_message('success', 'You are registered successfully. <br> After administrator activate your account, you can login system.');
    return redirect('');
  }

  public function logout()
  {
    Auth::logout();
    set_flash_message('success', 'Hope to see you again');
    return redirect('login');
  }

}
