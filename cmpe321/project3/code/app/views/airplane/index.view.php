<?php partial_view('header') ?>

<div class="row">
  <div class="col-md-12">
    <div class="panel panel-default">
      <div class="panel-heading">
        <h3 class="panel-title pull-left">Airplanes</h3>
        <div class="btn-group pull-right" role="group">
          <a class="btn btn-success" href="\airplane\create"><i class="fa fa-plus"></i> Create</a>
        </div>
        <div class="clearfix"></div>
      </div>
      <div class="panel-body">
        <div class="row">
          <div class="col-md-12">
            <div class="table-responsive">
              <table id="airplanes-table" class="table table-bordered table-striped table-hover">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Model</th>
                    <th>Age</th>
                    <th>Column</th>
                    <th>Row</th>
                    <th class="two-button"></th>
                  </tr>
                </thead>
                <tbody>
                  <?php foreach ($airplanes as $airplane) : ?>
                    <tr id='airplane-<?= $airplane->id ?>'>
                      <td><?= $airplane->id ?></td>
                      <td><?= $airplane->model ?></td>
                      <td><?= $airplane->age ?></td>
                      <td><?= $airplane->vertical ?></td>
                      <td><?= $airplane->horizantal ?></td>
                      <td>
                        <div class="btn-group" role="group">
                          <a class="btn btn-warning btn-xs" href="\airplane\edit\<?= $airplane->id ?>"><i class="fa fa-edit"></i></a>
                          <button class="delete btn btn-danger btn-xs" type="button"
                          airplane-id="<?= $airplane->id ?>"
                          airplane-model="<?= $airplane->model ?>"><i class="fa fa-trash"></i>
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
  deleteItem("airplane", "airplane-id", "airplane-model", "")
</script>
<?php partial_view('footer') ?>
