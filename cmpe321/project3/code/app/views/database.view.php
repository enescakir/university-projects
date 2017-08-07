<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>Database Operations</title>

  <!-- Styles -->
  <link href="<?= asset('css/app.css') ?>" rel="stylesheet">
  <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
</head>
<body>
  <div id="app">
    <nav class="navbar navbar-default navbar-static-top">
      <div class="container">
        <div class="navbar-header">

          <!-- Collapsed Hamburger -->
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#app-navbar-collapse">
            <span class="sr-only">Toggle Navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>

          <!-- Branding Image -->
          <a class="navbar-brand" href="/">
            CmpE321 Project3 Database Operations
          </a>
        </div>
      </div>
    </nav>
    <div class="container">
      <?php partial_view('message') ?>
    </div>
    <div class="container">

<div class="row">
    <div class="col-md-8 col-md-offset-2">
        <div class="panel panel-default">
            <div class="panel-heading">Database Operations</div>
            <div class="panel-body">
              <div class="row">
                <div class="col-md-6">
                  <a href="/database/table/create" class="btn btn-primary btn-block">Create Tables</a>
                  <a href="/database/procedure/create" class="btn btn-primary btn-block">Create Procedures</a>
                  <a href="/database/trigger/create" class="btn btn-primary btn-block">Create Triggers</a>
                </div>
                <div class="col-md-6">
                  <a href="/database/table/delete" class="btn btn-danger btn-block">Delete Tables</a>
                  <a href="/database/procedure/delete" class="btn btn-danger btn-block">Delete Procedures</a>
                  <a href="/database/trigger/delete" class="btn btn-danger btn-block">Delete Triggers</a>
                </div>
              </div>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">Result</div>
            <div class="panel-body">
              <?php $messages = get_flash_message('database'); ?>
              <?php foreach ($messages ?:[]  as $message) : ?>
                <?= $message ?>
              <?php endforeach ; ?>
            </div>
        </div>
    </div>
</div>

</div>
</div>

<!-- Scripts -->

<script src="<?= asset('js/app.js') ?>"></script>
</body>
</html>
