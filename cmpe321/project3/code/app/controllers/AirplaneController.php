<?php

namespace App\Controllers;

use Core\Auth;
use App\Model\Airplane;

class AirplaneController
{

  public function __construct()
  {
    if (!Auth::check('administrator')) {
      return redirect('login');
    }
  }

  public function index()
  {
    $airplanes = Airplane::all();
    return view('airplane/index', compact('airplanes'));
  }

  public function create()
  {
    return view('airplane/create');
  }

  public function store()
  {
    Airplane::create([
      'model'      => $_POST['model'],
      'age'        => $_POST['age'],
      'horizantal' => $_POST['row'],
      'vertical'   => $_POST['column'],
      'created_at' => date("Y-m-d H:i:s"),
      'created_by' => Auth::user()->id,
    ]);
    set_flash_message('success', "{$_POST['model']} is created succesfully");
    return redirect('airplane');
  }

  public function show($id)
  {
    dd(Airplane::find($id));
  }

  public function edit($id)
  {
    $airplane = Airplane::find($id);
    return view('airplane/edit', compact('airplane'));
  }

  public function update($id)
  {
    $airplane = Airplane::find($id);
    $airplane->update([
      'model'      => $_POST['model'],
      'age'        => $_POST['age'],
      'horizantal' => $_POST['row'],
      'vertical'   => $_POST['column'],
      'updated_at' => date("Y-m-d H:i:s"),
      'updated_by' => Auth::user()->id,
    ]);
    set_flash_message('success', "{$airplane->model} is updated succesfully");
    return redirect('airplane');
  }

  public function delete($id)
  {
    $airplane = Airplane::find($id);
    $airplane->delete();
    return json_encode($airplane);
  }

}
