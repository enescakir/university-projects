<?php

namespace Core\Database;

use PDO;

class QueryBuilder
{
  /**
  * The PDO instance.
  *
  * @var PDO
  */
  protected $pdo;

  /**
  * Create a new QueryBuilder instance.
  *
  * @param PDO $pdo
  */
  public function __construct($pdo)
  {
    $this->pdo = $pdo;
  }

  /**
  * Select all records from a database table.
  *
  * @param string $table
  */
  public function selectAll($table, $class)
  {
    $statement = $this->pdo->prepare("SELECT * FROM {$table}");
    $statement->execute();

    return $statement->fetchAll(PDO::FETCH_CLASS, "\\App\\Model\\" . $class);
  }

  /**
  * Runs raw SQL string.
  *
  * @param string $table
  */
  public function raw($sql, $class)
  {
    $statement = $this->pdo->prepare($sql);
    $statement->execute();
    return $statement->fetchAll(PDO::FETCH_CLASS, "\\App\\Model\\" . $class);
  }

  public function count($table, $group_by = null)
  {
    if ($group_by) {
      $statement = $this->pdo->prepare("SELECT {$group_by}, COUNT(*) FROM {$table} GROUP BY {$group_by}");
      $statement->execute();
      return $statement->fetchAll(PDO::FETCH_ASSOC);
    } else {
      $statement = $this->pdo->prepare("SELECT COUNT(*) FROM {$table}");
      $statement->execute();
      return $statement->fetchAll(PDO::FETCH_COLUMN)[0];
    }
  }

  /**
  * Find records from a database table.
  *
  * @param string $table
  * @param string $value
  * @param string $pk
  */
  public function find($table, $value, $class, $pk = "id")
  {
    try {
      $statement = $this->pdo->prepare("SELECT * FROM {$table} WHERE {$pk} = :value");
      $statement->execute(["value" => $value]);
      return $statement->fetchAll(PDO::FETCH_CLASS, "\\App\\Model\\" . $class);
    } catch (\Exception $e) {
      echo $e->getMessage() . "<br>";
    }
  }

  /**
  * Delete record from a database table.
  *
  * @param string $table
  * @param string $value
  * @param string $pk
  */
  public function delete($table, $value, $pk = "id")
  {
    try {
      $statement = $this->pdo->prepare("DELETE FROM {$table} WHERE {$pk} = :value");
      $statement->execute(["value" => $value]);
    } catch (\Exception $e) {
      echo $e->getMessage() . "<br>";
    }
  }

  /**
  * Insert a record into a table.
  *
  * @param  string $table
  * @param  array  $parameters
  */
  public function insert($table, $parameters)
  {
    $sql = sprintf(
      'INSERT INTO %s (%s) VALUES (%s)',
      $table,
      implode(', ', array_keys($parameters)),
      ':' . implode(', :', array_keys($parameters))
    );

    try {
      $statement = $this->pdo->prepare($sql);
      $statement->execute($parameters);
    } catch (\Exception $e) {
      echo $e->getMessage() . "<br>";
    }
  }

  /**
  * Insert a record into a table.
  *
  * @param  string $table
  * @param  array  $parameters
  */
  public function update($table, $parameters, $value, $pk)
  {
    $sql = sprintf(
      'UPDATE %s SET %s WHERE %s = %s',
      $table,
      implode(', ',
        array_map( function($key, $value) {
          return "$key = :$key";
        }, array_keys($parameters), $parameters)
      ),
      $pk,
      $value
    );

    try {
      $statement = $this->pdo->prepare($sql);
      $statement->execute($parameters);
    } catch (\Exception $e) {
      echo $e->getMessage() . "<br>";
    }
  }

  public function procedure($name, $parameters, $class = null)
  {
    $sql = sprintf(
      'CALL %s (%s)',
      $name,
      implode(', ', array_map(function($param){ return "\"{$param}\""; }, $parameters))
    );
    try {
      $statement = $this->pdo->prepare($sql);
      $statement->execute();
      if ($class) {
        return $statement->fetchAll(PDO::FETCH_CLASS, "\\App\\Model\\" . $class);
      } else {
        return $statement->fetchAll(PDO::FETCH_ASSOC);
      }
    } catch (\Exception $e) {
      echo $e->getMessage() . "<br>";
    }
  }

  /**
  * Create a new table on DB.
  *
  * @param  string $table
  * @param  array  $parameters
  */
  public function createTable($table, $parameters, $keys = [])
  {
    $sql = sprintf(
      'CREATE TABLE %s (%s %s)',
      $table,
      implode(', ',
        array_map( function($key, $value) {
          return "$key $value";
        }, array_keys($parameters), $parameters)
      ),
      $keys ? ", " .
        implode(', ',
          array_map( function($key, $value) {
            if ($value[0] == "primary") {
              return "PRIMARY KEY ({$key})";
            } else if ($value[0] == "foreing") {
              return "FOREIGN KEY ({$key}) REFERENCES {$value[1]}({$value[2]}) ON DELETE {$value[3]}";
            }
            return "";
          }, array_keys($keys), $keys)
        )
      : ""
    );
    try {
      $statement = $this->pdo->prepare($sql);
      $statement->execute();
      $messages = get_flash_message('database') ?: [];
      array_push($messages, "<strong>TABLE SUCCESS</strong><br>");
      array_push($messages, "<strong>{$table}</strong> is created successfully <br><br>");
      set_flash_message('database', $messages);
    } catch (\Exception $e) {
      $messages =  get_flash_message('database') ?: [];
      array_push($messages, "<strong>TABLE ERROR</strong><br>");
      array_push($messages, $e->getMessage() . "<br>");
      array_push($messages, "<strong>{$table}</strong> couldn't created <br><br>");
      set_flash_message('database', $messages);
    }
  }

