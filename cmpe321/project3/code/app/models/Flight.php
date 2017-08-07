<?php

namespace App\Model;
use Core\App;

use App\Model\Airplane;

class Flight extends Model
{

  protected static $table_name = "flights";
  protected static $class_name = "Flight";

  public static function allWithRelations()
  {
    $sql = "SELECT f.*, des.name as des_name, des.city as des_city, dep.name as dep_name, dep.city as dep_city, p.name as p_name, ap.model as ap_model
    FROM flights as f
    JOIN airports as des ON des.id = f.destination_id
    JOIN airports as dep ON dep.id = f.departure_id
    JOIN pilots as p ON p.id = f.pilot_id
    JOIN airplanes as ap ON ap.id = f.airplane_id
    ";
    return App::get('database')->raw($sql, static::$class_name);
  }

  public static function future($pid = "ALL")
  {
    return App::get('database')->procedure("future_flights", [$pid], static::$class_name);
  }

  public static function past($pid = "ALL")
  {
    return App::get('database')->procedure("past_flights", [$pid], static::$class_name);
  }

  public function full_seats()
  {
    return array_column(App::get('database')->procedure("full_seats", [$this->id]), 'seat');
  }

  public function airplane()
  {
    return Airplane::find($this->airplane_id);
  }


}
