<?php

/**
* Require a view.
*
* @param  string $name
* @param  array  $data
*/
function view($name, $data = [])
{
  extract($data);

  return require "app/views/{$name}.view.php";
}

function partial_view($name)
{
  return require "app/views/partials/{$name}.view.php";
}

/**
* Redirect to a new page.
*
* @param  string $path
*/
function redirect($path)
{
  header("Location: /{$path}");
}

function redirect_back()
{
  header("Location: {$_SERVER['HTTP_REFERER']}");
}

function asset($path)
{
  // TODO: Change it from config
  //   // return '/PHP/cmpe321/project3/public/' . $path;
  return '/public/' . $path;
}

function set_flash_message($type, $message)
{
  $_SESSION[$type] = $message;
}

function get_flash_message($type)
{
  if (isset($_SESSION[$type])) {
    $message = $_SESSION[$type];
    unset($_SESSION[$type]);
    return $message;
  }
  return false;
}

function dd($data)
{
  echo '<pre>';
  die(var_dump($data));
  echo '</pre>';
}


function str_random($length = 10) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomString;
}
