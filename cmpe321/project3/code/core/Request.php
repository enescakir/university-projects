<?php

namespace Core;

class Request
{
  /**
  * Fetch the request URI.
  *
  * @return string
  */
  public static function uri()
  {
    $uri = trim(parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH), '/');
    $last_part = strrchr($uri , '/');
    if ($last_part && (int)trim($last_part, '/') != 0) {
      $uri = str_replace($last_part, "", $uri);
    }
    return $uri;
  }

  /**
  * Fetch the id as parameter.
  *
  * @return string
  */
  public static function parameter()
  {
    $uri = trim(parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH), '/');
    $last_part = strrchr($uri , '/');
    if ($last_part) {
      return (int)trim($last_part, '/');
    }
  }

  /**
  * Fetch the request method.
  *
  * @return string
  */
  public static function method()
  {
    return $_SERVER['REQUEST_METHOD'];
  }
}
