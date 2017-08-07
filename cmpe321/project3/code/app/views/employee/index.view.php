<?php partial_view('header') ?>

<div class="row">
  <div class="col-md-12">
    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title pull-left">Employees</h3>
        <div class="btn-group pull-right" role="group">
          <a class="btn btn-success" href="\employee\create"><i class="fa fa-plus"></i> Create</a>
        </div>
        <div class="clearfix"></div>
      </div>
      <div class="panel-body">
        <div class="row">
          <div class="col-md-12">
            <div class="table-responsive">
              <table id="employees-table" class="table table-bordered table-striped table-hover">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th class="two-button"></th>
                  </tr>
                </thead>
                <tbody>
                  <?php foreach ($employees as $employee) : ?>
                    <tr id='employee-<?= $employee->id ?>'>
                      <td><?= $employee->id ?></td>
                      <td><?= $employee->name ?></td>
                      <td><?= $employee->email ?></td>
                      <td>
                        <div class="btn-group" role="group">
                          <a class="btn btn-warning btn-xs" href="\employee\edit\<?= $employee->id ?>"><i class="fa fa-edit"></i></a>
                          <button class="delete btn btn-danger btn-xs" type="button"
                          employee-id="<?= $employee->id ?>"
                          employee-name="<?= $employee->name ?>"><i class="fa fa-trash"></i>
                        </button>
                      </div>
                    </td>
                  </tr>
                <?php endforeach; ?>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</div>

<?php partial_view('scripts') ?>
<script type="text/javascript">
  deleteItem("employee", "employee-id", "employee-name", "")
</script>
<?php partial_view('footer') ?>
