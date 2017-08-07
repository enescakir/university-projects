<?php require('partials/header.view.php'); ?>

<div class="row">
    <div class="col-md-8 col-md-offset-2">
        <div class="panel panel-default">
            <div class="panel-heading">Dashboard</div>
            <div class="panel-body text-center">
              <h2><strong>Administrator: </strong> <?= \App\Model\Administrator::count() ?></h2>
              <h2><strong>Employee: </strong> <?= \App\Model\Employee::count() ?></h2>
              <h2><strong>Airport: </strong> <?= \App\Model\Airport::count() ?></h2>
              <h2><strong>Airplane: </strong> <?= \App\Model\Airplane::count() ?></h2>
              <h2><strong>Pilot: </strong> <?= \App\Model\Pilot::count() ?></h2>
              <h2><strong>Flight: </strong> <?= \App\Model\Flight::count() ?></h2>
              <h2><strong>Reservation: </strong> <?= \App\Model\Reservation::count() ?></h2>
              <h2><strong>Customer: </strong> <?= \App\Model\Customer::count() ?></h2>
            </div>
        </div>
    </div>
</div>

<?php require('partials/scripts.view.php'); ?>
<?php require('partials/footer.view.php'); ?>
