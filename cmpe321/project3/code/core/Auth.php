<?php

namespace Core;
use App\Model\{Administrator, Employee};

class Auth
{
  public static function login($email, $password, $type)
  {
    if ($type == 'administrator') {
      $user = Administrator::findByEmail($email);
      if ($user && !$user->activated_at) {
        set_flash_message('error', 'Your account has not been activated yet.');
        return false;
      }
    } else if ($type == 'employee') {
      $user = Employee::findByEmail($email);
    }
    if ($user) {
      if (password_verify($password, $user->password)) {
        $_SESSION['auth'] = [
          'id'    => $user->id,
          'name'  => $user->name,
          'email' => $user->email,
          'type'  => $type,
        ];
        set_flash_message('success', "Welcome {$user->name}");
        return true;
      } else {
        set_flash_message('error', 'Your password is wrong.');
        return false;
      }
    }
    set_flash_message('error', 'Your email isn\'t registered our system.');
    return false;
  }
  public static function logout()
  {
    session_unset();
  }

  public static function check($type = "")
  {
    if (isset($_SESSION['auth'])) {
      if ($type) {
        return $_SESSION['auth']['type'] == $type;
      }
      return true;
    }
    return false;
  }

  public static function user()
  {
    $user = new \stdClass;
    if (isset($_SESSION['auth'])) {
      $user->id = $_SESSION['auth']['id'];
      $user->name = $_SESSION['auth']['name'];
      $user->email = $_SESSION['auth']['email'];
      $user->type = $_SESSION['auth']['type'];
    }
    return $user;
  }
}
