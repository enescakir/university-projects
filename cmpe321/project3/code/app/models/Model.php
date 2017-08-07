<?php

namespace App\Model;
use Core\App;

class Model
{

  protected static $table_name = "";
  protected static $class_name = "";
  protected static $primary_key = "id";

  public static function all()
  {
    return App::get('database')->selectAll(static::$table_name, static::$class_name);
  }

  public static function count($group_by = null)
  {
    return App::get('database')->count(static::$table_name, $group_by);
  }

  public static function find($pk)
  {
    $result = App::get('database')->find(static::$table_name, $pk, static::$class_name, static::$primary_key);
    if ($result) {
      return $result[0];
    }
    return [];
  }

  public static function findByEmail($email)
  {
    $result = App::get('database')->find(static::$table_name, $email, static::$class_name, 'email');
    if ($result) {
      return $result[0];
    }
    return [];
  }

  public static function create($data)
  {
    App::get('database')->insert(static::$table_name, $data);
  }

  public function update($data)
  {
    App::get('database')->update(static::$table_name, $data, $this->id, static::$primary_key);
  }

  public function delete()
  {
    App::get('database')->delete(static::$table_name, $this->id, static::$primary_key);
  }

  public static function getPrimaryKey()
  {
    return static::$primary_key;
  }

}
