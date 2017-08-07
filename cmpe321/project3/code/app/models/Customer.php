<?php

namespace App\Model;
use Core\App;

class Customer extends Model
{

  protected static $table_name = "customers";
  protected static $class_name = "Customer";

  public static function allWithRelations()
  {
    $sql = "
      SELECT c.*, COUNT(r.customer_id) reservation_count
      FROM customers as c
      LEFT JOIN reservations as r ON r.customer_id = c.id
      GROUP BY c.id
      ";
    return App::get('database')->raw($sql, static::$class_name);
  }
}
