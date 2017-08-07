<?php

namespace App\Model;
use Core\App;

class Reservation extends Model
{

  protected static $table_name = "reservations";
  protected static $class_name = "Reservation";

  public static function allWithRelations()
  {
    $sql = "SELECT r.*, f.number as f_number, e.name as e_name, c.name as c_name
    FROM reservations as r
    JOIN flights as f ON f.id = r.flight_id
    LEFT JOIN employees as e ON e.id = r.employee_id
    LEFT JOIN customers as c ON c.id = r.customer_id
    ";
    return App::get('database')->raw($sql, static::$class_name);
  }

  public static function findWithRelations($id)
  {
    $sql = "
    SELECT r.*, c.name as c_name
    FROM reservations as r
    JOIN customers as c ON c.id = r.customer_id
    WHERE r.id = {$id}
    ";
    return App::get('database')->raw($sql, static::$class_name)[0];
  }

}