  /**
  * Delete the table on DB.
  *
  * @param  string $table
  * @param  array  $parameters
  */
  public function deleteTable($table, $force = false)
  {
    $sql = sprintf('DROP TABLE %s',$table);
    try {
      if ($force) {
        $statement = $this->pdo->prepare('SET FOREIGN_KEY_CHECKS = 0');
        $statement->execute();
      }
      $statement = $this->pdo->prepare($sql);
      $statement->execute();
      if ($force) {
        $statement = $this->pdo->prepare('SET FOREIGN_KEY_CHECKS = 1');
        $statement->execute();
      }
      $messages = get_flash_message('database') ?: [];
      array_push($messages, "<strong>TABLE SUCCESS</strong><br>");
      array_push($messages, "<strong>{$table}</strong> is deleted successfully <br><br>");
      set_flash_message('database', $messages);
    } catch (\Exception $e) {
      $messages = get_flash_message('database') ?: [];
      array_push($messages, "<strong>TABLE ERROR</strong><br>");
      array_push($messages, $e->getMessage() . "<br>");
      array_push($messages, "<strong>{$table}</strong> couldn't deleted <br><br>");
      set_flash_message('database', $messages);
    }
  }

  public function createProcedure($name, $parameters, $procedure)
  {
    $sql = sprintf(
      'CREATE PROCEDURE `%s` (%s) BEGIN %s END',
      $name, $parameters, $procedure
    );
    try {
      $statement = $this->pdo->prepare($sql);
      $statement->execute();
      $messages = get_flash_message('database') ?: [];
      array_push($messages, "<strong>PROCEDURE SUCCESS</strong><br>");
      array_push($messages, "<strong>{$name}</strong> procedure is created successfully <br><br>");
      set_flash_message('database', $messages);
    } catch (\Exception $e) {
      $messages = get_flash_message('database') ?: [];
      array_push($messages, "<strong>PROCEDURE ERROR</strong><br>");
      array_push($messages, $e->getMessage() . "<br>");
      array_push($messages, "<strong>{$name}</strong> procedure couldn't created <br><br>");
      set_flash_message('database', $messages);
    }
  }

  public function createTrigger($name, $time, $trigger)
  {
    $sql = sprintf(
      'CREATE TRIGGER `%s` %s FOR EACH ROW BEGIN %s END',
      $name, $time, $trigger
    );
    try {
      $statement = $this->pdo->prepare($sql);
      $statement->execute();
      $messages = get_flash_message('database') ?: [];
      array_push($messages, "<strong>TRIGGER SUCCESS</strong><br>");
      array_push($messages, "<strong>{$name}</strong> trigger is created successfully <br><br>");
      set_flash_message('database', $messages);
    } catch (\Exception $e) {
      $messages = get_flash_message('database') ?: [];
      array_push($messages, "<strong>TRIGGER ERROR</strong><br>");
      array_push($messages, $e->getMessage() . "<br>");
      array_push($messages, "<strong>{$name}</strong> trigger couldn't created <br><br>");
      set_flash_message('database', $messages);
    }
  }

  public function deleteProcedure($name)
  {
    $sql = sprintf('DROP PROCEDURE %s',$name);
    try {
      $statement = $this->pdo->prepare($sql);
      $statement->execute();
      $messages = get_flash_message('database') ?: [];
      array_push($messages, "<strong>PROCEDURE SUCCESS</strong><br>");
      array_push($messages, "<strong>{$name}</strong> procedure is deleted successfully <br><br>");
      set_flash_message('database', $messages);
    } catch (\Exception $e) {
      $messages = get_flash_message('database') ?: [];
      array_push($messages, "<strong>PROCEDURE ERROR</strong><br>");
      array_push($messages, $e->getMessage() . "<br>");
      array_push($messages, "<strong>{$name}</strong> procedure couldn't deleted <br><br>");
      set_flash_message('database', $messages);
    }
  }

  public function deleteTrigger($name)
  {
    $sql = sprintf('DROP TRIGGER %s',$name);
    try {
      $statement = $this->pdo->prepare($sql);
      $statement->execute();
      $messages = get_flash_message('database') ?: [];
      array_push($messages, "<strong>TRIGGER SUCCESS</strong><br>");
      array_push($messages, "<strong>{$name}</strong> trigger is deleted successfully <br><br>");
      set_flash_message('database', $messages);
    } catch (\Exception $e) {
      $messages = get_flash_message('database') ?: [];
      array_push($messages, "<strong>TRIGGER ERROR</strong><br>");
      array_push($messages, $e->getMessage() . "<br>");
      array_push($messages, "<strong>{$name}</strong> trigger couldn't deleted <br><br>");
      set_flash_message('database', $messages);
    }
  }
}
