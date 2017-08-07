<?php partial_view('header') ?>

<div class="row">
  <div class="col-md-12">
    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title pull-left">Pilots</h3>
        <div class="btn-group pull-right" role="group">
          <a class="btn btn-success" href="\pilot\create"><i class="fa fa-plus"></i> Create</a>
        </div>
        <div class="clearfix"></div>
      </div>
      <div class="panel-body">
        <div class="row">
          <div class="col-md-12">
            <div class="table-responsive">
              <table id="pilots-table" class="table table-bordered table-striped table-hover">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Age</th>
                    <th class="two-button"></th>
                  </tr>
                </thead>
                <tbody>
                  <?php foreach ($pilots as $pilot) : ?>
                    <tr id='pilot-<?= $pilot->id ?>'>
                      <td><?= $pilot->id ?></td>
                      <td><?= $pilot->name ?></td>
                      <td><?= $pilot->age ?></td>
                      <td>
                        <div class="btn-group" role="group">
                          <a class="btn btn-warning btn-xs" href="\pilot\edit\<?= $pilot->id ?>"><i class="fa fa-edit"></i></a>
                          <button class="delete btn btn-danger btn-xs" type="button"
                          pilot-id="<?= $pilot->id ?>"
                          pilot-name="<?= $pilot->name ?>"><i class="fa fa-trash"></i>
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
  deleteItem("pilot", "pilot-id", "pilot-name", "")
</script>
<?php partial_view('footer') ?>
